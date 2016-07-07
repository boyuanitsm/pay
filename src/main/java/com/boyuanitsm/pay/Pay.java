package com.boyuanitsm.pay;

import java.io.*;

/**
 * @author hookszhang on 7/7/16.
 */
public class Pay {
    public static void main(String[] args) throws IOException {
        System.out.println(PayProperties.getInstance().getWechat().getAppid());
    }
}
