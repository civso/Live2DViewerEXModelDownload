package org.Live2DViewerEX.ModelDownload.LpxFileName.utils;

public class FileInfo {
    String suffix;
    String oldmPath;
    String oldName;
    String newName;
    String newPath;
    Boolean jump = false;

    public String getOldFile(){
        return oldmPath + oldName;
    }
    public Boolean isNameChanged(){
        return !newName.equals(oldName);
    }
    public String getNewName(){
        if (newName.equals(oldName)){
            return oldName;
        }
        if (suffix == null){
            return newName;
        }
        return newName + suffix;
    }
    public String setNewPathAndName(String path,String Name){
        if (!path.equals("")){
            path += "/";
        }
        newPath = path;
        newName = Name;
        return newPath + newName + suffix;
    }
    public FileInfo(String oldmPath_,String oldName_,String newName_){
        if (newName_ == null) {
            newName_ = oldName_;
        }
        oldmPath = oldmPath_;
        oldName = oldName_;
        newName = newName_;
        newPath = "";
    }
    public void print_(){
        System.out.println(oldmPath + " " +oldName + " "+ suffix);
    }
}
