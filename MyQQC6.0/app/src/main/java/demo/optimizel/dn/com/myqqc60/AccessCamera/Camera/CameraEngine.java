package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengguochuan on 2017/8/25.
 */

public class CameraEngine implements CameraOperation {
    private Context mContext;
    private SurfaceView mCameraView;
    private Camera mCamera;

    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;//默认后置
    private Camera.Parameters mParameters;
    private boolean isFront = false;//是否前置摄像头
    private boolean mPreviewing=false;
    private FlashMode flashMode=FlashMode.OFF;
    private CameraRecorder mMediaRecorder;


    protected CameraEngine(Context context, SurfaceView view) throws IOException {
        this.mContext = context;
        this.mCameraView = view;
        if (mCamera == null) {
//            mCamera = Camera.open();
        }
        isFront = false;
    }

    @Override
    public boolean startRecord() {
        if (mCamera == null)
            openCamera(cameraId);
        if (mCamera == null) {
            return false;
        }
        if (mMediaRecorder == null) {
            mMediaRecorder = new CameraRecorder(mCamera, mContext);
        }
        return mMediaRecorder.startRecord();
    }

    @Override
    public Bitmap stopRecord() {
        if(mMediaRecorder!=null){
            Bitmap bitmap = mMediaRecorder.stopRecord();
            return bitmap;
        }
       return null;
    }

    @Override
    public void switchCamera() {
        closeCamera();
        cameraId=cameraId== Camera.CameraInfo.CAMERA_FACING_BACK?Camera.CameraInfo.CAMERA_FACING_FRONT:Camera.CameraInfo.CAMERA_FACING_BACK;
        startPreview();
    }

    @Override
    public FlashMode getFlashMode() {
        return flashMode;
    }

    @Override
    public void switchFlashMode() {
        flashMode=flashMode==FlashMode.OFF?FlashMode.ON:FlashMode.OFF;
        Camera.Parameters parameters=mCamera.getParameters();
        switch (flashMode){
            case OFF:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case ON:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
        }
        mCamera.setParameters(parameters);
    }

    @Override
    public int getMaxZoom() {
        Log.i("deng", "getMaxZoom: "+mCamera.getParameters().getMaxZoom());
        return mCamera.getParameters().getMaxZoom();
    }

    @Override
    public void setZoom(int zoom) {
        if(zoom<0){
            zoom=0;
        }
        if(zoom>getMaxZoom()){
            zoom=getMaxZoom();
        }
        Log.i("deng", "setZoom: "+zoom);
        try{
            Camera.Parameters parameters=mCamera.getParameters();
            parameters.setZoom(zoom);
            mCamera.setParameters(parameters);
        }catch (RuntimeException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getZoom() {
        return mCamera.getParameters().getZoom();
    }

    public void onFocus(Point point, Camera.AutoFocusCallback autoFocusCallback) {
        Camera.Parameters parameters=mCamera.getParameters();
        if (parameters.getMaxNumFocusAreas()<=0) {
            mCamera.autoFocus(autoFocusCallback);
            return;
        }
        mCamera.cancelAutoFocus();
        List<Camera.Area> areas=new ArrayList<Camera.Area>();
        List<Camera.Area> areasMetrix=new ArrayList<Camera.Area>();
        Camera.Size previewSize = parameters.getPreviewSize();
        Rect focusRect = calculateTapArea(point.x, point.y, 1.0f, previewSize);
        Rect metrixRect = calculateTapArea(point.x, point.y, 1.5f, previewSize);

        areas.add(new Camera.Area(focusRect, 1000));
        areasMetrix.add(new Camera.Area(metrixRect,1000));
        parameters.setMeteringAreas(areasMetrix);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setFocusAreas(areas);
        try {
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        mCamera.autoFocus(autoFocusCallback);
    }
    private  Rect calculateTapArea(float x, float y, float coefficient, Camera.Size previewSize) {
        Display display=((WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenHeight=display.getHeight();
        int screenWidth=display.getWidth();
        float focusAreaSize = 200;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerY =0;
        int  centerX=0;
        centerY = (int) (-x / screenWidth*2000 + 1000);
        centerX= (int) (y / screenHeight*2000 - 1000);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }
    public static interface TakePictureListener {

        public void onTakePictureEnd(Bitmap bm);


        public void onAnimtionEnd(Bitmap bm, boolean isVideo);
    }

    public enum FlashMode {
        ON,
        OFF,
    }

    private void openCamera(int cameraId) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (isFront) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        mCamera = Camera.open(i);
                    } catch (Exception e) {
                        mCamera = null;
                        throw new RuntimeException("openCamera 打开前置摄像头失败");
                    }
                }
            }
        } else {
            try {
                if (mCamera == null) {
                    mCamera = Camera.open(cameraId);
                }
            } catch (Exception e) {
                mCamera = null;
                throw new RuntimeException("打开摄像头失败");
            }

        }
    }

    protected void startPreview() {
        openCamera(cameraId);
        stopPreview();
        setCameraParameters();
        mCamera.startPreview();
        mPreviewing = true;
    }

    /**
     * 停止摄像头预览
     */
    public void stopPreview() {
        if (mPreviewing == true) {
            try {
//                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            } catch (Exception e) {

            }
            mPreviewing = false;
        }
    }
    /**
     * 关闭摄像头
     */
    public void closeCamera() {
        if (mCamera == null) {
            return;
        }
        try {
            if (mCamera != null) {
//                mCamera.setPreviewCallback(null);
                stopPreview();
                mCamera.release();
            }
            mCamera = null;
            mParameters = null;
            mPreviewing = false;

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    private void setCameraParameters() {
        try {
            mParameters = mCamera.getParameters();
            Display d = ((Activity) mContext).getWindowManager()
                    .getDefaultDisplay();
            setCameraDisplayOrientation(d, cameraId, mCamera);
            mParameters.setPreviewSize(1920, 1080);//一般都支持  暂且简单写这个  需要获取支持列表选取
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            setVideoStabilization();
            mCamera.setPreviewDisplay(mCameraView.getHolder());
            mCamera.setParameters(mParameters);
        } catch (RuntimeException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置预览角度
     */
    private void setCameraDisplayOrientation(Display display, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        if (camera != null) {
            camera.setDisplayOrientation(result);
        }
    }

    /**
     * 防震设置
     */
    @SuppressLint("NewApi")
    public void setVideoStabilization() {
        if (mParameters == null)
            return;
        if (Build.VERSION.SDK_INT >= 15) {
            if (mParameters.isVideoStabilizationSupported()) {
                mParameters.setVideoStabilization(true);
                return;
            }
            return;
        }
        String str = mParameters.get("video-stabilization-supported");
        if (!"true".equals(str))
            return;
        mParameters.set("video-stabilization", "true");
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

}
