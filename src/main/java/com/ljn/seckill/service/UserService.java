package com.ljn.seckill.service;

import com.ljn.seckill.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljn.seckill.vo.LoginVO;
import com.ljn.seckill.vo.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljn
 * @since 2022-04-28
 */
public interface UserService extends IService<User> {

    R doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response);

    User getUserFromRedis(HttpServletRequest request, HttpServletResponse response, String userTicket);
}
