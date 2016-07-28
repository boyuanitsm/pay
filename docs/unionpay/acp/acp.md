# 银联支付 无跳转Token API

> [银联官方无跳转支付文档](https://open.unionpay.com/ajweb/product/detail?id=2)

## 签名与验证签名
**前后台接收通知时一定要验证签名，以防财产损失**
```java
Map<String, String> allRequestMap = AcpService.getAllRequestParam(request.getParameterMap());
boolean isSign = AcpService.validate(allRequestMap, Acp.encoding_UTF8);
```
调用时会抛出`SignValidateFailException`异常的方法无需再次验证签名，捕获异常并根据业务逻辑处理即可

## 卡开通
银联全渠道支付开通交易用于开通银行卡的银联全渠道支付功能

交易步骤：

1、持卡人通过商户页面，选择银联全渠道支付开通交易；

2、商户组织相关报文发送至全渠道；

3、持卡人在银联全渠道支付系统页面输入相关信息；

4、全渠道系统完成用户的处理要求；

5、全渠道将处理结果反馈给商户后台通知。

#### 方法
```java
OpenCardFront openCardFront = new OpenCardFront();
String html = openCardFront.build(String);
```
#### 方法参数
- `String orderId` 商户订单号，8-40位数字字母，不能含 - 或 _ ，可以自行定制规则; 订单号商户务必留存，以备开通查询时使用

#### 返回
银联全渠道支付开通交易用于开通银行卡的银联全渠道支付功能HTML请求报文, 将这个报文输出到浏览器，Content-Type: text/html 即可实现自动提交表单到银联全渠道系统

**卡开通通知报文含有Token, 请务必保留，当第二次消费的时候使用，如果丢失，调用开通查询方法传入卡开通订单号即可查询Token**

## 开通查询
全渠道支付开通查询交易，用于查询银行卡是否已开通银联全渠道支付。

交易步骤：

1、商户发起银联全渠道支付开通查询交易交易至全渠道平台；

2、全渠道系统完成交易处理；

3、全渠道系统组织交易结果报文，返回给商户。

#### 方法
```java
OpenQuery openQuery = new OpenQuery();
Map<String, String> resData = openQuery.query(String, String);
```
#### 方法参数
- `String orderId` 商户订单号，8-40位数字字母，不能含 - 或 _ ，可以自行定制规则
- `String reqReserved` 请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节

#### 返回
开通状态应答报文，包含Token

## 短信交易（消费短信）
发送短信验证码类交易为商户提供在银行在线支付平台的辅助交易功能支持。发送短信验证码类交易可由商户通过SDK向银联全渠道支付交易平台发起交易。

交易步骤：

1、商户发起发送短信验证码交易至全渠道平台；

2、全渠道系统完成交易处理；

3、全渠道系统组织交易结果报文，返回给商户。

#### 方法
```java
ConsumeSMS consumeSMS = new ConsumeSMS();
Map<String, String> resData = consumeSMS.request(String, String, String);
```
#### 方法参数
- `String orderId` 商户订单号，8-40位数字字母，不能含 - 或 _ ，可以自行定制规则
- `String txnAmt` 交易金额，单位分，不要带小数点
- `String token` 从前台开通的后台通知中获取或者后台开通的返回报文中获取

#### 返回
应答报文

## 消费
消费是指境内外持卡人在境内外商户网站进行购物等消费时用银行卡结算的交易，经批准的消费额将即时地反映到该持卡人的账户余额上。

交易步骤：

1、商户组织消费交易报文，发送报文给全渠道系统；

2、全渠道系统完成商户的交易处理；

3、全渠道系统组织受理结果报文，返回给商户；

4、因消费交易涉及资金清算，全渠道系统发送后台通知（交易结果）给商户。

#### 方法
```java
Consume consume = new Consume();
Map<String, String> resData = consume.consume(String, String, String, String, String);
```
#### 方法参数
- `String orderId`     商户订单号，8-40位数字字母，不能含 - 或 _ ，可以自行定制规则
- `String txnAmt`      交易金额，单位分，不要带小数点
- `String token`       从前台开通的后台通知中获取或者后台开通的返回报文中获取
- `String smsCode`     短信验证码
- `String reqReserved` 请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节

#### 返回
应答报文

## 开通并支付
即消费交易和开通交易两者合一，发往前台交易地址。

#### 方法
```java
OpenAndConsume openAndConsume = new OpenAndConsume();
String html = openAndConsume.build(String, String, String);
```
#### 方法参数
- `String orderId` 商户订单号，8-40位数字字母，不能含 - 或 _ ，可以自行定制规则; 订单号商户务必留存，以备开通查询时使用
- `String txnAmt` 交易金额，单位分，不要带小数点
- `String accNo` 银联卡号 这里测试的时候使用的是测试卡号，正式环境请使用真实卡号

#### 返回
消费交易和开通交易两者合一功能HTML请求报文, 将这个报文输出到浏览器，Content-Type: text/html 即可实现自动提交表单到银联全渠道系统。

**开通并支付通知报文不含有Token, 需要调用开通查询方法传入开通并支付订单号查询Token**

## 交易状态查询
对于未收到交易结果的联机交易，商户向银联全渠道支付平台发起交易状态查询交易，查询交易结果。完成交易的过程不需要同持卡人交互，属于后台交易。交易查询类交易可由商户通过SDK向银联全渠道支付交易平台发起交易。

对于成功的前台资金类交易，一般情况下以接收后台通知为主，若未收到后台通知(如3分钟后)，则可间隔（2的n次方秒）发起交易查询；

对于成功的后台资金类交易，商户在通讯读超时时，可间隔（2的n次方秒）发起交易查询；

对于失败的前台/后台资金类交易，全渠道平台不会发送后台通知；

若收到respCode为“03、04、05”的应答时，则间隔（5分、10分、30分、60分、120分）发起交易查询。

注：应答报文中，“应答码”即respCode字段，表示的是查询交易本身的应答，即查询这个动作是否成功，不代表被查询交易的状态；

若查询动作成功，即应答码为“00“，则根据“原交易应答码”即origRespCode来判断被查询交易是否成功。此时若origRespCode为00，则表示被查询交易成功。

#### 方法
```java
ConsumeStatusQuery consumeStatusQuery = new ConsumeStatusQuery();
Map<String, String> resData = consumeStatusQuery.query(String, String);
```
#### 方法参数
- `String orderId` 商户订单号，每次发交易测试需修改为被查询的交易的订单号
- `String txnTime` 订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

#### 返回
应答报文

## 删除Token（解除绑定）
商户可通过发起解除标记交易解除之前在银联全渠道支付平台申请的Token标记。

#### 方法
```java
DeleteToken deleteToken = new DeleteToken();
Map<String, String> resData = deleteToken.delete(String, String);
```
#### 方法参数
- `String orderId` 商户订单号，每次发交易测试需修改为被查询的交易的订单号
- `String token`       从前台开通的后台通知中获取或者后台开通的返回报文中获取

## 返回
应答报文
