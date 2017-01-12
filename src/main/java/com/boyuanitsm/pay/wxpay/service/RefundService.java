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

package com.boyuanitsm.pay.wxpay.service;

import com.boyuanitsm.pay.wxpay.common.XMLParser;
import com.boyuanitsm.pay.wxpay.protocol.refund_protocol.RefundReqData;
import com.boyuanitsm.pay.wxpay.common.Configure;
import com.boyuanitsm.pay.wxpay.protocol.refund_protocol.RefundResData;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 16:04
 */
public class RefundService extends BaseService{

    public RefundService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.REFUND_API);
    }

    /**
     * 请求退款服务
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String  request(RefundReqData refundReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(refundReqData);

        return responseString;
        //return (RefundResData) XMLParser.getObjectFromXML(responseString, RefundResData.class);
    }

    public RefundResData refund(RefundReqData refundReqData) throws Exception {
        return (RefundResData) XMLParser.getObjectFromXML(request(refundReqData), RefundResData.class);
    }

}
