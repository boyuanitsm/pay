package com.boyuanitsm.pay.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088121145819891";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK7c15JTPdZvMlRy\n" +
			"yru6Cd8X24aWxB1D1Kcy/2X2xfgFsYae61iCACf3DPMnyKBlSd2iNqDadfLAzItN\n" +
			"qJ2bimp7S5BP60ojdpv1w42y4mOpBgE2xKjPm1k9ZBJ7SKW1TkRFf5jIT30wzfjs\n" +
			"kkL2pf/dQ1In3DYm3eF1kRD+69NNAgMBAAECgYBMFyFfNQXFl+GIyeLaSZweDh2i\n" +
			"tVxld6zDjPeCAR4oTSHw+Jpkiz1SJrjl3jv5P9oirYGkASwuFCNtwr9KxfI2ps/y\n" +
			"zU1xnJQGszy+G3hBz0LNd73gFzp9BrdQuN87RCbrLjbkmebTaERqXCASq6rYBtBN\n" +
			"NvdsJDfGPwo6ZBrkqQJBAOcz92TyH7qT6FRlXz5EYgH70bL87wmLdrLqdlq2QuKv\n" +
			"n07cKbZClg4NJAyfJJQxMugKzk6w8XukJlAe58FRx2MCQQDBnfcggjcMf+13JWZz\n" +
			"aTq7MjctEgBJEGc+L+72nV9Ty0PXpl71gmWy2afcP1MQDb/2a7NOYdZPvB+pOI2d\n" +
			"trGPAkB+S8zgv8LFUPag459bjE9ddgnfFHwfYqe8pIdylg2Ddxw3H91JSqZdlqAS\n" +
			"pPx+V0HPr9dy9QV03P5w1fETkXCxAkAz/jaBzVX5DlV3fFyhu21hHaABg2b+N+Bx\n" +
			"q6+RHDugJeKHA49WgHjM6XQRShQFKJwXEV7qOo2xhcQ+zggBhTDzAkEAl2j5j8BX\n" +
			"RzlEG4gpKOVFst2nD5dXO2IooIoeXrRWsDJSwPxmy9qKhST2ouKXPDkqfwnZlcOd\n" +
			"+76985Xy+i+ing==";
	
	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCu3NeSUz3WbzJUcsq7ugnfF9uG\n" +
			"lsQdQ9SnMv9l9sX4BbGGnutYggAn9wzzJ8igZUndojag2nXywMyLTaidm4pqe0uQ\n" +
			"T+tKI3ab9cONsuJjqQYBNsSoz5tZPWQSe0iltU5ERX+YyE99MM347JJC9qX/3UNS\n" +
			"J9w2Jt3hdZEQ/uvTTQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "/Users/zhanghua";
		
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// 调用的接口名，无需修改
	public static String service = "create_direct_pay_by_user";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
//↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	// 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
	public static String anti_phishing_key = "";
	
	// 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
	public static String exter_invoke_ip = "";
		
//↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
}

