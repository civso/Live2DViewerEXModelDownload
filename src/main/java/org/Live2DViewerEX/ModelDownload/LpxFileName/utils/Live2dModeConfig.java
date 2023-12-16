package org.Live2DViewerEX.ModelDownload.LpxFileName.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class Live2dModeConfig {
    String oldName;
    String newName;
    JSONObject json;

    ArrayList<Live2dModeJson> jsonList = new ArrayList<>();

    public void setNewName(String thisName) {
        newName = thisName;
    }
    public void setJson(JSONObject json_) {
        json = json_;
    }
    public JSONObject getJson(){
        return json;
    }
    public String oldName() {
        return oldName;
    }
    public String newName(){
        return newName;
    }
    public Live2dModeConfig(String oldName_){
        oldName = oldName_;
    }
    public ArrayList<Live2dModeJson> jsonObjectList() {
        return jsonList;
    }
    public void addCommandJson(JSONObject json, String key){
        Live2dModeJson live2dModeJson = new Live2dModeJson();
        live2dModeJson.json = json;
        live2dModeJson.key = key;
        jsonList.add(live2dModeJson);
    }
}
