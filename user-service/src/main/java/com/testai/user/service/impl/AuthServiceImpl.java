package com.testai.user.service.impl;

import com.testai.user.dto.LoginRequest;
import com.testai.user.dto.LoginResponse;
import com.testai.user.entity.User;
import com.testai.user.service.AuthService;
import com.testai.user.service.UserService;
import com.testai.user.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 【新增】认证服务实现类
 *
 * 职责说明：
 * 1. 处理用户登录认证：验证用户名密码，生成JWT token
 * 2. 处理用户退出：清理token缓存（当前简化实现）git push origin master
 *
 * 依赖关系：
 * - UserService: 获取用户信息
 * - JwtUtil: JWT token生成和验证
 * - BCryptPasswordEncoder: 密码加密验证
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;  // 用户服务，用于查询用户信息
    private final JwtUtil jwtUtil;  // JWT工具类，用于生成和验证token
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // 密码加密器

    @Value("${jwt.expiration:86400000}")  // JWT过期时间，默认24小时
    private Long jwtExpiration;

    /**
     * 【核心方法】用户登录
     *
     * 登录流程：
     * 1. 根据用户名查询用户 -> 不存在抛异常
     * 2. 验证密码 -> 错误抛异常
     * 3. 检查用户状态 -> 被禁用抛异常
     * 4. 生成JWT token
     * 5. 构建登录响应（包含token和用户信息）
     *
     * @param request 登录请求（包含username和password）
     * @return LoginResponse 登录响应（包含token和用户基本信息）
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("【登录】开始处理登录请求，用户名: {}", request.getUsername());

        // 步骤1: 查询用户
        User user = userService.getUserByUsername(request.getUsername());
        if (user == null) {
            log.warn("【登录失败】用户不存在: {}", request.getUsername());
            throw new RuntimeException("用户名或密码错误");  // 安全考虑，不暴露具体是用户名还是密码错误
        }

        // 步骤2: 验证密码（使用BCrypt比对加密后的密码）
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("【登录失败】密码错误，用户: {}", request.getUsername());
            throw new RuntimeException("用户名或密码错误");
        }

        // 步骤3: 检查用户状态（status=1表示正常，0表示禁用）
        if (user.getStatus() == null || user.getStatus() != 1) {
            log.warn("【登录失败】用户已被禁用: {}", request.getUsername());
            throw new RuntimeException("用户已被禁用");
        }

        // 步骤4: 生成JWT token（包含用户ID和用户名）
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        log.info("【登录成功】用户: {}, 生成token", request.getUsername());

        // 步骤5: 构建用户信息对象
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .build();

        // 步骤6: 构建登录响应
        return LoginResponse.builder()
                .accessToken(token)  // JWT token
                .tokenType("Bearer")  // token类型，标准格式
                .expiresIn(jwtExpiration / 1000)  // 过期时间（转换为秒）
                .userInfo(userInfo)  // 用户基本信息
                .build();
    }

    /**
     * 【核心方法】用户退出登录
     *
     * 退出流程：
     * 1. 验证token有效性
     * 2. 记录退出日志
     * 3. （可选）将token加入黑名单（需要Redis支持）
     *
     * 注意：
     * - 当前为简化实现，仅记录日志
     * - 实际生产环境建议将token加入Redis黑名单，禁止该token再次使用
     * - JWT本身是无状态的，服务端不存储，只能通过黑名单机制实现强制失效
     *
     * @param token JWT token（已去除"Bearer "前缀）
     */
    @Override
    public void logout(String token) {
        // 步骤1: 验证token有效性
        if (!jwtUtil.validateToken(token)) {
            log.warn("【退出失败】token无效或已过期");
            throw new RuntimeException("Token无效或已过期");
        }

        // 步骤2: 获取用户信息并记录日志
        String username = jwtUtil.getUsernameFromToken(token);
        log.info("【退出登录】用户: {} 退出成功", username);

        // 步骤3: （可选）加入黑名单
        // TODO: 如果需要强制token失效，可在此将token存入Redis黑名单
        // redisTemplate.opsForValue().set("blacklist:" + token, "1", jwtExpiration, TimeUnit.MILLISECONDS);
    }
}
