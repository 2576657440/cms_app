package org.com.gannan.farminginfoplatform.comm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;


public class DownLoadReceiver extends BroadcastReceiver {
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("InlinedApi")
	@Override
	public void onReceive(Context context, Intent intent) {

		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
			DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Query query = new DownloadManager.Query();
			// 在广播中取出下载任务的id
//			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			query.setFilterById(UpgradeDownManager.DOWNLOADID);
			Cursor c = manager.query(query);
			if (c.moveToFirst()) {
				String fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
				if (fileUri != null) {
					DownLoadApkResult.downInstall(context, fileUri);
				}
			}
			c.close();
		}

	}

}
