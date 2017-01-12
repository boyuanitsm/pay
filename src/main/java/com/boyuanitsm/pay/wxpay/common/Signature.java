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

package com.boyuanitsm.pay.wxpay.common;

import com.boyuanitsm.pay.PayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * WeChat scan pay signature algorithm
 *
 * @author rizenguo hookszhang
 */
public class Signature {

    private static Logger log = LoggerFactory.getLogger(Signature.class);

    private static String packageKeyword(String name) {
        if ("_package".equals(name)) {
            name = "package";
        }
        return name;
    }

    /**
     * 签名算法
     *
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getSign(Object o) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                String name = f.getName();
                // 处理package关键字
                name = packageKeyword(name);
                list.add(name + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + PayProperties.getInstance().getWechat().getKey();
        log.debug("Sign Before MD5: {}", result);
        result = MD5.MD5Encode(result).toUpperCase();
        log.debug("Sign Result: {}", result);
        return result;
    }

    public static String getSign(Map<String, Object> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + PayProperties.getInstance().getWechat().getKey();
        log.debug("Sign Before MD5: {}", result);
        result = MD5.MD5Encode(result).toUpperCase();
        log.debug("Sign Result: {}", result);
        return result;
    }

    /**
     * 从API返回的XML数据里面重新计算一次签名
     *
     * @param responseString API返回的XML数据
     * @return 新鲜出炉的签名
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static String getSignFromResponseString(String responseString) throws IOException, SAXException, ParserConfigurationException {
        Map<String, Object> map = XMLParser.getMapFromXML(responseString);
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        return Signature.getSign(map);
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     *
     * @param responseString API返回的XML数据字符串
     * @return API签名是否合法
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean checkIsSignValidFromResponseString(String responseString) throws ParserConfigurationException, IOException, SAXException {

        Map<String, Object> map = XMLParser.getMapFromXML(responseString);
        log.debug(map.toString());

        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse == null || signFromAPIResponse.isEmpty()) {
            log.warn("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
        // Util.log("服务器回包里面的签名是:" + signFromAPIResponse);
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String signForAPIResponse = Signature.getSign(map);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {
            //签名验不过，表示这个API返回的数据有可能已经被篡改了
            log.warn("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
            return false;
        }
        // Util.log("恭喜，API返回的数据签名验证通过!!!");
        return true;
    }

}
