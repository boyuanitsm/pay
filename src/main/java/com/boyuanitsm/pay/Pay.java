package com.boyuanitsm.pay;

import net.glxn.qrgen.javase.QRCode;

import java.io.*;

/**
 * @author hookszhang on 7/7/16.
 */
public class Pay {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");
        ByteArrayOutputStream stream = QRCode.from("Hello World").stream();
        OutputStream outputStream = new FileOutputStream("/Users/zhanghua/Desktop/qrcode.png");
        outputStream.write(stream.toByteArray());
    }
}
