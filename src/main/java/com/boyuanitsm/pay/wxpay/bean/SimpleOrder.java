/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
