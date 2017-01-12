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

package com.boyuanitsm.pay.unionpay.common;

import com.boyuanitsm.pay.unionpay.Acp;
import com.boyuanitsm.pay.unionpay.config.SDKConfig;
import com.boyuanitsm.pay.unionpay.error.SignValidateFailException;
import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 对于未收到交易结果的联机交易，商户向银联全渠道支付平台发起交易状态查询交易，查询交易结果。完成交易的过程不需要同持卡人交互，属于后台交易。交易查询类交易可由商户通过SDK向银联全渠道支付交易平台发起交易。
 * <p>
 * 对于成功的前台资金类交易，一般情况下以接收后台通知为主，若未收到后台通知(如3分钟后)，则可间隔（2的n次方秒）发起交易查询；
 * <p>
 * 对于成功的后台资金类交易，商户在通讯读超时时，可间隔（2的n次方秒）发起交易查询；
 * <p>
 * 对于失败的前台/后台资金类交易，全渠道平台不会发送后台通知；
 * <p>
 * 若收到respCode为“03、04、05”的应答时，则间隔（5分、10分、30分、60分、120分）发起交易查询。
 * <p>
 * 注：
 * <p>
 * 应答报文中，“应答码”即respCode字段，表示的是查询交易本身的应答，即查询这个动作是否成功，不代表被查询交易的状态；
 * <p>
 * 若查询动作成功，即应答码为“00“，则根据“原交易应答码”即origRespCode来判断被查询交易是否成功。此时若origRespCode为00，则表示被查询交易成功。
 * <p>
 * 交易步骤：
 * <p>
 * 1、商户发起交易状态查询交易；
 * <p>
 * 2、全渠道系统完成商户的交易处理；
 * <p>
 * 3、银联全渠道系统组织交易报文，返回给商户。
 *
 * @author hookszhang on 7/26/16.
 */
public class ConsumeStatusQuery {

    private Logger log = LoggerFactory.getLogger(ConsumeStatusQuery.class);

    /**
     * 对于未收到交易结果的联机交易，商户向银联全渠道支付平台发起交易状态查询交易，查询交易结果。完成交易的过程不需要同持卡人交互，属于后台交易。交易查询类交易可由商户通过SDK向银联全渠道支付交易平台发起交易。
     * <p>
     * 对于成功的前台资金类交易，一般情况下以接收后台通知为主，若未收到后台通知(如3分钟后)，则可间隔（2的n次方秒）发起交易查询；
     * <p>
     * 对于成功的后台资金类交易，商户在通讯读超时时，可间隔（2的n次方秒）发起交易查询；
     * <p>
     * 对于失败的前台/后台资金类交易，全渠道平台不会发送后台通知；
     * <p>
     * 若收到respCode为“03、04、05”的应答时，则间隔（5分、10分、30分、60分、120分）发起交易查询。
     * <p>
     * 注：
     * <p>
     * 应答报文中，“应答码”即respCode字段，表示的是查询交易本身的应答，即查询这个动作是否成功，不代表被查询交易的状态；
     * <p>
     * 若查询动作成功，即应答码为“00“，则根据“原交易应答码”即origRespCode来判断被查询交易是否成功。此时若origRespCode为00，则表示被查询交易成功。
     * <p>
     * 交易步骤：
     * <p>
     * 1、商户发起交易状态查询交易；
     * <p>
     * 2、全渠道系统完成商户的交易处理；
     * <p>
     * 3、银联全渠道系统组织交易报文，返回给商户。
     *
     * @param orderId 商户订单号，每次发交易测试需修改为被查询的交易的订单号
     * @param txnTime 订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
     * @return 应答报文
     * @throws SignValidateFailException
     * @throws HttpException
     */
    public Map<String, String> query(String orderId, String txnTime) throws SignValidateFailException, HttpException {
        Map<String, String> data = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", Acp.version);                 //版本号
        data.put("encoding", Acp.encoding_UTF8);               //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", "01");                          //签名方法 目前只支持01-RSA方式证书加密
        data.put("txnType", "00");                             //交易类型 00-默认
        data.put("txnSubType", "00");                          //交易子类型  默认00
        data.put("bizType", "000902");                         //业务类型

        /***商户接入参数***/
        data.put("merId", Acp.merId);                               //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

        /***要调通交易以下字段必须修改***/
        data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        data.put("txnTime", txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

        Map<String, String> reqData = AcpService.sign(data, Acp.encoding_UTF8);           //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getSingleQueryUrl();                                  //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
        Map<String, String> rspData = AcpService.post(reqData, url, Acp.encoding_UTF8);     //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

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
