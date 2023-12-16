package org.Live2DViewerEX.ModelDownload;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.Live2DViewerEX.ModelDownload.LpxFileName.FormatteName;
import org.Live2DViewerEX.ModelDownload.utils.LpxDecrypt;
import org.Live2DViewerEX.ModelDownload.utils.LpxHttpInterface;
import org.Live2DViewerEX.ModelDownload.utils.ModelInfoPrinter;

import java.util.Scanner;

public class ListSelector {
    JSONObject postData;
    //获取请求体json
    public ListSelector(){
        LpxHttpInterface lpxHttp = new LpxHttpInterface();
        postData = lpxHttp.Getjson();
        if(postData == null){
            System.err.println("无法获取请求json,请检查网络连接");
            Scanner scan = new Scanner(System.in);
            scan.nextLine();
            System.exit(0);
        }
    }
    public void inputList(String option){
        if (option.equals("list")){
            postData.put("page",1);
            postData.put("search_text","");
            showModelList();
            return;
        }
        if (option.length() < 8 || !option.startsWith("list -p")){
            System.out.println("未知命令行选项");
            return;
        }
        option = option.replaceAll("[^\\d]", "");
        if (option.equals("")){
            System.out.println("页数输入错误");
            return;
        }
        if (option.length() > 8) {
            option = option.substring(0, 8);
        }
        postData.put("page",Integer.parseInt(option));
        postData.put("search_text","");
        showModelList();
    }
    public void inputSearch(String option){
        option = option.substring(7);
        if (option.equals("")){
            System.out.println("请输入模型名称");
            return;
        }
        postData.put("search_text",option);
        postData.put("page",1);
        showModelList();
    }
    //-------
    public void showModelList(){
        ModelInfoPrinter mp = new ModelInfoPrinter();
        JSONArray json = mp.printModelList(postData);
        while(true){
            String option = getUserInput();
            if (option.matches("\\d+")){
                int inputId = Integer.parseInt(option);
                if (inputId < 1 || inputId > json.size()){
                    System.out.println("输入值错误");
                    continue;
                }
                showModelDetails(json.getJSONObject(inputId-1));
                json = mp.printModelList(postData);
                continue;
            }
            //---------
            if (option.equals("n") || option.equals("next")){
                postData.put("page", postData.getIntValue("page")+1);
                json = mp.printModelList(postData);
                continue;
            }
            if (option.equals("p") || option.equals("prev")){
                if (postData.getIntValue("page") <=1){
                    System.out.println("已到第一页");
                    continue;
                }
                postData.put("page", postData.getIntValue("page")-1);
                json = mp.printModelList(postData);
                continue;
            }
            if (option.equals("e") || option.equals("exit")){
                System.out.println("\n");
                break;
            }
            if (option.equals("download all")){
                for (int i = 0; i < json.size(); i++){
                    JSONObject thisjson = json.getJSONObject(i);
                    DownloadModel(thisjson);
                }
                continue;
            }
            if (!option.equals("")){
                System.out.println("未知命令");
                System.out.println("  当前第"+postData.getIntValue("page")+"页  [int]-输入序号选择项目  n/next-下一页  p/prev-上一页  e/exit-退出选择");
            }
        }
    }
    private void showModelDetails(JSONObject thisjson){
        ModelInfoPrinter mp = new ModelInfoPrinter();
        mp.printModelDetails(thisjson);
        while(true) {
            String option = getUserInput();
            if (option.equals("d") ||option.equals("download")){
                DownloadModel(thisjson);
                System.out.println(" d/download-下载  previewurl-查看封面链接  e/exit-退出选择");
                continue;
            }
            if (option.equals("previewurl")){
                System.out.println(thisjson.getString("preview_url"));
                continue;
            }
            if (option.equals("e") ||option.equals("exit")){
                System.out.println("---------------------");
                break;
            }
            if (!option.equals("")){
                System.out.println("未知命令");
            }
        }
    }
    private void DownloadModel(JSONObject thisjson){
        LpxHttpInterface lpxHttp = new LpxHttpInterface();
        String Link = lpxHttp.getDownloadLink(thisjson.getString("publishedfileid"),thisjson.getString("hcontent_file"));
        if (Link == null){
            System.err.println("无法获取下载链接");
            return;
        }
        System.out.println("下载链接: " + Link);
        String lpkpath = lpxHttp.DownloadFile(Link,thisjson.getString("title"));
        if (lpkpath == null){
            System.err.println("文件下载失败");
            return;
        }
        System.out.println("文件下载完成,准备解密...");
        LpxDecrypt lpxDecrypt = new LpxDecrypt();
        JSONObject json = lpxDecrypt.decrypt(lpkpath);
        if (json == null){
            System.err.println("文件解密失败");
            return;
        }
        System.out.println("文件解密完成,准备整理文件内容..");
        FormatteName formatteName = new FormatteName();
        String path_ = formatteName.Main(json);
        System.out.println("文件整理完成.");
        System.out.println("-文件已存储到: " + path_ + " 目录下");
        System.out.println("---------------------");
    }

    private String getUserInput(){
        Scanner scan = new Scanner(System.in);
        System.out.print(">>");
        String option = scan.nextLine();
        if (option.length() > 200) {
            option = option.substring(0, 200);
        }
        if (option.matches("\\d+")) {
            if (option.length() > 8) {
                option = option.substring(0, 9);
            }
        }
        return option;
    }

}
