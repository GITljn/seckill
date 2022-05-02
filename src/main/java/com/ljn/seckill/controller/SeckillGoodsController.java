package com.ljn.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljn.seckill.entity.Order;
import com.ljn.seckill.entity.SeckillGoods;
import com.ljn.seckill.entity.SeckillOrder;
import com.ljn.seckill.entity.User;
import com.ljn.seckill.rabbitmq.Provider;
import com.ljn.seckill.service.GoodsService;
import com.ljn.seckill.service.OrderService;
import com.ljn.seckill.service.SeckillGoodsService;
import com.ljn.seckill.service.SeckillOrderService;
import com.ljn.seckill.utils.JsonUtil;
import com.ljn.seckill.vo.GoodsVO;
import com.ljn.seckill.vo.R;
import com.ljn.seckill.vo.REnum;
import com.ljn.seckill.vo.SeckillMsgVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljn
 * @since 2022-04-30
 */
@Controller
@RequestMapping("/seckill/seckillGoods")
public class SeckillGoodsController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Provider provider;

    private HashMap<Long, Boolean> emptyStock = new HashMap<>();

    // windows 747  865   692
    @PostMapping("/toSeckill")
    @ResponseBody
    public R toSeckill(Model model, User user, Long goodsId) {
        if (user == null) {
            return R.error(REnum.SESSION_ERROR);
//            return "login";
        }
        if (emptyStock.get(goodsId)) {
            return R.error(REnum.EMPTY_STOCK);
//            model.addAttribute("errmsg", REnum.EMPTY_STOCK.getMessage());
//            return "seckillFail";
        }
        // 判断是否重复抢购，由原来的去数据库中查改为去redis中查
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue()
                .get("seckillOrder:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return R.error(REnum.REPEATE_BUY);
//            model.addAttribute("errmsg", REnum.REPEATE_BUY.getMessage());
//            return "seckillFail";
        }

        // 在redis中预减库存，如果先判断再减库存则不是原子操作
        Long stock = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
        if (stock < 0) {
            emptyStock.put(goodsId, true);
            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
            return R.error(REnum.EMPTY_STOCK);
//            model.addAttribute("errmsg", REnum.EMPTY_STOCK.getMessage());
//            return "seckillFail";
        }

        // 异步下单
        // rabbitmq直接发送对象需要序列化，因此此处转成字符串
        SeckillMsgVo seckillMsgVo = new SeckillMsgVo(user, goodsId);
        provider.sendSeckillMsg(JsonUtil.object2JsonStr(seckillMsgVo));

        return R.success(REnum.QUEUE);
//        model.addAttribute("msg", 0);
//        return "goodsDetail";

        /*
        model.addAttribute("user", user);
        GoodsVO goodsVO = goodsService.findGoodsVOByGoodsId(goodsId);
        // 库存判断，不是原子操作，无法解决超卖问题
//        Integer stock = goodsVO.getSeckillStock();
//        if (stock < 1) {
//            model.addAttribute("errmsg", REnum.EMPTY_STOCK.getMessage());
//            return "seckillFail";
//        }
        // 限购判断(商品订单表中允许一个用户购买多件相同的商品，秒杀商品订单表不允许)
//        QueryWrapper<SeckillOrder> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_id", user.getId());
//        wrapper.eq("goods_id", goodsId);
//        SeckillOrder seckillOrder = seckillOrderService.getOne(wrapper);
        // 判断是否重复抢购，由原来的去数据库中查改为去redis中查
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue()
                .get("seckillOrder:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            model.addAttribute("errmsg", REnum.REPEATE_BUY.getMessage());
            return "seckillFail";
        }
        // 秒杀
        Order order = orderService.toSeckill(user, goodsVO);
        if (order == null) {
            model.addAttribute("errmsg", REnum.EMPTY_STOCK.getMessage());
            return "seckillFail";
        }
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVO);
        return "orderDetail";

         */
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsVOList = goodsService.findGoodsVOList();
        for (GoodsVO goodsVO : goodsVOList) {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVO.getId(), goodsVO.getSeckillStock(), 1, TimeUnit.MINUTES);
            emptyStock.put(goodsVO.getId(), false);
        }
    }
}

