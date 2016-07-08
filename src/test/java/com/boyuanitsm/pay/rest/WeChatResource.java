package com.boyuanitsm.pay.rest;

import com.boyuanitsm.pay.wechat.scan.common.Util;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * WeChat scan pay qrcode builder.
     *
     * @param product_id the id of the product
     * @param response http servlet response, auto inject
     */
    @RequestMapping(value = "qrcode", method = RequestMethod.GET)
    public void qrcode(String product_id, HttpServletResponse response) throws IOException {
        String qrcodeUrl = Util.buildQRcodeUrl(product_id);
        ByteArrayOutputStream stream = QRCode.from(qrcodeUrl).stream();
        response.getOutputStream().write(stream.toByteArray());
    }
}
