/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boyuanitsm.pay.wxpay.business;

import com.boyuanitsm.pay.wxpay.common.XMLParser;
import com.boyuanitsm.pay.wxpay.protocol.unified_order_protocol.UnifiedOrderResData;
import com.boyuanitsm.pay.wxpay.common.Configure;
import com.boyuanitsm.pay.wxpay.common.Signature;
import com.boyuanitsm.pay.wxpay.protocol.unified_order_protocol.UnifiedOrderReqData;
import com.boyuanitsm.pay.wxpay.service.BaseService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * 统一下单. 应用场景: 除被扫支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
 * 接口链接: URL地址：https://api.mch.weixin.qq.com/pay/unifiedorder
 *
 * @see <a href="https://api.mch.weixin.qq.com/pay/unifiedorder">https://api.mch.weixin.qq.com/pay/unifiedorder</a>
 * @author hookszhang on 7/8/16.
 */
public class UnifiedOrderBusiness extends BaseService{

    public UnifiedOrderBusiness() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.UNIFIFED_ORDER_API);
    }

    public UnifiedOrderResData run(UnifiedOrderReqData unifiedOrderReqData) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, ParserConfigurationException, SAXException {
        String responseString = sendPost(unifiedOrderReqData);
        boolean isSignValid = Signature.checkIsSignValidFromResponseString(responseString);
        if (!isSignValid) {
            throw new RuntimeException(String.format("验证签名失败! api: %s, response: %s", Configure.UNIFIFED_ORDER_API, responseString));
        }
        UnifiedOrderResData unifiedOrderResData = (UnifiedOrderResData) XMLParser.getObjectFromXML(responseString, UnifiedOrderResData.class);
        return unifiedOrderResData;
    }
}
