package org.com.gannan.farminginfoplatform.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.com.gannan.farminginfoplatform.widget.BadgeView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.activity.BuySellAuditListActivity;
import org.com.gannan.farminginfoplatform.activity.LoginActivity;
import org.com.gannan.farminginfoplatform.activity.MyInfoActivity;
import org.com.gannan.farminginfoplatform.activity.NewsAuditListActivity;
import org.com.gannan.farminginfoplatform.activity.PasswordActivity;
import org.com.gannan.farminginfoplatform.activity.PublishHistoryListActivity;
import org.com.gannan.farminginfoplatform.activity.WaitPublishListActivity;
import org.com.gannan.farminginfoplatform.easypermissions.EasyPermissions;
import org.com.gannan.farminginfoplatform.request.CheckVersionRequest;
import org.com.gannan.farminginfoplatform.request.GetAuditCountRequest;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.com.gannan.farminginfoplatform.utils.Util;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static org.com.gannan.farminginfoplatform.R.id.Reg_quit;

/**
 * Created by Administrator on 2016/11/15.
 */

public class MineFragment extends Fragment implements View.OnClickListener,
        GetAuditCountRequest.RefreshCount, EasyPermissions.PermissionCallbacks  {
    private static final int PERMISSIONS_REQUEST_STORAGE1 = 7;//自动检查
    private static final int PERMISSIONS_REQUEST_STORAGE2 = 8;//手动检查
    private View view;
    private TextView nameTv;
    private TextView login;
    private ImageView headIv;
    private TextView publishTv;
    private TextView modifyTv;
    private TextView versionTv;
    private TextView buySellTv;
    private TextView newsTv;
    private TextView pubnumTv;
    private TextView myInfoTv;
    private Button quit;
    private BadgeView badgeViewBs;
    private BadgeView badgeViewNs;
    private BadgeView badgeViewPs;
    private RelativeLayout relativeLayout2;//发布记录
    private RelativeLayout relativeLayout5;//密码修改
    private RelativeLayout relativeLayout3;//供求审核
    private RelativeLayout relativeLayout4;//资讯审核
    private RelativeLayout relativeLayout8;//个人信息
    private RelativeLayout relativeLayout9;//待发布
    private LinearLayout auditLayout;
    private View line1;
    private View line2;
    private View line3;
    private View line4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        init();
        return view;
    }

    private void init() {
        TextView set_version = (TextView) view.findViewById(R.id.set_version);
        set_version.setText(Util.getVersionName(getContext()));

        auditLayout = (LinearLayout) view.findViewById(R.id.linearlayout2);
        relativeLayout2 = (RelativeLayout) view.findViewById(R.id.relativeLayout2);//发布记录
        relativeLayout3 = (RelativeLayout) view.findViewById(R.id.relativeLayout3);//供求审核
        relativeLayout4 = (RelativeLayout) view.findViewById(R.id.relativeLayout4);//资讯审核
        relativeLayout5 = (RelativeLayout) view.findViewById(R.id.relativeLayout5);//密码修改
        relativeLayout8 = (RelativeLayout) view.findViewById(R.id.relativeLayout8);//个人信息
        relativeLayout9 = (RelativeLayout) view.findViewById(R.id.relativeLayout9);//待发布
        RelativeLayout relativeLayout6 = (RelativeLayout) view.findViewById(R.id.relativeLayout6);
        line1 = (View) view.findViewById(R.id.view1);//供求分割线
        line2 = (View) view.findViewById(R.id.view2);//资讯分割线
        line3 = (View) view.findViewById(R.id.view3);//个人信息
        line4 = (View) view.findViewById(R.id.view4);//待发布

        publishTv = (TextView) view.findViewById(R.id.textView2);
        buySellTv = (TextView) view.findViewById(R.id.textView3);
        newsTv = (TextView) view.findViewById(R.id.textView4);
        pubnumTv = (TextView) view.findViewById(R.id.textView10);
        modifyTv = (TextView) view.findViewById(R.id.textView5);
        versionTv = (TextView) view.findViewById(R.id.textView6);
        myInfoTv = (TextView) view.findViewById(R.id.textView9);

        quit = (Button) view.findViewById(Reg_quit);
        quit.setOnClickListener(this);

        login = (TextView) view.findViewById(R.id.textView1);
        login.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);
        relativeLayout4.setOnClickListener(this);
        relativeLayout5.setOnClickListener(this);
        relativeLayout6.setOnClickListener(this);
        relativeLayout8.setOnClickListener(this);
        relativeLayout9.setOnClickListener(this);
        headIv = (ImageView) view.findViewById(R.id.imageView1);
        nameTv = (TextView) view.findViewById(R.id.textView8);

        requestPermission(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (SharedUtil.getLoginFlag(getContext())) {
            login.setVisibility(View.GONE);
            headIv.setVisibility(View.VISIBLE);
            quit.setVisibility(View.VISIBLE);
            publishTv.setTextColor(getResources().getColor(R.color.diy_dark));
            modifyTv.setTextColor(getResources().getColor(R.color.diy_dark));
            myInfoTv.setTextColor(getResources().getColor(R.color.diy_dark));
            nameTv.setText(SharedUtil.getName(getContext()));
            relativeLayout2.setClickable(true);
            relativeLayout5.setClickable(true);
            relativeLayout8.setClickable(true);
            String flag = SharedUtil.getRoleFlag(getContext());
            if ("d".equals(flag)) {
                auditLayout.setVisibility(View.GONE);
                relativeLayout8.setVisibility(View.VISIBLE);
                line3.setVisibility(View.VISIBLE);
            } else {
                auditLayout.setVisibility(View.VISIBLE);
                relativeLayout8.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);
            }
            if ("infoadmin".equals(flag)) {
                line1.setVisibility(View.VISIBLE);
                relativeLayout3.setVisibility(View.VISIBLE);
                line2.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.GONE);
                relativeLayout9.setVisibility(View.GONE);
                //获取待审核个数
                GetAuditCountRequest getAuditCountRequest = new GetAuditCountRequest(getActivity(), buySellTv, newsTv,pubnumTv);
                getAuditCountRequest.setRefreshCount(this);
            } else if ("dept".equals(flag)) {
                line1.setVisibility(View.VISIBLE);
                relativeLayout3.setVisibility(View.VISIBLE);
                relativeLayout9.setVisibility(View.VISIBLE);
                line4.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.VISIBLE);
                //获取待审核个数
                GetAuditCountRequest getAuditCountRequest = new GetAuditCountRequest(getActivity(), buySellTv, newsTv,pubnumTv);
                getAuditCountRequest.setRefreshCount(this);
            }
        } else {
            login.setVisibility(View.VISIBLE);
            headIv.setVisibility(View.GONE);
            quit.setVisibility(View.GONE);
            publishTv.setTextColor(getResources().getColor(R.color.diy_date));
            modifyTv.setTextColor(getResources().getColor(R.color.diy_date));
            myInfoTv.setTextColor(getResources().getColor(R.color.diy_date));
            nameTv.setText(getResources().getString(R.string.str_msg101));
            relativeLayout2.setClickable(false);
            relativeLayout5.setClickable(false);
            relativeLayout8.setClickable(false);
            line1.setVisibility(View.GONE);
            relativeLayout3.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            relativeLayout4.setVisibility(View.GONE);
            relativeLayout9.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.relativeLayout2://发布记录
                intent = new Intent(getActivity(), PublishHistoryListActivity.class);
                getActivity().startActivity(intent);
                AnimUtil.changePageIn(getActivity());
                break;
            case R.id.relativeLayout3://供求审核
                intent = new Intent(getActivity(), BuySellAuditListActivity.class);
                intent.putExtra("flag", "0");
                getActivity().startActivity(intent);
                AnimUtil.changePageIn(getActivity());
                break;
            case R.id.relativeLayout4://资讯审核
                intent = new Intent(getActivity(), NewsAuditListActivity.class);
                intent.putExtra("flag", "1");
                getActivity().startActivity(intent);
                AnimUtil.changePageIn(getActivity());
                break;
            case R.id.relativeLayout9://待发布信息
                intent = new Intent(getActivity(), WaitPublishListActivity.class);
                getActivity().startActivity(intent);
                AnimUtil.changePageIn(getActivity());
                break;
            case R.id.relativeLayout5://密码修改
                intent = new Intent(getActivity(), PasswordActivity.class);
                getActivity().startActivity(intent);
                AnimUtil.changePageIn(getActivity());
                break;
            case R.id.relativeLayout6://版本检测
                requestPermission(false);
                break;
            case R.id.relativeLayout8://个人信息
                intent = new Intent(getActivity(), MyInfoActivity.class);
                getActivity().startActivity(intent);
                AnimUtil.changePageIn(getActivity());
                break;
            case R.id.textView1://登录、注册
                intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intent,777);
                AnimUtil.changePageIn(getActivity());
                break;
            case R.id.Reg_quit://退出
                List<Fragment> list=getFragmentManager().getFragments();
                SharedUtil.saveLoginFlag(getContext(), false);
                SharedUtil.savePassword(getContext(), "");
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i) instanceof PublishFragment){
                        ((PublishFragment)list.get(i)).clearUI();
                        ((PublishFragment)list.get(i)).changeType();
                    }
                }
                mHandler.sendMessage(mHandler.obtainMessage(0, ""));
                intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intent,777);
                AnimUtil.changePageIn(getActivity());
                break;
        }
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            JPushInterface.setAliasAndTags(getContext(), (String) msg.obj, null, mAliasCallback);
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
//            Toast.makeText(activity, logs, Toast.LENGTH_LONG).show();
        }

    };
    @Override
    public void changeNum(int flag, int num) {
        if (flag == 0) {
            if (badgeViewBs == null) {
                //供求待审核个数
                badgeViewBs = new BadgeView(getContext());
                badgeViewBs.setBadgeCount(num);
                badgeViewBs.setBackground(10, getActivity().getResources().getColor(R.color.diy_red));
                badgeViewBs.setTargetView(buySellTv);
                badgeViewBs.setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            } else {
                badgeViewBs.setBadgeCount(num);
            }
            badgeViewBs.setVisibility(View.VISIBLE);
        } else if (flag == 1) {
            if (badgeViewNs == null) {
                //新闻待审核个数
                badgeViewNs = new BadgeView(getContext());
                badgeViewNs.setBadgeCount(num);
                badgeViewNs.setBackground(10, getActivity().getResources().getColor(R.color.diy_red));
                badgeViewNs.setTargetView(newsTv);
                badgeViewNs.setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            } else {
                badgeViewNs.setBadgeCount(num);
            }
            badgeViewNs.setVisibility(View.VISIBLE);
        }else if (flag == 2) {
            if (badgeViewPs == null) {
                //待发布记录个数
                badgeViewPs = new BadgeView(getContext());
                badgeViewPs.setBadgeCount(num);
                badgeViewPs.setBackground(10, getActivity().getResources().getColor(R.color.diy_red));
                badgeViewPs.setTargetView(pubnumTv);
                badgeViewPs.setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            } else {
                badgeViewPs.setBadgeCount(num);
            }
            badgeViewPs.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void removeNum(int flag) {
        if (flag == 0 && badgeViewBs != null) {
            badgeViewBs.setVisibility(View.GONE);
        } else if (flag == 1 && badgeViewNs != null) {
            badgeViewNs.setVisibility(View.GONE);
        }else if (flag == 2 && badgeViewPs != null) {
            badgeViewPs.setVisibility(View.GONE);
        }
    }
    private void requestPermission(boolean auto) {
        if (Build.VERSION.SDK_INT >= 23) {
            //获取位置信息
            if (ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                int requestCode;
                if (auto) {
                    requestCode = PERMISSIONS_REQUEST_STORAGE1;
                } else {
                    requestCode = PERMISSIONS_REQUEST_STORAGE2;
                }
                // Request permission
                EasyPermissions.requestPermissions(this, getString(R.string.str_msg133),
                        requestCode, WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                checkVersion(auto);
            }
        } else {
            checkVersion(auto);
        }
    }
    private void checkVersion(boolean auto) {

        if (auto) {
            //自动检测版本
            new CheckVersionRequest(getActivity(), versionTv);
        } else {
            //手动检测版本
            new CheckVersionRequest(getActivity(), null);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_STORAGE1 || requestCode == PERMISSIONS_REQUEST_STORAGE2) {
            boolean flag = true;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                }
            }
            if (flag) {
                if(requestCode == PERMISSIONS_REQUEST_STORAGE1){
                    checkVersion(true);
                }else {
                    checkVersion(false);
                }

            } else {
                Toast.makeText(getContext(), getString(R.string.str_msg133), Toast.LENGTH_SHORT).show();
            }

        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
}
