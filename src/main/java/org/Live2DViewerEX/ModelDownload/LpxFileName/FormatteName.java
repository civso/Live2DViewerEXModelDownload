package org.Live2DViewerEX.ModelDownload.LpxFileName;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.Live2DViewerEX.ModelDownload.LpxFileName.utils.*;

import java.util.ArrayList;
import java.util.Set;

public class FormatteName {

    FileUtils Utils;
    ArrayList<Live2dModeConfig> LpxConfigrList = new ArrayList<>();

    public String Main(JSONObject configJson){
        String filesPath = configJson.getString("filesPath");
        JSONArray List = configJson.getJSONArray("list");
        Utils = new FileUtils(filesPath);
        Utils.setmodeName(filesPath);
        for (int i = 0; i < List.size(); i++){
            JSONArray costume = List.getJSONObject(i).getJSONArray("costume");
            String oldName = List.getJSONObject(i).getString("avatar");
            FileInfo f = new FileInfo(filesPath+"EncFiles/",oldName,"preview.jpeg");
            Utils.FileInfoList.add(f);
            for (int j = 0; j < costume.size(); j++){
                getlpxConfig(costume.getJSONObject(j).getString("path"),"");
            }
        }
        for (Live2dModeConfig lmc : LpxConfigrList){
            ArrayList<Live2dModeJson> jsonList = lmc.jsonObjectList();
            for (Live2dModeJson ModeJson : jsonList ){
                ModeJsonrename(ModeJson);
            }
            Utils.setJump(lmc.oldName());
            Utils.saveJson(lmc.getJson(),lmc.newName());
        }
        String savePath = Utils.saveFile();
        Utils.decryptFile(filesPath);
        return savePath;
    }
    private void ModeJsonrename(Live2dModeJson j){
        JSONObject json = j.json;
        String oldName = json.getString(j.key).substring(11);
        String newName = null;
        for (Live2dModeConfig lmc : LpxConfigrList) {
            if (lmc.oldName().equals(oldName)) {
                newName = Utils.getUniqueName(oldName, lmc.newName(),"");
                lmc.setNewName(newName);
            }
        }
        json.put(j.key, "change_cos " + newName);
    }

    private void getlpxConfig(String oldName, String thisName){
        Live2dModeConfig modeConfig = new Live2dModeConfig(oldName);
        LpxConfigrList.add(modeConfig);
        FileInfo f = Utils.findFileInfo(oldName);
        if (f == null){
            return;
        }
        JSONObject thisJson = Utils.getJson(f.getOldFile());
        if (thisJson == null){
            return;
        }
        String modelMoc3 = thisName;
        if (thisName == null || thisName.equals("")){
            thisName = "Live2DViewerEXCostume";
            modelMoc3 = "modelMoc3";
        }
        thisName = Utils.getUniqueName(oldName,thisName,"");
        modeConfig.setNewName(thisName);
        JSONObject json;
        Live2dModelJsonKeys k = new Live2dModelJsonKeys();
        if (thisJson.containsKey("FileReferences")){
            json = thisJson.getJSONObject("FileReferences");
            k.setLive2d();
        }else{
            json = thisJson;
            k.setSpine();
        }
        JSONArray Textureslist = json.getJSONArray(k.Textures);
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < Textureslist.size(); i++){
            temp.add(Utils.getUniqueName(Textureslist.getString(i),"image","image"));
        }
        json.put(k.Textures,temp);
        renameFile(json,k.Moc,modelMoc3);
        renameFile(json,k.Physics,"physics");
        JSONObject PhysicsV2 = json.getJSONObject(k.PhysicsV2);
        renameFile(PhysicsV2,k.File,"physicsV2");
        renameFile(json,k.Pose,"pose");
        renameExpressions(json,k);
        JSONObject motions = json.getJSONObject(k.Motions);
        if (motions == null){
            modeConfig.setJson(thisJson);
            return;
        }
        Set<String> keys = motions.keySet();
        for (String motionsKey : keys) {
            JSONArray thisArray = motions.getJSONArray(motionsKey);
            if (thisArray == null) {
                continue;
            }
            for (int i = 0; i < thisArray.size(); i++) {
                JSONObject thisObject = thisArray.getJSONObject(i);
                if (thisObject == null) {
                    continue;
                }
                String cKey = renameMotions(thisObject, k, motionsKey);
                if (cKey == null){
                    continue;
                }
                modeConfig.addCommandJson(thisObject,cKey);
            }
        }
        modeConfig.setJson(thisJson);
    }
    private void renameExpressions(JSONObject json, Live2dModelJsonKeys k){
        if (!json.containsKey(k.Expressions)){
            return;
        }
        JSONArray thisArray = json.getJSONArray(k.Expressions);
        if (thisArray == null){
            return;
        }
        for (int i = 0; i < thisArray.size(); i++) {
            JSONObject thisObject = thisArray.getJSONObject(i);
            if (thisObject == null) {
                continue;
            }
            String name;
            if (thisObject.containsKey(k.Name)) {
                name = thisObject.getString(k.Name);
            }else if (thisObject.containsKey(k.Expressions)) {
                name = thisObject.getString(k.Expressions);
            }else{
                name = "expressions";
            }
            renameFile(thisObject, k.File, name, "expressions");
        }
    }

    private String renameMotions(JSONObject json, Live2dModelJsonKeys k, String motionsKey){
        String name = null;
        String[] nameKeyList = new String[]{k.Name_,k.name_,k.Expressions};
        for (String key : nameKeyList){
            if (json.containsKey(key)){
                name = json.getString(key);
                break;
            }
            name = motionsKey;
        }
        if (name.matches("\\d+") && name.length() < 4){
            name = motionsKey;
        }
        if (k.isSample && name.lastIndexOf('.') != -1){
            name = name.substring(0, name.lastIndexOf('.'));
        }
        if (json.containsKey(k.File)) {
            renameFile(json, k.File, name,"expressions");
        }
        if (json.containsKey(k.Sound)){
            renameFile(json, k.Sound, name,"expressions");
        }
        //command
        String cKey = null;
        String[] commandkeyList = new String[]{k.command_,k.Command_,k.PostCommand_};
        for (String key : commandkeyList){
            if (json.containsKey(key)){
                cKey = key;
            }
        }
        if (cKey == null){
            return null;
        }
        String commandStr = json.getString(cKey);
        if (commandStr.length() < 12) {
            return null;
        }
        if (commandStr.substring(0,11).equals("replace_tex")){
            String oldName = commandStr.substring(12);
            String jsonNewName = Utils.getUniqueName(oldName,name+"_image","image");
            json.put(cKey, "replace_tex " + jsonNewName);
        }
        if (commandStr.substring(0,10).equals("change_cos")){
            String filename = commandStr.substring(11);
            for (Live2dModeConfig lmc : LpxConfigrList){
                if (lmc.oldName().equals(filename)){
                    return cKey;
                }
            }
            getlpxConfig(filename,name);
            return cKey;
        }
        return null;
    }

    public void renameFile(JSONObject json, String key, String newName){
        renameFile(json,key,newName,"");
    }
    public void renameFile(JSONObject json, String key, String newName, String path){
        if (json == null){
            return;
        }
        if (!json.containsKey(key)){
            return;
        }
        json.put(key, Utils.getUniqueName(json.getString(key), newName,path));
    }
}
