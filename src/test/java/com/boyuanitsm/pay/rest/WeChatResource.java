package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.wxpay.bean.AppPayParams;
import com.boyuanitsm.pay.wxpay.bean.Result;
import com.boyuanitsm.pay.wxpay.bean.SimpleOrder;
import com.boyuanitsm.pay.wxpay.business.UnifiedOrderBusiness;
import com.boyuanitsm.pay.wxpay.common.Signature;
import com.boyuanitsm.pay.wxpay.common.XMLParser;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryReqData;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryResData;
import com.boyuanitsm.pay.wxpay.protocol.refund_protocol.RefundReqData;
import com.boyuanitsm.pay.wxpay.protocol.refund_protocol.RefundResData;
import com.boyuanitsm.pay.wxpay.protocol.unified_order_protocol.UnifiedOrderReqData;
import com.boyuanitsm.pay.wxpay.protocol.unified_order_protocol.UnifiedOrderResData;
import com.boyuanitsm.pay.wxpay.service.OrderQueryService;
import com.boyuanitsm.pay.wxpay.service.RefundService;
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
    private RefundService refundService = new RefundService();

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

    /**
     * 申请退款
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * 注意：
     * 1、交易时间超过一年的订单无法提交退款；
     * 2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，要采用原来的退款单号。总退款金额不能超过用户实际支付金额。
     *
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo    商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @param deviceInfo    微信支付分配的终端设备号，与下单一致
     * @param outRefundNo   商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param totalFee      订单总金额，单位为分
     * @param refundFee     退款总金额，单位为分,可以做部分退款
     * @param opUserID      操作员帐号, 默认为商户号
     * @param refundFeeType 货币类型，符合ISO 4217标准的三位字母代码，默认为CNY（人民币）
     * @return
     */
    @RequestMapping(value = "refund", method = RequestMethod.POST)
    public RefundResData refund(String transactionID, String outTradeNo, String deviceInfo, String outRefundNo, int totalFee, int refundFee, String opUserID, String refundFeeType) {
        try {
            return refundService.refund(new RefundReqData(transactionID, outTradeNo, deviceInfo, outRefundNo, totalFee, refundFee, opUserID, refundFeeType));
        } catch (Exception e) {
            return null;
        }
    }
}
