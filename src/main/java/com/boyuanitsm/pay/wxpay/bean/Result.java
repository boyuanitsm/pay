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

package com.boyuanitsm.pay.wxpay.bean;

/**
 * 商户处理后同步返回给微信参数
 *
 * @author hookszhang on 7/21/16.
 */
public class Result {

    public Result(String return_code, String return_msg) {
        this.return_code = return_code;
        this.return_msg = return_msg;
    }

    private String return_code;
    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                '}';
    }
}
