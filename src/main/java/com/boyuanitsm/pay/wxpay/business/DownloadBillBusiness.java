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

import com.boyuanitsm.pay.wxpay.common.report.ReporterFactory;
import com.boyuanitsm.pay.wxpay.common.report.protocol.ReportReqData;
import com.boyuanitsm.pay.wxpay.common.Configure;
import com.boyuanitsm.pay.wxpay.common.Util;
import com.boyuanitsm.pay.wxpay.common.report.service.ReportService;
import com.boyuanitsm.pay.wxpay.protocol.downloadbill_protocol.DownloadBillReqData;
import com.boyuanitsm.pay.wxpay.protocol.downloadbill_protocol.DownloadBillResData;
import com.boyuanitsm.pay.wxpay.service.DownloadBillService;
import com.thoughtworks.xstream.io.StreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rizenguo hookszhang
 */
public class DownloadBillBusiness {

    public DownloadBillBusiness() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        downloadBillService = new DownloadBillService();
    }

    public interface ResultListener{
        //API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
        void onFailByReturnCodeError(DownloadBillResData downloadBillResData);

        //API返回ReturnCode为FAIL，支付API系统返回失败，请检测Post给API的数据是否规范合法
        void onFailByReturnCodeFail(DownloadBillResData downloadBillResData);

        //下载对账单失败
        void onDownloadBillFail(String response);

        //下载对账单成功
        void onDownloadBillSuccess(String response);

    }

    //打log用
    private static Logger log = LoggerFactory.getLogger(DownloadBillBusiness.class);

    //执行结果
    private static String result = "";

    private DownloadBillService downloadBillService;

    /**
     * 请求对账单下载服务
     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @return API返回的XML数据
     * @throws Exception
     */

    public void run(DownloadBillReqData downloadBillReqData, ResultListener resultListener) throws Exception {

        //--------------------------------------------------------------------
        //构造请求“对账单API”所需要提交的数据
        //--------------------------------------------------------------------

        //API返回的数据
        String downloadBillServiceResponseString;

        long costTimeStart = System.currentTimeMillis();

        //支持加载本地测试数据进行调试

        log.info("对账单API返回的数据如下：");
        downloadBillServiceResponseString = downloadBillService.request(downloadBillReqData);


        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;
        log.info("api请求总耗时：" + totalTimeCost + "ms");

        log.info(downloadBillServiceResponseString);

        DownloadBillResData downloadBillResData;

        String returnCode = "";
        String returnMsg = "";

        try {
            //注意，这里失败的时候是返回xml数据，成功的时候反而返回非xml数据
            downloadBillResData = (DownloadBillResData) Util.getObjectFromXML(downloadBillServiceResponseString, DownloadBillResData.class);

            if (downloadBillResData == null || downloadBillResData.getReturn_code() == null) {
                log.error("Case1:对账单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
                setResult("Case1:对账单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
                resultListener.onFailByReturnCodeError(downloadBillResData);
                return;
            }
            if (downloadBillResData.getReturn_code().equals("FAIL")) {
                ///注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
                log.error("Case2:对账单API系统返回失败，请检测Post给API的数据是否规范合法");
                setResult("Case2:对账单API系统返回失败，请检测Post给API的数据是否规范合法");
                resultListener.onFailByReturnCodeFail(downloadBillResData);
                returnCode = "FAIL";
                returnMsg = downloadBillResData.getReturn_msg();
            }
        } catch (StreamException e) {
            //注意，这里成功的时候是直接返回纯文本的对账单文本数据，非XML格式
            if (downloadBillServiceResponseString.equals(null) || downloadBillServiceResponseString.equals("")) {
                log.error("Case4:对账单API系统返回数据为空");
                setResult("Case4:对账单API系统返回数据为空");
                resultListener.onDownloadBillFail(downloadBillServiceResponseString);
            } else {
                log.info("Case3:对账单API系统成功返回数据");
                setResult("Case3:对账单API系统成功返回数据");
                resultListener.onDownloadBillSuccess(downloadBillServiceResponseString);
            }
            returnCode = "SUCCESS";
        } finally {

            ReportReqData reportReqData = new ReportReqData(
                    downloadBillReqData.getDevice_info(),
                    Configure.DOWNLOAD_BILL_API,
                    (int) (totalTimeCost),//本次请求耗时
                    returnCode,
                    returnMsg,
                    "",
                    "",
                    "",
                    "",
                    Configure.getIP()
            );

            long timeAfterReport;
            if(Configure.isUseThreadToDoReport()){
                ReporterFactory.getReporter(reportReqData).run();
                timeAfterReport = System.currentTimeMillis();
                log.debug("pay+report总耗时（异步方式上报）："+(timeAfterReport-costTimeStart) + "ms");
            }else{
                ReportService.request(reportReqData);
                timeAfterReport = System.currentTimeMillis();
                log.debug("pay+report总耗时（同步方式上报）："+(timeAfterReport-costTimeStart) + "ms");
            }
        }
    }

    public void setDownloadBillService(DownloadBillService service) {
        downloadBillService = service;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        DownloadBillBusiness.result = result;
    }
}
