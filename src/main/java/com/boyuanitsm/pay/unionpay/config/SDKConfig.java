/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package com.boyuanitsm.pay.unionpay.config;

import com.boyuanitsm.pay.PayProperties;

/**
 * 软件开发工具包 配置
 * 
 * @author xuyaowen hookszhang
 * 
 */
public class SDKConfig {

	private SDKConfig() {
		// 从PayProperties中获得配置
		PayProperties.UnionPay unionpay = PayProperties.getInstance().getUnionpay();
		this.frontRequestUrl = unionpay.getFrontTransUrl();
		this.backRequestUrl = unionpay.getBackTransUrl();
		this.singleQueryUrl = unionpay.getSingleQueryUrl();
		this.batchTransUrl = unionpay.getBatchTransUrl();
		this.fileTransUrl = unionpay.getFileTransUrl();
		this.signCertPath = unionpay.getSignCertPath();
		this.signCertPwd = unionpay.getSignCertPwd();
		this.signCertType = unionpay.getSignCertType();
		this.encryptCertPath = unionpay.getEncryptCertPath();
		this.validateCertDir = unionpay.getValidateCertDir();
		this.cardRequestUrl = unionpay.getCardTransUrl();
		this.appRequestUrl = unionpay.getAppTransUrl();
		this.singleMode = unionpay.getSingleMode();

		this.jfFrontRequestUrl = unionpay.getJfFrontTransUrl();
		this.jfBackRequestUrl = unionpay.getJfBackTransUrl();
		this.jfSingleQueryUrl = unionpay.getJfSingleQueryUrl();
		this.jfCardRequestUrl = unionpay.getJfCardTransUrl();
		this.jfAppRequestUrl = unionpay.getJfAppTransUrl();
	}

	/** 前台请求URL. */
	private String frontRequestUrl;
	/** 后台请求URL. */
	private String backRequestUrl;
	/** 单笔查询 */
	private String singleQueryUrl;
	/** 批量交易 */
	private String batchTransUrl;
	/** 文件传输 */
	private String fileTransUrl;
	/** 签名证书路径. */
	private String signCertPath;
	/** 签名证书密码. */
	private String signCertPwd;
	/** 签名证书类型. */
	private String signCertType;
	/** 加密公钥证书路径. */
	private String encryptCertPath;
	/** 验证签名公钥证书目录. */
	private String validateCertDir;
	/** 有卡交易. */
	private String cardRequestUrl;
	/** app交易 */
	private String appRequestUrl;
	/** 证书使用模式(单证书/多证书) */
	private String singleMode;

	/** 缴费相关地址 */
	private String jfFrontRequestUrl;
	private String jfBackRequestUrl;
	private String jfSingleQueryUrl;
	private String jfCardRequestUrl;
	private String jfAppRequestUrl;

	// 无用配置
	/** 磁道加密证书路径. */
	private String encryptTrackCertPath;
	/** 磁道加密公钥模数. */
	private String encryptTrackKeyModulus;
	/** 磁道加密公钥指数. */
	private String encryptTrackKeyExponent;

	/** 操作对象. */
	private static SDKConfig config = new SDKConfig();

	/**
	 * 获取config对象.
	 * 
	 * @return
	 */
	public static SDKConfig getConfig() {
		if (null == config) {
			config = new SDKConfig();
		}
		return config;
	}

	public String getFrontRequestUrl() {
		return frontRequestUrl;
	}

	public String getBackRequestUrl() {
		return backRequestUrl;
	}

	public String getSingleQueryUrl() {
		return singleQueryUrl;
	}

	public String getBatchTransUrl() {
		return batchTransUrl;
	}

	public String getFileTransUrl() {
		return fileTransUrl;
	}

	public String getSignCertPath() {
		return signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public String getSignCertType() {
		return signCertType;
	}

	public String getEncryptCertPath() {
		return encryptCertPath;
	}

	public String getValidateCertDir() {
		return validateCertDir;
	}

	public String getCardRequestUrl() {
		return cardRequestUrl;
	}

	public String getAppRequestUrl() {
		return appRequestUrl;
	}

	public String getSingleMode() {
		return singleMode;
	}

	public String getJfFrontRequestUrl() {
		return jfFrontRequestUrl;
	}

	public String getJfBackRequestUrl() {
		return jfBackRequestUrl;
	}

	public String getJfSingleQueryUrl() {
		return jfSingleQueryUrl;
	}

	public String getJfCardRequestUrl() {
		return jfCardRequestUrl;
	}

	public String getJfAppRequestUrl() {
		return jfAppRequestUrl;
	}

	public String getEncryptTrackCertPath() {
		return encryptTrackCertPath;
	}

	public String getEncryptTrackKeyModulus() {
		return encryptTrackKeyModulus;
	}

	public String getEncryptTrackKeyExponent() {
		return encryptTrackKeyExponent;
	}


}
