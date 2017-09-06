package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.util.BitmapUtils;
import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.view.CameraContainer;
import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.view.VerticalTextView;
import demo.optimizel.dn.com.myqqc60.R;

import static demo.optimizel.dn.com.myqqc60.R.id.switch_camera_iv_potrait;


/**
 * Created by dengguochuan on 2017/8/25.
 */

public class CameraActivity extends Activity implements View.OnClickListener {
    private static final String TAG="CameraActivity";
    private CameraContainer mCameraContainer;
    /**
     * 视频列表按钮
     */
    private LinearLayout mLocalVideoLayout;
    /**
     * 视频列表按钮  右侧
     */
    private LinearLayout mLocalVideoLayout1;
    /**
     * 视频列表按钮左下角小图片
     */
    private ImageView mLocalVideoImg;
    private ImageView mLocalVideoImg1;
    private ImageView mTimeTipImageView;
    private RelativeLayout mTimeLayout;
    private VerticalTextView mTimeTipTextView;
    private LinearLayout lCacel;
    private TextView shotDialog;//视频拍摄按钮，初次提醒
    /**
     * 是否闪光
     */
    private boolean mFlashLight = false;
    /**
     * 上次角度
     */
    private int lastAngle = 0;
    /**
     * 当前屏幕的方向
     */
    private int currentScreenAngle;

    /**
     * 更换本地视频图片
     **/
    private static final int UPDATE_LOCALVIDEOPIC = 5013;
    /**
     * 选择本地视频图片
     **/
    private static final int CHOOSE_VIDEO_REQUEST = 6001;

    /**
     * 选择本地视频图片
     **/
    public static final int CHOOSE_VIDEO_RESULT = 6002;
    /**
     * 相机Layout
     **/
    private RelativeLayout mCameraRecordLayout;
    /**
     * 长视频时间提示margin值
     */
    private final int LONG_TIMER_MARGIN = 45;
    /**
     * 顶部View
     */
    private RelativeLayout mCameraRecordTopControlLayout_portrait;
    /**
     * 长视频录制结束按钮
     */
    private LinearLayout mLongVideoStopRecordingImgll;
    private ImageView mLongVideoStopRecordingImg;
    /**
     * 关闭按钮
     */
    private ImageView mCloseRecordImg_portrait;
    /**
     * 闪光按钮
     */
    private ImageView mSwitchFlashImg_portrait;
    /**
     * 前后摄像头切换按钮
     */
    private ImageView mSwitchCameraImg_portrait;
    /**
     * 视频列表按钮
     */
    /**
     * 录制按钮
     */
    private ImageView mLongVideoRecorderImg;
    private LinearLayout mVideoRecordStart;
    private static final int CODE_FOR_WRITE_PERMISSION = 51;
    private static final int CODE_FOR_CAMERA_PERMISSION=52;

    private CameraManager cameraManagerInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera_activity);
        findViews();
        setOnCLickListener();
        initLocalVideoPic();
        cameraManagerInstance = CameraManager.getInstance();
        cameraManagerInstance.init(this, "/test", mCameraContainer.getmCameraView());
        Log.i(TAG, "onCreate: ");
    }

    private void findViews() {
        mCameraContainer = (CameraContainer) findViewById(R.id.container);
        lCacel = (LinearLayout) findViewById(R.id.ll_cancle_iv_potrait);
        mCameraRecordTopControlLayout_portrait = (RelativeLayout) findViewById(R.id.camera_record_top_control_layout_potrait);
        mLongVideoStopRecordingImgll = (LinearLayout) findViewById(R.id.camera_record_control_stop);//停止按钮
        mLongVideoStopRecordingImg = (ImageView) findViewById(R.id.video_recorder_stop_iv);//停止按钮
        mCloseRecordImg_portrait = (ImageView) findViewById(R.id.close_record_iv_potrait);
        mSwitchFlashImg_portrait = (ImageView) findViewById(R.id.switch_flash_iv_potrait);
        mSwitchCameraImg_portrait = (ImageView) findViewById(switch_camera_iv_potrait);//转换摄像头
        mLocalVideoImg = (ImageView) findViewById(R.id.local_video_iv);
        mLocalVideoImg1 = (ImageView) findViewById(R.id.local_video_iv1);
        mLocalVideoLayout = (LinearLayout) findViewById(R.id.local_video_layout);
        mLocalVideoLayout1 = (LinearLayout) findViewById(R.id.local_video_layout1);
        mLongVideoRecorderImg = (ImageView) findViewById(R.id.video_recorder_iv);//开始录制按钮img
        mVideoRecordStart = (LinearLayout) findViewById(R.id.camera_record_control_linearlayout);//开始录制按钮
        shotDialog = (TextView) findViewById(R.id.shot_dialog_tv);
        if (!getSharedPreferences("remind_camera_video_preference", 0)
                .getBoolean("remind_key", false)) {
            shotDialog.setVisibility(View.VISIBLE);
        }
    }

    private void setOnCLickListener() {
        mLongVideoRecorderImg.setOnClickListener(this);
        mLongVideoStopRecordingImg.setOnClickListener(this);
        mSwitchCameraImg_portrait.setOnClickListener(this);
        mSwitchFlashImg_portrait.setOnClickListener(this);
        lCacel.setOnClickListener(this);
    }


    /**
     * 获取本地视频缩略图
     */
    private void initLocalVideoPic() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            Activity activty = this;
            ActivityCompat.requestPermissions(activty, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    CODE_FOR_WRITE_PERMISSION);
            return;
        }
        new GetLocalVideoPicAsyncTask().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_recorder_iv:
                startRecord();
                break;
            case R.id.video_recorder_stop_iv:
                stopRecord();
                break;
            case R.id.switch_camera_iv_potrait:
                switchCamera();
                break;
            case R.id.switch_flash_iv_potrait:
                switchFlash();
                break;
            case R.id.ll_cancle_iv_potrait:
                close();
                break;
        }
    }
    private void startRecord(){
        mLongVideoRecorderImg.setVisibility(View.GONE);
        if(cameraManagerInstance.startRecord()){
            getSharedPreferences("remind_camera_video_preference", 0)
                    .edit().putBoolean("remind_key", true).commit();
            mLongVideoStopRecordingImgll.setVisibility(View.VISIBLE);
            mLongVideoStopRecordingImg.setVisibility(View.VISIBLE);
        }
    }
    private void stopRecord(){
        Log.i("deng", "stopRecord: ============");
        mLongVideoRecorderImg.setVisibility(View.VISIBLE);
        mLongVideoStopRecordingImg.setVisibility(View.GONE);
        cameraManagerInstance.stopRecord();
    }
    private void switchCamera(){
        cameraManagerInstance.switchCamera();
    }
    private void switchFlash(){
        cameraManagerInstance.switchFlashMode();
    }
    private void close(){
        finish();
    }
    private class GetLocalVideoPicAsyncTask extends AsyncTask<Void, Void, Map<String, Bitmap>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Map<String, Bitmap> doInBackground(Void... param) {
            Map<String, Bitmap> map = new HashMap<String, Bitmap>();
            String[] mediaColumns = {
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.DURATION};
//	        String selection = MediaStore.Video.Media.MIME_TYPE +"=?";
//	        String[] selectionArgs = new String[]{"video/mp4"};

            String[] selectionArgs = new String[]{"video/mp4", "video/3gp", "video/3gpp"};
            String selection = "";
            for (int i = 0; i < selectionArgs.length; i++) {
                if (i > 0) {
                    selection += " OR ";
                }
                selection += MediaStore.Video.Media.MIME_TYPE + "=?";
            }
            String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " DESC";

            ContentResolver cr1 = getContentResolver();
            Cursor cursor = cr1.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    mediaColumns, selection, selectionArgs, sortOrder);

//	        Cursor cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//	                mediaColumns, selection, selectionArgs, sortOrder);
            if (cursor.moveToFirst()) {
                do {
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    //过滤1秒以下的视频
                    if (duration >= 1000) {
                        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        File file = new File(filePath);
                        if (file.exists()) {
                            Bitmap bm = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                            if (bm != null) {
                                int size = bm.getWidth() < bm.getHeight() ? bm.getWidth() : bm.getHeight();
                                bm = BitmapUtils.cropBitmapBySize(bm, size, size);
                                if (!map.containsKey("long")) {
                                    map.put("long", bm);
                                }
                            }
                        }
                    }

                    //过滤3秒以下的视频
                    if (duration >= 3000) {
                        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        File file = new File(filePath);
                        if (file.exists()) {
                            Bitmap bm = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                            if (bm != null) {
                                int size = bm.getWidth() < bm.getHeight() ? bm.getWidth() : bm.getHeight();
                                bm = BitmapUtils.cropBitmapBySize(bm, size, size);
                                if (!map.containsKey("short")) {
                                    map.put("short", bm);

                                    try {
                                        if (null != cursor) {
                                            cursor.close();
                                        }
                                    } catch (Exception e) {
//                                        DebugLog.log(TAG, "error:" + e);
                                    }
                                    return map;
                                }
                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
            try {
                if (null != cursor) {
                    cursor.close();
                }
            } catch (Exception e) {

            }

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Bitmap> result) {
            Message msg = new Message();
            Bundle b = new Bundle();
            b.putParcelable("UPDATE_LOCALVIDEOPIC", (Parcelable) result.get("long"));
            msg.what = UPDATE_LOCALVIDEOPIC;
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    private final Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case UPDATE_LOCALVIDEOPIC:
                    Bitmap bitmap = msg.getData().getParcelable("UPDATE_LOCALVIDEOPIC");
                    if (bitmap != null) {
                        mLocalVideoLayout.setBackground(new BitmapDrawable(bitmap));
                        mLocalVideoLayout1.setBackground(new BitmapDrawable(bitmap));
                        mLocalVideoImg.setVisibility(View.VISIBLE);
                        mLocalVideoImg1.setVisibility(View.VISIBLE);
                    }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_FOR_WRITE_PERMISSION) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
                new GetLocalVideoPicAsyncTask().execute();
            } else {
                //用户不同意，自行处理即可
                finish();
            }
        }

    }

}
