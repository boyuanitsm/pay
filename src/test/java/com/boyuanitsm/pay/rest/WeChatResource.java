package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.wxpay.bean.AppPayParams;
import com.boyuanitsm.pay.wxpay.bean.Result;
import com.boyuanitsm.pay.wxpay.bean.SimpleOrder;
import com.boyuanitsm.pay.wxpay.business.UnifiedOrderBusiness;
import com.boyuanitsm.pay.wxpay.common.Signature;
import com.boyuanitsm.pay.wxpay.common.XMLParser;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryReqData;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryResData;
import com.boyuanitsm.pay.wxpay.protocol.unified_order_protocol.UnifiedOrderReqData;
import com.boyuanitsm.pay.wxpay.protocol.unified_order_protocol.UnifiedOrderResData;
import com.boyuanitsm.pay.wxpay.service.OrderQueryService;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.cookie.PublicSuffixDomainFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Rest controller WeChat Resource.
 *
 * @author hookszhang on 7/8/16.
 */
@RestController
@RequestMapping("api/wechat")
public class WeChatResource {

    private static Logger log = LoggerFactory.getLogger(WeChatResource.class);

    private OrderQueryService orderQueryService = new OrderQueryService();

    public WeChatResource() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
    }

    /**
     * 统一下单
     * 除被扫支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
     *
     * @param productId 产品ID
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1</a>
     */
    @RequestMapping(value = "unifiedorder", method = RequestMethod.GET)
    public void unifiedorder(HttpServletResponse response, String productId) throws IOException {
        // 调用统一下单API
        UnifiedOrderBusiness unifiedOrderBusiness = null;
        try {
            unifiedOrderBusiness = new UnifiedOrderBusiness();
            UnifiedOrderResData resData = unifiedOrderBusiness.run(new UnifiedOrderReqData(getOrderById(productId)));
            log.debug("订单信息: {}", resData);
            // 获得二维码URL
            String qrcodeUrl = resData.getCode_url();
            // 生成二维码字节数组输出流
            ByteArrayOutputStream stream = QRCode.from(qrcodeUrl).stream();
            // 输出
            response.getOutputStream().write(stream.toByteArray());
        } catch (Exception e) {
            response.getWriter().println("ServerError");
        }
    }

    private SimpleOrder getOrderById(String productId) {
        return new SimpleOrder("WxPay Text", "wxtest" + System.currentTimeMillis(), 1, productId);
    }

    /**
     * 支付结果通用通知
     * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
     * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。 （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
     * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
     * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
     * 技术人员可登进微信商户后台扫描加入接口报警群。
     *
     * @param request the http request
     * @return success or fail
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7</a>
     */
    @RequestMapping(value = "pay_result_callback", method = RequestMethod.POST)
    public String payResultCallback(HttpServletRequest request) throws IOException {
        String responseString = IOUtils.toString(request.getInputStream());
        log.debug("Pay result callback response string is: {}", responseString);
        try {
            boolean isSign = Signature.checkIsSignValidFromResponseString(responseString);
            if (isSign) {
                // TODO 检查对应业务数据的状态，判断该通知是否已经处理过, 如果没有处理过再进行处理，如果处理过直接返回结果成功
                boolean isDealWith = false;
                if (isDealWith) {
                    // TODO 处理支付成功的业务逻辑
                    UnifiedOrderReqData reqData = (UnifiedOrderReqData) XMLParser.getObjectFromXML(responseString, UnifiedOrderReqData.class);
                } else {
                    // 处理过直接返回结果成功
                    return XMLParser.getXMLFromObject(new Result("SUCCESS", "OK"));
                }
            } else {
                // 签名验证失败, "假通知"
                log.warn("签名验证失败: {}", responseString);
                return XMLParser.getXMLFromObject(new Result("FAIL", "Sign Fail"));
            }
        } catch (Exception e) {
            log.error("pay_result_callback error!", e);
            return XMLParser.getXMLFromObject(new Result("FAIL", "Server Error"));
        }
        return XMLParser.getXMLFromObject(new Result("SUCCESS", "OK"));
    }

    /**
     * 获得App 调起支付需要的请求参数
     * APP端调起支付的参数列表
     *
     * @param productId 产品ID
     * @return
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2">https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2</a>
     */
    @RequestMapping(value = "app_pay_params", method = RequestMethod.GET)
    public AppPayParams appPayParams(String productId) {
        // 调用统一下单API
        UnifiedOrderBusiness unifiedOrderBusiness = null;
        try {
            unifiedOrderBusiness = new UnifiedOrderBusiness();
            UnifiedOrderResData resData = unifiedOrderBusiness.run(new UnifiedOrderReqData(getOrderById(productId)));
            log.debug("订单信息: {}", resData);
            // 获得预支付交易会话ID
            String prepay_id = resData.getPrepay_id();
            return new AppPayParams(prepay_id);
        } catch (Exception e) {
            log.error("app_pay_params error!", e);
            return null;
        }
    }

    /**
     * 查询订单
     * 该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * 需要调用查询接口的情况：
     * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
     * ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
     * ◆ 调用被扫支付API，返回USERPAYING的状态；
     * ◆ 调用关单或撤销接口API之前，需确认支付状态；
     *
     * @param transactionID
     * @param outTradeNo
     * @return
     */
    @RequestMapping(value = "order_query", method = RequestMethod.GET)
    public OrderQueryResData orderQuery(String transactionID, String outTradeNo) {
        try {
            return orderQueryService.query(new OrderQueryReqData(transactionID, outTradeNo));
        } catch (Exception e) {
            return null;
        }
    }
}
