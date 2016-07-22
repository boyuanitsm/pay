package com.boyuanitsm.pay.wxpay.service;

import com.boyuanitsm.pay.wxpay.common.Configure;
import com.boyuanitsm.pay.wxpay.common.XMLParser;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryReqData;
import com.boyuanitsm.pay.wxpay.protocol.pay_query_protocol.OrderQueryResData;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 16:04
 */
public class OrderQueryService extends BaseService{

    public OrderQueryService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.PAY_QUERY_API);
    }

    /**
     * 请求支付查询服务
     * @param orderQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(OrderQueryReqData orderQueryReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(orderQueryReqData);

        return responseString;
    }

    public OrderQueryResData query(OrderQueryReqData orderQueryReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(orderQueryReqData);

        return (OrderQueryResData) XMLParser.getObjectFromXML(responseString, OrderQueryResData.class);
    }


}
