package org.com.gannan.farminginfoplatform;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.com.gannan.farminginfoplatform.bartint.SystemBarTintManager;
import org.com.gannan.farminginfoplatform.comm.ChoicePic;
import org.com.gannan.farminginfoplatform.comm.ImageTools;
import org.com.gannan.farminginfoplatform.fragment.HomeFragment;
import org.com.gannan.farminginfoplatform.fragment.MineFragment;
import org.com.gannan.farminginfoplatform.fragment.PublishFragment;
import org.com.gannan.farminginfoplatform.selectiempic.bean.PhotoInfo;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.com.gannan.farminginfoplatform.R.id.viewPager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean exit;
    @InjectView(id = viewPager)
    private ViewPager vPager;
    @InjectView(id = R.id.tabbar_linear1)
    private LinearLayout tabbarLinear1;
    @InjectView(id = R.id.tabbar_linear2)
    private LinearLayout tabbarLinear2;
    @InjectView(id = R.id.tabbar_linear3)
    private LinearLayout tabbarLinear3;
    @InjectView(id = R.id.tabbar_linear4)
    private LinearLayout tabbarLinear4;
    @InjectView(id = R.id.tabbar_tv1)
    private TextView tabbarTv1;
    @InjectView(id = R.id.tabbar_tv2)
    private TextView tabbarTv2;
    @InjectView(id = R.id.tabbar_tv3)
    private TextView tabbarTv3;
    @InjectView(id = R.id.tabbar_iv1)
    private ImageView tabbarIv1;
    @InjectView(id = R.id.tabbar_iv2)
    private ImageView tabbarIv2;
    @InjectView(id = R.id.tabbar_iv3)
    private ImageView tabbarIv3;
    private List<Fragment> list = new ArrayList<>();
    private PublishFragment publishFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        Injector.injectAll(this);
        init();
        addListener();

    }

    private void addListener() {
        tabbarLinear1.setOnClickListener(this);
        tabbarLinear2.setOnClickListener(this);
        tabbarLinear3.setOnClickListener(this);
        tabbarLinear4.setOnClickListener(this);
    }

    private void init() {

        HomeFragment homeFragment = new HomeFragment();
        publishFragment = new PublishFragment();
        MineFragment mineFragment = new MineFragment();
        list.add(0, homeFragment);
        list.add(1, publishFragment);
        list.add(2, mineFragment);
        vPager.setOffscreenPageLimit(2);
        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        vPager.setAdapter(adapter);
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void onSelected(int index) {
        resetInit();
        switch (index) {
            case 0:
                tabbarIv1.setImageResource(R.mipmap.bar_home_focus);
                tabbarTv1.setTextColor(getResources().getColor(R.color.diyGreenColor));
                break;
            case 1:
                tabbarIv2.setImageResource(R.mipmap.bar_publish_focus);
                tabbarTv2.setTextColor(getResources().getColor(R.color.diyGreenColor));
                break;
            case 2:
                tabbarIv3.setImageResource(R.mipmap.bar_mine_focus);
                tabbarTv3.setTextColor(getResources().getColor(R.color.diyGreenColor));
                break;

        }
    }

    /**
     * 重置底部导航栏按钮状态
     */
    private void resetInit() {
        tabbarIv1.setImageResource(R.mipmap.bar_home);
        tabbarTv1.setTextColor(getResources().getColor(R.color.diy_bar_grey));
        tabbarIv2.setImageResource(R.mipmap.bar_publish);
        tabbarTv2.setTextColor(getResources().getColor(R.color.diy_bar_grey));
        tabbarIv3.setImageResource(R.mipmap.bar_mine);
        tabbarTv3.setTextColor(getResources().getColor(R.color.diy_bar_grey));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tabbar_linear1:
                vPager.setCurrentItem(0, true);
                break;
            case R.id.tabbar_linear2:
                vPager.setCurrentItem(1, true);
                break;
            case R.id.tabbar_linear3:
                vPager.setCurrentItem(2, true);
                break;
            case R.id.tabbar_linear4:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://baidu.com");
                intent.setData(content_url);
                startActivity(intent);

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
                case 777:
                    List<Fragment> fragments= getSupportFragmentManager().getFragments();
                    for (int i = 0; i <fragments.size() ; i++) {
                        Fragment f=fragments.get(i);
                        if(f instanceof PublishFragment){
                            ((PublishFragment)f).changeType();
                        }
                    }
                    break;
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
                    publishFragment.setImageViewPic(bitmap, fileName);
                    File file = new File(savePath + "image.png");
                    //上传
                    publishFragment.toUpLoad(file, fileName);

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
                            publishFragment.setImageViewPic(bit, fn);

                            File file1 = new File(savePath + fn);
                            //上传
                            publishFragment.toUpLoad(file1, fn);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除内存缓存图片
        ImageLoader.getInstance().clearMemoryCache();
//        ImageLoader.getInstance().clearDiskCache();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Timer timer = new Timer();
            if (!exit) {
                TimerTask task = new TimerTask() {

                    @Override
                    public void run() {
                        exit = false;
                    }
                };
                timer.schedule(task, 3000);
                //弹出提示信息
                Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
                exit = true;
            } else {
                timer.cancel();
                finish();
            }
        }
        return false;
    }
}
