package org.Live2DViewerEX.ModelDownload.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LpxDecrypt {
    public JSONObject decrypt(String lpkpath) {
        Zip zip = new Zip();
        String filesPath = zip.unzip(lpkpath);
        if (filesPath == null){
            return null;
        }
        List<String> fileList = encfileFileList(filesPath);
        JSONObject json = getConfig(filesPath, fileList);
        if (json == null){
            return null;
        }
        if (!json.getString("type").equals("STM_1_0")){
            System.out.println("Temporarily does not support decryption of this .lpk file format");
            return null;
        }
        File file = new File(filesPath + "DecFiles/");
        if (!file.exists()) {
            file.mkdirs();
        }
        for (String filename : fileList){
            if (!filename.contains(".")) {
                continue;
            }
            String stringkey = json.getString("id") + json.getString("fileId") + filename + json.getString("metaData");
            decryptFile(filesPath,filename,stringkey);
        }
        json.put("filesPath",filesPath);
        return json;
    }

    private JSONObject getConfig(String filesPath, List<String> fileList){
        JSONObject retJson = new JSONObject();
        JSONObject json = getJson(filesPath + "config.json");
        if (json == null || json.getString("metaData") == null){
            System.err.println("Unable to read config.json");
            return null;
        }
        retJson.put("metaData", json.getString("metaData"));
        retJson.put("fileId", json.getString("fileId"));
        //.lpk config
        for (String filename : fileList){
            if (filename.contains(".")) {
                continue;
            }
            json = getJson(filesPath+ "EncFiles/"+filename);
            if (json != null){
                retJson.put("type", json.getString("type"));
                retJson.put("id", json.getString("id"));
                retJson.put("encrypt", json.getString("encrypt").equals("true"));
                retJson.put("version",json.getString("version"));
                retJson.put("list",json.get("list"));
                return retJson;
            }
        }
        return null;
    }

    private void decryptFile(String filesPath, String filename, String stringkey){
        Decrypt decrypt = new Decrypt();
        int key = decrypt.KeyCompute(stringkey);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(filesPath + "EncFiles/" + filename);
            fos = new FileOutputStream(filesPath + "DecFiles/" + filename);
            byte[] buffer = new byte[1024];
            int length = 0;
            Boolean temp = true;
            while ((length = fis.read(buffer)) != -1) {
                buffer = decrypt.encryptDecrypt(buffer,key);
                fos.write(buffer, 0, length);
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("File: " + filename + "Processing failed.");
    }
    private List<String> encfileFileList(String FilePath) {
        ArrayList<String> fileNmaeList = new ArrayList<>();
        File file_ = new File(FilePath + "EncFiles");
        File[] fileList = file_.listFiles();
        for (File file : fileList) {
            if (file.isFile()) {
                fileNmaeList.add(file.getName());
            }
        }
        return fileNmaeList;
    }

    private static JSONObject getJson(String path){
        JSONObject json = null;
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            if (content.length() < 1){
                return null;
            }
            if (!content.substring(0,1).equals("{")){
                return null;
            }
            json = JSON.parseObject(content, Feature.OrderedField);
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
        return json;
    }
}
