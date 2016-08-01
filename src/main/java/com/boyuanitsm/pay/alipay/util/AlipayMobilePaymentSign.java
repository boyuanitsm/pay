package com.boyuanitsm.pay.alipay.util;

import com.boyuanitsm.pay.alipay.config.AlipayConfig;
import com.boyuanitsm.pay.alipay.sign.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 移动支付 签名工具类
 *
 * @author hookszhang on 7/19/16.
 * @see <a href="https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.y40CRD&treeId=59&articleId=103927&docType=1">https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.y40CRD&treeId=59&articleId=103927&docType=1</a>
 */
public class AlipayMobilePaymentSign {

    private static Logger log = LoggerFactory.getLogger(AlipayMobilePaymentSign.class);

    /**
     * 支付接口待签名字符串生成
     *
     * @param outTradeNO
     * @param subject
     * @param totalFee
     * @return
     */
    public static String pay(String outTradeNO, String subject, String totalFee) throws UnsupportedEncodingException {
        String[] parameters = new String[]{
                String.format("service=\"%s\"", AlipayConfig.mobile_securitypay_pay),
                String.format("partner=\"%s\"", AlipayConfig.partner),
                String.format("_input_charset=\"%s\"", AlipayConfig.input_charset),
                String.format("notify_url=\"%s\"", AlipayConfig.notify_url),
                String.format("out_trade_no=\"%s\"", outTradeNO),
                String.format("subject=\"%s\"", subject),
                String.format("payment_type=\"%s\"", AlipayConfig.payment_type),
                String.format("seller_id=\"%s\"", AlipayConfig.seller_id),
                String.format("total_fee=\"%s\"", totalFee)
        };

        String waitSignStr = "";
        log.debug("支付接口待签名字符串: {}", waitSignStr);

        for (int i = 0; i < parameters.length; i++) {
            if (i == (parameters.length - 1)) {
                waitSignStr += parameters[i];
            } else {
                waitSignStr += parameters[i] + "&";
            }
        }

        String sign = RSA.sign(waitSignStr, AlipayConfig.private_key, AlipayConfig.input_charset);
        log.debug("SignType: RSA, 签名: {}", sign);
        sign = URLEncoder.encode(sign, AlipayConfig.input_charset);

        waitSignStr += String.format("&sign=\"%s\"", sign);
        waitSignStr += "&sign_type=\"RSA\"";// 签名类型RSA
        return waitSignStr;
    }
}
