package com.testai.user.service;

import com.testai.user.dto.LoginRequest;
import com.testai.user.dto.LoginResponse;
import com.testai.user.dto.RegisterRequest;
import com.testai.user.dto.UserDTO;
import com.testai.user.entity.User;

import java.util.List;

public interface UserService {
    // 1. 保留你impl中实际实现的方法（原样复制方法签名）
    LoginResponse login(LoginRequest request);
    void register(RegisterRequest request);
    User getUserByUsername(String username);
    User getUserById(Long userId); // 注意：返回值和impl保持一致为User，不是UserDTO

    // 2. 接口原有方法标记为默认实现（避免"未实现"报错，且不修改impl）
    default List<UserDTO> getAllUsers() {
        throw new UnsupportedOperationException("暂未实现");
    }

    default UserDTO createUser(UserDTO userDTO) {
        throw new UnsupportedOperationException("暂未实现");
    }

    default UserDTO updateUser(Long id, UserDTO userDTO) {
        throw new UnsupportedOperationException("暂未实现");
    }

    default void deleteUser(Long id) {
        throw new UnsupportedOperationException("暂未实现");
    }
}