package com.testai.user.security;

import com.testai.user.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 【新增】JWT认证过滤器
 *
 * 功能说明：
 * 1. 拦截所有HTTP请求，检查请求头中的JWT token
 * 2. 验证token有效性，如果有效则设置用户认证信息到Spring Security上下文
 * 3. 继承OncePerRequestFilter确保每个请求只执行一次过滤
 *
 * 工作流程：
 * 1. 从请求头"Authorization"中提取token（格式：Bearer xxxxx）
 * 2. 使用JwtUtil验证token是否有效
 * 3. 如果有效，从token中提取用户信息并设置到SecurityContext
 * 4. 放行请求，让后续过滤器或Controller处理
 *
 * 依赖说明：
 * - JwtUtil: JWT工具类，用于验证token和提取用户信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * 过滤器核心方法：处理每个HTTP请求
     *
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param filterChain 过滤器链，用于放行请求到下一个过滤器
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 步骤1: 从请求头获取Authorization字段
            String authHeader = request.getHeader("Authorization");

            // 步骤2: 检查token是否存在且格式正确（以"Bearer "开头）
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // 步骤3: 提取实际的token（去掉"Bearer "前缀）
                String token = authHeader.substring(7);

                // 步骤4: 验证token是否有效（未过期、签名正确）
                if (jwtUtil.validateToken(token)) {
                    // 步骤5: 从token中提取用户名
                    String username = jwtUtil.getUsernameFromToken(token);

                    // 步骤6: 如果用户名存在且SecurityContext中还没有认证信息
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 步骤7: 创建认证对象（用户名作为principal，密码为null因为已通过token认证，权限列表为空）
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                        // 步骤8: 设置请求详情（IP地址、SessionId等）
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//ip地址和sessionid
                        // 步骤9: 将认证信息存入SecurityContext，后续可通过SecurityContextHolder获取当前用户
                        SecurityContextHolder.getContext().setAuthentication(authentication);
//之后就可以这样 业务层/Controller层都能获取
//                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                        String currentUsername = auth.getName(); // 获取当前用户名
                        log.debug("【JWT认证】设置用户认证信息: {}", username);
                    }
                } else {
                    log.warn("【JWT认证】Token验证失败");
                }
            }
        } catch (Exception e) {
            log.error("【JWT认证】处理认证时发生异常", e);
        }

        // 步骤10: 无论认证成功与否，都放行请求到下一个过滤器或Controller
        // 如果认证失败，SecurityContext中没有认证信息，会被Spring Security拦截返回401
        filterChain.doFilter(request, response);
    }
}
