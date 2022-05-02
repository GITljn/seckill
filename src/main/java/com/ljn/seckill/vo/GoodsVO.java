package com.ljn.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVO {
    private Long id;

    private String goodsName;

    private String goodsImg;

    private String goodsDetail;

    private BigDecimal goodsPrice;

    private BigDecimal seckillPrice;

    private Integer seckillStock;

    private Date seckillStartDate;

    private Date seckillEndDate;
}
