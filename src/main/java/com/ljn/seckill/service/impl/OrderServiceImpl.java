package com.ljn.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ljn.seckill.entity.Order;
import com.ljn.seckill.entity.SeckillGoods;
import com.ljn.seckill.entity.SeckillOrder;
import com.ljn.seckill.entity.User;
import com.ljn.seckill.mapper.OrderMapper;
import com.ljn.seckill.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljn.seckill.service.SeckillGoodsService;
import com.ljn.seckill.service.SeckillOrderService;
import com.ljn.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljn
 * @since 2022-04-30
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Order toSeckill(User user, GoodsVO goodsVO) {
//        SeckillGoods seckillGoods = seckillGoodsService.getById(goodsVO.getId());
//        seckillGoods.setSeckillStock(seckillGoods.getSeckillStock() - 1);
//        seckillGoodsService.updateById(seckillGoods);
        // 在修改库存前判断是否卖空
        UpdateWrapper<SeckillGoods> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.set("seckill_stock", seckillGoods.getSeckillStock());
        // 保证判断和修改库存是原子操作
        updateWrapper.setSql("seckill_stock = seckill_stock-1");
        updateWrapper.eq("goods_id", goodsVO.getId());
        updateWrapper.gt("seckill_stock", 0);
        boolean update = seckillGoodsService.update(updateWrapper);
        if (!update) {
            return null;
        }
        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVO.getId());
        order.setAddrId(0l);
        order.setGoodsName(goodsVO.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goodsVO.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());

        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goodsVO.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderService.save(seckillOrder);
        // 将订单存入redis，避免在查看重复购买的时候去数据库中查询
        redisTemplate.opsForValue().set("seckillOrder:"+user.getId()+":"+goodsVO.getId(), seckillOrder);
        // 秒杀成功才可以插入，秒杀的时候可能会出现重复购买，插入失败会自动回滚
        orderService.save(order);

        return order;
    }
}
