package com.testai.user.common;

import lombok.Data;

/**
 * 通用返回结果类
 * 统一封装接口返回给前端的数据格式
 * @param <L> 泛型，支持不同类型的返回数据
 */
@Data // lombok注解，自动生成get/set/toString等方法
public class Result<L> {
    // 通用返回字段（前端统一解析这几个字段）
    private int code; // 状态码：200成功，500失败
    private String msg; // 提示信息：比如“操作成功”“用户名密码错误”
    private L data; // 具体返回数据（泛型，可传任意类型：登录结果、用户列表等）

    // 私有构造方法：禁止外部直接new，统一用静态方法创建
//    不让别人直接通过new Result()创建对象，必须用下面的success()/error()静态方法创建；
    private Result() {}

    /**
     * 无数据的成功返回（比如退出登录）
     * @param <L> 泛型
     * @return Result<L>
     */
    public static <L> Result<L> success() {
        Result<L> result = new Result<>();
        result.setCode(200); // 成功状态码
        result.setMsg("操作成功"); // 默认成功提示
        result.setData(null); // 无数据
        return result;
    }

    /**
     * 带数据的成功返回（比如登录成功返回token）
     * @param data 要返回的具体数据
     * @param <L> 泛型
     * @return Result<L>
     */
//    比如书调用方式：return Result.success(authService.login(request))
//    static：不需要创建 Result 类的实例，直接通过类名调用，是工具方法的典型特征。
    public static <L> Result<L> success(L data) {
        Result<L> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data); // 传入具体数据（如LoginResponse）
        return result;
    }

    // 可选补充：失败返回方法（方便后续扩展）
    public static <L> Result<L> error(String msg) {
        Result<L> result = new Result<>();
        result.setCode(500); // 失败状态码
        result.setMsg(msg); // 失败提示信息
        result.setData(null);
        return result;
    }
}