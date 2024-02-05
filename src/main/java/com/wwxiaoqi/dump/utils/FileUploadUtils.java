package com.wwxiaoqi.dump.utils;

import com.wwxiaoqi.dump.constant.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUploadUtils {

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     * @return 上传结果
     * @throws IOException IO异常
     */
    public static String update(String filePath) throws IOException {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(Constants.UPDATE_API_URL);
            File fileToUpload = new File(filePath);

            HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("file", fileToUpload, ContentType.DEFAULT_BINARY, fileToUpload.getName()).build();
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                LogUtils.logError("HTTP 响应码为: " + response.getStatusLine().getStatusCode());
            }
            return null;
        } catch (IOException e) {
            LogUtils.logError("上传过程中发生 IO 异常: " + e.getMessage());
            throw e;
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    LogUtils.logError("关闭 HttpClient 时发生异常: " + e.getMessage());
                }
            }
        }
    }

}
