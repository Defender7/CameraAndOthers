package demo.optimizel.dn.com.myqqc60.TagView.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import demo.optimizel.dn.com.myqqc60.TagView.DIRECTION;

/**
 * Created by dengguochuan on 2017/7/27.
 */

public class RippleView extends View implements ITagView{
    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private int mAlpha;
    private Paint mPaint;
    private AnimatorSet mAnimator;
    private DIRECTION mDirection;
    public RippleView(Context context){
        this(context,null);
    }
    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAlpha(mAlpha);
        canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRipple();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRipple();
    }

    public void setmCenterX(float mCenterX) {
        this.mCenterX = mCenterX;
    }

    public void setmCenterY(float mCenterY) {
        this.mCenterY = mCenterY;
    }
    public  void startRipple(){
        mAnimator.start();
    }
    public void stopRipple(){
        mAnimator.end();
    }
    private void setRippleRadius(float radius){
        mRadius=radius;
        invalidate();
    }
    private void setRippleAlpha(int alpha){
        mAlpha=alpha;
        invalidate();
    }
   public void initAnimitor(float minRadius,float maxRadius,int alpha){
       ObjectAnimator obj1=ObjectAnimator.ofFloat(this,"RippleRadius",minRadius,maxRadius);
       obj1.setRepeatCount(ValueAnimator.INFINITE);
       obj1.setRepeatMode(ValueAnimator.RESTART);
       ObjectAnimator obj2=ObjectAnimator.ofInt(this,"RippleAlpha",alpha,0);
       obj2.setRepeatCount(ValueAnimator.INFINITE);
       obj2.setRepeatMode(ValueAnimator.RESTART);
       mAnimator=new AnimatorSet();
       mAnimator.playTogether(obj1,obj2);
       mAnimator.setDuration(600);
       mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());


   }

    @Override
    public void setDirection(DIRECTION value) {
        mDirection=value;
    }


    @Override
    public DIRECTION getDirection() {
        return mDirection;
    }
}
