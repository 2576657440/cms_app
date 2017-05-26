package org.com.gannan.farminginfoplatform;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.com.gannan.farminginfoplatform.comm.ImageLoaderComm;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    public static MyApplication application;
    private List<Context> activityList = new LinkedList<Context>();
    public static ImageLoaderConfiguration configuration;

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        application = new MyApplication();
        ImageLoader.getInstance().init(ImageLoaderComm.getImageConfig(getApplicationContext()));

//		ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
//				.cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
				.sslSocketFactory(HttpsUtils.sSLSocketFactory, HttpsUtils.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
    /**
     * 添加Activity到容器中
     */
    public void addActivity(Context context) {
        activityList.add(context);
    }

    /**
     * 从Activity容器中移除
     */
    public void removeActivity(Context context) {
        activityList.remove(context);
    }

    /**
     * 清空容器
     */
    public void clearList() {
        activityList.clear();
    }

    /**
     * 循环迭代Activity并finish
     */
    public void finishActivity() {
        for (Context context : activityList) {
            ((Activity) context).finish();
        }
        activityList.clear();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
        //程序终止
    }
}
