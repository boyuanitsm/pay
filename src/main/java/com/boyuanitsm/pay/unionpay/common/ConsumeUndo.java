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
 * 消费撤销
 *
 * @author hookszhang on 7/26/16.
 */
public class ConsumeUndo {

    private Logger log = LoggerFactory.getLogger(ConsumeUndo.class);

    public Map<String, String> undo(String merId, String orderId, String txnTime, String txnAmt, String origQryId) throws SignValidateFailException, HttpException {
        Map<String, String> data = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", Acp.version);            //版本号
        data.put("encoding", Acp.encoding_UTF8);          //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", "01");                     //签名方法 目前只支持01-RSA方式证书加密
        data.put("txnType", "31");                        //交易类型 31-消费撤销
        data.put("txnSubType", "00");                     //交易子类型  默认00
        data.put("bizType", "000902");                    //业务类型
        data.put("channelType", "07");                    //渠道类型，07-PC，08-手机

        /***商户接入参数***/
        data.put("merId", merId);             			  //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                      //接入类型，商户接入固定填0，不需修改
        data.put("orderId", orderId);       			  //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        data.put("txnTime", txnTime);   				  //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put("txnAmt", txnAmt);                       //【撤销金额】，消费撤销时必须和原消费金额相同
        data.put("currencyCode", "156");                  //交易币种(境内商户一般是156 人民币)
        //data.put("reqReserved", "透传信息");                 //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
        data.put("backUrl", Acp.backUrl);            //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知

        /***要调通交易以下字段必须修改***/
        data.put("origQryId", origQryId);   			  //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文**/
        Map<String, String> reqData  = AcpService.sign(data, Acp.encoding_UTF8);		//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getBackRequestUrl();							    //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData, url, Acp.encoding_UTF8);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/

        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if(!rspData.isEmpty()){
            if(!AcpService.validate(rspData, Acp.encoding_UTF8)){
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
