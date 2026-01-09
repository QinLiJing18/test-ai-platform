package com.testai.user.security;
//拦截请求
import lombok.RequiredArgsConstructor; // Lombok注解：自动生成构造方法，注入依赖
import org.springframework.context.annotation.Bean; // 声明方法返回值是Spring管理的Bean
import org.springframework.context.annotation.Configuration; // 标记这是Spring配置类
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//：是 Spring 的核心配置注解，替代传统的 xml 配置文件；
@Configuration
//简化依赖注入，不用写@Autowired，直接为final
@RequiredArgsConstructor // Lombok：为final修饰的属性生成构造方法，注入jwtAuthenticationFilter
public class SecurityConfig {
    // 注入JWT认证过滤器（你项目中自定义的，用于验证请求头中的token）
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭CSRF防护：CSRF是针对有状态会话的防护，JWT是无状态的，不需要
                .csrf().disable()
                // 会话管理：设置为无状态（STATELESS），Spring Security不创建、不使用HttpSession
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and() // 链式调用的分隔符，相当于“然后”
                // 配置接口授权规则
                .authorizeHttpRequests()
                // 匹配/login和/register接口，允许所有人访问（无需登录）
                .antMatchers("/auth/login", "/auth/register").permitAll()
                // 除了上面的接口，其他所有请求都需要认证（登录后才能访问）
                .anyRequest().authenticated();

        // 把JWT过滤器添加到Spring Security的过滤器链中，放在“用户名密码认证过滤器”之前
        // 作用：所有请求先经过JWT过滤器验证token，再走后续的认证逻辑
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build(); // 构建并返回配置好的过滤器链
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}