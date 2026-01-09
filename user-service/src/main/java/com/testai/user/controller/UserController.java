package com.testai.user.controller;

import com.testai.common.dto.Response;
import com.testai.user.dto.LoginRequest;
import com.testai.user.dto.LoginResponse;
import com.testai.user.dto.RegisterRequest;
import com.testai.user.entity.User;
import com.testai.user.service.UserService;
import com.testai.user.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
//通过@RequiredArgsConstructor自动注入UserService（业务逻辑）和JwtUtil（JWT 工具），无需手动写@Autowired。
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Response<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        try {
            log.info("用户登录请求: {}", request.getUsername());
            LoginResponse response = userService.login(request);
            return Response.success("登录成功", response);
        } catch (Exception e) {
            log.error("用户登录失败", e);
            return Response.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Response<Void> register(@Validated @RequestBody RegisterRequest request) {
        try {
            log.info("用户注册请求: {}", request.getUsername());
            userService.register(request);
            return Response.success("注册成功");
        } catch (Exception e) {
            log.error("用户注册失败", e);
            return Response.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Response<LoginResponse.UserInfo> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            // 解析 Token
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return Response.error(401, "Token无效或已过期");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getUserById(userId);

            if (user == null) {
                return Response.error(404, "用户不存在");
            }

            LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .avatar(user.getAvatar())
                    .build();

            return Response.success(userInfo);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Response.error(e.getMessage());
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Response<String> health() {
        return Response.success("User Service is running");
    }
}