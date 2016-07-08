package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.wechat.scan.common.Signature;
import com.boyuanitsm.pay.wechat.scan.common.Util;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Rest controller WeChat Resource.
 *
 * @author hookszhang on 7/8/16.
 */
@RestController
@RequestMapping("api/wechat")
public class WeChatResource {

    private static Logger log = LoggerFactory.getLogger(WeChatResource.class);

    /**
     * 构造微信支付的二维码
     *
     * @param product_id 产品ID
     * @param response http servlet response, auto inject
     */
    @RequestMapping(value = "qrcode", method = RequestMethod.GET)
    public void qrcode(String product_id, HttpServletResponse response) throws IOException {
        String qrcodeUrl = Util.buildQRcodeUrl(product_id);
        log.debug("QRCode url is: {}", qrcodeUrl);
        ByteArrayOutputStream stream = QRCode.from(qrcodeUrl).stream();
        response.getOutputStream().write(stream.toByteArray());
    }

    /**
     * 回调商户支付URL, 商户提供的支付回调URL（回调地址设置）需要实现以下功能：接收用户扫码后微信支付系统发送的数据，根据接收的数据生成支付订单，调用【统一下单API】提交支付交易。
     *
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4</a>
     * @param request http request
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @RequestMapping(value = "pay_callback", method = RequestMethod.POST)
    public String payCallback(HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException {
        InputStream inputStream = request.getInputStream();
        String responseString = IOUtils.toString(inputStream);
        log.debug("Pay callback response string is: {}", responseString);
        // 检查签名
        boolean isSignValid = Signature.checkIsSignValidFromResponseString(responseString);

        return null;
    }
}
