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

package com.boyuanitsm.pay.wxpay.common.report;


import com.boyuanitsm.pay.wxpay.common.report.protocol.ReportReqData;

/**
 * User: rizenguo
 * Date: 2014/12/3
 * Time: 17:44
 */
public class ReporterFactory {

    /**
     * 请求统计上报API
     * @param reportReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return 返回一个Reporter
     */
    public static Reporter getReporter(ReportReqData reportReqData){
        return new Reporter(reportReqData);
    }

}
