package com.gudushidai.mall.dao;

import com.gudushidai.mall.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select(" select id,csid,`desc`,image,is_hot as isHot,market_price as marketPrice,pdate,shop_price as shopPrice,title,create_at,update_at from product ORDER BY id desc limit #{start},#{rows}")
    List<Product> findAllProduct(@Param("start") int start, @Param("rows")int rows);

    @Select(" select id,csid,`desc`,image,is_hot as isHot,market_price as marketPrice,pdate,shop_price as shopPrice,title,create_at,update_at from product" +
            " where title like CONCAT('%',#{good_name},'%')  " +
            " ORDER BY id desc limit #{start},#{rows} ")
    List<Product> searchAllProduct(@Param("start") int start, @Param("rows")int rows, @Param("good_name") String good_name);

    @Select(" select id,csid,`desc`,image,is_hot as isHot,market_price as marketPrice,pdate,shop_price as shopPrice,title,create_at,update_at from product" +
            " where is_hot = 1 " +
            " ORDER BY id desc limit 12 ")
    List<Product> findByIsHot();
}
