package com.boyuanitsm.pay.wechat.scan.common;

import com.boyuanitsm.pay.PayProperties;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test WeChat scan pay signature algorithm
 *
 * @author hookszhang on 7/7/16.
 */
public class SignatureTest {

    @Test
    public void getSignTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", PayProperties.getInstance().getWechat().getAppid());
        map.put("mch_id", PayProperties.getInstance().getWechat().getMchid());
        map.put("device_info", 1000);
        map.put("body", "test");
        map.put("nonce_str", "ibuaiVcKdpRxkhJA");
        String sign = Signature.getSign(map);
        System.out.println(sign);
    }
}
