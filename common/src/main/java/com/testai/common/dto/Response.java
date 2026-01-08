package com.testai.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应对象
 * 【Claude修改】实现了完整的Response类，原代码只有TODO注释
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    /**
     * 响应码：200-成功，其他-失败
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    // ====================================================================
    // 【Claude修改】以下静态工厂方法是新增的，用于创建Response对象
    // 这些方法被UserController.java使用
    // ====================================================================

    /**
     * 成功响应（带数据和消息）
     * 【Claude修改】UserController第35行使用: Response.success("登录成功", response)
     */
    public static <T> Response<T> success(String message, T data) {
        return new Response<>(200, message, data);
    }

    /**
     * 成功响应（只带消息）
     * 【Claude修改】UserController第50、97行使用: Response.success("注册成功")
     */
    public static <T> Response<T> success(String message) {
        return new Response<>(200, message, null);
    }

    /**
     * 成功响应（只带数据）
     * 【Claude修改】UserController第85行使用: Response.success(userInfo)
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(200, "操作成功", data);
    }

    /**
     * 失败响应（默认500错误码）
     * 【Claude修改】UserController第38、54、88行使用: Response.error(e.getMessage())
     */
    public static <T> Response<T> error(String message) {
        return new Response<>(500, message, null);
    }

    /**
     * 失败响应（自定义错误码）
     * 【Claude修改】UserController第66、73行使用: Response.error(401, "Token无效")
     */
    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null);
    }
}
