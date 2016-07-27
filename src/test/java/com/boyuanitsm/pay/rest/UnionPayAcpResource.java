package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.unionpay.acp.Acp;
import com.boyuanitsm.pay.unionpay.acp.service.AcpService;
import com.boyuanitsm.pay.unionpay.acp.service.token.OpenCardFront;
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
}
