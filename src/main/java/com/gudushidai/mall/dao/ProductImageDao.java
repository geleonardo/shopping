package com.gudushidai.mall.dao;

import com.gudushidai.mall.entity.ProcuctImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductImageDao {

    @Insert(" insert into product_image (product_id,image_src,create_at,update_at) values (#{product_id},#{imgUrl},now(),now()) ")
    void create(@Param("product_id") int id, @Param("imgUrl")String imgUrl);

    @Select(" select * from product_image where product_id = #{product_id} order by id desc  ")
    List<ProcuctImage> getImages(@Param("product_id") int id);
}
