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

package com.boyuanitsm.pay.wxpay.common.report.service;

import com.boyuanitsm.pay.wxpay.common.HttpsRequest;
import com.boyuanitsm.pay.wxpay.common.report.protocol.ReportReqData;
import com.boyuanitsm.pay.wxpay.common.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: rizenguo
 * Date: 2014/11/12
 * Time: 17:07
 */
public class ReportService {

    private static Logger log = LoggerFactory.getLogger(ReportService.class);

    private ReportReqData reqData ;

    /**
     * 请求统计上报API
     * @param reportReqData 这个数据对象里面包含了API要求提交的各种数据字段
     */
    public ReportService(ReportReqData reportReqData){
        reqData = reportReqData;
    }

    public String request() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String responseString = new HttpsRequest().sendPost(Configure.REPORT_API, reqData);

        log.info("   report返回的数据：" + responseString);

        return responseString;
    }

    /**
     * 请求统计上报API
     * @param reportReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String request(ReportReqData reportReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = new HttpsRequest().sendPost(Configure.REPORT_API, reportReqData);

        log.info("report返回的数据：" + responseString);

        return responseString;
    }

    /**
     * 获取time:统计发送时间，格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10 秒表示为20091225091010。时区为GMT+8 beijing。
     * @return 订单生成时间
     */
    private static String getTime(){
        //订单生成时间自然就是当前服务器系统时间咯
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

}
