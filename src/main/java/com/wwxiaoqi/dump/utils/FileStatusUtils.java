package com.wwxiaoqi.dump.utils;

import com.wwxiaoqi.dump.constant.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileStatusUtils {

    /**
     * 获取文件状态
     *
     * @param bash 文件名
     * @return 文件状态
     * @throws IOException IO异常
     */
    public static String getFileStatus(String bash) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(String.format(Constants.FILE_STATUS_API_URL, bash));

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

        StringBuilder queryResultBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            queryResultBuilder.append(line);
        }
        reader.close();

        return queryResultBuilder.toString();
    }

}
