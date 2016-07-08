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

    static {
        try {
            YamlReader reader = new YamlReader(new InputStreamReader(PayProperties.class.getClassLoader().getResourceAsStream("pay.yml")));
            PayProperties.instance = reader.read(PayProperties.class);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
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
