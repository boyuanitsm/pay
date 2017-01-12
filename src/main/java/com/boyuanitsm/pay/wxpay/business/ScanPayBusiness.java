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
import com.boyuanitsm.pay.wxpay.protocol.pay_protocol.ScanPayReqData;
import com.boyuanitsm.pay.wxpay.common.Configure;
import com.boyuanitsm.pay.wxpay.common.Signature;
import com.boyuanitsm.pay.wxpay.common.Util;
import com.boyuanitsm.pay.wxpay.common.report.service.ReportService;
import com.boyuanitsm.pay.wxpay.protocol.pay_protocol.ScanPayResData;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryReqData;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryResData;
import com.boyuanitsm.pay.wxpay.protocol.reverse_protocol.ReverseReqData;
import com.boyuanitsm.pay.wxpay.protocol.reverse_protocol.ReverseResData;
import com.boyuanitsm.pay.wxpay.service.ReverseService;
import com.boyuanitsm.pay.wxpay.service.OrderQueryService;
import com.boyuanitsm.pay.wxpay.service.ScanPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

/**
 * User: rizenguo
 * Date: 2014/12/1
 * Time: 17:05
 */
public class ScanPayBusiness {

    public ScanPayBusiness() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        scanPayService = new ScanPayService();
        orderQueryService = new OrderQueryService();
        reverseService = new ReverseService();
    }

    public interface ResultListener {

        //API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
        void onFailByReturnCodeError(ScanPayResData scanPayResData);

        //API返回ReturnCode为FAIL，支付API系统返回失败，请检测Post给API的数据是否规范合法
        void onFailByReturnCodeFail(ScanPayResData scanPayResData);

        //支付请求API返回的数据签名验证失败，有可能数据被篡改了
        void onFailBySignInvalid(ScanPayResData scanPayResData);


        //用户用来支付的二维码已经过期，提示收银员重新扫一下用户微信“刷卡”里面的二维码
        void onFailByAuthCodeExpire(ScanPayResData scanPayResData);

        //授权码无效，提示用户刷新一维码/二维码，之后重新扫码支付"
        void onFailByAuthCodeInvalid(ScanPayResData scanPayResData);

        //用户余额不足，换其他卡支付或是用现金支付
        void onFailByMoneyNotEnough(ScanPayResData scanPayResData);

        //支付失败
        void onFail(ScanPayResData scanPayResData);

        //支付成功
        void onSuccess(ScanPayResData scanPayResData);

    }

    //打log用
    private static Logger log = LoggerFactory.getLogger(ScanPayBusiness.class);

    //每次调用订单查询API时的等待时间，因为当出现支付失败的时候，如果马上发起查询不一定就能查到结果，所以这里建议先等待一定时间再发起查询

    private int waitingTimeBeforePayQueryServiceInvoked = 5000;

    //循环调用订单查询API的次数
    private int payQueryLoopInvokedCount = 3;

    //每次调用撤销API的等待时间
    private int waitingTimeBeforeReverseServiceInvoked = 5000;

    private ScanPayService scanPayService;

    private OrderQueryService orderQueryService;

    private ReverseService reverseService;

    /**
     * 直接执行被扫支付业务逻辑（包含最佳实践流程）
     *
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public void run(ScanPayReqData scanPayReqData, ResultListener resultListener) throws Exception {

        //--------------------------------------------------------------------
        //构造请求“被扫支付API”所需要提交的数据
        //--------------------------------------------------------------------

        String outTradeNo = scanPayReqData.getOut_trade_no();

        //接受API返回
        String payServiceResponseString;

        long costTimeStart = System.currentTimeMillis();


        log.info("支付API返回的数据如下：");
        payServiceResponseString = scanPayService.request(scanPayReqData);

        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;
        log.info("api请求总耗时：" + totalTimeCost + "ms");

        //打印回包数据
        log.info(payServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        ScanPayResData scanPayResData = (ScanPayResData) Util.getObjectFromXML(payServiceResponseString, ScanPayResData.class);

        //异步发送统计请求
        //*

        ReportReqData reportReqData = new ReportReqData(
                scanPayReqData.getDevice_info(),
                Configure.PAY_API,
                (int) (totalTimeCost),//本次请求耗时
                scanPayResData.getReturn_code(),
                scanPayResData.getReturn_msg(),
                scanPayResData.getResult_code(),
                scanPayResData.getErr_code(),
                scanPayResData.getErr_code_des(),
                scanPayResData.getOut_trade_no(),
                scanPayReqData.getSpbill_create_ip()
        );
        long timeAfterReport;
        if (Configure.isUseThreadToDoReport()) {
            ReporterFactory.getReporter(reportReqData).run();
            timeAfterReport = System.currentTimeMillis();
            log.info("pay+report总耗时（异步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        } else {
            ReportService.request(reportReqData);
            timeAfterReport = System.currentTimeMillis();
            log.info("pay+report总耗时（同步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        }

        if (scanPayResData == null || scanPayResData.getReturn_code() == null) {
            log.error("【支付失败】支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
            resultListener.onFailByReturnCodeError(scanPayResData);
            return;
        }

        if (scanPayResData.getReturn_code().equals("FAIL")) {
            //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            log.error("【支付失败】支付API系统返回失败，请检测Post给API的数据是否规范合法");
            resultListener.onFailByReturnCodeFail(scanPayResData);
            return;
        } else {
            log.info("支付API系统成功返回数据");
            //--------------------------------------------------------------------
            //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
            //--------------------------------------------------------------------
            if (!Signature.checkIsSignValidFromResponseString(payServiceResponseString)) {
                log.error("【支付失败】支付请求API返回的数据签名验证失败，有可能数据被篡改了");
                resultListener.onFailBySignInvalid(scanPayResData);
                return;
            }

            //获取错误码
            String errorCode = scanPayResData.getErr_code();
            //获取错误描述
            String errorCodeDes = scanPayResData.getErr_code_des();

            if (scanPayResData.getResult_code().equals("SUCCESS")) {

                //--------------------------------------------------------------------
                //1)直接扣款成功
                //--------------------------------------------------------------------

                log.info("【一次性支付成功】");
                resultListener.onSuccess(scanPayResData);
            }else{

                //出现业务错误
                log.info("业务返回失败");
                log.info("err_code:" + errorCode);
                log.info("err_code_des:" + errorCodeDes);

                //业务错误时错误码有好几种，商户重点提示以下几种
                if (errorCode.equals("AUTHCODEEXPIRE") || errorCode.equals("AUTH_CODE_INVALID") || errorCode.equals("NOTENOUGH")) {

                    //--------------------------------------------------------------------
                    //2)扣款明确失败
                    //--------------------------------------------------------------------

                    //对于扣款明确失败的情况直接走撤销逻辑
                    doReverseLoop(outTradeNo);

                    //以下几种情况建议明确提示用户，指导接下来的工作
                    if (errorCode.equals("AUTHCODEEXPIRE")) {
                        //表示用户用来支付的二维码已经过期，提示收银员重新扫一下用户微信“刷卡”里面的二维码
                        log.warn("【支付扣款明确失败】原因是：" + errorCodeDes);
                        resultListener.onFailByAuthCodeExpire(scanPayResData);
                    } else if (errorCode.equals("AUTH_CODE_INVALID")) {
                        //授权码无效，提示用户刷新一维码/二维码，之后重新扫码支付
                        log.warn("【支付扣款明确失败】原因是：" + errorCodeDes);
                        resultListener.onFailByAuthCodeInvalid(scanPayResData);
                    } else if (errorCode.equals("NOTENOUGH")) {
                        //提示用户余额不足，换其他卡支付或是用现金支付
                        log.warn("【支付扣款明确失败】原因是：" + errorCodeDes);
                        resultListener.onFailByMoneyNotEnough(scanPayResData);
                    }
                } else if (errorCode.equals("USERPAYING")) {

                    //--------------------------------------------------------------------
                    //3)需要输入密码
                    //--------------------------------------------------------------------

                    //表示有可能单次消费超过300元，或是免输密码消费次数已经超过当天的最大限制，这个时候提示用户输入密码，商户自己隔一段时间去查单，查询一定次数，看用户是否已经输入了密码
                    if (doPayQueryLoop(payQueryLoopInvokedCount, outTradeNo)) {
                        log.info("【需要用户输入密码、查询到支付成功】");
                        resultListener.onSuccess(scanPayResData);
                    } else {
                        log.info("【需要用户输入密码、在一定时间内没有查询到支付成功、走撤销流程】");
                        doReverseLoop(outTradeNo);
                        resultListener.onFail(scanPayResData);
                    }
                } else {

                    //--------------------------------------------------------------------
                    //4)扣款未知失败
                    //--------------------------------------------------------------------

                    if (doPayQueryLoop(payQueryLoopInvokedCount, outTradeNo)) {
                        log.info("【支付扣款未知失败、查询到支付成功】");
                        resultListener.onSuccess(scanPayResData);
                    } else {
                        log.info("【支付扣款未知失败、在一定时间内没有查询到支付成功、走撤销流程】");
                        doReverseLoop(outTradeNo);
                        resultListener.onFail(scanPayResData);
                    }
                }
            }
        }
    }

    /**
     * 进行一次支付订单查询操作
     *
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @return 该订单是否支付成功
     * @throws Exception
     */
    private boolean doOnePayQuery(String outTradeNo) throws Exception {

        sleep(waitingTimeBeforePayQueryServiceInvoked);//等待一定时间再进行查询，避免状态还没来得及被更新

        String payQueryServiceResponseString;

        OrderQueryReqData orderQueryReqData = new OrderQueryReqData("",outTradeNo);
        payQueryServiceResponseString = orderQueryService.request(orderQueryReqData);

        log.info("支付订单查询API返回的数据如下：");
        log.info(payQueryServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        OrderQueryResData orderQueryResData = (OrderQueryResData) Util.getObjectFromXML(payQueryServiceResponseString, OrderQueryResData.class);
        if (orderQueryResData == null || orderQueryResData.getReturn_code() == null) {
            log.info("支付订单查询请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
            return false;
        }

        if (orderQueryResData.getReturn_code().equals("FAIL")) {
            //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            log.info("支付订单查询API系统返回失败，失败信息为：" + orderQueryResData.getReturn_msg());
            return false;
        } else {
            if (orderQueryResData.getResult_code().equals("SUCCESS")) {//业务层成功
                if (orderQueryResData.getTrade_state().equals("SUCCESS")) {
                    //表示查单结果为“支付成功”
                    log.info("查询到订单支付成功");
                    return true;
                } else {
                    //支付不成功
                    log.info("查询到订单支付不成功");
                    return false;
                }
            } else {
                log.info("查询出错，错误码：" + orderQueryResData.getErr_code() + "     错误信息：" + orderQueryResData.getErr_code_des());
                return false;
            }
        }
    }

    /**
     * 由于有的时候是因为服务延时，所以需要商户每隔一段时间（建议5秒）后再进行查询操作，多试几次（建议3次）
     *
     * @param loopCount     循环次数，至少一次
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @return 该订单是否支付成功
     * @throws InterruptedException
     */
    private boolean doPayQueryLoop(int loopCount, String outTradeNo) throws Exception {
        //至少查询一次
        if (loopCount == 0) {
            loopCount = 1;
        }
        //进行循环查询
        for (int i = 0; i < loopCount; i++) {
            if (doOnePayQuery(outTradeNo)) {
                return true;
            }
        }
        return false;
    }

    //是否需要再调一次撤销，这个值由撤销API回包的recall字段决定
    private boolean needRecallReverse = false;

    /**
     * 进行一次撤销操作
     *
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @return 该订单是否支付成功
     * @throws Exception
     */
    private boolean doOneReverse(String outTradeNo) throws Exception {

        sleep(waitingTimeBeforeReverseServiceInvoked);//等待一定时间再进行查询，避免状态还没来得及被更新

        String reverseResponseString;

        ReverseReqData reverseReqData = new ReverseReqData("",outTradeNo);
        reverseResponseString = reverseService.request(reverseReqData);

        log.info("撤销API返回的数据如下：");
        log.info(reverseResponseString);
        //将从API返回的XML数据映射到Java对象
        ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(reverseResponseString, ReverseResData.class);
        if (reverseResData == null) {
            log.info("支付订单撤销请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
            return false;
        }
        if (reverseResData.getReturn_code().equals("FAIL")) {
            //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            log.info("支付订单撤销API系统返回失败，失败信息为：" + reverseResData.getReturn_msg());
            return false;
        } else {
            if (reverseResData.getResult_code().equals("FAIL")) {
                log.info("撤销出错，错误码：" + reverseResData.getErr_code() + "     错误信息：" + reverseResData.getErr_code_des());
                if (reverseResData.getRecall().equals("Y")) {
                    //表示需要重试
                    needRecallReverse = true;
                    return false;
                } else {
                    //表示不需要重试，也可以当作是撤销成功
                    needRecallReverse = false;
                    return true;
                }
            } else {
                //查询成功，打印交易状态
                log.info("支付订单撤销成功");
                return true;
            }
        }
    }


    /**
     * 由于有的时候是因为服务延时，所以需要商户每隔一段时间（建议5秒）后再进行查询操作，是否需要继续循环调用撤销API由撤销API回包里面的recall字段决定。
     *
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @throws InterruptedException
     */
    private void doReverseLoop(String outTradeNo) throws Exception {
        //初始化这个标记
        needRecallReverse = true;
        //进行循环撤销，直到撤销成功，或是API返回recall字段为"Y"
        while (needRecallReverse) {
            if (doOneReverse(outTradeNo)) {
                return;
            }
        }
    }

    /**
     * 设置循环多次调用订单查询API的时间间隔
     *
     * @param duration 时间间隔，默认为10秒
     */
    public void setWaitingTimeBeforePayQueryServiceInvoked(int duration) {
        waitingTimeBeforePayQueryServiceInvoked = duration;
    }

    /**
     * 设置循环多次调用订单查询API的次数
     *
     * @param count 调用次数，默认为三次
     */
    public void setPayQueryLoopInvokedCount(int count) {
        payQueryLoopInvokedCount = count;
    }

    /**
     * 设置循环多次调用撤销API的时间间隔
     *
     * @param duration 时间间隔，默认为5秒
     */
    public void setWaitingTimeBeforeReverseServiceInvoked(int duration) {
        waitingTimeBeforeReverseServiceInvoked = duration;
    }

    public void setScanPayService(ScanPayService service) {
        scanPayService = service;
    }

    public void setOrderQueryService(OrderQueryService service) {
        orderQueryService = service;
    }

    public void setReverseService(ReverseService service) {
        reverseService = service;
    }

}
