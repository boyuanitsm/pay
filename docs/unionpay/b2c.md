# 银联支付 网关支付API

> [银联官方网关支付文档](https://open.unionpay.com/ajweb/product/detail?id=1)

## 消费
消费是指境内外持卡人在境内外商户网站进行购物等消费时用银行卡结算的交易，经批准的消费额将即时地反映到该持卡人的账户余额上。

#### 方法
```java
FrontConsume frontConsume = new FrontConsume();
String html = frontConsume.consume(String);
```
#### 方法参数
- `String txnAmt` 交易金额，单位分，不要带小数点

#### 返回
银联网关支付HTML请求报文， 将这个报文输出到浏览器，Content-Type: text/html 即可实现自动提交表单到银联网关支付
