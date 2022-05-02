package com.ljn.seckill.exception;

import com.ljn.seckill.vo.REnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecKillException extends RuntimeException {
    private REnum rEnum;
}
