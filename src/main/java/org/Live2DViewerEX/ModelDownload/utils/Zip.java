package org.Live2DViewerEX.ModelDownload.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zip {
    public String unzip(String zipFileName){
        if (zipFileName == null){
            return null;
        }
        String OutputPath = zipFileName.substring(0, zipFileName.lastIndexOf('.')) + "/";
        if (!unzipFile(zipFileName,OutputPath)){
            System.err.println("ZIP file is corrupted");
            return null;
        }
        JSONObject json = getJson(OutputPath + "config.json");
        if (json == null){
            System.err.println("Unable to read config.json");
            return null;
        }
        if (!unzipFile(OutputPath+json.getString("lpkFile"),OutputPath+"EncFiles/")){
            return null;
        }
        deleteFile(zipFileName);
        return OutputPath;
    }
    private void deleteFile(String fileName){
        try {
            File file = new File(fileName);
            if(!file.delete()) {
                System.out.println(fileName + " file deletion failed");
            }
        } catch(Exception e) {
            System.out.println(fileName + " file deletion failed");
        }
    }
    private boolean unzipFile(String zipFilePath, String OutputPath){
        File file = new File(OutputPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String filePath = OutputPath + zipEntry.getName();
                if (!zipEntry.isDirectory()) {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                    byte[] bytesIn = new byte[4096];
                    int read = 0;
                    while ((read = zis.read(bytesIn)) != -1) {
                        bos.write(bytesIn, 0, read);
                    }
                    bos.close();
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
            zis.close();
            return true;
        } catch (IOException e) {
            System.err.println("error: "+zipFilePath);
            System.err.println("  File decompression failed.");
        }
        return false;
    }
    private static JSONObject getJson(String path){
        JSONObject json = null;
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            json = JSON.parseObject(content, Feature.OrderedField);
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
        return json;
    }
}

