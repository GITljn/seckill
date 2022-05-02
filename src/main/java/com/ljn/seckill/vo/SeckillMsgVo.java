package com.ljn.seckill.vo;

import com.ljn.seckill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMsgVo {
    private User user;
    private Long goodsId;
}
