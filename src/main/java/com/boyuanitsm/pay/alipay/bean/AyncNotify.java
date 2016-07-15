package com.boyuanitsm.pay.alipay.bean;

/**
 * 支付宝对商户的请求数据处理完成后，会将处理的结果数据通过系统程序控制客户端页面自动跳转的方式通知给商户网站。这些处理结果数据就是页面跳转同步通知参数。
 *
 * @see <a href="https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.7EWBns&treeId=62&articleId=104743&docType=1#s1">https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.7EWBns&treeId=62&articleId=104743&docType=1#s1</a>
 * @author hookszhang on 7/15/16.
 */
public class AyncNotify {

    private String is_success;
    private String sing_type;
    private String sign;
    private String out_trade_no;
    private String subject;
    private String payment_type;
    private String exterface;
    private String trade_no;
    private String trade_status;
    private String notify_id;
    private String notify_time;
    private String notify_type;
    private String seller_email;
    private String buyer_email;
    private String seller_id;
    private String buyer_id;
    private String total_fee;
    private String body;
    private String extra_common_param;

    public String getIs_success() {
        return is_success;
    }

    public void setIs_success(String is_success) {
        this.is_success = is_success;
    }

    public String getSing_type() {
        return sing_type;
    }

    public void setSing_type(String sing_type) {
        this.sing_type = sing_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getExterface() {
        return exterface;
    }

    public void setExterface(String exterface) {
        this.exterface = exterface;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtra_common_param() {
        return extra_common_param;
    }

    public void setExtra_common_param(String extra_common_param) {
        this.extra_common_param = extra_common_param;
    }

    @Override
    public String toString() {
        return "AyncNotify{" +
                "is_success='" + is_success + '\'' +
                ", sing_type='" + sing_type + '\'' +
                ", sign='" + sign + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", subject='" + subject + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", exterface='" + exterface + '\'' +
                ", trade_no='" + trade_no + '\'' +
                ", trade_status='" + trade_status + '\'' +
                ", notify_id='" + notify_id + '\'' +
                ", notify_time='" + notify_time + '\'' +
                ", notify_type='" + notify_type + '\'' +
                ", seller_email='" + seller_email + '\'' +
                ", buyer_email='" + buyer_email + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", buyer_id='" + buyer_id + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", body='" + body + '\'' +
                ", extra_common_param='" + extra_common_param + '\'' +
                '}';
    }
}
