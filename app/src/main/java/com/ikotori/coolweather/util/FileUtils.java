package com.ikotori.coolweather.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Fashion at 2018/05/17 00:08.
 * Describe:
 */

public class FileUtils {

    /**
     * bitmap保存到文件,默认保存到应用缓存目录
     * @param bitmap
     * @param context
     * @return
     */
    public static File saveImage(Bitmap bitmap, Context context) {
        File dir = new File(context.getExternalCacheDir(), "share");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dir, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件的Uri
     * @param file
     * @param context
     * @return
     */
    public static Uri getShareUri(File file, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, "com.ikotori.coolweather.fileprovider", file);
        }
        return Uri.fromFile(file);
    }
}
