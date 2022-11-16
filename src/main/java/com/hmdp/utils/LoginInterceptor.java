package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * 登入拦截器
 *
 * @author xoke
 * @date 2022/11/15
 */
@SuppressWarnings("all")
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否需要拦截（ThreadLocal是否有用户）
        if (UserHolder.getUser() == null) {
            // 没有，需要拦截
            response.setStatus(401);
            return false;
        }
        // 有用户，放行
        return true;
    }
}
