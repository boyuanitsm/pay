package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.wechat.scan.bean.Result;
import com.boyuanitsm.pay.wechat.scan.business.UnifiedOrderBusiness;
import com.boyuanitsm.pay.wechat.scan.common.*;
import com.boyuanitsm.pay.wechat.scan.protocol.unified_order_protocol.UnifiedOrderReqData;
import com.boyuanitsm.pay.wechat.scan.protocol.unified_order_protocol.UnifiedOrderResData;
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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;

/**
 * Rest controller WeChat Resource.
 *
 * @author hookszhang on 7/8/16.
 */
@RestController
@RequestMapping("api/wechat")
public class WeChatResource {

    private static Logger log = LoggerFactory.getLogger(WeChatResource.class);

    // ================== 扫码支付, 模式一 ===========================

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
    public String payCallback(HttpServletRequest request) throws Exception {
        InputStream inputStream = request.getInputStream();
        String responseString = IOUtils.toString(inputStream);
        log.debug("Pay callback response string is: {}", responseString);
        // 检查签名
        boolean isSignValid = Signature.checkIsSignValidFromResponseString(responseString);
        // 输出结果
        Map<String, Object> result = new HashMap<>();

        if (isSignValid) {
            result.put("return_code", "SUCCESS");
            result.put("appid", Configure.getAppid());
            result.put("mch_id", Configure.getMchid());
            result.put("nonce_str", RandomStringGenerator.getRandomStringByLength(Configure.NONCE_STR_LENGTH));
            // 调用统一下单API
            UnifiedOrderBusiness unifiedOrderBusiness = new UnifiedOrderBusiness();
            UnifiedOrderResData resData = unifiedOrderBusiness.run(getUnifiedOrderReqDataTest(responseString));
            log.info("预支付交易会话标识: {}", resData.getPrepay_id());
            result.put("prepay_id", resData.getPrepay_id());
            result.put("result_code", "SUCCESS");
        } else {
            result.put("return_code", "FAIL");
            result.put("return_msg", "签名失败");
        }

        // 签名
        result.put("sign", Signature.getSign(request));

        String xml = XMLParser.getXMLFromObject(result);
        log.debug("Pay callback return string is: {}", xml);
        return xml;
    }

    @RequestMapping(value = "unifiedorder", method = RequestMethod.GET)
    public void unifiedorder(HttpServletResponse response) throws IllegalAccessException, ClassNotFoundException, InstantiationException, SAXException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, ParserConfigurationException, IOException {
        String notify_url = "http://180.167.77.58:30003/api/wechat/pay_result_callback";
        // 调用统一下单API
        UnifiedOrderBusiness unifiedOrderBusiness = new UnifiedOrderBusiness();
        int total_fee = 1;// 1分钱
        UnifiedOrderResData resData = unifiedOrderBusiness.run(new UnifiedOrderReqData("WxPay Test", "wxtest" + System.currentTimeMillis(), total_fee, notify_url, "1"));
        log.info(resData.toString());
        String qrcodeUrl = resData.getCode_url();
        log.debug("QRCode url is: {}", qrcodeUrl);
        ByteArrayOutputStream stream = QRCode.from(qrcodeUrl).stream();
        response.getOutputStream().write(stream.toByteArray());
    }

    private UnifiedOrderReqData getUnifiedOrderReqDataTest(String responseString) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        String product_id = String.valueOf(map.get("product_id"));
        String notify_url = "http://180.167.77.58:30003/api/wechat/pay_result_callback";
        int total_fee = 1;// 1分钱
        return new UnifiedOrderReqData("WePayTest", "wxtest123456", total_fee, notify_url, product_id);
    }


    /**
     * 支付结果通用通知
     *
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7</a>
     * @param request
     * @return
     */
    @RequestMapping(value = "pay_result_callback", method = RequestMethod.POST)
    public String payResultCallback(HttpServletRequest request) throws IOException {
        String responseString = IOUtils.toString(request.getInputStream());
        log.debug("Pay result callback response string is: {}", responseString);
        String xml = XMLParser.getXMLFromObject(new Result("SUCCESS", "OK"));
        log.debug("Pay result callback return string is: {}", xml);
        return xml;
    }

    // ================== 扫码支付, 模式一 ===========================

}
