package it.fuyk.com.imagecachecode.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author: senseLo
 * date: 2018/3/20
 */

public class FileUtil {
    private static FileUtil instance;
    private Context context;

    public FileUtil(Context context) {
        this.context = context;
    }

    public static FileUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (FileUtil.class) {
                if (instance == null) {
                    instance = new FileUtil(context);
                }
            }
        }
        return instance;
    }

    /*
    * 将文件存储到内存中
    * */
    public void writeFileToStorage(String fileName, byte[] b) {
        FileOutputStream fos = null;
        File file = new File(context.getFilesDir(), fileName);
        try {
            fos = new FileOutputStream(file);
            fos.write(b, 0, b.length);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
    * 从内存中读取文件字节码
    * */
    public byte[] readByteFromStorage(String fileName) {
        byte[] b = null;
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = context.openFileInput(fileName);
            baos = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int length = 0;
            while ((length = fis.read(temp)) != -1) {
                baos.write(temp, 0, length);
            }
            b = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}
