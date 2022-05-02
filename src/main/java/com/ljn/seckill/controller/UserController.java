package com.ljn.seckill.controller;

import com.ljn.seckill.entity.User;
import com.ljn.seckill.rabbitmq.Provider;
import com.ljn.seckill.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljn
 * @since 2022-04-28
 */
@Controller
@RequestMapping("/seckill/user")
public class UserController {
    @Autowired
    private Provider provider;

    @GetMapping("/info")
    @ResponseBody
    public R info(User user) {
        return R.success(user);
    }

    @GetMapping("/mq")
    @ResponseBody
    public void mq() {
        provider.send("hello");
    }

    @GetMapping("/mq_fanout")
    @ResponseBody
    public void mq_fanout() {
        provider.send_fanout("hello");
    }

    @GetMapping("/mq_direct_red")
    @ResponseBody
    public void mq_direct_red() {
        provider.send_direct_red("hello red");
    }

    @GetMapping("/mq_direct_blue")
    @ResponseBody
    public void mq_direct_blue() {
        provider.send_direct_blue("hello blue");
    }

    @GetMapping("/mq_topic01")
    @ResponseBody
    public void mq_topic01() {
        provider.send_topic01("goods.detail.1");
    }

    @GetMapping("/mq_topic02")
    @ResponseBody
    public void mq_topic02() {
        provider.send_topic02("order.detail.2");
    }
}

