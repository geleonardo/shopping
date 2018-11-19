package com.gudushidai.mall.service;

import com.gudushidai.mall.entity.ProductSpecifications;

import java.util.List;

public interface ProductSpecificationsService {
    void createDefault(int id,Double shopPrice);

    List<ProductSpecifications> getByProduct(int id);

    void delSpecifications(Integer id);

    void addSpecifications(Integer id, String toAddSpeName, Integer toAddSpePrice);

    ProductSpecifications getBySpecificationId(Long product_specificationId);

    void changeSpecificationName(Integer id, String new_type_name);

    void changeSpecificationPrice(Integer id, Integer new_type_price);
}
