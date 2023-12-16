package org.Live2DViewerEX.ModelDownload.LpxFileName.utils;

public class FileNameCounter {
    String NewName;
    int count;

    public FileNameCounter(String name){
        NewName = name;
        count = 1;
    }
    public int getCountAndAddOne(){
        int tmp = count;
        count++;
        return tmp;
    }
}
