package com.gudushidai.mall.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单项
 */
@Entity
public class OrderItem implements Serializable {
    @Id
    @GeneratedValue
    @Column
    private Integer id;
    /**
     * 订单Id
     */
    @Column
    private Integer orderId;
    /**
     * 商品Id
     */
    @Column
    private Integer productId;
    /**
     * 数量
     */
    @Column
    private Integer count;
    /**
     * 总价
     */
    @Column
    private Double subTotal;

    private Double singlePrice;

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", count=" + count +
                ", subTotal=" + subTotal +
                ", singlePrice=" + singlePrice +
                ", typeName='" + typeName + '\'' +
                ", cartId=" + cartId +
                ", product=" + product +
                '}';
    }

    public Double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(Double singlePrice) {
        this.singlePrice = singlePrice;
    }

    private String typeName;

    private Integer cartId;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    @Transient
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private static final long serialVersionUID = 1L;

    public OrderItem(Integer id, Integer orderId, Integer productId, Integer count, Double subTotal) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
        this.subTotal = subTotal;
    }

    public OrderItem() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OrderItem other = (OrderItem) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId()))
            && (this.getCount() == null ? other.getCount() == null : this.getCount().equals(other.getCount()))
            && (this.getSubTotal() == null ? other.getSubTotal() == null : this.getSubTotal().equals(other.getSubTotal()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
        result = prime * result + ((getSubTotal() == null) ? 0 : getSubTotal().hashCode());
        return result;
    }

}