package com.Download.util;

import java.io.*;

public class  FileUtils {
    public static long gerFileContentLength(String path){
        File file = new File(path);
        return file.exists()&&file.isFile()?file.length():0;
    }
}
