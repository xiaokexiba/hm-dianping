package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {

    /**
     * 发送验证码
     *
     * @param phone   手机号
     * @param session session对象
     * @return 统一返回类
     */
    Result sendCode(String phone, HttpSession session);

    /**
     * 登入注册
     *
     * @param loginForm 前端登入数据
     * @param session   session对象
     * @return 统一返回类
     */
    Result login(LoginFormDTO loginForm, HttpSession session);
}
