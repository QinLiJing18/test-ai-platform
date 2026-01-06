package com.testai.user.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通用用户信息DTO
 * 作用：用于用户信息查询、修改、展示等场景，适配除登录外的大部分用户数据传输
 * 特点：字段完整但隐藏敏感信息，支持参数校验（用于修改场景）
 */
@Data
public class UserDTO {
    /**
     * 用户ID（查询/修改时用）
     */
    private Long id;

    /**
     * 用户名（唯一，不可修改）
     */
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    /**
     * 密码（仅注册/修改密码时用，查询时返回null）
     * 注意：传输过程中密码是明文，后端接收后需立即加密存储
     */
    private String password;

    /**
     * 邮箱（带格式校验）
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号（带格式校验）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 创建时间（只读，前端无需传递）
     */
    private LocalDateTime createTime;

    /**
     * 更新时间（只读）
     */
    private LocalDateTime updateTime;

    /**
     * 角色列表（展示/鉴权用）
     */
    private List<String> roles;

}

