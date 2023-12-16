package org.Live2DViewerEX.ModelDownload.LpxFileName.utils;

public class Live2dModelJsonKeys {
    public String Name_ = "Name";
    public String name_ = "name";
    public String Command_ = "Command";
    public String command_ = "command";
    public String PostCommand_ = "PostCommand";
    public String Moc;
    public String Textures;
    public String Physics;
    public String PhysicsV2;
    public String File;
    public String Expressions;
    public String Name;
    public String Motions;
    public String Sound;
    public String Command;
    public String Pose;
    public Boolean isSample = false;

    public void setLive2d(){
        Moc = "Moc";
        Textures = "Textures";
        Physics = "Physics";
        PhysicsV2 = "PhysicsV2";
        File = "File";
        Expressions = "Expressions";
        Name = "Name";
        Motions = "Motions";
        Sound = "Sound";
        Pose = "Pose";
        Command = "Command";
    }
    public void setSpine(){
        Moc = "model";
        Textures = "textures";
        Physics = "physics";
        PhysicsV2 = "physics_v2";
        File = "file";
        Expressions = "expressions";
        Name = "name";
        Motions = "motions";
        Sound = "sound";
        Pose = "pose";
        Command = "command";
        isSample = true;
    }
}