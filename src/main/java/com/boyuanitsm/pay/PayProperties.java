package com.boyuanitsm.pay;

import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.InputStreamReader;

/**
 * Payment of the properties. Read classloader folder pay.yml file.
 * PayProperties.getInstance() to use it.
 *
 * @author hookszhang on 7/7/16.
 */
public class PayProperties {

    private PayProperties () {
    }

    private static PayProperties instance;

    private WeChat wechat;

    private Ali ali;

    static {
        try {
            YamlReader reader = new YamlReader(new InputStreamReader(PayProperties.class.getClassLoader().getResourceAsStream("pay.yml")));
            PayProperties.instance = reader.read(PayProperties.class);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
