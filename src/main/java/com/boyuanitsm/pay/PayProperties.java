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
        private String appid;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
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
