package com.gudushidai.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gudushidai.mall.dao.ProductSpecificationsMapper;
import com.gudushidai.mall.entity.ProductSpecifications;
import com.gudushidai.mall.service.ProductSpecificationsService;

import java.util.List;

@Service
public class ProductSpecificationsServiceImpl implements ProductSpecificationsService {

    @Autowired
    private ProductSpecificationsMapper productSpecificationsMapper;

    @Override
    public void createDefault(int id, Double shopPrice) {
        productSpecificationsMapper.createDefault(id,shopPrice);
    }

    @Override
    public List<ProductSpecifications> getByProduct(int id) {
        return productSpecificationsMapper.getByProduct(id);
    }

    @Override
    public void delSpecifications(Integer id) {
        productSpecificationsMapper.delSpecifications(id);
    }

    @Override
    public void addSpecifications(Integer id, String toAddSpeName, Integer toAddSpePrice) {
        productSpecificationsMapper.addSpecifications(id,toAddSpeName,toAddSpePrice);
    }

    @Override
    public ProductSpecifications getBySpecificationId(Long product_specificationId) {
        return productSpecificationsMapper.getBySpecificationId(product_specificationId);
    }

    @Override
    public void changeSpecificationName(Integer id, String new_type_name) {
        productSpecificationsMapper.changeSpecificationName(id,new_type_name);
    }

    @Override
    public void changeSpecificationPrice(Integer id, Integer new_type_price) {
        productSpecificationsMapper.changeSpecificationPrice(id,new_type_price);
    }
}
