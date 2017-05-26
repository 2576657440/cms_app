package org.com.gannan.farminginfoplatform.player;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.ImageLoaderComm;
import org.com.gannan.farminginfoplatform.request.AudioNewsDetailRequest;

public class PlayerActivity extends AppCompatActivity {

    private PlayerView player;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_player);
        ((RelativeLayout) findViewById(R.id.commTitleLayout)).setVisibility(View.GONE);
        String[] url=null;
        String[] path=null;
       if(getIntent().getStringExtra("video")!=null){
           url=getIntent().getStringExtra("video").split("\\|");
        }
        if(getIntent().getStringExtra("image")!=null){
            path=getIntent().getStringExtra("image").split("\\|");
        }
        if(url!=null){
            if(path==null){
                init(Global.URL+url[1], "");
            }else {
                init(Global.URL+url[1], Global.URL+path[1]);
            }
        }
        //查看视频已发布详情
        new AudioNewsDetailRequest(this,"");
    }

    public void init(String url, final String path) {
      //常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getAttributes().screenBrightness=-1;
//        String url1 = "http://baobab.wdjcdn.com/145076769089714.mp4";
        player = new PlayerView(this)
                .setScaleType(PlayStateParams.fitparent)
                .hideControlPanl(true)//隐藏底部操作栏
//                .hideMenu(true)
//                .hideRotation(true)
//                .hideSteam(true)
//                .hideCenterPlayer(true)
                .forbidTouch(false)
                .setForbidDoulbeUp(true)
                .setProcessDurationOrientation(PlayStateParams.PROCESS_LANDSCAPE)
                .hideFullscreen(false)
                .setChargeTie(false,36000)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView iv) {
                        ImageLoader.getInstance().displayImage(path, iv, ImageLoaderComm.getImageOptions());
                    }
                })
                .setPlaySource(url)
                .startPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        //恢复系统其它媒体的状态
        MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        //暂停系统其它媒体的状态
        MediaUtils.muteAudioFocus(mContext, false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();

    }

}
