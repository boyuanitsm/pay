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


import com.boyuanitsm.pay.wxpay.protocol.reverse_protocol.ReverseReqData;
import com.boyuanitsm.pay.wxpay.common.Configure;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 16:04
 */
public class ReverseService extends BaseService{

    public ReverseService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.REVERSE_API);
    }

    /**
     * 请求撤销服务
     * @param reverseReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(ReverseReqData reverseReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(reverseReqData);

        return responseString;
    }

}
