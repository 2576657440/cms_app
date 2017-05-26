package org.com.gannan.farminginfoplatform.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.ChoicePic;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.ImageTools;
import org.com.gannan.farminginfoplatform.comm.UtilsDialog;
import org.com.gannan.farminginfoplatform.easypermissions.EasyPermissions;
import org.com.gannan.farminginfoplatform.entity.ClassifyEntity;
import org.com.gannan.farminginfoplatform.request.MClassifyListRequest;
import org.com.gannan.farminginfoplatform.request.ModifyDetailRequest;
import org.com.gannan.farminginfoplatform.request.ModifySbtRequest;
import org.com.gannan.farminginfoplatform.selectiempic.bean.PhotoInfo;
import org.com.gannan.farminginfoplatform.utils.DateUtils;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.com.gannan.farminginfoplatform.utils.Util;
import org.com.gannan.farminginfoplatform.widget.ChooseOneDateDialog;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ModifyDetailActivity extends AppCompatActivity implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks {

    private static final int PERMISSIONS_REQUEST_FLAG = 2;
    @InjectView(id = R.id.title_tv)
    private TextView titleTv;
    @InjectView(id = R.id.imageView1)
    private ImageView back;
    @InjectView(id = R.id.picLayout)
    private LinearLayout picLayout;
    @InjectView(id = R.id.addPic)
    private ImageView addPic;
    @InjectView(id = R.id.buttonView1)
    private Button submitBtn;
    @InjectView(id = R.id.editText1)
    private EditText title;
    @InjectView(id = R.id.editText2)
    private EditText content;
    @InjectView(id = R.id.editText3)
    private EditText tel;
    @InjectView(id = R.id.editText4)
    private EditText address;
    @InjectView(id = R.id.editText5)
    private EditText linkman;
    @InjectView(id = R.id.editText6)
    private EditText infoArea;
    @InjectView(id = R.id.textView1)
    private TextView dateTv;
    @InjectView(id = R.id.textView2)
    private TextView typeTv;
    @InjectView(id = R.id.hintText)
    private TextView hintTv;
    @InjectView(id = R.id.linearlayout2)
    private LinearLayout linearLayout2;//有效期
    @InjectView(id = R.id.dLinear)
    private LinearLayout dLinear;//个人信息版块
    @InjectView(id = R.id.linearlayout3)
    private LinearLayout linearLayout3;//类型
    //    private LinearLayout linearLayout4;//电话
//    private LinearLayout linearLayout5;//地址
//    private LinearLayout linearLayout6;//联系人
//    private LinearLayout linearLayout7;//所属乡镇
    @InjectView(id = R.id.view4)
    private View view4;
    @InjectView(id = R.id.scrollView)
    private ScrollView scrollView;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    public ChooseOneDateDialog dateDialog;
    public Map<String, String> pathMap = new HashMap<>();
    private String typeId;
    public String type = "";
    private String roleFlag = "";
    public List<ClassifyEntity> listNews = new ArrayList<>();
    public List<ClassifyEntity> listOther = new ArrayList<>();
    public List<ClassifyEntity> listOtherChild = new ArrayList<>();
    public List<ClassifyEntity> AllList = new ArrayList<>();
    public List<ClassifyEntity> newsAndOther = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_publish);
        Injector.injectAll(this);
        picLayout = (LinearLayout) findViewById(R.id.picLayout);
        titleTv.setText(R.string.str_msg137);
        addPic.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
//        linearLayout3.setOnClickListener(this);
        addListener();
        //获取资讯详情，以便修改
        new ModifyDetailRequest(this, "1");
        //获取栏目列表
        new MClassifyListRequest(this,"");
    }

    private void addListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.addTextChangedListener(new TextWatcher() {
            private boolean reset;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!reset && count >= 2 && count <= 3) {
                    CharSequence input = s.subSequence(start, start + count);
                    if (input.toString().equals("（）")) {
                        return;
                    }
                    for (int i = 0; i < input.length(); i++) {
                        if (Util.isEmoChar(input.charAt(i))) {
                            reset = true;
                            title.setText(s.toString().replace(input.toString(), ""));
                            title.setSelection(start);
                        }
                    }
                } else {
                    reset = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            private boolean reset;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!reset && count >= 2 && count <= 3) {
                    CharSequence input = s.subSequence(start, start + count);
                    if (input.toString().equals("（）")) {
                        return;
                    }
                    for (int i = 0; i < input.length(); i++) {
                        if (Util.isEmoChar(input.charAt(i))) {
                            reset = true;
                            content.setText(s.toString().replace(input.toString(), ""));
                            content.setSelection(start);
                            break;
                        }
                    }
                } else {
                    reset = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPic:
                if (pathMap.size() >= 5) {
                    UtilsDialog dialog = new UtilsDialog();
                    dialog.showSingleNoticeDialog(this, getString(R.string.str_msg222), "最多上传5幅图片");
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //判断是否已获取权限
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED) {
                            // Request permission
                            EasyPermissions.requestPermissions(this, "请允许打开媒体",
                                    PERMISSIONS_REQUEST_FLAG, Manifest.permission.CAMERA);

                        } else {
                            //已获取
                            //添加图片
                            new ChoicePic(this, false, pathMap.size());
                        }
                    } else {
                        //低版本直接执行
                        //添加图片
                        new ChoicePic(this, false, pathMap.size());
                    }
                }
                break;
            case R.id.buttonView1:
                if (check()) {
                    hintTv.setText("");
                    HashMap<String, String> map = new HashMap<>();
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("id", getIntent().getStringExtra("id"));
                        obj.put("title", title.getText().toString());
                        obj.put("content", content.getText().toString());
                        obj.put("mobil", tel.getText().toString());
                        obj.put("address", address.getText().toString());
                        obj.put("contacts", linkman.getText().toString());
                        obj.put("expiryDates", dateTv.getText().toString());
                        obj.put("infoArea", infoArea.getText().toString());
                        obj.put("categoryId", getTypeId());// ------栏目id
                        obj.put("image", getImagePathStr());
                        map.put("jsonStr", obj.toString());
                        map.put("token", SharedUtil.getToken(this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //提交数据
                    new ModifySbtRequest(this, map);
                }
                break;
            case R.id.linearlayout2://有效期
                showDate();
                break;
            case R.id.linearlayout3://类型
                openSelectDialog(newsAndOther);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String savePath = "";
            String getFilePath = "";
            switch (requestCode) {
                //拍照获取图片
                case ChoicePic.PHOTO_WITH_CAMERA:
                    String status = Environment.getExternalStorageState();

                    //是否挂载了SD卡
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        savePath = Environment.getExternalStorageDirectory() + "/images/";
                        getFilePath = Environment.getExternalStorageDirectory() + "/images/image.png";
                    } else {
                        savePath = this.getCacheDir() + "/images/";
                        getFilePath = this.getCacheDir() + "/images/image.png";
                    }
                    //缩小原图比例，减少内存占用
                    Bitmap bitmap = ImageTools.imageZoom(BitmapFactory.decodeFile(getFilePath));

                    ImageTools.saveFile(bitmap, "image.png", savePath, this);
                    String fileName = ImageTools.createPhotoFileName();
                    //显示到界面
                    setImageViewPic(bitmap, fileName);
                    File file = new File(savePath + "image.png");
                    //上传
                    toUpLoad(file, fileName);

                    break;
                //从图库中选择图片
                case ChoicePic.PHOTO_WITH_DATA:
                    try {
                        //多选
                        List<PhotoInfo> objectList = (List<PhotoInfo>) data.getSerializableExtra("picInfoList");
                        for (PhotoInfo pi : objectList) {
                            String sta = Environment.getExternalStorageState();
                            //是否挂载了SD卡
                            if (sta.equals(Environment.MEDIA_MOUNTED)) {
                                savePath = Environment.getExternalStorageDirectory() + "/images/";
                            } else {
                                savePath = this.getCacheDir() + "/images/";
                            }
                            //缩小原图比例，减少内存占用
                            Bitmap bit = ImageTools.imageZoom(BitmapFactory.decodeFile(pi.getPath_absolute()));

                            String fn = ImageTools.createPhotoFileName();
                            ImageTools.saveFile(bit, fn, savePath, this);
                            //显示到界面
                            setImageViewPic(bit, fn);

                            File file1 = new File(savePath + fn);
                            //上传
                            toUpLoad(file1, fn);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }

    public void setTypeList(List<ClassifyEntity> list) {
        AllList = list;
        for (int i = 0; i < list.size(); i++) {
            final String type = list.get(i).getType();
            if ("0".equals(type)) {//0为供求，2为新闻,3为供求二级列表
                listOther.add(list.get(i));
                newsAndOther.add(list.get(i));
            } else if ("2".equals(type)) {
                listNews.add(list.get(i));
                newsAndOther.add(list.get(i));
            } else if ("3".equals(type)) {
                listOtherChild.add(list.get(i));
            }
        }
    }

    private String getImagePathStr() {
        StringBuffer sbf = new StringBuffer();
        if (pathMap != null && pathMap.size() > 0) {
            Set<String> key = pathMap.keySet();
            Iterator it = key.iterator();
            while (it.hasNext()) {
                String tempKey = (String) it.next();
                sbf.append("," + pathMap.get(tempKey));
            }
        }
        return sbf.toString();
    }

    public void setHintText(String str) {
        hintTv.setText(str);
    }

    private boolean check() {

        if ("".equals(typeTv.getText().toString())) {
            hintTv.setText(getResources().getString(R.string.hint21));
            return false;
        }
        if ("".equals(title.getText().toString())) {
            hintTv.setText(getResources().getString(R.string.hint15));
            return false;
        }
        if ("".equals(content.getText().toString())) {
            hintTv.setText(getResources().getString(R.string.hint16));
            return false;
        }
        if ("".equals(tel.getText().toString())) {
            if ("3".equals(type)) {
                hintTv.setText(getResources().getString(R.string.hint17));
                return false;
            }
        }
        if ("".equals(address.getText().toString())) {
            if ("3".equals(type)) {
                hintTv.setText(getResources().getString(R.string.hint18));
                return false;
            }
        }
        if ("".equals(linkman.getText().toString())) {
            if ("3".equals(type)) {
                hintTv.setText(getResources().getString(R.string.hint19));
                return false;
            }
        }
        if ("".equals(infoArea.getText().toString())) {
            if ("3".equals(type)) {
                hintTv.setText(getResources().getString(R.string.hint22));
                return false;
            }
        }
        if ("".equals(dateTv.getText().toString())) {
            if ("2".equals(type)) {
                hintTv.setText(getResources().getString(R.string.hint20));
                return false;
            }
        }
        return true;
    }

    //弹出选择对话框
    public void openSelectDialog(final List<ClassifyEntity> list) {

        //自定义Context,添加主题
        Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        String[] choiceItems = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            choiceItems[i] = list.get(i).getName();
        }
        ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choiceItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();
                type = list.get(position).getType();
                if (!"0".equals(type)) {
                    String name = list.get(position).getName();
                    typeTv.setText(name);
                    setTypeId(list.get(position).getId());
                }
                if ("0".equals(type)) {//0或1为供求，2为新闻
                    openSelectDialog(listOtherChild);
                } else if ("2".equals(type)){
                    dLinear.setVisibility(View.GONE);
                    tel.setText("");
                    address.setText("");
                    linkman.setText("");
                    infoArea.setText("");
                    linearLayout2.setVisibility(View.VISIBLE);
                    view4.setVisibility(View.VISIBLE);
                } else if ("3".equals(type)) {
                    dLinear.setVisibility(View.VISIBLE);
                    tel.setText(SharedUtil.getPhoneNo(ModifyDetailActivity.this));
                    address.setText(SharedUtil.getAreaName(ModifyDetailActivity.this));
                    linkman.setText(SharedUtil.getName(ModifyDetailActivity.this));
                    dateTv.setText("");
                    linearLayout2.setVisibility(View.GONE);
                    view4.setVisibility(View.GONE);
                }
            }

        });
        builder.create().show();

    }

    private void showDate() {
        // 选择日期
        int[] date = null;
        if (year == 0 && month == 0 && day == 0) {
            String curDate = DateUtils.getCurrentDate();
            date = DateUtils.getYMDArray(curDate, "-");
        }

        if (date != null) {
            year = date[0];
            month = date[1];
            day = date[2];
        }
        dateDialog = new ChooseOneDateDialog(this, new ChooseOneDateDialog.PriorityListener() {

            public void refreshPriorityUI(String sltYear, String sltMonth,
                                          String sltDay, int flag) {

                year = Integer.parseInt(sltYear);
                month = Integer.parseInt(sltMonth);
                day = Integer.parseInt(sltDay);
                String st = year + "-" + month + "-" + day;
                dateTv.setText(st);

            }

        }, year, month, day);

        Window window = dateDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dateDialog.setCancelable(true);
        dateDialog.show();
    }

    public void setImageViewPic(Bitmap bitmap, String fileName) {
        int parentw = (int) Util.dpToPx(this, 100);
        int width = (int) Util.dpToPx(this, 85);
        int btnWidth = (int) Util.dpToPx(this, 25);
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(parentw, parentw);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;

        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(btnWidth, btnWidth);
        param.gravity = Gravity.TOP | Gravity.RIGHT;

        final FrameLayout frame = new FrameLayout(this);
        frame.setLayoutParams(p);
        frame.setTag(fileName);
        ImageView pic = new ImageView(this);
        pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        pic.setImageBitmap(bitmap);

        ImageView delBtn = new ImageView(this);
        delBtn.setImageResource(R.mipmap.btn_del);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picLayout.removeView(frame);
                pathMap.remove(frame.getTag());
            }
        });
        frame.addView(pic, params);
        frame.addView(delBtn, param);

        picLayout.addView(frame);

    }

    public void toUpLoad(File file, final String fileName) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        final String str = year + "/" + month + "/" + fileName;
        String url = Global.SERVER_URL + "ph/phoneFileUpload?token=" + SharedUtil.getToken(this)+"&actID="+getIntent().getStringExtra("id");
        OkHttpUtils.post()
                .url(url)
                .addFile("file_fields", fileName, file)
//                .addParams("","")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("result") && "0".equals(obj.getString("result"))) {
                                pathMap.put(fileName, str);

                            } else if (obj.has("result") && "2".equals(obj.getString("result"))) {
                                UtilsDialog utilsDialog =UtilsDialog.getInstance();
                                    utilsDialog.showExitNoticeDialog(ModifyDetailActivity.this, getString
                                            (R.string.str_msg222), obj.getString("msg"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
//                        progressDialog = new StartProgressDialog(activity);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
//                        progressDialog.stopProgressDialog();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(Global.TAG, tag + ":" + e.toString());
                    }
                });
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void clearUI() {
        title.setText("");
        content.setText("");
        dateTv.setText("");
        typeTv.setText("");
        hintTv.setText("");
        infoArea.setText("");
        setTypeId(null);
        pathMap.clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
                int count = picLayout.getChildCount();
                for (int i = count; i >= 1; i--) {
                    View view = picLayout.getChildAt(i);
                    picLayout.removeView(view);
                }
            }
        });
        if (SharedUtil.getLoginFlag(this)) {
            String msg = "";
            if ("dept".equals(SharedUtil.getRoleFlag(this))) {
                msg = getString(R.string.str_msg226);
            } else {
                msg = getString(R.string.str_msg221);
            }
            final UtilsDialog dialog = new UtilsDialog();
            dialog.showSingleNoticeDialog(this, getString(R.string.str_msg222), msg);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FLAG: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new ChoicePic(this, false, pathMap.size());

                }
                break;
            }
        }
    }
}
