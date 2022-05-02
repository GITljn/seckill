package com.ljn.seckill.service;

import com.ljn.seckill.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljn.seckill.entity.User;
import com.ljn.seckill.vo.GoodsVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljn
 * @since 2022-04-30
 */
public interface OrderService extends IService<Order> {

    Order toSeckill(User user, GoodsVO goodsVO);
}
