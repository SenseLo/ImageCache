package it.fuyk.com.imagecachecode.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * author: senseLo
 * date: 2018/3/20
 */

public class CacheUtil {
    private static CacheUtil instance;
    private Context context;
    private ImageCache imageCache;

    public CacheUtil(Context context) {
        this.context = context;
        Map<String, SoftReference<Bitmap>> map = new HashMap<>();
        this.imageCache = new ImageCache(map);
    }

    public static CacheUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (CacheUtil.class) {
                if (instance == null) {
                    instance = new CacheUtil(context);
                }
            }
        }
        return instance;
    }

    /*
    * 将图片添加到缓存中
    * */
    private void putImageToCache(String fileName, byte[] data) {
        //将图片的字节数组写入到内存中
        FileUtil.getInstance(context).writeFileToStorage(fileName, data);
        //将图片存入强引用
        imageCache.put(fileName, BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    /*
    * 将图片从缓存中取出
    * */
    private Bitmap getImageFromCache(String fileName) {
        //从强引用取出图片
        Bitmap bitmap = null;
        bitmap = imageCache.get(fileName);

        if (bitmap == null) {
            //如果图片不存在强引用中，去软引用中查找
            Map<String, SoftReference<Bitmap>> cacheMap = imageCache.getCacheMap();
            SoftReference<Bitmap> softReference = cacheMap.get(fileName);
            if (softReference != null) {
                //软引用中存在
                //取出图片
                bitmap = softReference.get();
                //并将图片存放到强引用中
                imageCache.put(fileName, bitmap);
            }else {
                //软引用中不存在,去内存中取
                byte[] data = FileUtil.getInstance(context).readByteFromStorage(fileName);
                if (data != null && data.length > 0) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageCache.put(fileName, bitmap);
                }
            }
        }
        return bitmap;
    }

    /*
    * 使用三级缓存为IamgeView设置图片
    * */
    public void setImageToView(final String path, final ImageView imageView) {
        final String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
        //从缓存中取出图片
        Bitmap bitmap = getImageFromCache(fileName);
        if (bitmap != null) {
            //缓存中有图片
            imageView.setImageBitmap(bitmap);
        }else  {
            //缓存中没有图片，从网络获取图片
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] b = HttpUtil.getInstance().getByteFromWeb(path);
                    if (b != null && b.length >0) {
                        //将获取到的图片字节数组写入到缓存中
                        putImageToCache(fileName, b);
                        final Bitmap bm = BitmapFactory.decodeByteArray(b, 0, b.length);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bm);
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
