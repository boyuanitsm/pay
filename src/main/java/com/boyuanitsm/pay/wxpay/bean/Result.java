package com.boyuanitsm.pay.wxpay.bean;

/**
 * 商户处理后同步返回给微信参数
 *
 * @author hookszhang on 7/21/16.
 */
public class Result {

    public Result(String return_code, String return_msg) {
        this.return_code = return_code;
        this.return_msg = return_msg;
    }

    private String return_code;
    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                '}';
    }
}
