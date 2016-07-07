package com.boyuanitsm.pay;

import net.glxn.qrgen.javase.QRCode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Spring boot pay test.
 *
 * @author hookszhang on 7/7/16.
 */
@EnableAutoConfiguration
@RestController
public class PayTest implements EmbeddedServletContainerCustomizer {

    @RequestMapping(value = "qrcode", method = RequestMethod.GET)
    public void qrcode(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        ByteArrayOutputStream stream = QRCode.from("Hello World").stream();
        response.getOutputStream().write(stream.toByteArray());
    }

    public static void main(String[] args) throws Exception {
        System.out.println(PayProperties.getInstance().getWechat().getAppid());
        SpringApplication.run(PayTest.class, args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(9001);
    }
}
