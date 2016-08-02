package com.boyuanitsm.pay.unionpay.error;

/**
 * 签名验证失败异常
 *
 * @author hookszhang on 7/26/16.
 */
public class SignValidateFailException extends Exception {

    public SignValidateFailException(String message) {
        super(message);
    }
}
