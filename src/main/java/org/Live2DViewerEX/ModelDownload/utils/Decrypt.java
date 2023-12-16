package org.Live2DViewerEX.ModelDownload.utils;

public class Decrypt {
    public byte[] encryptDecrypt(byte[] bytes, int intkey){
        long key = intkey;
        for (int i = 0; i < bytes.length; i++){
            key = 65535 & ((2531011L + 214013L * key) >> 16);
            bytes[i] = (byte)(bytes[i] ^ key);
        }
        return bytes;
    }
    public int KeyCompute(String key){
        int intkey = 0;
        for (int i = 0; i < key.length(); i++){
            char c = key.charAt(i);
            intkey = 31 * intkey + c;
        }
        return intkey;
    }
}
