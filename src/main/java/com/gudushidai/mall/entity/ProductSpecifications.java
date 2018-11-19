package com.gudushidai.mall.entity;

import java.util.Date;

public class ProductSpecifications {

    private Long id;
    private Long p_id;
    private String type_name;
    private Integer type_price;
    private Integer type_stock;
    private Date create_at;
    private Date update_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getP_id() {
        return p_id;
    }

    public void setP_id(Long p_id) {
        this.p_id = p_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public Integer getType_price() {
        return type_price;
    }

    public void setType_price(Integer type_price) {
        this.type_price = type_price;
    }

    public Integer getType_stock() {
        return type_stock;
    }

    public void setType_stock(Integer type_stock) {
        this.type_stock = type_stock;
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
        return "ProductSpecifications{" +
                "id=" + id +
                ", p_id=" + p_id +
                ", type_name='" + type_name + '\'' +
                ", type_price=" + type_price +
                ", type_stock=" + type_stock +
                ", create_at=" + create_at +
                ", update_at=" + update_at +
                '}';
    }
}
