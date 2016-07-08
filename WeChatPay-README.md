# 微信WeChat“被扫支付SDK”


欢迎大家使用，以下是SDK的说明文档  
有任何疑问请随身发邮件给我，我会持续优化（grz@grzcn.com）

## 快速上手
1. [什么是“被扫支付”](#user-content-什么是被扫支付)
2. [一张图看懂整个SDK的结构](#user-content-一张图看懂整个sdk的结构)
     1. [通用层](#user-content-1通用层)
     2. [协议层](#user-content-2协议层)
     3. [服务层](#user-content-3服务层)  
     4. [业务层](#user-content-4业务层)
3. [如何使用这个SDK](#user-content-如何使用该sdk)


## 什么是“被扫支付”  
“被扫支付”是用户展示微信上“我的刷卡条码/二维码”给商户系统扫描后直接完成支付的模式，主要应用线下面对面收银的场景。
具体的场景如下：
1. 第一步用户选择被扫支付付款并打开微信，进入“我”->“钱包”->“刷卡”条码界面；（如图5.1所示）
2. 第二步收银员在商户系统操作生成支付订单，用户确认支付金额；
3. 第三步商户收银员用扫码设备扫描用户的条码/二维码，商户收银系统提交支付；
4. 第四步微信支付后台系统收到支付请求，根据验证密码规则判断是否验证用户的支付密码，不需要验证密码的交易直接发起扣款，需要验证密码的交易会弹出密码输入框（如图5.2所示）。支付成功后微信端会弹出成功页面（如图5.3所示），支付失败会弹出错误提示。
![img](https://raw.githubusercontent.com/grz/wxpay_scanpay_java_sdk_proj/master/docs/asset/scanpay.jpg "scanpay") 

## 一张图看懂整个SDK的结构：
![img](https://raw.githubusercontent.com/grz/wxpay_scanpay_java_sdk_proj/master/docs/asset/scanpay_sdk_structure.png "scanpay_sdk") 

## SDK层级详解：

### 1）通用层

![img](https://raw.githubusercontent.com/grz/wxpay_scanpay_java_sdk_proj/master/docs/asset/common_layer.png "common_layer") 
这里封装了很多非常基础的组件，供上层服务调用，其中包括以下组件：  

1.  基础配置组件（Configure）
该组件用来从wxpay.properties里面获取到跟商户相关的几个关键配置信息。
2.  HTTPS请求器（HttpsRequest）
发HTTPS请求的底层封装。
3.  随机数生成器（RandomStringGenerator）
用来生成指定长度的随机数。
4.  MD5加密算法（MD5）
5.  XMLParser（XML解析器）
由于API返回的数据是XML格式，所以SDK这里也提供了对返回的XML数据进行解析成Java对象的能力，方便大家可以快速处理API返回的数据。
6.  签名（Signature）
为了防止数据在传输过程中被篡改，所以这里要对字段做签名运算。
7.  基础工具（Util）
开发过程中用到的一些基础工具类函数。
8.  日志上报
这里会将SDK里面的模块调用情况，程序执行流程给打好详细日志，Log系统用的是SLF4J这套通用解决方案，方便对接商户系统自己的日志逻辑。
9.  性能上报
这里用的是微信支付统一的API性能上报接口进行上报，可以实现将每次调用支付API的耗时、返回码等相关数据进行上报。
（以上讲了这么多，只要使用了这个SDK，这些东西都帮大家解决掉了^_^，这就是我们为啥需要有一个SDK~）

### 2）协议层

![img](https://raw.githubusercontent.com/grz/wxpay_scanpay_java_sdk_proj/master/docs/asset/protocol_layer.png "protocol_layer")
这里跟API文档定义的字段进行一一对应，协议层这里分为两部分：  

*   第一部分是“请求数据”，这里定义了每一个API请求时需要传过去的具体数据字段；  
*   第二部分是“返回数据”，这里定义了每一个API返回时会传的具体数据字段；

以上协议在“服务层”提供的各种服务里面已经帮忙封装好，直接使用服务即可。

### 3）服务层

![img](https://raw.githubusercontent.com/grz/wxpay_scanpay_java_sdk_proj/master/docs/asset/service_layer.png "service_layer")
这里已经根据API文档封装好具体服务，供开发者直接调用。  
例如，以下代码直接调用PayService.request提交支付请求，商户只需要从自己的系统生成该服务提交协议里面要求的数据项即可：  

```java
payServiceResponseString = PayService.request(
               authCode,//auth_code:这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟
               body,//body:要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
               attach, //attach:支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回，有助于商户自己可以注明该笔消费的具体内容，方便后续的运营和记录
               outTradeNo,//out_trade_no:商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
               totalFee,//total_fee:订单总金额，单位为“分”，只能整数
               deviceInfo,//device_info:商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
               spBillCreateIP,//spBillCreateIP:订单生成的机器IP
               timeStart,//time_start:订单生成时间
               timeEnd,//time_end:订单失效时间
               goodsTag//goods_tag:商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
       );
```

### 4）业务层
![img](https://raw.githubusercontent.com/grz/wxpay_scanpay_java_sdk_proj/master/docs/asset/business_layer.png "business_layer")  
业务层是比服务更加高级的封装。业务层通过服务层向API提交请求，拿到API的返回数据之后会对返回数据做一些数据解析、签名校验、出错判断等操作。  
对于像“被扫支付”这种比较复杂和常用的业务，这里特别封装了官方建议的最佳实践流程。里面涵盖了“支付”、“支付查询”、“撤销”等几个服务和建议的流程、轮询次数、轮询间隔等。商户开发可以直接使用，也可以通过修改里面的配置来自定义自己的流程。



## 如何使用该SDK：   
请直接下载demo，demo那边也有详细的指引，商户用demo中完善的代码进行补充流程处理就可以了。[点此获取demo](https://github.com/grz/wxpay_scanpay_java_demo)        
###### ([返回目录](#user-content-快速上手))  