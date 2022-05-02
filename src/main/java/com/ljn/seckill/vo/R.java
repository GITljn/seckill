package com.ljn.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    private Integer code;
    private String message;
    private Object obj;

    public static R success() {
        return new R(REnum.SUCCESS.getCode(), REnum.SUCCESS.getMessage(), null);
    }

    public static R success(REnum rEnum) {
        return new R(rEnum.getCode(), rEnum.getMessage(), null);
    }
    public static R success(Object obj) {
        return new R(REnum.SUCCESS.getCode(), REnum.SUCCESS.getMessage(), obj);
    }

    public static R error(REnum rEnum) {
        return new R(rEnum.getCode(), rEnum.getMessage(), null);
    }
    public static R error(REnum rEnum, Object obj) {
        return new R(rEnum.getCode(), rEnum.getMessage(), obj);
    }
}
