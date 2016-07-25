package com.boyuanitsm.pay.wxpay.bean;

import com.boyuanitsm.pay.wxpay.common.Configure;
import com.boyuanitsm.pay.wxpay.common.RandomStringGenerator;
import com.boyuanitsm.pay.wxpay.common.Signature;

/**
 * H5 调起支付需要的请求参数
 *
 * @author hookszhang on 7/25/16.
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6">https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6</a>
 */
public class H5PayParams {

    public H5PayParams(String prepayid) throws IllegalAccessException {
        this.appId = Configure.getAppid();
        this.timeStamp = System.currentTimeMillis() / 1000 + "";
        this.nonceStr = RandomStringGenerator.getRandomStringByLength(32);
        // 统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
        this._package = String.format("prepay_id=%s", prepayid);
        this.signType = "MD5";
        this.paySign = Signature.getSign(this);
    }

    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String _package;
    private String signType;
    private String paySign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String get_package() {
        return _package;
    }

    public void set_package(String _package) {
        this._package = _package;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
