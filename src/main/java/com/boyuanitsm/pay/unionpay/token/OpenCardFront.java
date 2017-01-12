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
import com.boyuanitsm.pay.unionpay.common.AcpService;
import com.boyuanitsm.pay.unionpay.config.SDKConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 银联全渠道支付开通交易用于开通银行卡的银联全渠道支付功能。
 *
 * @author hookszhang on 7/26/16.
 */
public class OpenCardFront {

    private Logger log = LoggerFactory.getLogger(OpenCardFront.class);

    /**
     * 构建银联全渠道支付开通交易用于开通银行卡的银联全渠道支付功能HTML请求报文
     * 交易步骤：
     * <p>
     * 1、持卡人通过商户页面，选择银联全渠道支付开通交易；
     * <p>
     * 2、商户组织相关报文发送至全渠道；
     * <p>
     * 3、持卡人在银联全渠道支付系统页面输入相关信息；
     * <p>
     * 4、全渠道系统完成用户的处理要求；
     * <p>
     * 5、全渠道将处理结果反馈给商户后台通知。
     *
     * @param orderId 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
     * @return 跳转到银联侧开通快捷支付的HTML代码
     */
    public String build(String orderId) {
        Map<String, String> contentData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        contentData.put("version", Acp.version);                  //版本号
        contentData.put("encoding", Acp.encoding_UTF8);            //字符集编码 可以使用UTF-8,GBK两种方式
        contentData.put("signMethod", "01");                           //签名方法 目前只支持01-RSA方式证书加密
        contentData.put("txnType", "79");                              //交易类型 11-代收
        contentData.put("txnSubType", "00");                           //交易子类型 00-默认开通
        contentData.put("bizType", "000902");                          //业务类型 Token支付
        contentData.put("channelType", "07");                          //渠道类型07-PC

        /***商户接入参数***/
        contentData.put("merId", Acp.merId);                               //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        contentData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        contentData.put("orderId", orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        contentData.put("txnTime", Acp.getCurrentTime());                           //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        contentData.put("accType", "01");                              //账号类型

        //测试环境trId固定值62000000001，生产环境的trId值请咨询银联业务。
        contentData.put("tokenPayData", "{trId=" + Acp.trId + "&tokenType=01}");

        //只支持贷记卡 必送：卡号、手机号、CVN2、有效期；验证码看业务配置（默认不要短信验证码,本测试商户777290058110097配置了需要）。
        //        Map<String, String> customerInfoMap = new HashMap<String, String>();
        //        customerInfoMap.put("phoneNo", "13552535506");                    //手机号
        //        customerInfoMap.put("cvn2", "123");                            //卡背面的cvn2三位数字
        //        customerInfoMap.put("expired", "1711");                        //有效期 年在前月在后
        //        customerInfoMap.put("certifTp", "01");                        //证件类型
        //        customerInfoMap.put("certifId", "341126197709218366");        //证件号码

        //选送卡号、手机号、证件类型+证件号、姓名
        //也可以都不送,在界面输入这些要素
        //此测试商户号777290058110097 后台开通业务只支持 贷记卡
        //Map<String,String> customerInfoMap = new HashMap<String,String>();
        //customerInfoMap.put("certifTp", "01");						//证件类型
        //customerInfoMap.put("certifId", "341126197709218366");		//证件号码
        //customerInfoMap.put("customerNm", "全渠道1");					//姓名
        //customerInfoMap.put("phoneNo", "13552535506");			    //手机号

        ////////////如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo，pin和phoneNo，cvn2，expired加密（如果这些上送的话），对敏感信息加密使用：
        //String accNo = AcpService.encryptData("6216261000000000018", "UTF-8");  //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
        //contentData.put("accNo", accNo);
        //contentData.put("encryptCertId",AcpService.getEncryptCertId());       //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
        //String customerInfoStr = AcpService.getCustomerInfoWithEncrypt(customerInfoMap,null,Acp.encoding_UTF8);
        //////////

        /////////如果商户号未开通【商户对敏感信息加密】权限那么不需对敏感信息加密使用：
        //contentData.put("accNo", "6216261000000000018");
        //String customerInfoStr = AcpService.getCustomerInfo(customerInfoMap,null,Acp.encoding_UTF8);   //前台实名认证送支付验证要素 customerInfo中要素不要加密
        ////////
        //contentData.put("customerInfo", customerInfoStr);

        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”的时候将异步通知报文post到该地址
        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        //注：如果开通失败的“返回商户”按钮也是触发frontUrl地址，点击时是按照get方法返回的，没有通知数据返回商户
        contentData.put("frontUrl", Acp.frontUrl);

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        contentData.put("backUrl", Acp.backUrl);

        //contentData.put("reqReserved", "透传字段");         				//请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
        //contentData.put("reserved", "{customPage=true}");         	//如果开通页面需要使用嵌入页面的话，请上送此用法

        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> reqData = AcpService.sign(contentData, Acp.encoding_UTF8);             //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();                             //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl, reqData, Acp.encoding_UTF8);     //生成自动跳转的Html表单
        log.debug("请求报文: {}", html);
        return html;
    }
}
