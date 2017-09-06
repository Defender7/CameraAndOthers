package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;

import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.view.CameraView;

/**
 * Created by dengguochuan on 2017/8/25.
 */

public class CameraManager implements CameraView.ISurfaceListener, CameraOperation {
    public static volatile CameraManager instance = null;
    private CameraEngine mCameraEngine=null;
    private CameraView mCameraView;

    private CameraManager() {
    }

    public static CameraManager getInstance() {

        if (instance == null) {
            synchronized (CameraManager.class) {
                if (instance == null) {
                    instance = new CameraManager();
                }
            }
        }
        return instance;
    }

    /**
     * @des a
     * 初始化摄像机  拍摄保存路径   绑定相机和view
     * @parameter a
     */
    public void init(Context context, String recordPath, CameraView view) {
        mCameraView = view;
        mCameraView.setSurfaceListener(this);
        if (mCameraEngine == null) {
            try {
                Log.i("CameraActivity", "init: ");
                mCameraEngine = new CameraEngine(context, mCameraView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onCreated() {
        Log.i("dengguochuan", "CameraManager------onCreated: ");
        mCameraEngine.startPreview();
    }

    @Override
    public void onChanged() {
        Log.i("dengguochuan", "CameraManager------onChanged: ");
    }

    @Override
    public void onDestroyed() {
        Log.i("dengguochuan", "CameraManager------onDestroyed: ");
        stopRecord();
        if (mCameraEngine != null) {
            mCameraEngine.closeCamera();
        }
        mCameraEngine=null;
    }

    @Override
    public boolean startRecord() {
        return mCameraEngine.startRecord();
    }

    @Override
    public Bitmap stopRecord() {
        return mCameraEngine.stopRecord();
    }

    @Override
    public void switchCamera() {
        mCameraEngine.switchCamera();
    }

    @Override
    public CameraEngine.FlashMode getFlashMode() {
        return null;
    }

    @Override
    public void switchFlashMode() {
        mCameraEngine.switchFlashMode();
    }

    @Override
    public int getMaxZoom() {
        return mCameraEngine.getMaxZoom();
    }

    @Override
    public void setZoom(int zoom) {
        mCameraEngine.setZoom(zoom);
    }

    @Override
    public int getZoom() {
        return mCameraEngine.getZoom();
    }

    public void onFocus(Point point, Camera.AutoFocusCallback autoFocusCallback) {
        mCameraEngine.onFocus(point,autoFocusCallback);
    }
}
