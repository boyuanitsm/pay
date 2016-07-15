package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.alipay.config.AlipayConfig;
import com.boyuanitsm.pay.alipay.util.AlipaySubmit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        //把请求参数打包成MAP
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
        sParaTemp.put("out_trade_no", WIDout_trade_no);
        sParaTemp.put("subject", WIDsubject);
        sParaTemp.put("total_fee", WIDtotal_fee);
        sParaTemp.put("body", WIDbody);

        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.getWriter().println(sHtmlText);
    }
}
