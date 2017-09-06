package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.view;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.CameraManager;
import demo.optimizel.dn.com.myqqc60.R;

/**
 * Created by dengguochuan on 2017/8/30.
 */

public class CameraContainer extends RelativeLayout {



    private CameraView mCameraView;
    private Context mContext;

    private TempImageView mTempImageView;
    private FocusImageView mFocusImageView;
    private ImageView mWaterMarkImageView;
//    private SeekBar mZoomSeekBar;

    private int touchMode=1;
    private final int Single=1;
    private final int TWO=2;
    private float startTouchDistance=0;
    private float moveTouchDistance=0;
    private int scale=0;
    private int currenZoom=0;

    public  CameraContainer(Context context){
        this(context,null);
    }
    public CameraContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.cameracontainer, this);
        mCameraView=(CameraView) findViewById(R.id.cameraView);
        mTempImageView=(TempImageView) findViewById(R.id.tempImageView);
        mFocusImageView=(FocusImageView) findViewById(R.id.focusImageView);
        mWaterMarkImageView=(ImageView) findViewById(R.id.waterMark);
//        mZoomSeekBar=(SeekBar) findViewById(R.id.zoomSeekBar);
        setOnTouchListener(new TouchListener());
    }

    public CameraView getmCameraView() {
        return mCameraView;
    }

    public void setmCameraView(CameraView mCameraView) {
        this.mCameraView = mCameraView;
    }
    public TempImageView getmTempImageView() {
        return mTempImageView;
    }

    public void setmTempImageView(TempImageView mTempImageView) {
        this.mTempImageView = mTempImageView;
    }

    public FocusImageView getmFocusImageView() {
        return mFocusImageView;
    }

    public void setmFocusImageView(FocusImageView mFocusImageView) {
        this.mFocusImageView = mFocusImageView;
    }

    public ImageView getmWaterMarkImageView() {
        return mWaterMarkImageView;
    }

    public void setmWaterMarkImageView(ImageView mWaterMarkImageView) {
        this.mWaterMarkImageView = mWaterMarkImageView;
    }

//    public SeekBar getmZoomSeekBar() {
//        return mZoomSeekBar;
//    }
//
//    public void setmZoomSeekBar(SeekBar mZoomSeekBar) {
//        this.mZoomSeekBar = mZoomSeekBar;
//    }

    private final class TouchListener implements OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction() &MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    touchMode=Single;
                    Log.i("deng", "ACTION_DOWN: "+touchMode);

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchMode=TWO;
                    Log.i("deng", "ACTION_POINTER_DOWN: "+touchMode);
//                    mZoomSeekBar.setVisibility(View.VISIBLE);
                    startTouchDistance=distance(motionEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(touchMode==TWO){
                        Log.i("deng", "onTouch: "+motionEvent.getPointerCount());
                        if(motionEvent.getPointerCount()<2) return true;
                        moveTouchDistance=distance(motionEvent);
                        scale=(int)((moveTouchDistance-startTouchDistance)/5f);
                        Log.i("deng", "onTouch: "+motionEvent.getPointerCount());
                        if(scale>=1||scale<=-1){
                            currenZoom= CameraManager.getInstance().getZoom()+scale;
                            currenZoom=currenZoom>CameraManager.getInstance().getMaxZoom()?CameraManager.getInstance().getMaxZoom():currenZoom;
                            CameraManager.getInstance().setZoom(currenZoom);
                            startTouchDistance=moveTouchDistance;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(touchMode!=TWO){
                        Point point=new Point((int)motionEvent.getX(),(int)motionEvent.getY());
                        CameraManager.getInstance().onFocus(point, new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean b, Camera camera) {
                                if(b){//手动对焦成功
                                    mFocusImageView.onFocusSuccess();
                                }else{
                                    mFocusImageView.onFocusFailed();
                                }
                            }

                        });
                        mFocusImageView.startFocus(point);//对焦动画
                    }
                    break;
            }
            return true;
        }
        private float distance(MotionEvent event){
            try {
                float dx = event.getX(1) - event.getX(0);
                float dy = event.getY(1) - event.getY(0);
                return (float) Math.sqrt(dx * dx + dy * dy);
            }catch (RuntimeException e){
                e.printStackTrace();
            }
            return 0;
        }
    }


}
