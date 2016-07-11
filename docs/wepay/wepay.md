## 微信扫码支付 API

本SDK是基于[wxpay_scanpay_java_sdk_proj](https://github.com/grz/wxpay_scanpay_java_sdk_proj)开发，这里对grz表示感谢🙏

有任何疑问请发邮件给我（hua.zhang@boyuanitsm.com）, 也可以直接在GitLab上提issue，感谢大家的贡献

### 快速上手

- [场景介绍](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_1)
- [案例及规范](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_2)
- 开发步骤
    - [模式一]()
- 后期工作
    - [查询订单]()
    - [关闭订单]()
    - [申请退款]()
    - [查询退款]()
    - [下载对账单]()
    - [转换短链接]()

### 开发步骤

#### 模式一

以下均以Spring MVC为例, log使用slf4j

> [微信开发者文档](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4)

##### 构造微信扫码支付的二维码

```java
/**
 * 构造微信支付的二维码
 *
 * @param product_id 产品ID
 * @param response http servlet response, auto inject
 */
@RequestMapping(value = "qrcode", method = RequestMethod.GET)
public void qrcode(String product_id, HttpServletResponse response) throws IOException {
    String qrcodeUrl = Util.buildQRcodeUrl(product_id);
    log.debug("QRCode url is: {}", qrcodeUrl);
    ByteArrayOutputStream stream = QRCode.from(qrcodeUrl).stream();
    response.getOutputStream().write(stream.toByteArray());
}
```

##### 回调商户支付URL

```java
/**
 * 回调商户支付URL, 商户提供的支付回调URL（回调地址设置）需要实现以下功能：接收用户扫码后微信支付系统发送的数据，根据接收的数据生成支付订单，调用【统一下单API】提交支付交易。
 *
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4</a>
 * @param request http request
 * @return
 * @throws IOException
 * @throws ParserConfigurationException
 * @throws SAXException
 */
@RequestMapping(value = "pay_callback", method = RequestMethod.POST)
public String payCallback(HttpServletRequest request) throws Exception {
    InputStream inputStream = request.getInputStream();
    String responseString = IOUtils.toString(inputStream);
    log.debug("Pay callback response string is: {}", responseString);
    // 检查签名
    boolean isSignValid = Signature.checkIsSignValidFromResponseString(responseString);
    // 输出结果
    Map<String, Object> result = new HashMap<>();

    if (isSignValid) {
        result.put("return_code", "SUCCESS");
        result.put("appid", Configure.getAppid());
        result.put("mch_id", Configure.getMchid());
        result.put("nonce_str", RandomStringGenerator.getRandomStringByLength(Configure.NONCE_STR_LENGTH));
        // 调用统一下单API
        UnifiedOrderBusiness unifiedOrderBusiness = new UnifiedOrderBusiness();
        UnifiedOrderResData resData = unifiedOrderBusiness.run(getUnifiedOrderReqDataTest(responseString));
        log.info("预支付交易会话标识: {}", resData.getPrepay_id());
        result.put("prepay_id", resData.getPrepay_id());
        result.put("result_code", "SUCCESS");
    } else {
        result.put("return_code", "FAIL");
        result.put("return_msg", "签名失败");
    }

    // 签名
    result.put("sign", Signature.getSign(request));

    String xml = XMLParser.getXMLFromMap(result);
    log.debug("Pay callback return string is: {}", xml);
    return xml;
}

private UnifiedOrderReqData getUnifiedOrderReqDataTest(String responseString) {
    Map<String, Object> map = new HashMap<>();
    String product_id = String.valueOf(map.get("product_id"));
    String notify_url = "localhost/api/wechat/pay_result_callback";
    int total_fee = 1;// 1分钱
    return new UnifiedOrderReqData("WePay Test", "wxtest123456", total_fee, notify_url, product_id);
}
```

##### 支付结果通用通知

> [微信开发者文档](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7)

```java
/**
 * 支付结果通用通知
 *
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7</a>
 * @param request
 * @return
 */
@RequestMapping(value = "pay_result_callback", method = RequestMethod.POST)
public String payResultCallback(HttpServletRequest request) throws IOException {
    String responseString = IOUtils.toString(request.getInputStream());
    log.debug("Pay result callback response string is: {}", responseString);
    Map<String, Object> result = new HashMap<>();
    result.put("return_code", "SUCCESS");
    result.put("return_msg", "OK");
    String xml = XMLParser.getXMLFromMap(result);
    log.debug("Pay result callback return string is: {}", xml);
    return xml;
}
```

### 后期工作

#### 查询订单
