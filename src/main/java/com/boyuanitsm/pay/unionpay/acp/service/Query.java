package com.boyuanitsm.pay.unionpay.acp.service;

import com.boyuanitsm.pay.unionpay.acp.AcpService;
import com.boyuanitsm.pay.unionpay.acp.DemoBase;
import com.boyuanitsm.pay.unionpay.acp.SDKConfig;
import com.boyuanitsm.pay.unionpay.acp.exception.SignValidateFailException;
import com.boyuanitsm.pay.unionpay.acp.service.token.DeleteToken;
import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易状态查询
 *
 * @author hookszhang on 7/26/16.
 */
public class Query {

    private Logger log = LoggerFactory.getLogger(Query.class);

    public Map<String, String> query(String merId, String orderId, String txnTime) throws SignValidateFailException, HttpException {
        Map<String, String> data = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", DemoBase.version);                 //版本号
        data.put("encoding", DemoBase.encoding_UTF8);               //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", "01");                          //签名方法 目前只支持01-RSA方式证书加密
        data.put("txnType", "00");                             //交易类型 00-默认
        data.put("txnSubType", "00");                          //交易子类型  默认00
        data.put("bizType", "000902");                         //业务类型

        /***商户接入参数***/
        data.put("merId", merId);                               //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

        /***要调通交易以下字段必须修改***/
        data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        data.put("txnTime", txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

        Map<String, String> reqData = AcpService.sign(data, DemoBase.encoding_UTF8);           //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getSingleQueryUrl();                                  //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
        Map<String, String> rspData = AcpService.post(reqData, url, DemoBase.encoding_UTF8);     //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()) {
            if (!AcpService.validate(rspData, DemoBase.encoding_UTF8)) {
                throw new SignValidateFailException("验证签名失败");
            }
        } else {
            //未返回正确的http状态
            throw new HttpException("未获取到返回报文或返回http状态码非200");
        }

        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);

        log.debug("Request : {}", reqMessage);
        log.debug("Response : {}", rspMessage);

        return rspData;
    }

}
