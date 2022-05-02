package com.ljn.seckill.service.impl;

import com.ljn.seckill.entity.Goods;
import com.ljn.seckill.mapper.GoodsMapper;
import com.ljn.seckill.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljn.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljn
 * @since 2022-04-30
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVO> findGoodsVOList() {
        return goodsMapper.findGoodsVOList();
    }

    @Override
    public GoodsVO findGoodsVOByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVOByGoodsId(goodsId);
    }
}
