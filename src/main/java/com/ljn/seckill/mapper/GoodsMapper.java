package com.ljn.seckill.mapper;

import com.ljn.seckill.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljn.seckill.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljn
 * @since 2022-04-30
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVO> findGoodsVOList();

    GoodsVO findGoodsVOByGoodsId(Long goodsId);
}
