package com.ljn.seckill.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginVO {
    @NotNull
    private String mobile;

    @NotNull
    private String password;
}
