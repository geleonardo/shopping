package com.gudushidai.mall.service;

import com.gudushidai.mall.entity.ProcuctImage;

import java.util.List;

public interface ProductImageService {
    int create(int id,String imgUrl);

    List<ProcuctImage> getImages(int id);
}
