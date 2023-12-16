package org.Live2DViewerEX.ModelDownload.utils;

import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;

public class LpxHttpInterface {
    HttpUtil httpUtil = new HttpUtil();
    String queryItemsurl = "http://live2d.pavostudio.com:8080/Live2DWeb/queryItems.jsp";
    String getFileUel = "http://live2d.pavostudio.com:8080/Live2DWeb/getFile.jsp";
    String getSecretKeyUrl = "https://gitee.com/xhnsk/key/raw/master/key";
    String tempFolder = "./temp/";

    public JSONObject Getjson(){
        String SecretKey = httpUtil.Get(getSecretKeyUrl);
        if (SecretKey == null || SecretKey.equals("null")){
            System.err.println("Failed to get secretKey");
            return null;
        }
        return httpUtil.getqueryItemsInfo(SecretKey);
    }

    public JSONObject getItemList(JSONObject json_){
        String data = String.valueOf(json_);
        String ret = httpUtil.Post(queryItemsurl,data);
        if (!ret.equals("")) {
            JSONObject retjson = JSONObject.parseObject(ret);
            if (retjson.getIntValue("result") == 1){
                return retjson;
            }else{
                System.err.println(retjson);
            }
        }
        System.err.println("Failed to fetch Live2D model data list");
        return null;
    }

    public String getDownloadLink(String fileid, String ugcid) {
        String data = "?fileid=" + fileid + "&ugcid=" + ugcid;
        String ret = httpUtil.Get(getFileUel + data);
        if (ret.equals("")) {
            System.err.println("network request error");
            return null;
        }
        JSONObject retjson = JSONObject.parseObject(ret);
        if (retjson.getIntValue("result") != 1) {
            System.err.println("Failed to get download link");
            return null;
        }
        return retjson.getString("content");
    }

    public String DownloadFile(String link, String fileName) {
        fileName = fileName.replace("\n", "").replace("\r", "");
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_") + ".zip";
        if (link == null){
            System.err.println("download Link is null");
            return null;
        }

        if (!httpUtil.download(link,tempFolder,fileName)) {
            System.err.println("File download failed");
            return null;
        }
        return tempFolder + fileName;
    }
}
