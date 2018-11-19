package com.gudushidai.mall.dao;

import com.gudushidai.mall.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select(" select id,addr,name,order_time as orderTime,phone,state,total,user_id as userId,deliver_id as deliverId,create_at as createAt,update_at as updateAt " +
            " from `order` order by id desc limit #{start},#{rows} ")
    List<Order> findAllOrder(@Param("start") int start,@Param("rows") int rows);

    @Update(" update `order` set deliver_id = #{deliverId},update_at = now() where id = #{orderId}  ")
    void updateDeliverId(@Param("orderId")int orderId,@Param("deliverId") String deliverId);

    @Select(" select id,addr,name,order_time as orderTime,phone,state,total,user_id as userId,deliver_id as deliverId,create_at as createAt,update_at as updateAt " +
            " from `order` where user_id =#{userid} order by id desc limit #{start},#{rows} ")
    List<Order> findByUserId(@Param("userid") Integer id,@Param("start") int start,@Param("rows") int rows);

    @Select(" select id,addr,name,order_time as orderTime,phone,state,total,user_id as userId,deliver_id as deliverId,create_at as createAt,update_at as updateAt " +
            " from `order` where user_id =#{userid} and state = #{state} order by id desc limit #{start},#{rows} ")
    List<Order> findByUserIdAndState(@Param("userid") Integer id,@Param("start") int start,@Param("rows") int rows,@Param("state") int state);
}
