# 银联支付 通用API

> [银联网上支付－产品大全](https://open.unionpay.com/ajweb/product)

## 签名与验证签名
**前后台接收通知时一定要验证签名，以防财产损失**
```java
Map<String, String> allRequestMap = AcpService.getAllRequestParam(request.getParameterMap());
boolean isSign = AcpService.validate(allRequestMap, Acp.encoding_UTF8);
```
调用时会抛出`SignValidateFailException`异常的方法无需再次验证签名，捕获异常并根据业务逻辑处理即可
