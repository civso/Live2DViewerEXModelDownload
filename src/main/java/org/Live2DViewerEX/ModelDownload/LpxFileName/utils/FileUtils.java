package org.Live2DViewerEX.ModelDownload.LpxFileName.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileUtils {

    ArrayList<FileNameCounter> fileNameCountersList = new ArrayList<>();
    public ArrayList<FileInfo> FileInfoList = new ArrayList<>();
    String molename;
    String modeFile;

    public FileUtils(String filesPath){
        filesPath += "DecFiles/";
        File[] fileList = new File(filesPath).listFiles();
        FileSuffix fs = new FileSuffix();
        if (fileList != null) {
            for (File file : fileList) {
                if (!file.isFile()) {
                    continue;
                }
                FileInfo f = new FileInfo(filesPath,file.getName(),null);
                f.suffix = fs.getSuffix(f.getOldFile());
                FileInfoList.add(f);
            }
        }
    }
    public String getUniqueName(String oldName, String newName,String path) {
        FileInfo f = findFileInfo(oldName);
        if (f == null) {
            return oldName;
        }
        if (f.isNameChanged()) {
            return f.newPath + f.getNewName();
        }
        newName = newName.replaceAll("[\\\\/:*?\"<>|]", "_");
        for (FileNameCounter fnc : fileNameCountersList) {
            if (fnc.NewName.equals(newName + f.suffix)) {
                //rename
                int num = fnc.getCountAndAddOne();
                return f.setNewPathAndName(path,newName + "_" + num);
            }
        }
        FileNameCounter n = new FileNameCounter(newName + f.suffix);
        fileNameCountersList.add(n);
        return f.setNewPathAndName(path,newName);
    }

    public FileInfo findFileInfo(String name){
        if (name == null){
            return null;
        }
        for (FileInfo f : FileInfoList){
            if(f.oldName.equals(name)){
                return f;
            }
        }
        return null;
    }

    public void setmodeName(String Path){
        String filename = Path.substring(0,Path.length()-1);
        molename = filename.substring(filename.lastIndexOf('/')+1);
        modeFile = "./mode/"+molename+"/";
    }

    public static JSONObject getJson(String path){
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
            return null;
        }
        return json;
    }
    public void setJump(String oldName){
        FileInfo f = findFileInfo(oldName);
        if (f == null) {
            return;
        }
        f.jump = true;
    }

    public void saveJson(JSONObject json, String Name){
        File file_ = new File(modeFile);
        if (!file_.exists()) {
            file_.mkdirs();
        }
        String strjson = JSON.toJSONString(json, SerializerFeature.PrettyFormat);
        strjson = strjson.replace("\t", "    ");
        if (strjson == null){
            System.err.println("Live2DViewerEXCostume Mode Json File storage failure");
        }
        try (FileWriter file = new FileWriter(modeFile + Name)) {
            file.write(strjson);
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Live2DViewerEXCostume Mode Json File storage failure");
        }
    }
    public String saveFile() {
        for (FileInfo f : FileInfoList){
            copyFile(f);
        }
        return modeFile.substring(1,modeFile.length()-1);
    }
    private void copyFile(FileInfo f){
        String oldFilePath = f.getOldFile();
        String newfilepath = modeFile + f.newPath;
        if (f.oldName.equals("") || f.jump){
            return;
        }
        File file_ = new File(newfilepath);
        if (!file_.exists()) {
            file_.mkdirs();
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            byte[] buffer = new byte[1024];
            int length;
            fis = new FileInputStream(oldFilePath);
            fos = new FileOutputStream(newfilepath + f.getNewName());
            while ((length = fis.read(buffer)) != -1) {
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
        System.err.println("File: " + f.oldName + "Processing failed.");
    }
    public void decryptFile(String path) {
        File directory = new File(path);
        deleteDirectory(directory);
    }
    public void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}
