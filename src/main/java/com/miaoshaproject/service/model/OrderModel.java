package com.miaoshaproject.service.model;

import java.math.BigDecimal;

/**
 * @Author 86178
 * @create 2019/7/1 15:56
 */
//用户下单的交易模型
public class OrderModel {

    //交易单号
    //2019070100001
    private String id;

   //购买的商品单价 ， 若promoId非空则表示秒杀商品的价格
    private BigDecimal itemPrice;

    //购买的用户id
    private Integer userId;

    //若非空 则表示是秒杀下单
    private Integer promoId;

    //购买商品id
    private Integer itemId;

    //购买数量
    private Integer amount;

    //购买金额(总金额)  若promoId非空则表示秒杀商品的价格
    private BigDecimal orderPrice;

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
