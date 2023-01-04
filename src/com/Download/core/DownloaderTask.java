package com.Download.core;

import com.Download.Constant.Constant;
import com.Download.util.HttpUtils;
import com.Download.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class DownloaderTask implements Callable<Boolean> {

    private String url;

    private long startPos;

    private long endPos;

    //SignPart
    public int part;


    public DownloaderTask(String url,long startPos,long endPos,int part){
        this.url=url;
        this.startPos=startPos;
        this.endPos=endPos;
        this.part=part;
    }
    public Boolean call() throws Exception{
        String httpFileName =HttpUtils.getHttpFileName(url);

        httpFileName=httpFileName+".temp"+part;

        httpFileName= Constant.PATH+httpFileName;
        HttpURLConnection httpURLConnection=HttpUtils.getHttpURLConnection(url,startPos,endPos);
        try(
                InputStream input=httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                RandomAccessFile accessFile = new RandomAccessFile(httpFileName,"rw")

        ){
            byte[] buffer = new byte[Constant.BYTE_SIZE];
            int len = -1;
            while((len=bis.read (buffer))!=1){
                DownloadInfoThread.downSize.add(len);
                accessFile.write(buffer,0,len);
            }
        } catch (FileNotFoundException e){
            LogUtils.error("FileNotFound{}",url);
            return false;
        } catch (Exception e){
            LogUtils.error("Error{}",url);
            return false;
        }finally {
            httpURLConnection.disconnect();
        }
        return true;
    }
}
