package com.gudushidai.mall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.gudushidai.mall.entity.User;

@Mapper
public interface UserMapper {

    @Select(" select * from `user` where phone = #{username} and password = #{password} ")
    User findByUserPhoneAndPassword(@Param("username") String username, @Param("password") String password);
}
