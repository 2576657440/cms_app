package org.com.gannan.farminginfoplatform.comm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.com.gannan.farminginfoplatform.selectiempic.main.SelectPhotoActy;

import java.io.File;

/**
 * 图片选取
 */
public class ChoicePic {
    public static final int PHOTO_WITH_DATA = 18;  //本地图片
    public static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片

    private Activity activity;
    private boolean single;
    private int picCount;
    private static final int allCount=5;

    public ChoicePic(Activity activity, boolean single, int picCount) {
        this.activity = activity;
        this.single = single;
        this.picCount = picCount;
        openPictureSelectDialog();
    }

    //弹出选择对话框
    private void openPictureSelectDialog() {

        //自定义Context,添加主题
        Context dialogContext = new ContextThemeWrapper(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        String[] choiceItems = new String[2];
        choiceItems[0] = "相机拍摄";  //拍照
        choiceItems[1] = "本地相册";  //从相册中选择
        ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choiceItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        doTakePhoto();
                        break;
                    case 1:
                        doPickPhotoFromGallery();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    /**
     * 调用系统拍照
     */
    private void doTakePhoto() {
        //调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=new File(Environment.getExternalStorageDirectory() + "/images/image.png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = activity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }else{
            Uri imageUri = Uri.fromFile(file);
            //指定照片保存路径，image为一个临时文件，每次拍照后这个图片都会被替换
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        activity.startActivityForResult(intent, PHOTO_WITH_CAMERA);
    }

    /**
     * 跳转到相册
     */
    private void doPickPhotoFromGallery() {
        if (single) {
            Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent, PHOTO_WITH_DATA);
        } else {
            Intent intent = new Intent(activity, SelectPhotoActy.class);
            if (picCount == 0) {
                intent.putExtra("count", allCount);
            } else if(picCount != 0) {
                intent.putExtra("count", allCount-picCount);
            }
            activity.startActivityForResult(intent, PHOTO_WITH_DATA);
        }
    }

}
