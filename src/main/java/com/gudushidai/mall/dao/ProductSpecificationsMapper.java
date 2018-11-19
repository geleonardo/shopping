package com.gudushidai.mall.dao;

import com.gudushidai.mall.entity.ProductSpecifications;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductSpecificationsMapper {


    @Insert(" insert into product_specifications (p_id,type_name,type_price,type_stock,create_at,update_at) values (#{p_id},'默认',#{type_price},0,now(),now()) ")
    void createDefault(@Param("p_id") int id, @Param("type_price") Double shopPrice);

    @Select(" select * from product_specifications where p_id = #{p_id} ")
    List<ProductSpecifications> getByProduct(@Param("p_id")int id);

    @Update("update product_specifications set p_id=(p_id-p_id-p_id),update_at = now() where id = #{id} ")
    void delSpecifications(@Param("id")Integer id);

    @Insert(" insert into product_specifications (p_id,type_name,type_price,type_stock,create_at,update_at) values (#{p_id},#{type_name},#{type_price},0,now(),now()) ")
    void addSpecifications(@Param("p_id") Integer id, @Param("type_name") String toAddSpeName, @Param("type_price") Integer toAddSpePrice);

    @Select(" select * from product_specifications where id = #{id} ")
    ProductSpecifications getBySpecificationId(@Param("id") Long product_specificationId);

    @Update(" update product_specifications set type_name = #{new_type_name},update_at = now() where id = #{id}  ")
    Integer changeSpecificationName(@Param("id") Integer id,@Param("new_type_name")  String new_type_name);

    @Update(" update product_specifications set type_price = #{new_type_price},update_at = now() where id = #{id} ")
    void changeSpecificationPrice(@Param("id")Integer id,@Param("new_type_price")Integer new_type_price);
}
