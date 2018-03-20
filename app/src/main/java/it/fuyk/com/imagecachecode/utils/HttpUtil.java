package it.fuyk.com.imagecachecode.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: senseLo
 * date: 2018/3/20
 * 访问Http工具类
 */

public class HttpUtil {
    private static HttpUtil instance;

    public HttpUtil() {
    }

    //双重校验锁
    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    /*
    * 通过url从网络获取返回的字节数组
    * */
    public byte[] getByteFromWeb(String path) {
        byte[] b = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                inputStream = connection.getInputStream();
                byte[] temp = new byte[1024];
                int length = 0;
                while ((length = inputStream.read(temp)) != -1) {
                    byteArrayOutputStream.write(temp, 0, length);
                }
            }
            b = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

}
