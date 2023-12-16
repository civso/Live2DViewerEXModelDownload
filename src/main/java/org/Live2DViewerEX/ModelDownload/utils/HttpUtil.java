package org.Live2DViewerEX.ModelDownload.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class HttpUtil {
    public String Post(String httpUrl, String param) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("param", param)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .header("Accept", "*/*")
                .header("X-Unity-Version", "2019.4.40f1")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("user-agent", "UnityPlayer/2019.4.40f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)")
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.body() != null && response.code() == 200){
                return response.body().string();
            }
            System.err.println("error:"+httpUrl+
                    " \n  The response value of the URL com is " + response.code());
            return "";
        } catch (IOException e) {
            System.err.println("error:"+httpUrl);
            System.err.println("  Network connection failed:" +
                    "\n  " +e.getMessage()+"\n");
        }
        return "";
    }

    public String Get(String httpUrl){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(httpUrl)
                .header("Accept", "*/*")
                .header("user-agent", "UnityPlayer/2019.4.40f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)")
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.body() != null && response.code() == 200){
                return response.body().string();
            }
            System.err.println("error:"+httpUrl+
                    " \n  The response value of the URL com is " + response.code());
            return "";
        } catch (IOException e) {
            System.err.println("error:"+httpUrl);
            System.err.println("  Network connection failed:" + "\n  "
                    +e.getMessage()+"\n");
        }
        return "";
    }

    public JSONObject getqueryItemsInfo(String secretKey){
        List<String> arrays = Arrays.asList("Live2D","Mobile Video");
        JSONObject object = new JSONObject();
        object.put("appVersion",285);
        object.put("matchAllTags",false);
        object.put("numperpage",0);
        object.put("page",1);
        object.put("query_type",3);
        object.put("requiredtags",arrays);
        object.put("search_text","");
        object.put("secretKey",secretKey);
        object.put("showMatureContent",true);
        return object;
    }

    public Boolean download(String url_, String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        HttpURLConnection connection;
        URL url;
        try {
            url = new URL(url_);
            connection = (HttpURLConnection) url.openConnection();
            int fileSize = connection.getContentLength();
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(filePath + fileName);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            int bytesRead;
            int totalBytesRead = 0;
            while ((bytesRead = rbc.read(buffer)) != -1) {
                buffer.flip();
                fos.getChannel().write(buffer);
                buffer.clear();
                totalBytesRead += bytesRead;
                printProgress(totalBytesRead, fileSize, fileName);
            }
            fos.close();
        } catch (IOException e) {
            System.err.println("error:"+url_+"\n" +
                    "  File download error.\n"+
                    "  "+e.getMessage());

            return false;
        }
        System.out.print("\n");
        return true;
    }

    private void printProgress(int bytesRead, int totalBytes, String fileName) {
        int progressPercentage = (int) ((double) bytesRead / totalBytes * 100);
        StringBuilder progressBar = new StringBuilder(fileName +"  [");
        for (int i = 0; i < 20; i++){
            if (i < progressPercentage/5){
                progressBar.append("=");
            } else if (i == progressPercentage/5) {
                progressBar.append(">");
            } else {
                progressBar.append(" ");
            }
        }
        progressBar.append("] ").append(progressPercentage).append("%");
        progressBar.append("  ").append(FilseSize(bytesRead)).append("/").append(FilseSize(totalBytes));
        System.out.print("\r" + progressBar);
    }

    public String FilseSize(int file_size) {
        long size = file_size;
        int GB = 1024 * 1024 * 1024;
        int MB = 1024 * 1024;
        int KB = 1024;
        DecimalFormat df = new DecimalFormat("0.00");
        String resultSize = "";
        if (size / GB >= 1) {
            resultSize = df.format(size / (float) GB) + "GB";
        } else if (size / MB >= 1) {
            resultSize = df.format(size / (float) MB) + "MB";
        } else if (size / KB >= 1) {
            resultSize = df.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B";
        }
        return resultSize;
    }
}
