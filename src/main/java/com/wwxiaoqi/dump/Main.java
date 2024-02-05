package com.wwxiaoqi.dump;

import com.wwxiaoqi.dump.constant.Constants;
import com.wwxiaoqi.dump.utils.FileStatusUtils;
import com.wwxiaoqi.dump.utils.FileUploadUtils;
import com.wwxiaoqi.dump.utils.HashUtils;
import com.wwxiaoqi.dump.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class Main {
    private static final int WAIT_TIME = 5; // 等待时间，单位秒
    private static final String STATUS_SUCCESS = "任务成功";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LogUtils.logMessage("请在下方输入 APK 完整路径:");
        String filePath = reader.readLine();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String fileUpdateResponse = FileUploadUtils.update(filePath);
        if (fileUpdateResponse == null) {
            LogUtils.logMessage("上传失败!");
            return;
        }

        String hash = HashUtils.getHash(fileUpdateResponse);
        if (hash == null) {
            LogUtils.logMessage("哈希获取失败!");
            return;
        }
        LogUtils.logMessage("上传成功!，当前哈希：" + hash);

        while (true) {
            Future<String> future = executorService.submit(() -> FileStatusUtils.getFileStatus(hash));
            try {
                String fileStatus = future.get(WAIT_TIME, TimeUnit.SECONDS);
                if (fileStatus.contains(STATUS_SUCCESS)) {
                    LogUtils.logMessage(String.format("下载地址为：" + Constants.FILE_DOWNLOAD_ADDRESS_URL, hash));
                    break;
                } else {
                    LogUtils.logMessage("上传成功!，请等待任务执行完毕...");
                }
            } catch (TimeoutException ignored) {
            } catch (InterruptedException | ExecutionException e) {
                LogUtils.logMessage("获取文件状态时出现异常: " + e.getMessage());
                break;
            }
        }
        executorService.shutdown();
    }

}
