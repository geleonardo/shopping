package com.gudushidai.mall.entity;

import java.util.Date;

public class ShopCart {

    private Long id;
    private Long userid;
    private Long productId;
    private Long product_specificationId;
    private Integer good_num;
    private Date create_at;
    private Date update_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProduct_specificationId() {
        return product_specificationId;
    }

    public void setProduct_specificationId(Long product_specificationId) {
        this.product_specificationId = product_specificationId;
    }

    public Integer getGood_num() {
        return good_num;
    }

    public void setGood_num(Integer good_num) {
        this.good_num = good_num;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    @Override
    public String toString() {
        return "ShopCart{" +
                "id=" + id +
                ", userid=" + userid +
                ", productId=" + productId +
                ", product_specificationId=" + product_specificationId +
                ", good_num=" + good_num +
                ", create_at=" + create_at +
                ", update_at=" + update_at +
                '}';
    }
}
