package com.ikotori.coolweather.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Fashion at 2018/04/21 11:43.
 * Describe:
 */

public class HttpUtils {

    static {
        INSTANCE = new HttpUtils();
    }

    private static HttpUtils INSTANCE;

    private OkHttpClient mOkhttpClient;

    private HttpUtils() {
        mOkhttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = mOkhttpClient.newBuilder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
    }

    public static HttpUtils getInstance() {
        if (null == INSTANCE) {
            synchronized ((HttpUtils.class)) {
                if (null == INSTANCE) {
                    INSTANCE = new HttpUtils();
                }
            }
        }
        return INSTANCE;
    }

    public void getAsync(String url, okhttp3.Callback callback) {
        Request request = new Request.Builder().url(url).build();
        mOkhttpClient.newCall(request).enqueue(callback);
    }

    public Response getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return mOkhttpClient.newCall(request).execute();
    }

}
