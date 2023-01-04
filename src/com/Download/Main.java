package com.Download;

import com.Download.core.Downloader;
import com.Download.util.LogUtils;

import java.util.Scanner;

//UrlGET
public class Main {
    public static void main(String[] args) {
        //Url
        String url=null;
        System.out.print("DownloadLink:");
        Scanner scanner1 = new Scanner(System.in);
        url=scanner1.nextLine();
        if (url==null){
            System.out.print("java -jar MultiThreadDownloader.jar <DownloadLink> <SavePath>");
            return;
        }

        try {
            Downloader downloader =new Downloader();
            downloader.download(url);
        }
        catch (Exception e){
            LogUtils.error("Error:{}",url);
        }
    }
}
//        if (args==null||args.length==0){
//            for (; ;){
//                LogUtils.info ("DownloadLink");
//                Scanner scanner = new Scanner(System.in);
//                url=scanner.nextLine();
//                if (url!=null){
//                    System.out.println("Start Downloading...");
//                    break;
//                }
//            }
//        }else{
//            url = args[0];
//        }
