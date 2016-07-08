package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.wechat.scan.business.ScanPayBusiness;
import com.boyuanitsm.pay.wechat.scan.common.*;
import com.boyuanitsm.pay.wechat.scan.protocol.pay_protocol.ScanPayReqData;
import com.boyuanitsm.pay.wechat.scan.protocol.pay_protocol.ScanPayResData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
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
            result.put("nonce_str", RandomStringGenerator.getRandomStringByLength(24));
            // 调用统一下单API
            ScanPayBusiness scanPayBusiness = new ScanPayBusiness();
            scanPayBusiness.run(getTestScanPayReqData(responseString), new MyResultListener());
            result.put("result_code", "SUCCESS");
        } else {
            result.put("return_code", "FAIL");
            result.put("return_msg", "签名失败");
        }

        result.put("sign", Signature.getSign(request));
        //解决XStream对出现双下划线的bug
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        //将要提交给API的数据对象转换成XML格式数据Post给API
        String xml = xStream.toXML(result);
        log.debug("Pay callback return string is: {}", xml);
        return xml;
    }

    private ScanPayReqData getTestScanPayReqData(String responseString) throws IOException, SAXException, ParserConfigurationException {
        Map<String, Object> map = XMLParser.getMapFromXML(responseString);
        String productId = String.valueOf(map.get("product_id"));
        String deviceInfo = "WEB";
        String body = "WePay Test";
        String outTradeNo = "wepaytest" + System.currentTimeMillis() / 1000;
        int totalFee = 1;
        String spBillCreateIP = "127.0.0.1";
        String tradeType = "NATIVE";
        return null;
    }

    class MyResultListener implements ScanPayBusiness.ResultListener {

        @Override
        public void onFailByReturnCodeError(ScanPayResData scanPayResData) {

        }

        @Override
        public void onFailByReturnCodeFail(ScanPayResData scanPayResData) {

        }

        @Override
        public void onFailBySignInvalid(ScanPayResData scanPayResData) {

        }

        @Override
        public void onFailByAuthCodeExpire(ScanPayResData scanPayResData) {

        }

        @Override
        public void onFailByAuthCodeInvalid(ScanPayResData scanPayResData) {

        }

        @Override
        public void onFailByMoneyNotEnough(ScanPayResData scanPayResData) {

        }

        @Override
        public void onFail(ScanPayResData scanPayResData) {

        }

        @Override
        public void onSuccess(ScanPayResData scanPayResData) {

        }
    }
}
