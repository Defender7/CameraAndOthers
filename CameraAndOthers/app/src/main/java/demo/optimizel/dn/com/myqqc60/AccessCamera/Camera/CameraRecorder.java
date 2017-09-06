package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.util.FileOperateUtil;

import static demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.util.FileOperateUtil.TAG;

/**
 * Created by dengguochuan on 2017/8/29.
 */

public class CameraRecorder {
    private Camera mCamera;
    private Context mContext;
    private MediaRecorder mMediaRecorder;
    private String mRecordPath;

    public CameraRecorder(Camera camera, Context context) {
        this.mCamera = camera;
        this.mContext = context;
    }

    public boolean startRecord() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        } else {
            mMediaRecorder.reset();
        }
        try {
            mMediaRecorder.setCamera(mCamera);
            mCamera.unlock();
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOrientationHint(90);
            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));//1280 720
            String path = FileOperateUtil.getFolderPath(mContext, FileOperateUtil.TYPE_VIDEO, null);
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String name = FileOperateUtil.createFileNmae(null, tm.getDeviceId());
            mRecordPath = path + name;
            File mRecAudioFile = new File(mRecordPath);
            mMediaRecorder.setOutputFile(mRecAudioFile
                    .getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Bitmap stopRecord() {
        exportToGallery(mRecordPath);
        Bitmap bitmap = null;
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            try {
                bitmap = saveThumbnail();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }
    private Uri exportToGallery(String filename) {
        // Save the name and description of a video in a ContentValues map.
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filename);
        // Add a new record (identified by uri)
        final Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://"+ filename)));
        return uri;
    }
    private Bitmap saveThumbnail() throws FileNotFoundException, IOException {
        if (mRecordPath != null) {

//            			Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(mRecordPath, MediaStore.Video.Thumbnails.MINI_KIND);
            Bitmap bitmap = getVideoThumbnail(mRecordPath);

            if (bitmap != null) {
                String mThumbnailFolder = FileOperateUtil.getFolderPath(mContext, FileOperateUtil.TYPE_THUMBNAIL, null);
                File folder = new File(mThumbnailFolder);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file = new File(mRecordPath);
                file = new File(folder + File.separator + file.getName().replace("3gp", "jpg"));
                //��ͼƬСͼ
                BufferedOutputStream bufferos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferos);
                bufferos.flush();
                bufferos.close();
                return bitmap;
            }
            mRecordPath = null;
        }
        return null;
    }

    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        if (bitmap == null)
            return null;
        // Scale down the bitmap if it's too large.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.i(TAG, "bitmap:" + width + " " + height);
//        int pWidth=getWidth();
//        int pHeight=getHeight();
        int pWidth = width;
        int pHeight = height;
        Log.i(TAG, "parent:" + pWidth + " " + pHeight);
        //��ȡ��߸����������Ƚ�С�ı������Դ�Ϊ��׼��������
        float scale = Math.min((float) width / pWidth, (float) height / pHeight);
        Log.i(TAG, scale + "");
        int w = Math.round(scale * pWidth);
        int h = Math.round(scale * pHeight);
        Log.i(TAG, "parent:" + w + " " + h);
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        return bitmap;
    }
}
