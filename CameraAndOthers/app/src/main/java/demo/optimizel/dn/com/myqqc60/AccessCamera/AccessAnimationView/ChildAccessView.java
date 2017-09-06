package demo.optimizel.dn.com.myqqc60.AccessCamera.AccessAnimationView;

import android.graphics.Path;

/**
 * Created by dengguochuan on 2017/8/3.
 */

public class ChildAccessView  {

    private int processValue;//动画过程中的变化，确定运动的位置
    private  int type;//左边1  Left，右边是0 Right
    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private Path mOpenPath;
    private Path mClosePath;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public Path getmOpenPath() {
        return mOpenPath;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setmOpenPath(Path mOpenPath) {
        this.mOpenPath = mOpenPath;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public Path getmClosePath() {
        return mClosePath;
    }

    public void setmClosePath(Path mClosePath) {
        this.mClosePath = mClosePath;
    }

}
