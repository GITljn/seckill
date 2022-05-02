package com.ljn.seckill.exception;

import com.ljn.seckill.vo.R;
import com.ljn.seckill.vo.REnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 可以捕获所有controller中的异常，将异常信息展示给用户
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 指定触发改方法的异常的种类，按异常匹配度决定执行哪个异常
//    @ExceptionHandler(Exception.class)
//    public R error(Exception exception) {
//        return R.error(REnum.ERROR);
//    }

    @ExceptionHandler(SecKillException.class)
    public R error(SecKillException exception) {
        return R.error(exception.getREnum());
    }

    @ExceptionHandler(BindException.class)
    public R error(BindException exception) {
        R r = R.error(REnum.BIND_ERROR);
        r.setMessage("参数校验异常: " + exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return r;
    }
//    @ExceptionHandler(Exception.class)
//    public R exceptionHandler(Exception exception) {
//        if (exception instanceof GlobalException) {
//            GlobalException e = (GlobalException) exception;
//            return R.error(e.getREnum());
//        } else if (exception instanceof BindException) {
//            BindException e = (BindException) exception;
//            R r = R.error(REnum.BIND_ERROR);
//            r.setMessage("参数校验异常: " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
//            return r;
//        }
//        return R.error(REnum.ERROR);
//    }
}
