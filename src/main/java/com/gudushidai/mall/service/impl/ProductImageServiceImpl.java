package com.gudushidai.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gudushidai.mall.dao.ProductImageDao;
import com.gudushidai.mall.entity.ProcuctImage;
import com.gudushidai.mall.service.ProductImageService;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageDao productImageDao;

    @Override
    public int create(int id,String imgUrl) {
        productImageDao.create(id,imgUrl);
        return 1;
    }

    @Override
    public List<ProcuctImage> getImages(int id) {
        List<ProcuctImage> list = productImageDao.getImages(id);
        return list;
    }
}
