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
import com.boyuanitsm.pay.unionpay.error.SignValidateFailException;
import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费是指境内外持卡人在境内外商户网站进行购物等消费时用银行卡结算的交易，经批准的消费额将即时地反映到该持卡人的账户余额上。
 *
 * @author hookszhang on 7/26/16.
 */
public class Consume {

    private Logger log = LoggerFactory.getLogger(Consume.class);

    /**
     * 消费是指境内外持卡人在境内外商户网站进行购物等消费时用银行卡结算的交易，经批准的消费额将即时地反映到该持卡人的账户余额上。
     * 交易步骤：
     * <p>
     * 1、商户组织消费交易报文，发送报文给全渠道系统；
     * <p>
     * 2、全渠道系统完成商户的交易处理；
     * <p>
     * 3、全渠道系统组织受理结果报文，返回给商户；
     * <p>
     * 4、因消费交易涉及资金清算，全渠道系统发送后台通知（交易结果）给商户。
     *
     * @param orderId     商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
     * @param txnAmt      交易金额，单位分，不要带小数点
     * @param token       从前台开通的后台通知中获取或者后台开通的返回报文中获取
     * @param smsCode     短信验证码
     * @param reqReserved 请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
     * @return 应答报文
     * @throws HttpException
     * @throws SignValidateFailException
     */
    public Map<String, String> consume(String orderId, String txnAmt, String token, String smsCode, String reqReserved) throws HttpException, SignValidateFailException {
        Map<String, String> contentData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        contentData.put("version", Acp.version);                  //版本号
        contentData.put("encoding", Acp.encoding_UTF8);                //字符集编码 可以使用UTF-8,GBK两种方式
        contentData.put("signMethod", "01");                           //签名方法 目前只支持01-RSA方式证书加密
        contentData.put("txnType", "01");                              //交易类型 01-消费
        contentData.put("txnSubType", "01");                           //交易子类型 01-消费
        contentData.put("bizType", "000902");                          //业务类型 认证支付2.0
        contentData.put("channelType", "07");                          //渠道类型07-PC

        /***商户接入参数***/
        contentData.put("merId", Acp.merId);                               //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        contentData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        contentData.put("orderId", orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        contentData.put("txnTime", Acp.getCurrentTime());                           //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        contentData.put("currencyCode", "156");                           //交易币种（境内商户一般是156 人民币）
        contentData.put("txnAmt", txnAmt);                               //交易金额，单位分，不要带小数点
        contentData.put("accType", "01");                              //账号类型

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  代收产品接口规范 代收交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        contentData.put("backUrl", Acp.backUrl);

        //消费：token号（从前台开通的后台通知中获取或者后台开通的返回报文中获取），验证码看业务配置(默认要短信验证码)。
        contentData.put("tokenPayData", "{token=" + token + "&trId=" + Acp.trId + "}");
        Map<String, String> customerInfoMap = new HashMap<>();
        customerInfoMap.put("smsCode", smsCode);                    //短信验证码
        //customerInfoMap不送pin的话 该方法可以不送 卡号
        String customerInfoStr = AcpService.getCustomerInfo(customerInfoMap, null, Acp.encoding_UTF8);
        contentData.put("customerInfo", customerInfoStr);
        contentData.put("reqReserved", reqReserved);                    //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节

        //分期付款用法（商户自行设计分期付款展示界面）：
        //修改txnSubType=03，增加instalTransInfo域
        //【测试环境】固定使用测试卡号6221558812340000，测试金额固定在100-1000元之间，分期数固定填06
        //【生产环境】支持的银行列表清单请联系银联业务运营接口人索要
        //contentData.put("txnSubType", "03");                           //交易子类型 03-分期付款
        //contentData.put("instalTransInfo","{numberOfInstallments=06}");//分期付款信息域，numberOfInstallments期数

        /**对请求参数进行签名并发送http post请求，接收同步应答报文**/
        Map<String, String> reqData = AcpService.sign(contentData, Acp.encoding_UTF8);                //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();                            //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData, requestBackUrl, Acp.encoding_UTF8);    //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()) {
            if (!AcpService.validate(rspData, Acp.encoding_UTF8)) {
                throw new SignValidateFailException("验证签名失败");
            }
        } else {
            //未返回正确的http状态
            throw new HttpException("未获取到返回报文或返回http状态码非200");
        }

        String reqMessage = Acp.genHtmlResult(reqData);
        String rspMessage = Acp.genHtmlResult(rspData);

        log.debug("Request : {}", reqMessage);
        log.debug("Response : {}", rspMessage);

        return rspData;
    }
}
