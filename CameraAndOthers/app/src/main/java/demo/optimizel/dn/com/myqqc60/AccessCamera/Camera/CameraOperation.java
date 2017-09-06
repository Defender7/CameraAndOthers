package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera;

import android.graphics.Bitmap;

import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.CameraEngine.FlashMode;




public interface CameraOperation {

	 boolean startRecord();


	 Bitmap stopRecord();

	 void switchCamera();

	 FlashMode getFlashMode();

	 void switchFlashMode();

//	 void takePicture(PictureCallback callback, TakePictureListener listener);

	 int getMaxZoom();

	 void setZoom(int zoom);

	 int getZoom();
}
