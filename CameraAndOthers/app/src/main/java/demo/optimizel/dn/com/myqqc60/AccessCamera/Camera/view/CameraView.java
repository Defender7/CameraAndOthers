package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by dengguochuan on 2017/8/24.
 */

public class CameraView extends SurfaceView {


    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(surfaceCallback);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback()

    {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            surfaceListener.onCreated();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            surfaceListener.onChanged();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            getHolder().removeCallback(this);
            surfaceListener.onDestroyed();
        }
    };

    public void setSurfaceListener(ISurfaceListener surfaceListener) {
        this.surfaceListener = surfaceListener;
    }

    ISurfaceListener surfaceListener;

    public interface ISurfaceListener {
        void onCreated();

        void onChanged();

        void onDestroyed();
    }

}
