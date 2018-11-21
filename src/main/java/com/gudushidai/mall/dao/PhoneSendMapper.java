package com.gudushidai.mall.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface PhoneSendMapper {


    @Select("select id from phone_send where phone = #{phone} order by id desc limit 1 ")
    Long getPhoneSendId(@Param("phone") String phone);

    @Update(" update phone_send set send_times = (send_times + 1),update_time = now() where phone = '#{phone}' ")
    void updatePhoneSendTimes(@Param("phone")String phone);


    @Insert(" insert into phone_send (phone,send_times,create_time,update_time) values (#{phone},1,now(),now()) ")
    void insertPhoneSend(@Param("phone")String phone);

    @Select(" select count(1) from phone_send ")
    int countPhoneNum();
}
