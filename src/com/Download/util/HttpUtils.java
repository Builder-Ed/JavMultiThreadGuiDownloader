package com.Download.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static long getHttpFileContentLength(String url) throws IOException {
        int contentLength;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = getHttpURLConnection(url);
            contentLength = httpURLConnection.getContentLength();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return contentLength;
    }

    public static HttpURLConnection getHttpURLConnection(String url, long startPos, long endPos) throws IOException {

        HttpURLConnection httpURLConnection = getHttpURLConnection(url);
        httpURLConnection = getHttpURLConnection(url);
        LogUtils.info("Range:{}-{}", startPos, endPos);
        if (endPos != 0) {
            httpURLConnection.setRequestProperty("RANGE", "bytes-" + startPos + "-" + endPos);
        } else {
            httpURLConnection.setRequestProperty("RANGE", "bytes-" + startPos + "-");
        }
        return httpURLConnection;
    }

    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL httpUrl =new URL(url);
        HttpURLConnection httpUrlConnection=(HttpURLConnection)httpUrl.openConnection();
        httpUrlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64; rv:78.0) Gecko/20100101 Firefox/78.0 Chrome/14.0.835.163 Safari/535.1");
        return httpUrlConnection;
    }
    public static String getHttpFileName(String url){
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }
}
