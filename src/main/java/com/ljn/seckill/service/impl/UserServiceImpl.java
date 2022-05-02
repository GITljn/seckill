package com.ljn.seckill.service.impl;

import com.ljn.seckill.entity.User;
import com.ljn.seckill.exception.SecKillException;
import com.ljn.seckill.mapper.UserMapper;
import com.ljn.seckill.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljn.seckill.utils.CookieUtil;
import com.ljn.seckill.utils.MD5Util;
import com.ljn.seckill.utils.UUIDUtil;
import com.ljn.seckill.utils.ValidatorUtil;
import com.ljn.seckill.vo.LoginVO;
import com.ljn.seckill.vo.R;
import com.ljn.seckill.vo.REnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljn
 * @since 2022-04-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public R doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return R.error(REnum.LOGIN_ERROR);
        }
        if (!ValidatorUtil.isMobile(mobile)) {
            return R.error(REnum.MOBILE_ERROR);
        }
        User user = userMapper.selectById(mobile);
        if (user == null) {
//            return R.error(REnum.LOGIN_ERROR);
            throw new SecKillException(REnum.LOGIN_ERROR);
        }
        if (!MD5Util.midPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
//            return R.error(REnum.LOGIN_ERROR);
            throw new SecKillException(REnum.LOGIN_ERROR);
        }

        // 通过cookie和session保存用户的状态
//        String sessionId = UUIDUtil.uuid();
//        request.getSession().setAttribute(sessionId, user);
//        CookieUtil.setCookie(request, response, "sessionId", sessionId);

        // 通过cookie和redis保存用户的状态
        String ticket = UUIDUtil.uuid();
//        redisTemplate.opsForValue().set("user:"+ticket, user, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("user:"+ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", "user:"+ticket);

        return R.success(ticket);
    }

    @Override
    public User getUserFromRedis(HttpServletRequest request, HttpServletResponse response, String userTicket) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get(userTicket);
        return user;
    }
}
