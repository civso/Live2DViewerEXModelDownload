package org.Live2DViewerEX.ModelDownload.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModelInfoPrinter {
    public JSONArray printModelList(JSONObject postData) {
        LpxHttpInterface lpxHttp = new LpxHttpInterface();
        JSONObject json = lpxHttp.getItemList(postData);
        if (json == null) {
            System.err.println("无法获取模型列表数据");
            return null;
        }
        JSONArray jsonArray = json.getJSONArray("content");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject thisjson = jsonArray.getJSONObject(i);
            String title = thisjson.getString("title");
            title = title.replace("\n", "").replace("\r", "");
            String personaname = thisjson.getJSONObject("creatorinfo").getString("personaname");
            String tag = getTag(thisjson.getJSONArray("tags"));
            String score = getStart(thisjson.getJSONObject("vote_data").getFloat("score"));
            String time_updated= getTime(thisjson.getIntValue("time_updated"));
            String file_size = getFilseSize(thisjson.getIntValue("file_size"));
            System.out.println((i+1)+"."+tag+" "+score+" "+title+" ("+personaname+")\n  -"+time_updated+" "+file_size);
        }
        int page = postData.getIntValue("page");
        System.out.println("  当前第"+page+"页  [int]-输入序号选择项目  n/next-下一页  p/prev-上一页  e/exit-退出选择");
        return jsonArray;
    }
    public void printModelDetails(JSONObject thisjson){
        String title = thisjson.getString("title");
        String personaname = thisjson.getJSONObject("creatorinfo").getString("personaname");
        String tag = getTag(thisjson.getJSONArray("tags"));
        String start = getStart(thisjson.getJSONObject("vote_data").getFloat("score"));
        String time_updated = getTime(thisjson.getIntValue("time_updated"));
        String time_created = getTime(thisjson.getIntValue("time_created"));
        String file_size = getFilseSize(thisjson.getIntValue("file_size"));
        String short_description = thisjson.getString("short_description");
        String subscriptions = String.valueOf(thisjson.getIntValue("subscriptions"));
        float score = (int) (thisjson.getJSONObject("vote_data").getFloat("score")*100);
        System.out.println("-----------------");
        System.out.println(title);
        System.out.println("作者: "+personaname);
        System.out.println("作品得分: "+ start +" ("+ score/20 + ")");
        System.out.println("文件类型: "+tag+" 文件大小: "+ file_size+" 订阅数: "+subscriptions);
        System.out.println("投稿日期: "+time_created+" 更新日期: "+time_updated);
        System.out.println("-----------------");
        System.out.println("作品简介:\n"+ short_description);
        System.out.println("  d/download-下载  previewurl-查看封面链接  e/exit-退出选择");
    }
    //---
    public String getFilseSize(int file_size) {
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
    public String getTime(int time){
        long timeStamp = ((long)time) * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
    }
    public String getTag(JSONArray taglist){
        for (int i = 0; i < taglist.size(); i++){
            String tag = taglist.getJSONObject(i).getString("tag");
            if (tag.equals("Live2D") || tag.equals("Mobile Video")){
                return tag;
            }
        }
        return "error";
    }
    public String getStart(float score_){
        int score = (int) (score_ * 10);
        String strStart = "★".repeat(score/2);
        if (score%2 == 1){
            strStart += "✭";
            score += 1;
        }
        strStart += "☆".repeat(5-score/2);
        return strStart;
    }
}
