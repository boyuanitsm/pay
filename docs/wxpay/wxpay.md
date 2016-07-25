# 微信支付 API

> [微信支付开发文档](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=1_1)

## 统一下单
除被扫支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
#### 方法

```
UnifiedOrderBusiness unifiedOrderBusiness = new UnifiedOrderBusiness();
UnifiedOrderResData resData = unifiedOrderBusiness.run(new UnifiedOrderReqData(new SimpleOrder(String, String, int, String)));
```

#### 方法参数
- String body 商品名称
- String tradeNo 商户订单号
- int totalFee 付款金额（分）
- String productId 商户产品ID

#### 返回
`UnifiedOrderResData`实体，为微信返回的统一下单返回结果

## 查询订单
该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
需要调用查询接口的情况：
- 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
- 调用支付接口后，返回系统错误或未知交易状态情况；
- 调用被扫支付API，返回USERPAYING的状态；
- 调用关单或撤销接口API之前，需确认支付状态；

#### 方法
```
OrderQueryService orderQueryService = new OrderQueryService();
orderQueryService.query(new OrderQueryReqData(String, String));
```
#### 方法参数

- String transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
- String outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no

#### 返回
`OrderQueryResData`实体，为微信返回的查询订单返回结果

## 申请退款

申请退款，当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。

注意：

- 交易时间超过一年的订单无法提交退款；
- 微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，要采用原来的退款单号。总退款金额不能超过用户实际支付金额。

#### 方法
```
RefundService refundService = new RefundService();
refundService.refund(new RefundReqData(String, String, String, int, int));
```
#### 方法参数
- String transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
- String outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
- String outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
- String totalFee 订单总金额，单位为分
- String refundFee 退款总金额，单位为分,可以做部分退款

#### 返回
`RefundResData`实体，为微信返回的申请退款结果

## 查询退款
提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。

#### 方法
```
RefundQueryService refundQueryService = new RefundQueryService();
refundQueryService.refundQuery(new RefundQueryReqData(String, String, String, String));
```
#### 方法参数
- String transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
- String outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
- String outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
- String refundID 来自退款API的成功返回，微信退款单号refund_id、out_refund_no、out_trade_no 、transaction_id 四个参数必填一个，如果同事存在优先级为：refund_id>out_refund_no>transaction_id>out_trade_no

#### 返回
`RefundResData`实体，为微信返回的查询退款结果

## 下载对账单
商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。

注意：
- 微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致，bill_type为REVOKED；
- 微信在次日9点启动生成前一天的对账单，建议商户10点后再获取；
- 对账单中涉及金额的字段单位为“元”。
- 对账单接口只能下载三个月以内的账单。

#### 方法
```
DownloadBillService downloadBillService = new DownloadBillService();
downloadBillService.request(new DownloadBillReqData(billDate, billType));
```
#### 方法参数
- String billDate
- String billType 账单类型
    - ALL，返回当日所有订单信息，默认值
    - SUCCESS，返回当日成功支付的订单
    - REFUND，返回当日退款订单
    - REVOKED，已撤销的订单

#### 返回
String为微信返回的下载对账单结果

## 支付结果通用通知
支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。 （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）

**注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。**

推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。

**特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。**

#### 方法
接收到通知后务必检查签名是否正确
```
boolean isSign = Signature.checkIsSignValidFromResponseString(responseString);
```

## 获得App 调起支付需要的请求参数

> [APP调起支付](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2)

#### 方法
首先需要调用`统一下单`，获得到返回值`resData`后, 得到预支付交易会话ID
```
new AppPayParams(resData.getPrepay_id())
```
#### 返回
AppPayParams实体为APP端调起支付的参数

## 获得H5 调起支付需要到请求参数

> [H5调起支付](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6)

#### 方法
首先需要调用`统一下单`，获得到返回值`resData`后, 得到预支付交易会话ID
```
new H5PayParams(resData.getPrepay_id())
```
#### 返回
H5PayParams实体为APP端调起支付的参数
