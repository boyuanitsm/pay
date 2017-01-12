/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boyuanitsm.pay;

import com.esotericsoftware.yamlbeans.YamlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;

/**
 * Payment of the properties. Read classloader folder pay.yml file.
 * PayProperties.getInstance() to use it.
 *
 * @author hookszhang on 7/7/16.
 */
public class PayProperties {

    private static Logger log = LoggerFactory.getLogger(PayProperties.class);

    private PayProperties() {
    }

    private static PayProperties instance;

    private WeChat wechat;

    private Ali ali;

    private UnionPay unionpay;

    static {
        try {
            YamlReader reader = new YamlReader(new InputStreamReader(PayProperties.class.getClassLoader().getResourceAsStream("pay.yml")));
            PayProperties.instance = reader.read(PayProperties.class);
            reader.close();
        } catch (Exception e) {
            log.error("Load pay.yml error!", e);
        }
    }

    /**
     * Alipay properties
     */
    public static class Ali {

        private String partner;
        private String sellerId;
        private String privateKey;
        private String alipayPublicKey;
        private String notifyUrl;
        private String returnUrl;
        private String signType;
        private String logPath;
        private String inputCharset;
        private String antiPhishingKey;
        private String exterInvokeIp;

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getAlipayPublicKey() {
            return alipayPublicKey;
        }

        public void setAlipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getLogPath() {
            return logPath;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public String getInputCharset() {
            return inputCharset;
        }

        public void setInputCharset(String inputCharset) {
            this.inputCharset = inputCharset;
        }

        public String getAntiPhishingKey() {
            return antiPhishingKey;
        }

        public void setAntiPhishingKey(String antiPhishingKey) {
            this.antiPhishingKey = antiPhishingKey;
        }

        public String getExterInvokeIp() {
            return exterInvokeIp;
        }

        public void setExterInvokeIp(String exterInvokeIp) {
            this.exterInvokeIp = exterInvokeIp;
        }
    }

    /**
     * Wxpay properties
     */
    public static class WeChat {

        private String key;
        private String appid;
        private String mchid;
        private String submchid;
        private String certLocalPath;
        private String certPassword;
        private String useThreadToDoReport;
        private String ip;
        private String notifyUrl;

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMchid() {
            return mchid;
        }

        public void setMchid(String mchid) {
            this.mchid = mchid;
        }

        public String getSubmchid() {
            return submchid;
        }

        public void setSubmchid(String submchid) {
            this.submchid = submchid;
        }

        public String getCertLocalPath() {
            return certLocalPath;
        }

        public void setCertLocalPath(String certLocalPath) {
            this.certLocalPath = certLocalPath;
        }

        public String getCertPassword() {
            return certPassword;
        }

        public void setCertPassword(String certPassword) {
            this.certPassword = certPassword;
        }

        public String getUseThreadToDoReport() {
            return useThreadToDoReport;
        }

        public void setUseThreadToDoReport(String useThreadToDoReport) {
            this.useThreadToDoReport = useThreadToDoReport;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    /**
     * UnionPay properties
     */
    public static class UnionPay {

        private String merId;
        private String trId;
        private String frontUrl;
        private String backUrl;
        private String frontTransUrl;
        private String backTransUrl;
        private String singleQueryUrl;
        private String batchTransUrl;
        private String fileTransUrl;
        private String appTransUrl;
        private String cardTransUrl;
        private String jfFrontTransUrl;
        private String jfBackTransUrl;
        private String jfSingleQueryUrl;
        private String jfCardTransUrl;
        private String jfAppTransUrl;
        private String signCertPath;
        private String signCertPwd;
        private String signCertType;
        private String validateCertDir;
        private String singleMode;
        private String encryptCertPath;

        public String getMerId() {
            return merId;
        }

        public void setMerId(String merId) {
            this.merId = merId;
        }

        public String getTrId() {
            return trId;
        }

        public void setTrId(String trId) {
            this.trId = trId;
        }

        public String getFrontUrl() {
            return frontUrl;
        }

        public void setFrontUrl(String frontUrl) {
            this.frontUrl = frontUrl;
        }

        public String getBackUrl() {
            return backUrl;
        }

        public void setBackUrl(String backUrl) {
            this.backUrl = backUrl;
        }

        public String getEncryptCertPath() {
            return encryptCertPath;
        }

        public void setEncryptCertPath(String encryptCertPath) {
            this.encryptCertPath = encryptCertPath;
        }

        public String getFrontTransUrl() {
            return frontTransUrl;
        }

        public void setFrontTransUrl(String frontTransUrl) {
            this.frontTransUrl = frontTransUrl;
        }

        public String getBackTransUrl() {
            return backTransUrl;
        }

        public void setBackTransUrl(String backTransUrl) {
            this.backTransUrl = backTransUrl;
        }

        public String getSingleQueryUrl() {
            return singleQueryUrl;
        }

        public void setSingleQueryUrl(String singleQueryUrl) {
            this.singleQueryUrl = singleQueryUrl;
        }

        public String getBatchTransUrl() {
            return batchTransUrl;
        }

        public void setBatchTransUrl(String batchTransUrl) {
            this.batchTransUrl = batchTransUrl;
        }

        public String getFileTransUrl() {
            return fileTransUrl;
        }

        public void setFileTransUrl(String fileTransUrl) {
            this.fileTransUrl = fileTransUrl;
        }

        public String getAppTransUrl() {
            return appTransUrl;
        }

        public void setAppTransUrl(String appTransUrl) {
            this.appTransUrl = appTransUrl;
        }

        public String getCardTransUrl() {
            return cardTransUrl;
        }

        public void setCardTransUrl(String cardTransUrl) {
            this.cardTransUrl = cardTransUrl;
        }

        public String getJfFrontTransUrl() {
            return jfFrontTransUrl;
        }

        public void setJfFrontTransUrl(String jfFrontTransUrl) {
            this.jfFrontTransUrl = jfFrontTransUrl;
        }

        public String getJfBackTransUrl() {
            return jfBackTransUrl;
        }

        public void setJfBackTransUrl(String jfBackTransUrl) {
            this.jfBackTransUrl = jfBackTransUrl;
        }

        public String getJfSingleQueryUrl() {
            return jfSingleQueryUrl;
        }

        public void setJfSingleQueryUrl(String jfSingleQueryUrl) {
            this.jfSingleQueryUrl = jfSingleQueryUrl;
        }

        public String getJfCardTransUrl() {
            return jfCardTransUrl;
        }

        public void setJfCardTransUrl(String jfCardTransUrl) {
            this.jfCardTransUrl = jfCardTransUrl;
        }

        public String getJfAppTransUrl() {
            return jfAppTransUrl;
        }

        public void setJfAppTransUrl(String jfAppTransUrl) {
            this.jfAppTransUrl = jfAppTransUrl;
        }

        public String getSignCertPath() {
            return signCertPath;
        }

        public void setSignCertPath(String signCertPath) {
            this.signCertPath = signCertPath;
        }

        public String getSignCertPwd() {
            return signCertPwd;
        }

        public void setSignCertPwd(String signCertPwd) {
            this.signCertPwd = signCertPwd;
        }

        public String getSignCertType() {
            return signCertType;
        }

        public void setSignCertType(String signCertType) {
            this.signCertType = signCertType;
        }

        public String getValidateCertDir() {
            return validateCertDir;
        }

        public void setValidateCertDir(String validateCertDir) {
            this.validateCertDir = validateCertDir;
        }

        public String getSingleMode() {
            return singleMode;
        }

        public void setSingleMode(String singleMode) {
            this.singleMode = singleMode;
        }

        @Override
        public String toString() {
            return "UnionPay{" +
                    "merId='" + merId + '\'' +
                    ", trId='" + trId + '\'' +
                    ", frontUrl='" + frontUrl + '\'' +
                    ", backUrl='" + backUrl + '\'' +
                    ", frontTransUrl='" + frontTransUrl + '\'' +
                    ", backTransUrl='" + backTransUrl + '\'' +
                    ", singleQueryUrl='" + singleQueryUrl + '\'' +
                    ", batchTransUrl='" + batchTransUrl + '\'' +
                    ", fileTransUrl='" + fileTransUrl + '\'' +
                    ", appTransUrl='" + appTransUrl + '\'' +
                    ", cardTransUrl='" + cardTransUrl + '\'' +
                    ", jfFrontTransUrl='" + jfFrontTransUrl + '\'' +
                    ", jfBackTransUrl='" + jfBackTransUrl + '\'' +
                    ", jfSingleQueryUrl='" + jfSingleQueryUrl + '\'' +
                    ", jfCardTransUrl='" + jfCardTransUrl + '\'' +
                    ", jfAppTransUrl='" + jfAppTransUrl + '\'' +
                    ", signCertPath='" + signCertPath + '\'' +
                    ", signCertPwd='" + signCertPwd + '\'' +
                    ", signCertType='" + signCertType + '\'' +
                    ", validateCertDir='" + validateCertDir + '\'' +
                    ", singleMode='" + singleMode + '\'' +
                    ", encryptCertPath='" + encryptCertPath + '\'' +
                    '}';
        }
    }

    public Ali getAli() {
        return ali;
    }

    public void setAli(Ali ali) {
        this.ali = ali;
    }

    public WeChat getWechat() {
        return wechat;
    }

    public void setWechat(WeChat wechat) {
        this.wechat = wechat;
    }

    public static PayProperties getInstance() {
        return instance;
    }

    public UnionPay getUnionpay() {
        return unionpay;
    }

    public void setUnionpay(UnionPay unionpay) {
        this.unionpay = unionpay;
    }
}
