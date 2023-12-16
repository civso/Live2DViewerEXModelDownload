package org.Live2DViewerEX.ModelDownload;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ListSelector listSelector = new ListSelector();
        help();
        while(true){
            System.out.print(">>");
            String option = scan.nextLine();
            if (option.length() > 200) {
                option = option.substring(0, 200);
            }
            if (option.startsWith("list")){
                listSelector.inputList(option);
                help();
                continue;
            }
            if (option.length() > 6 && option.startsWith("search")){
                listSelector.inputSearch(option);
                help();
                continue;
            }
            if (option.equals("exit")){
                System.out.println("Exit program.");
                break;
            }
            if (!option.equals("")){
                System.out.println("未知命令");
                help();
            }
        }
    }
    public static void help(){
        System.out.println("-------------");
        System.out.println("list --获取所有模型列表");
        System.out.println("list -p [int] --获取某页模型列表");
        System.out.println("search [string] --查找模型");
        System.out.println("exit -退出项目");
    }
}