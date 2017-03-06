package com.weiter.flashlight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Chu on 2017/3/2.
 */

public class MainActivity extends AppCompatActivity {
    ImageView large_onoff, little_onoff;
    private boolean onoff_glint = false;
    private MyCameraImpl mCameraImpl;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCameraImpl.toggleFlashlight();
            if (onoff_glint) {
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
        mCameraImpl = new MyCameraImpl(this);

        large_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraImpl.toggleFlashlight();
                handler.removeMessages(0);
                onoff_glint=false;
                if (mCameraImpl.getIsFlashlightOn()) {
                    large_onoff.setImageResource(R.drawable.large_button_3);
                    large_onoff.setImageResource(R.drawable.bg_largeoff);
                } else {
                    large_onoff.setImageResource(R.drawable.large_normal);
                    large_onoff.setImageResource(R.drawable.bg_large);
                }

            }
        });
        little_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onoff_glint){
                    handler.sendMessageDelayed(new Message(), 200);
                    onoff_glint=true;
                    large_onoff.setImageResource(R.drawable.large_normal);
                    large_onoff.setImageResource(R.drawable.bg_large);
                }else{
                    mCameraImpl.disableFlashlight();
                    handler.removeMessages(0);
                    onoff_glint=false;
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraImpl.releaseCamera();
    }
}
