package com.Download.core;

import com.Download.Constant.Constant;

import java.util.concurrent.atomic.LongAdder;

public class DownloadInfoThread implements Runnable{
    //FileSize
    private long httpFileContentLength;
    //FinishedSize
    public static LongAdder finishedSize=new LongAdder();
    //PrevSize
    public  double preSize;
    //DownSize
    public static volatile LongAdder downSize=new LongAdder();

    public DownloadInfoThread(long httpFileContentLength) {
        this.httpFileContentLength = httpFileContentLength;
    }
    public void run(){
        //CalculateFileSize(MB)
        String httpFileSize=String.format("%.2f",httpFileContentLength/ Constant.MB);
        //Speed
        int speed =(int) ((downSize.doubleValue()-preSize)/Constant.MB);
        preSize=downSize.doubleValue();

        //RemainSize
        double remainSize= httpFileContentLength-finishedSize.doubleValue()-downSize.doubleValue();

        //Time
        String remainTime=String.format("%.1f,",remainSize/1024d/speed);
        if("Infinity".equalsIgnoreCase(remainTime)){
            remainTime="-";
        }
        //Finished
        String currentFileSize=String.format("%.2f",(downSize.doubleValue()-finishedSize.doubleValue())/Constant.MB);
        String downInfo= String.format("PrevSize %sMb/%sMb,Speed %sMb/s,RemainTime %ss",currentFileSize,httpFileSize,speed,remainTime);
        System.out.print("\r");
        System.out.print(downInfo);
    }
}
