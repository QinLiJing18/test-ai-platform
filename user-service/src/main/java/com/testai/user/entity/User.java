package com.testai.user.entity;

// 补全所有用到的MyBatis-Plus注解导入
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_user")
public class User {
    @TableId(type = IdType.AUTO)  // 主键，数据库自增（MySQL的AUTO_INCREMENT）
    private Long id;

    private String username;      // 用户名（登录用）
    private String password;      // 密码（通常存储加密后的）
    private String email;         // 邮箱
    private String phone;         // 手机号
    private String nickname;      // 昵称（显示用）
    private String avatar;        // 头像URL地址
    private Integer status;       // 用户状态（如：0-禁用，1-启用）
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间

    @TableLogic  // 逻辑删除标记（0-未删除，1-已删除）
    private Integer deleted;
}