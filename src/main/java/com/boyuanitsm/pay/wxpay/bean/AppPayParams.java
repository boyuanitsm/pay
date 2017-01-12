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

import com.boyuanitsm.pay.wxpay.common.Configure;
import com.boyuanitsm.pay.wxpay.common.RandomStringGenerator;
import com.boyuanitsm.pay.wxpay.common.Signature;

/**
 * App 调起支付需要的请求参数
 *
 * @author hookszhang on 7/21/16.
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2">https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2</a>
 */
public class AppPayParams {

    public AppPayParams(String prepayid) throws IllegalAccessException {
        this.appid = Configure.getAppid();
        this.partnerid = Configure.getMchid();
        this.prepayid = prepayid;
        // 暂填写固定值Sign=WXPay
        this._package = "Sign=WXPay";
        this.noncestr = RandomStringGenerator.getRandomStringByLength(32);
        this.timestamp = System.currentTimeMillis() / 1000 + "";
        this.sign = Signature.getSign(this);
    }

    private String appid;
    private String partnerid;
    private String prepayid;
    /**
     * 官方文档要的参数时package, 由于这个属性是java关键字, 所以要在签名的时候将前面的下划线去掉,
     * 已在签名时处理这个问题
     */
    private String _package;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String get_package() {
        return _package;
    }

    public void set_package(String _package) {
        this._package = _package;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "AppPayParams{" +
                "appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", _package='" + _package + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
