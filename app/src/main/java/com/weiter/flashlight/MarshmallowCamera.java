package com.weiter.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

class MarshmallowCamera {
    private static final String TAG = MyCameraImpl.class.getSimpleName();

    private CameraManager manager;
    private String cameraId;
    private Context mContext;

    @TargetApi(Build.VERSION_CODES.M)
    MarshmallowCamera(Context context) {
        mContext = context;
        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            final String[] list = manager.getCameraIdList();
            cameraId = list[0];
        } catch (CameraAccessException ignored) {
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    void toggleMarshmallowFlashlight( boolean enable) {
        try {
            manager.setTorchMode(cameraId, enable);
        } catch (CameraAccessException e) {
            Log.e(TAG, "toggle marshmallow flashlight " + e.getMessage());

         /*   Runnable mainRunnable = new Runnable() {
                @Override
                public void run() {
                    bus.post(new Events.CameraUnavailable());
                }
            };*/
//            new Handler(mContext.getMainLooper()).post(mainRunnable);
        }
    }
}
