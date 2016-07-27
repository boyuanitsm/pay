package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.unionpay.acp.Acp;
import com.boyuanitsm.pay.unionpay.acp.exception.SignValidateFailException;
import com.boyuanitsm.pay.unionpay.acp.service.AcpService;
import com.boyuanitsm.pay.unionpay.acp.service.ConsumeStatusQuery;
import com.boyuanitsm.pay.unionpay.acp.service.token.*;
import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * UnionPay Acp Resource
 *
 * @author hookszhang on 7/27/16.
 */
@RestController
@RequestMapping("api/unionpay/acp")
public class UnionPayAcpResource {

    private Logger log = LoggerFactory.getLogger(UnionPayAcpResource.class);

    private OpenCardFront openCardFront = new OpenCardFront();
    private OpenQuery openQuery = new OpenQuery();
    private ConsumeSMS consumeSMS = new ConsumeSMS();
    private Consume consume = new Consume();
    private OpenAndConsume openAndConsume = new OpenAndConsume();
    private ConsumeStatusQuery consumeStatusQuery = new ConsumeStatusQuery();
    private DeleteToken deleteToken = new DeleteToken();

    @RequestMapping("open_card_front")
    public String openCardFront(String orderId) {
        String html = openCardFront.build(orderId);
        return html;
    }

    @RequestMapping("front_notify")
    public Map<String, String> frontNotify(HttpServletRequest request) {
        Map<String, String> allRequestMap = AcpService.getAllRequestParam(request.getParameterMap());
        log.debug("Front notify: {}", allRequestMap);
        boolean isSign = AcpService.validate(allRequestMap, Acp.encoding_UTF8);
        if (!isSign) {
            log.warn("Sign validate fail!");
            return new HashMap<>();
        }
        return allRequestMap;
    }

    @RequestMapping("back_notify")
    public Map<String, String> backNotify(HttpServletRequest request) {
        Map<String, String> allRequestMap = AcpService.getAllRequestParam(request.getParameterMap());
        log.debug("Back notify: {}", allRequestMap);
        boolean isSign = AcpService.validate(allRequestMap, Acp.encoding_UTF8);
        if (!isSign) {
            log.warn("Sign validate fail!");
            return new HashMap<>();
        }
        return allRequestMap;
    }

    @RequestMapping("open_query")
    public Map<String, String> openQuery(String orderId, HttpServletRequest request) {
        try {
            return openQuery.query(orderId, null);
        } catch (SignValidateFailException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("consume_sms")
    public Map<String, String> consumeSMS(String orderId, String txnAmt, String token) {
        try {
            return consumeSMS.request(orderId, txnAmt, token);
        } catch (SignValidateFailException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("consume")
    public Map<String, String> consume(String orderId, String txnAmt, String token, String smsCode, String reqReserved) {
        try {
            return consume.consume(orderId, txnAmt, token, smsCode, reqReserved);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (SignValidateFailException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("open_and_consume")
    public String openAndConsume(String orderId, String txnAmt, String accNo) {
        return openAndConsume.build(orderId, txnAmt, accNo);
    }

    @RequestMapping("consume_status_query")
    public Map<String, String> consumeStatusQuery(String orderId, String txnTime) {
        try {
            return consumeStatusQuery.query(orderId, txnTime);
        } catch (SignValidateFailException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("delete_token")
    public Map<String, String> deleteToken(String orderId, String token) {
        try {
            return deleteToken.delete(orderId, token);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (SignValidateFailException e) {
            e.printStackTrace();
        }
        return null;
    }
}
