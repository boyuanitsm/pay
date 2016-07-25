# Wxpay API

> [微信支付开发文档](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=1_1)

## 统一下单
除被扫支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
### 方法

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
UnifiedOrderResData实体，为微信返回的统一下单返回结果

## 支付结果通用通知
支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。 （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）

**注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。**

推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。

**特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。**

### 方法
接收到通知后务必检查签名是否正确
```
boolean isSign = Signature.checkIsSignValidFromResponseString(responseString);
```

## 获得App 调起支付需要的请求参数

> [APP调起支付](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2)

### 方法
首先需要调用`统一下单`，获得到返回值`resData`后, 得到预支付交易会话ID
```
new AppPayParams(resData.getPrepay_id())
```
### 返回
AppPayParams实体为APP端调起支付的参数

## 获得H5 调起支付需要到请求参数

> [H5调起支付](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6)

### 方法
首先需要调用`统一下单`，获得到返回值`resData`后, 得到预支付交易会话ID
```
new H5PayParams(resData.getPrepay_id())
```
### 返回
H5PayParams实体为APP端调起支付的参数
