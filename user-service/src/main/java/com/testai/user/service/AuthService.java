package com.testai.user.service;

import com.testai.user.dto.LoginRequest;
import com.testai.user.dto.LoginResponse;

// 真正处理登录/退出逻辑的业务类
public interface AuthService {

    /**
     * 用户登录方法声明
     * @param request 登录请求参数
     * @return 登录响应结果
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户退出登录方法声明
     * @param token JWT token（已去除"Bearer "前缀）
     */
    void logout(String token);
}