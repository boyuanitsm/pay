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

package com.boyuanitsm.pay.unionpay.token;

import com.boyuanitsm.pay.unionpay.Acp;
import com.boyuanitsm.pay.unionpay.config.SDKConfig;
import com.boyuanitsm.pay.unionpay.common.AcpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 即消费交易和开通交易两者合一，发往前台交易地址。
 *
 * @author hookszhang on 7/26/16.
 */
public class OpenAndConsume {

    private Logger log = LoggerFactory.getLogger(OpenAndConsume.class);

    /**
     * 构建即消费交易和开通交易两者合一，发往前台交易地址HTML报文
     *
     * @param orderId 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
     * @param txnAmt 交易金额，单位分，不要带小数点
     * @param accNo 银联卡号 这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
     * @return
     */
    public String build(String orderId, String txnAmt, String accNo) {
        Map<String, String> contentData = new HashMap<>();
        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        contentData.put("version", Acp.version);                   //版本号
        contentData.put("encoding", Acp.encoding_UTF8);            //字符集编码 可以使用UTF-8,GBK两种方式
        contentData.put("signMethod", "01");                           //签名方法 目前只支持01-RSA方式证书加密
        contentData.put("txnType", "01");                              //交易类型 01-消费
        contentData.put("txnSubType", "01");                           //交易子类型 01-消费
        contentData.put("bizType", "000301");                          //业务类型  000301
        contentData.put("channelType", "07");                          //渠道类型07-PC

        /***商户接入参数***/
        contentData.put("merId", Acp.merId);                               //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        contentData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        contentData.put("orderId", orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        contentData.put("txnTime", Acp.getCurrentTime());                           //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        contentData.put("currencyCode", "156");                           //交易币种（境内商户一般是156 人民币）
        contentData.put("txnAmt", txnAmt);                               //交易金额，单位分，不要带小数点
        contentData.put("accType", "01");                              //账号类型

        //contentData.put("reqReserved", "透传字段");        			   //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
        //contentData.put("reserved", "{customPage=true}");         	//如果开通并支付页面需要使用嵌入页面的话，请上送此用法

        ////////////【开通并付款卡号必送】如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo加密：
        String accNo1 = AcpService.encryptData(accNo, "UTF-8");            //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
        contentData.put("accNo", accNo1);
        contentData.put("encryptCertId", AcpService.getEncryptCertId());    //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下

        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”的时候将异步通知报文post到该地址
        //注：如果开通失败的“返回商户”按钮也是触发frontUrl地址，点击时是按照get方法返回的，没有通知数据返回商户
        contentData.put("frontUrl", Acp.frontUrl);

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        contentData.put("backUrl", Acp.backUrl);

        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> reqData = AcpService.sign(contentData, Acp.encoding_UTF8);             //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();                                 //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl, reqData, Acp.encoding_UTF8);     //生成自动跳转的Html表单
        log.debug("请求报文: {}", html);
        return html;
    }

}
