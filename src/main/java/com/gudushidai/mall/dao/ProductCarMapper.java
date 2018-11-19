package com.gudushidai.mall.dao;

import com.gudushidai.mall.entity.ShopCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductCarMapper {

    @Insert(" insert into user_cart (userid,productId,product_specificationId,good_num,create_at,update_at) values (#{userid},#{productId},#{product_specificationId},#{good_num},now(),now()) ")
    void addCart(@Param("userid") Integer userid,@Param("productId") int productId,@Param("product_specificationId") int product_specificationId,@Param("good_num") int good_num);

    @Select(" select * from user_cart where userid = #{userid} ")
    List<ShopCart> getUserShopCart(@Param("userid")Integer userid);

    @Update(" update user_cart set userid = (userid-userid-userid) where  userid = #{userid} and id = #{id} ")
    void remove(@Param("userid") Integer userid, @Param("id")int cartId);

    @Update(" update user_cart set userid = (userid-userid-userid) where  userid = #{userid} ")
    void deleteMyCart(@Param("userid") Integer userid);
}
