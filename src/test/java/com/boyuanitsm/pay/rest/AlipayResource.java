package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.alipay.bean.SyncReturn;
import com.boyuanitsm.pay.alipay.bean.AyncNotify;
import com.boyuanitsm.pay.alipay.util.AlipayNotify;
import com.boyuanitsm.pay.alipay.util.AlipaySubmit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Rest controller Alipay Resource
 *
 * @author hookszhang on 7/15/16.
 */
@RestController
@RequestMapping("api/alipay")
public class AlipayResource {

    private static Logger log = LoggerFactory.getLogger(AlipayResource.class);

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void pay(String WIDout_trade_no, String WIDsubject, String WIDtotal_fee, String WIDbody, HttpServletResponse response) throws IOException {
        String sHtmlText = AlipaySubmit.buildRequest(WIDout_trade_no, WIDsubject, WIDtotal_fee, WIDbody);
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.getWriter().println(sHtmlText);
    }

    @RequestMapping(value = "refund", method = RequestMethod.POST)
    public void refund(String WIDbatch_no, String WIDbatch_num, String WIDdetail_data, HttpServletResponse response) throws IOException {
        String sHtmlText = AlipaySubmit.buildRequest(WIDbatch_no, WIDbatch_num, WIDdetail_data);
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.getWriter().println(sHtmlText);
    }

    /**
     * 支付宝服务器异步通知页面
     *
     * @param ayncNotify
     * @param request
     * @return
     */
    @RequestMapping(value = "aync_notify", method = RequestMethod.POST)
    @ResponseBody
    public String ayncnotify(AyncNotify ayncNotify, HttpServletRequest request) {
        log.info("Alipay aync notify: {}", ayncNotify);
        // 验证签名
        if (AlipayNotify.verifyRequest(request.getParameterMap())) {
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if(ayncNotify.getTrade_status().equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (ayncNotify.getTrade_status().equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            log.info("Verify aync notify success!");
            return "success";
        } else {
            log.error("Verify aync notify fail!");
            return "fail";
        }
    }

    /**
     * 支付宝页面跳转同步通知页面
     *
     * @param ayncReturn
     * @param request
     * @return
     */
    @RequestMapping(value = "sync_return", method = RequestMethod.GET)
    public String syncreturn(SyncReturn ayncReturn, HttpServletRequest request) {
        log.info("Alipay sync return: {}", ayncReturn);
        // 验证签名
        if (AlipayNotify.verifyRequest(request.getParameterMap())) {
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if(ayncReturn.getTrade_status().equals("TRADE_FINISHED") || ayncReturn.getTrade_status().equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
            }
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            log.info("Verify sync notify success!");
            //该页面可做页面美工编辑
            return ayncReturn.toString();
        } else {
            log.info("Verify sync notify fail!");
            //该页面可做页面美工编辑
            return "验证支付宝签名失败";
        }
    }
}
