package com.boyuanitsm.pay.wxpay.bean;

/**
 * 统一下单 需要参数
 *
 * @author hookszhang on 7/21/16.
 */
public class SimpleOrder {

    public SimpleOrder(String body, String tradeNo, int totalFee, String productId) {
        this.body = body;
        this.tradeNo = tradeNo;
        this.totalFee = totalFee;
        this.productId = productId;
    }

    private String body;
    private String tradeNo;
    private int totalFee;
    private String productId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
