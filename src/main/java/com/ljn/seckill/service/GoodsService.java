package com.ljn.seckill.service;

import com.ljn.seckill.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljn.seckill.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljn
 * @since 2022-04-30
 */
public interface GoodsService extends IService<Goods> {

    List<GoodsVO> findGoodsVOList();

    GoodsVO findGoodsVOByGoodsId(Long goodsId);
}
