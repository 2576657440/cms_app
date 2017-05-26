package org.com.gannan.farminginfoplatform.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.com.gannan.farminginfoplatform.comm.UtilsDialog;
import org.com.gannan.farminginfoplatform.easypermissions.EasyPermissions;
import org.com.gannan.farminginfoplatform.entity.ClassifyEntity;
import org.com.gannan.farminginfoplatform.entity.MapEntity;
import org.com.gannan.farminginfoplatform.request.SbtBuySellRequest;
import org.com.gannan.farminginfoplatform.utils.DateUtils;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.com.gannan.farminginfoplatform.utils.Util;
import org.com.gannan.farminginfoplatform.widget.ChooseOneDateDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/15.
 */

public class PublishFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final int PERMISSIONS_REQUEST_FLAG = 2;
    private View view;
    private LinearLayout picLayout;
    private ImageView addPic;
    private Button submitBtn;
    private EditText title;
    private EditText content;
    private EditText tel;
    private EditText address;
    private EditText linkman;
    private EditText infoArea;
    private TextView dateTv;
    private TextView typeTv;
    private TextView hintTv;
    private TextView nothingTv;
    private LinearLayout linearLayout2;//有效期
    private LinearLayout dLinear;//个人信息版块
    private LinearLayout linearLayout3;//类型
    //    private LinearLayout linearLayout4;//电话
//    private LinearLayout linearLayout5;//地址
//    private LinearLayout linearLayout6;//联系人
//    private LinearLayout linearLayout7;//所属乡镇
    private View view4;
    private ScrollView scrollView;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    public ChooseOneDateDialog dateDialog;
//    public Map<String, String> pathMap = new HashMap<String, String>();
    public MapEntity mapEntity;
    private String typeId;
    private String type = "";
    private String roleFlag = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_publish, container, false);
        picLayout = (LinearLayout) view.findViewById(R.id.linearlayout1);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linearlayout2);
        dLinear = (LinearLayout) view.findViewById(R.id.dLinear);
        linearLayout3 = (LinearLayout) view.findViewById(R.id.linearlayout3);
        view4 = (View) view.findViewById(R.id.view4);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        nothingTv = (TextView) view.findViewById(R.id.nothing);
        title = (EditText) view.findViewById(R.id.editText1);
        content = (EditText) view.findViewById(R.id.editText2);
        tel = (EditText) view.findViewById(R.id.editText3);
        address = (EditText) view.findViewById(R.id.editText4);
        linkman = (EditText) view.findViewById(R.id.editText5);
        infoArea = (EditText) view.findViewById(R.id.editText6);
        dateTv = (TextView) view.findViewById(R.id.textView1);
        typeTv = (TextView) view.findViewById(R.id.textView2);
        hintTv = (TextView) view.findViewById(R.id.hintText);
        submitBtn = (Button) view.findViewById(R.id.buttonView1);
        addPic = (ImageView) view.findViewById(R.id.addPic);
        addPic.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        mapEntity=new MapEntity();
        addListener();
        changeType();
        return view;
    }

    //    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Log.i(TAG, "onSaveInstanceState: ");
//        if(!mapEntity.getPathMap().isEmpty()){
//            outState.putSerializable("pathMap", (Serializable) mapEntity.getPathMap());
//        }
//        outState.putString("title",title.getText().toString());
//        outState.putString("content",content.getText().toString());
//        outState.putString("mobil",tel.getText().toString());
//        outState.putString("address",address.getText().toString());
//        outState.putString("contacts",linkman.getText().toString());
//        outState.putString("expiryDates",dateTv.getText().toString());
//        outState.putString("infoArea",infoArea.getText().toString());
//        outState.putString("typeTv",typeTv.getText().toString());
//        outState.putString("categoryId",typeId);
//        outState.putString("type",type);
//
//    }

//    public void onStateRestored(Bundle savedState) {
//        super.onViewStateRestored(savedState);
//        Log.i(TAG, "onViewStateRestored: ");
//        if(savedState!=null){
//            Serializable s=savedState.getSerializable("pathMap");
//            if(s!=null){
//                mapEntity.setPathMap((Map<String, String>)s);
//            }
//            title.setText(savedState.getString("title"));
//            content.setText(savedState.getString("content"));
//            tel.setText(savedState.getString("mobil"));
//            address.setText(savedState.getString("address"));
//            linkman.setText(savedState.getString("contacts"));
//            dateTv.setText(savedState.getString("expiryDates"));
//            infoArea.setText(savedState.getString("infoArea"));
//            typeTv.setText(savedState.getString("typeTv"));
//            typeId=savedState.getString("categoryId");
//            type=savedState.getString("type");
//            if ("2".equals(type)) {
//                dLinear.setVisibility(View.GONE);
//                linearLayout2.setVisibility(View.VISIBLE);
//                view4.setVisibility(View.VISIBLE);
//            } else if ("3".equals(type)) {
//                dLinear.setVisibility(View.VISIBLE);
//                linearLayout2.setVisibility(View.GONE);
//                view4.setVisibility(View.GONE);
//            }
//        }
//    }

    private void addListener() {
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
    public void onStart() {
        super.onStart();
    }

    public void changeType() {
        if (SharedUtil.getLoginFlag(getContext())) {
            scrollView.setVisibility(View.VISIBLE);
            nothingTv.setVisibility(View.GONE);
            String flag = SharedUtil.getRoleFlag(getContext());
            if ("d".equals(flag)) {
                dLinear.setVisibility(View.VISIBLE);
                tel.setText(SharedUtil.getPhoneNo(getContext()));
                address.setText(SharedUtil.getAreaName(getContext()));
                linkman.setText(SharedUtil.getName(getContext()));
                linearLayout2.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
            } else {
                dLinear.setVisibility(View.GONE);
                tel.setText("");
                address.setText("");
                linkman.setText("");
                linearLayout2.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
            }

        } else {
            scrollView.setVisibility(View.GONE);
            nothingTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPic:
                if (mapEntity.getPathMap().size() >= 5) {
                    UtilsDialog dialog = new UtilsDialog();
                    dialog.showSingleNoticeDialog(getActivity(), getString(R.string.str_msg222), "最多上传5幅图片");
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //判断是否已获取权限
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED) {
                            // Request permission
                            EasyPermissions.requestPermissions(this, "请允许打开媒体",
                                    PERMISSIONS_REQUEST_FLAG, Manifest.permission.CAMERA);

                        } else {
                            //已获取
                            //添加图片
                            new ChoicePic(getActivity(), false, mapEntity.getPathMap().size());
                        }
                    } else {
                        //低版本直接执行
                        //添加图片
                        new ChoicePic(getActivity(), false, mapEntity.getPathMap().size());
                    }
                }

                break;
            case R.id.buttonView1:
                if (check()) {
                    hintTv.setText("");
                    HashMap<String, String> map = new HashMap<>();
                    try {
                        JSONObject obj = new JSONObject();
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
                        map.put("token", SharedUtil.getToken(getContext()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //提交数据
                    new SbtBuySellRequest(this, map);
                }
                break;
            case R.id.linearlayout2://有效期
                showDate();
                break;
            case R.id.linearlayout3://类型
                // d,infoadmin,dept
                roleFlag = SharedUtil.getRoleFlag(getContext());
                List<Fragment> list = getActivity().getSupportFragmentManager().getFragments();
                //普通用户
                if ("d".equals(roleFlag)) {
                    //供求信息二级列表
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof HomeFragment) {
                            openSelectDialog(((HomeFragment) list.get(i)).listOtherChild);
                        }
                    }
                } else if ("infoadmin".equals(roleFlag) || "dept".equals(roleFlag)) {
                    //新闻信息和供求
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof HomeFragment) {
                            openSelectDialog(((HomeFragment) list.get(i)).newsAndOther);
                        }
                    }
                }
                break;
        }
    }

    private String getImagePathStr() {
        StringBuffer sbf = new StringBuffer();
        if (mapEntity.getPathMap() != null && mapEntity.getPathMap().size() > 0) {
            Set<String> key = mapEntity.getPathMap().keySet();
            Iterator it = key.iterator();
            while (it.hasNext()) {
                String tempKey = (String) it.next();
                sbf.append("," + mapEntity.getPathMap().get(tempKey));
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
        Context dialogContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth);
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
                setHintText("");
                type = list.get(position).getType();
                if (!"0".equals(type)) {
                    String name = list.get(position).getName();
                    typeTv.setText(name);
                    setTypeId(list.get(position).getId());
                }
                if ("infoadmin".equals(roleFlag) || "dept".equals(roleFlag)) {
                    if ("0".equals(type)) {//0或1为供求，2为新闻
                        List<Fragment> list = getActivity().getSupportFragmentManager().getFragments();
                        //供求信息
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i) instanceof HomeFragment) {
                                openSelectDialog(((HomeFragment) list.get(i)).listOtherChild);
                            }
                        }
                    } else if ("2".equals(type)) {
                        dLinear.setVisibility(View.GONE);
                        tel.setText("");
                        address.setText("");
                        linkman.setText("");
                        infoArea.setText("");
                        linearLayout2.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                    } else if ("3".equals(type)) {
                        dLinear.setVisibility(View.VISIBLE);
                        tel.setText(SharedUtil.getPhoneNo(getContext()));
                        address.setText(SharedUtil.getAreaName(getContext()));
                        linkman.setText(SharedUtil.getName(getContext()));
                        dateTv.setText("");
                        linearLayout2.setVisibility(View.GONE);
                        view4.setVisibility(View.GONE);
                    }

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
        dateDialog = new ChooseOneDateDialog(getActivity(), new ChooseOneDateDialog.PriorityListener() {

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
        int parentw = (int) Util.dpToPx(getContext(), 100);
        int width = (int) Util.dpToPx(getContext(), 85);
        int btnWidth = (int) Util.dpToPx(getContext(), 25);
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(parentw, parentw);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;

        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(btnWidth, btnWidth);
        param.gravity = Gravity.TOP | Gravity.RIGHT;

        final FrameLayout frame = new FrameLayout(getContext());
        frame.setLayoutParams(p);
        frame.setTag(fileName);
        ImageView pic = new ImageView(getContext());
        pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        pic.setImageBitmap(bitmap);

        ImageView delBtn = new ImageView(getContext());
        delBtn.setImageResource(R.mipmap.btn_del);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picLayout.removeView(frame);
                mapEntity.getPathMap().remove(frame.getTag());
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
        String url = Global.SERVER_URL + "ph/phoneFileUpload?token=" + SharedUtil.getToken(getContext())+"&actID=";
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
                                mapEntity.getPathMap().put(fileName, str);

                            } else if (obj.has("result") && "2".equals(obj.getString("result"))) {
                                mapEntity.getPathMap().clear();
                                getActivity().runOnUiThread(new Runnable() {
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
                                 UtilsDialog utilsDialog =UtilsDialog.getInstance();
                                    utilsDialog.showExitNoticeDialog(getActivity(),
                                            getString(R.string.str_msg222), obj.getString("msg"));

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
        mapEntity.getPathMap().clear();
        getActivity().runOnUiThread(new Runnable() {
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
        if (SharedUtil.getLoginFlag(getContext())) {
            String msg = "";
            if ("dept".equals(SharedUtil.getRoleFlag(getContext()))) {
                msg = getString(R.string.str_msg226);
            } else {
                msg = getString(R.string.str_msg221);
            }
            final UtilsDialog dialog = new UtilsDialog();
            dialog.showSingleNoticeDialog(getActivity(), getString(R.string.str_msg222), msg);
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

                    new ChoicePic(getActivity(), false, mapEntity.getPathMap().size());

                }
                break;
            }
        }
    }

}
