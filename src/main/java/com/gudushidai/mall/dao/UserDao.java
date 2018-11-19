package com.gudushidai.mall.dao;

import com.gudushidai.mall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    /**
     * 根据用户名，密码查询用户
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    List<User> findByUsername(String username);

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    List<User> findByPhone(String phone);

    /**
     * 根据手机号查询用户
     * @param email
     * @return
     */
    List<User> findByEmail(String email);
}
