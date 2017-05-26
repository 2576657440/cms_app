package org.com.gannan.farminginfoplatform.comm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import java.io.File;
import java.net.URI;

public class DownLoadApkResult {

	public static void downInstall(Context context, String fileUri) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			File file = new File(URI.create(fileUri));
			//provider authorities
			Uri apkUri = FileProvider.getUriForFile(context, "org.com.gannan.farminginfoplatform", file);
			//Granting Temporary Permissions to a URI
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(Uri.parse(fileUri), "application/vnd.android.package-archive");
		}
		context.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
