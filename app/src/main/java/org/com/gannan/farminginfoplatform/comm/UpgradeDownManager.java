package org.com.gannan.farminginfoplatform.comm;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.com.gannan.farminginfoplatform.R;

import java.io.File;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE;

public class UpgradeDownManager {
    private DownloadManager downloadManager;
    private Activity activity;
    private String url;
    public static long DOWNLOADID;
    public UpgradeDownManager(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    public void loadFile() {
        downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        Request request = new Request(uri);
        // 设置下载网络为wifi才开始下载
        // request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
        request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);

        request.setTitle(activity.getString(R.string.app_name));
        //如果没有挂载SD卡将下载到缓存中，并会被回收
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String  path = Environment.getExternalStorageDirectory() + "/apk/";
            // 将文件下载到指定的SD卡路径中
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir,Global.APK_NAME);
            request.setDestinationUri(Uri.fromFile(file));
        }
        // 将文件下载到应用外部存储的专有文件夹中，删除应用时，文件夹也会被删除
//        request.setDestinationInExternalFilesDir(activity,
//                Environment.DIRECTORY_DOWNLOADS, "szy.apk");
        // 指定你的下载路径在外部存储的公共文件夹之下,下载的文件可被其他的应用共享
        // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,
        // "Android_Rock.mp3");
        request.setNotificationVisibility(VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        // 将要求下载的文件的Uri传递给Download Manager的enqueue方法
        // 在这里返回的reference变量是系统为当前的下载请求分配的一个唯一的ID，我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操作或者查询
        DOWNLOADID=downloadManager.enqueue(request);
    }

}
