### Configure

###### src/main/resource/pay.yml
```yaml
# 微信支付
wechat:
    # 这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
    # 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
    # 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改
    key: XXXXXXXXXXXXXXXX
    # 微信分配的公众号ID（开通公众号之后可以获取到）
    appid: XXXXXXXXXXXXXXXX
    # 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    mchid: 10000100
    # 受理模式下给子商户分配的子商户号
    submchid:
    # HTTPS证书的本地路径
    certLocalPath:
    # HTTPS证书密码，默认密码等于商户号MCHID
    certPassword:
    # 是否使用异步线程的方式来上报API测速，默认为异步模式
    useThreadToDoReport: true
    # 填写为本机的IP地址
    ip: 123.12.12.123
    # 支付结果通用通知URL https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7
    notifyUrl: https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7
# Alipay
ali:
    # 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    partner: 2088121145819891
    # 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
    sellerId: 2088121145819891
    # 商户的私钥,需要PKCS8格式
    # RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    privateKey:
    # 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
    publicKey:
    # 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    notifyUrl: http://商户地址/api/alipay/aync_notify
    # 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    returnUrl: http://商户地址/api/alipay/sync_return
    # 签名方式
    signType: RSA
    # 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
    logPath:
    # 字符编码格式 目前支持 gbk 或 utf-8
    inputCharset: utf-8
    # ⬇️请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可
    # 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
    antiPhishingKey:
    # 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
    exterInvokeIp:
```
