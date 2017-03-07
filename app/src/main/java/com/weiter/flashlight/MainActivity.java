package com.weiter.flashlight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Chu on 2017/3/2.
 */

public class MainActivity extends AppCompatActivity {
    ImageView large_onoff, little_onoff, point;
    private boolean onoff_glint = false;
    private MyCameraImpl mCameraImpl;
    private boolean ispointopen=false;
    public long exitTime = 0;//记录按下back键第一次的时间
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCameraImpl.toggleFlashlight();
            if (onoff_glint) {
                if(!ispointopen){
                    point.setImageResource(R.drawable.pilot_open);
                    ispointopen=true;
                }else{
                    point.setImageResource(R.drawable.pilot_close);
                    ispointopen=false;
                }

                handler.sendMessageDelayed(new Message(), 200);
            }
        }
    };
    ExecutorService singleThreadExecutor;

    private Thread thread;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        large_onoff = (ImageView) findViewById(R.id.large_onoff);
        little_onoff = (ImageView) findViewById(R.id.little_onoff);
        point = (ImageView) findViewById(R.id.flashing_point);
        mCameraImpl = new MyCameraImpl(this);

        large_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onoff_glint) {
                    little_onoff.setImageResource(R.drawable.bg_little);
                    mCameraImpl.disableFlashlight();
                    handler.removeMessages(0);
                    onoff_glint = false;
                }
                if(ispointopen){
                    point.setImageResource(R.drawable.pilot_close);
                    ispointopen=false;
                }
                mCameraImpl.toggleFlashlight();
                if (mCameraImpl.getIsFlashlightOn()) {
                    large_onoff.setImageResource(R.drawable.bg_largeoff);
                } else {
                    large_onoff.setImageResource(R.drawable.bg_large);
                }

            }
        });
        little_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onoff_glint) {
                    handler.sendMessageDelayed(new Message(), 200);
                    onoff_glint = true;
                    large_onoff.setImageResource(R.drawable.large_normal);
                    large_onoff.setImageResource(R.drawable.bg_large);
                    little_onoff.setImageResource(R.drawable.bg_littleoff);
                } else {
                    if(ispointopen){
                        point.setImageResource(R.drawable.pilot_close);
                        ispointopen=false;
                    }
                    mCameraImpl.disableFlashlight();
                    handler.removeMessages(0);
                    onoff_glint = false;
                    little_onoff.setImageResource(R.drawable.bg_little);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一下退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraImpl.releaseCamera();
    }
}
