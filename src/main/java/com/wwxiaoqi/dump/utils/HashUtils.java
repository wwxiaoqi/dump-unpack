package com.wwxiaoqi.dump.utils;

public class HashUtils {

    /**
     * 获取响应体中的哈希值
     *
     * @param responseBody 响应体字符串
     * @return 哈希值字符串
     */
    public static String getHash(String responseBody) {
        String hash = "";
        hash = responseBody.split("\"hash\":")[1].split("\"")[1];
        return hash;
    }

}
