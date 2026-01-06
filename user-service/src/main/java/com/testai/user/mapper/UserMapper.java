package com.testai.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.testai.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(String username);
}