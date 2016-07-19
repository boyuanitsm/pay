## Alipay Api

文档中的代码均以Spring Mvc为例

### 即时到账

> [https://doc.open.alipay.com/doc2/detail?treeId=62&articleId=103566&docType=1](https://doc.open.alipay.com/doc2/detail?treeId=62&articleId=103566&docType=1)

#### 即时到账交易接口

> [https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2IkYt2&treeId=62&articleId=104743&docType=1](https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2IkYt2&treeId=62&articleId=104743&docType=1)

##### 支付宝即时到账交易接口快速通道

```java
/**
 * 支付宝即时到账交易接口快速通道
 *
 * @param WIDout_trade_no 商户订单号
 * @param WIDsubject 商品名称
 * @param WIDtotal_fee 付款金额
 * @param WIDbody 商品描述
 * @param response
 * @throws IOException
 */
@RequestMapping(value = "pay", method = RequestMethod.POST)
public void pay(String WIDout_trade_no, String WIDsubject, String WIDtotal_fee, String WIDbody, HttpServletResponse response) throws IOException {
    String sHtmlText = AlipaySubmit.buildRequest(WIDout_trade_no, WIDsubject, WIDtotal_fee, WIDbody);
    response.setHeader("Content-Type", "text/html;charset=UTF-8");
    response.getWriter().println(sHtmlText);
}
```

##### 页面跳转同步

```java
/**
 * 支付宝页面跳转同步通知页面
 *
 * @param ayncReturn
 * @param request
 * @return
 */
@RequestMapping(value = "sync_return", method = RequestMethod.GET)
public String syncreturn(SyncReturn ayncReturn, HttpServletRequest request) {
    log.info("Alipay sync return: {}", ayncReturn);
    // 验证签名
    if (AlipayNotify.verifyRequest(request.getParameterMap())) {
        //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

        if(ayncReturn.getTrade_status().equals("TRADE_FINISHED") || ayncReturn.getTrade_status().equals("TRADE_SUCCESS")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序
        }
        //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

        log.info("Verify sync notify success!");
        //该页面可做页面美工编辑
        return ayncReturn.toString();
    } else {
        log.info("Verify sync notify fail!");
        //该页面可做页面美工编辑
        return "验证支付宝签名失败";
    }
}
```

##### 服务器异步通知

```java
/**
 * 支付宝服务器异步通知页面
 *
 * @param ayncNotify
 * @param request
 * @return
 */
@RequestMapping(value = "aync_notify", method = RequestMethod.POST)
@ResponseBody
public String ayncnotify(AyncNotify ayncNotify, HttpServletRequest request) {
    log.info("Alipay aync notify: {}", ayncNotify);
    // 验证签名
    if (AlipayNotify.verifyRequest(request.getParameterMap())) {
        //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

        if(ayncNotify.getTrade_status().equals("TRADE_FINISHED")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
        } else if (ayncNotify.getTrade_status().equals("TRADE_SUCCESS")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //付款完成后，支付宝系统发送该交易状态通知
        }

        //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
        log.info("Verify aync notify success!");
        return "success";
    } else {
        log.error("Verify aync notify fail!");
        return "fail";
    }
}
```

#### 即时到账有密退款接口

##### 支付宝即时到账批量退款有密接口快速通道

调用后会打开一个支付宝页面，需要输入支付密码才可以提交退款操作

```java
/**
 * 支付宝即时到账批量退款有密接口快速通道
 *
 * @param WIDbatch_no 退款批次号
 * @param WIDbatch_num 退款笔数
 * @param WIDdetail_data 退款详细数据
 * @param response
 * @throws IOException
 */
@RequestMapping(value = "refund", method = RequestMethod.POST)
public void refund(String WIDbatch_no, String WIDbatch_num, String WIDdetail_data, HttpServletResponse response) throws IOException {
    String sHtmlText = AlipaySubmit.buildRequest(WIDbatch_no, WIDbatch_num, WIDdetail_data);
    response.setHeader("Content-Type", "text/html;charset=UTF-8");
    response.getWriter().println(sHtmlText);
}
```

### 移动支付

> [https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.9OdILg&treeId=59&articleId=103563&docType=1](https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.9OdILg&treeId=59&articleId=103563&docType=1)

#### 签名

```java
/**
 * 移动支付 签名机制
 *
 * @param outTradeNO 商户网站唯一订单号
 * @param subject 商品名称
 * @param totalFee total_fee
 * @return orderStr 主要包含商户的订单信息，key=“value”形式，以&连接。
 * @throws UnsupportedEncodingException
 */
@RequestMapping(value = "mobile_payment_sign", method = RequestMethod.GET)
public String mobilePaymentSign(String outTradeNO, String subject, String totalFee) throws UnsupportedEncodingException {
    return AlipayMobilePaymentSign.pay(outTradeNO, subject, totalFee);
}
```
