package com.ljn.seckill.controller;

import com.ljn.seckill.entity.User;
import com.ljn.seckill.service.GoodsService;
import com.ljn.seckill.service.UserService;
import com.ljn.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    // 用于手动填充视图中的数据
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    // windows 303  635  724
    // 缓存页面 327   407
    // produces可以指定返回值的类型和编码方式
    @GetMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user,
                         HttpServletRequest request, HttpServletResponse response) {
        if (user == null) {
            WebContext context = new WebContext(request, response,
                    request.getServletContext(), request.getLocale(), model.asMap());
            return thymeleafViewResolver.getTemplateEngine().process("login", context);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 将用户数据传给前端页面
        model.addAttribute("user", user);
        // 将商品列表数据传给前端页面
        List<GoodsVO> goodsVOList = goodsService.findGoodsVOList();
        model.addAttribute("goodsList", goodsVOList);
        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        // 指定页面和数据
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 1, TimeUnit.MINUTES);
        }
        return html;
    }

    @GetMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable Long goodsId,
                           HttpServletRequest request, HttpServletResponse response) {
        if (user == null) {
            WebContext context = new WebContext(request, response,
                    request.getServletContext(), request.getLocale(), model.asMap());
            return thymeleafViewResolver.getTemplateEngine().process("login", context);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        GoodsVO goodsVO = goodsService.findGoodsVOByGoodsId(goodsId);
        model.addAttribute("goods", goodsVO);
        Date startDate = goodsVO.getSeckillStartDate();
        Date endDate = goodsVO.getSeckillEndDate();
        Date nowDate = new Date();
        // 秒杀状态
        int secKillStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            secKillStatus = 0;
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail:" + goodsId, html, 1, TimeUnit.MINUTES);
        }
        return html;
    }
/*
    @GetMapping("/toList")
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model,
                         @CookieValue(value = "userTicket", required = false) String userTicket) {
        System.out.println(userTicket);
        // 判断相应cookie是否存在或者过期
        if (StringUtils.isEmpty(userTicket)) {
            return "login";
        }
        // 从session中获取用户信息   HttpSession session
        // User user = (User)session.getAttribute(sessionId);

        // 从redis中获取用户信息
        User user = userService.getUserFromRedis(request, response, userTicket);
        // 判断相应session是否存在
        if (user == null) {
            return "login";
        }
        // 将用户传给前端页面
        model.addAttribute("user", user);
        return "goodsList";
    }
 */
}
