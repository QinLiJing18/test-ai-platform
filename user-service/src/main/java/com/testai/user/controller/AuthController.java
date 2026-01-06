// 1. 包路径：把这个类放在controller包下，这是规矩（控制器都放这）
package com.testai.user.controller;

// 2. 导入需要用的工具类/数据类
import com.testai.user.common.Result; // 统一返回给前端的格式（比如成功/失败）
import com.testai.user.dto.LoginRequest; // 前端登录时传的参数（用户名+密码）
import com.testai.user.dto.LoginResponse; // 登录成功后返回给前端的内容（token+用户信息）
import com.testai.user.service.AuthService; // 真正处理登录/退出逻辑的业务类
import lombok.RequiredArgsConstructor; // 自动生成构造方法，不用写new
import org.springframework.web.bind.annotation.*; // 做接口的核心注解

/**
 * 认证控制器：专门处理登录、退出这类“验证身份”的请求
 * 前端调接口的入口，本身不写业务逻辑，只负责接收请求、调用业务层、返回结果
 */
@RestController // 核心注解：告诉Spring这是个接口控制器，返回的是JSON数据（不是网页）
@RequestMapping("/auth") // 接口前缀：所有这个类里的接口都以 /auth 开头（比如 /auth/login）
@RequiredArgsConstructor // 自动生成构造方法，给下面的authService赋值（不用手动new）
public class AuthController {

    // 3. 注入认证业务类：真正干活的是AuthService，这里只是调用它
    // final表示这个变量不能改，更安全
    private final AuthService authService;

    // 4. 登录接口：前端调 POST /auth/login 就能登录
    @PostMapping("/login") // 注解：接收POST请求，接口路径是 /auth/login
    // 方法作用：处理登录请求
    // 参数：@RequestBody LoginRequest request → 接收前端传的JSON（比如{"username":"admin","password":"123"}），并封装成LoginRequest对象
    // 返回值：Result<LoginResponse> → 按统一格式返回登录结果（成功就带token，失败就提示错误）
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        // 核心逻辑：调用AuthService的login方法处理登录，然后用Result.success包装成统一格式返回给前端
        // 简单说：把前端的参数传给业务层，业务层处理完，再把结果包一下返回
        return Result.success(authService.login(request));
    }

    // 5. 退出接口：前端调 POST /auth/logout 就能退出
    @PostMapping("/logout") // 注解：接收POST请求，接口路径是 /auth/logout
    // 方法作用：处理退出请求
    // 参数：@RequestHeader("Authorization") String token → 从请求头里拿token（前端退出时要带登录时拿到的token）
    // 返回值：Result<Void> → 退出成功只返回“成功”，不用带具体数据（Void表示无数据）
//     ① @RequestHeader：注解，意思是“从HTTP请求的「请求头」里拿数据”（不是从请求体/URL里拿）；
//       ③ String token：把拿到的字段值存到叫token的字符串变量里；
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        // 处理token：前端传的token一般是 "Bearer + 真正的token"，所以要去掉"Bearer "只留核心token
//        replace 是 Java 字符串的替换方法，语法是：字符串.replace(要替换的内容, 替换成的内容)。
        authService.logout(token.replace("Bearer ", ""));
        // 返回成功：告诉前端退出成功了
        return Result.success();
    }
}