package org.Live2DViewerEX.ModelDownload.LpxFileName.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class FileSuffix {

    public String getSuffix(String oldFile){
        FileSuffix fileSuffix = new FileSuffix();
        FileInputStream fis = null;
        try {
            byte[] buffer = new byte[100];
            fis = new FileInputStream(oldFile);
            fis.read(buffer);
            return fileSuffix.SuffixChecker(buffer);
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(ex);
        } catch (IOException e) {
            //throw new RuntimeException(e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                //throw new RuntimeException(ex);
            }
        }
        return null;
    }

    public String SuffixChecker(byte[] bytes){
        int i = 0;
        for (byte[] hexsing : hexSignatures){
            if (hexsing.length > bytes.length){
                return null;
            }
            if (Arrays.equals(hexsing,Arrays.copyOfRange(bytes,0,hexsing.length))){
                return fileExtensions[i];
            }
            i++;
        }
        return null;
    }


    byte[][] hexSignatures = {
            { (byte) 0xff, (byte) 0xfb }, // MP3
            { (byte) 0xff, (byte) 0xf1 }, // AAC
            { 0x0b, 0x77 }, // AC3
            { 0x52, 0x49, 0x46, 0x46 }, // WAV
            { 0x30, 0x26, (byte) 0xb2, 0x75, (byte) 0x8e, 0x66, (byte) 0xcf, 0x11, (byte) 0xa6, (byte) 0xd9, 0x00, (byte) 0xaa, 0x00, (byte) 0x62, (byte) 0xce, 0x6c }, // WMA
            { 0x4f, 0x67, 0x67, 0x53 }, // Ogg Vorbis
            { 0x66, 0x4c, 0x61, 0x43 }, // FLAC
            { 0x00, 0x05, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00 }, // DSD
            {(byte) 0xff, (byte) 0xd8, (byte) 0xff }, // JPEG
            { 0x47, 0x49, 0x46, 0x38, 0x37, 0x61, 0x47, 0x49, 0x46, 0x38, 0x39, 0x61 }, // GIF
            { (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a }, // PNG
            { 0x52, 0x49, 0x46, 0x46, 0x00, 0x00, 0x00, 0x00, 0x57, 0x45, 0x42, 0x50 }, // WebP
            { 0x42, 0x4d }, // BMP
            { 0x66, 0x74, 0x79, 0x70, 0x4d, 0x53, 0x4e, 0x56 }, // MP4
            { 0x00, 0x00, 0x00, 0x20, 0x66, 0x74, 0x79, 0x70, 0x6d, 0x6f, 0x6f, 0x76 }, // MOV
            { 0x52, 0x49, 0x46, 0x46, 0x41, 0x56, 0x49, 0x20 }, // AVI
            { 0x46, 0x4c, 0x56, 0x01 }, // FLV
            { 0x1a, 0x45, (byte) 0xdf, (byte) 0xa3 }, // MKV
            { 0x6d, 0x6f, 0x63 },
            { 0x4d, 0x4f, 0x43 ,0x33 },
            { 0x49, 0x44, 0x33 },//.mp3
            { 0x23, 0x20, 0x4C, 0x69, 0x76, 0x65, 0x32, 0x44 },
            { 0x7b } //json
    };
    String[] fileExtensions = {
            ".mp3",
            ".aac",
            ".ac3",
            ".wav",
            ".wma",
            ".ogg",
            ".flac",
            ".dsd",
            ".jpg",
            ".gif",
            ".png",
            ".webp",
            ".bmp",
            ".mp4",
            ".mov",
            ".avi",
            ".flv",
            ".mkv",
            ".moc",
            ".moc3",
            ".mp3",
            ".skel",
            ".json"
    };
}
