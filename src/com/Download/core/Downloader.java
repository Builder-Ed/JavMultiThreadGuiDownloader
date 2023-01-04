package com.Download.core;

import com.Download.Constant.Constant;
import com.Download.util.FileUtils;
import com.Download.util.HttpUtils;
import com.Download.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Downloader {

    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    public ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM,Constant.THREAD_NUM,0,TimeUnit.SECONDS,new ArrayBlockingQueue<>(5));

    public void download(String url) {
        String httpFileName = HttpUtils.getHttpFileName(url);
        httpFileName = Constant.PATH + httpFileName;
        //FileSizeGET
        long localFileLength = FileUtils.gerFileContentLength(httpFileName);

        HttpURLConnection httpURLConnection = null;
        DownloadInfoThread downloadInfoThread = null;
        try {
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
            //GETFileFullSize
            int contentLength = httpURLConnection.getContentLength();
            if (localFileLength >= contentLength) {
                LogUtils.info("{}Alreadly Downloaded!", httpFileName);
                return;
            }
            //GETDownInfoMission
            downloadInfoThread = new DownloadInfoThread(contentLength);
            //GiveMissionToThread,Every1Sec
            scheduledExecutorService.scheduleAtFixedRate(downloadInfoThread, 1, 1, TimeUnit.SECONDS);
            //Cut
            ArrayList<Future> list = new ArrayList<>();
            spilt(url, list);

            list.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            if (merge(httpFileName)){
                clearTmp(httpFileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.print("\r");
            System.out.print("Finished");
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            scheduledExecutorService.shutdownNow();
            poolExecutor.shutdown();
        }
    }

    public void spilt(String url, ArrayList<Future> futureList) {
        try {
            long contentLength = HttpUtils.getHttpFileContentLength(url);
            long size=contentLength/Constant.THREAD_NUM;
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                long startPos=i*size;
                long endPos;
                if (1==Constant.THREAD_NUM-1){
                    endPos=0;
                }else{
                    endPos=startPos+size;
                }
                if (startPos!=0){
                    startPos++;
                }
                DownloaderTask downloaderTask=new DownloaderTask(url,startPos,endPos,i);
                Future<Boolean> future = poolExecutor.submit(downloaderTask);
                futureList.add(future);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean merge(String fileName){
        LogUtils.info("Merging{}",fileName);
        byte[]buffer=new byte[Constant.BYTE_SIZE];
        int len=-1;
        try(RandomAccessFile accessFile = new RandomAccessFile(fileName,"rw")){
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                try (BufferedInputStream bis =new BufferedInputStream(new FileInputStream(fileName+".temp"+i))){
                    while ((len=bis.read(buffer))!=-1){
                        accessFile.write(buffer,0,len);
                    }
                }
            }
            LogUtils.info("Finished Merging!{}"+fileName);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public  boolean clearTmp(String fileName){
        for (int i = 0; i < Constant.THREAD_NUM; i++) {
            File file= new File(fileName+".temp"+i);
            file.delete();
        }
        return true;
    }
}
