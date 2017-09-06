package demo.optimizel.dn.com.myqqc60.TagView.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import demo.optimizel.dn.com.myqqc60.TagView.DIRECTION;
import demo.optimizel.dn.com.myqqc60.TagView.data.TagGroupModel;

/**
 * Created by dengguochuan on 2017/7/28.
 */

public class TagViewGroup extends ViewGroup{
    private static Animator mHideAnimator;
    private static Animator mShowAnimator;
    private static boolean mIsShow=true;

    private int mRadius;//外圆半径

    public int getmInnerRadius() {
        return mInnerRadius;
    }

    private int mInnerRadius;//内圆半径
    private int lift_line;
    private int normal_line;
    private float mTagAlpha;//标签组各个小标签的透明度
    private float mLinesRatio=1;
    private RippleView mRippleView;

    private Paint mPaint;
    private Path mPath;
    private Path mDstPath;
    private PathMeasure mPathMeasure;

    private GestureDetectorCompat mDetector;

    private int mCenterX;
    private int mCenterY;
    private Rect mRect;


    public void setmCenterX(int mCenterX) {
        this.mCenterX = mCenterX;
    }

    public void setmCenterY(int mCenterY) {
        this.mCenterY = mCenterY;
    }

    public TagViewGroup(Context context){
        this(context,null);
    }
    public TagViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRadius=30;
        mInnerRadius=15;
        lift_line=2*mRadius;
        normal_line=mRadius*3;
//
        mPaint=new Paint();
        mPath=new Path();
        mDstPath=new Path();
        mPathMeasure=new PathMeasure();
        mDetector= new GestureDetectorCompat(context,new TagGestureListener());
        mRect=new Rect();
    }
    private class TagGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            if(mRect.contains((int)e.getX(),(int)e.getY())){
                return true;
            }
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mCenterY=mCenterY-(int)distanceY;
            mCenterX=mCenterX-(int)distanceX;
            requestLayout();

            return true;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        mRect=new Rect(mCenterX-5*mRadius,mCenterY-5*mRadius,mCenterX+5*mRadius,mCenterY+5*mRadius);
        if(mRippleView!=null){
//            Log.i("dengguochuan", "onMeasure: "+mCenterY+" mCenterX  "+mCenterX);
            if (mRippleView != null) {
                mRippleView.setmCenterX(mCenterX);
                mRippleView.setmCenterY(mCenterY);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
       int left=0,top=0;
        ITagView child;
        int count= getChildCount();
        for(int i=0;i<count;i++){
            child= (ITagView) getChildAt(i);
//            Log.i("mmm", "onLayout: "+child.getMeasuredWidth()+" " +child.getMeasuredHeight());
            switch(child.getDirection()){

                case CENTER:
                    left=0;top=0;
                    break;
                case RIGHT_TOP:
                    left=mCenterX;top=mCenterY-normal_line-child.getMeasuredHeight();
                    break;
                case RIGHT_TOP_LIFT:
                    left=mCenterX+lift_line;top=mCenterY-lift_line-child.getMeasuredHeight();
                    break;
                case RIGHT_CENTRN:
                    left=mCenterX+lift_line;top=mCenterY-child.getMeasuredHeight();
                    break;
                case RIGHT_BOTTOM_LIFT:
                    left=mCenterX+lift_line;top=mCenterY+lift_line-child.getMeasuredHeight();
                    break;
                case RIGHT_BOTTOM:
                    left=mCenterX;top=mCenterY+normal_line-child.getMeasuredHeight();
                    break;
                case LEFT_BOTTOM:
                    left=mCenterX-lift_line-child.getMeasuredWidth();top=mCenterY+normal_line-child.getMeasuredHeight();
                    break;
                case LEFT_BOTTOM_LIFT:
                    left=mCenterX-lift_line-child.getMeasuredWidth();top=mCenterY+lift_line-child.getMeasuredHeight();
                    break;
                case LEFT_CENTER:
                    left=mCenterX-normal_line-child.getMeasuredWidth();top=mCenterY-child.getMeasuredHeight();
                    break;
                case LEFT_TOP_LEFT:
                    left=mCenterX-lift_line-child.getMeasuredWidth();top=mCenterY-lift_line-child.getMeasuredHeight();
                    break;
                case LEFT_TOP:
                    left=mCenterX-lift_line-child.getMeasuredWidth();top=mCenterY-normal_line-child.getMeasuredHeight();
                    break;
                default:
                    left=mCenterX;top=mCenterY;
                    break;


            }
//            Log.i("dengguochuan", "onLayout: "+left+"---top-- "+top);
            child.layout(left,top,left+child.getMeasuredWidth(),top+child.getMeasuredHeight());
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawLines(canvas);
        mPaint.setColor(Color.parseColor("#30000000"));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,mInnerRadius,mPaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    private void drawLines(Canvas canvas){
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        int count=getChildCount();

        for(int i=0;i<count;i++){
            ITagView child= (ITagView) getChildAt(i);
            mPath.reset();
            mPath.moveTo(mCenterX,mCenterY);
            mDstPath.reset();
//            Log.i("dengguochuan", "drawLines: "+child.getDirection());
            switch (child.getDirection()){

                case RIGHT_TOP:
                case RIGHT_TOP_LIFT:
                case RIGHT_BOTTOM_LIFT:
                case RIGHT_BOTTOM:
                    mPath.lineTo(child.getLeft(),child.getBottom());
//                    Log.i("dengguochuan", "RIGHT_BOTTOM: "+child.getLeft()+" RIGHT_BOTTOM "+child.getBottom());
                case RIGHT_CENTRN:
                    mPath.lineTo(child.getRight(),child.getBottom());
//                    Log.i("dengguochuan", "RIGHT_CENTRN: "+child.getRight()+" RIGHT_CENTRN "+child.getBottom());

                    break;
                case LEFT_BOTTOM:
                case LEFT_BOTTOM_LIFT:

                case LEFT_TOP_LEFT:
                case LEFT_TOP:
                    mPath.lineTo(child.getRight(),child.getBottom());

                case LEFT_CENTER:
                    mPath.lineTo(child.getLeft(),child.getBottom());
                    break;

            }
            mPathMeasure.setPath(mPath, false);
            mPathMeasure.getSegment(0, mPathMeasure.getLength() * mLinesRatio, mDstPath, true);
            canvas.drawPath(mDstPath, mPaint);
        }
    }
    public void setHideAinimator(Animator animator){
        mHideAnimator=animator;
        mHideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsShow=false;
                setVisibility(INVISIBLE);
            }
        });
    }
    public void setShowAinimator(Animator animator){
        mShowAnimator=animator;
        mShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsShow=true;
                }
        });
    }
    public  void excuteAnimator(){
        if(mIsShow){
            if(mHideAnimator!=null){
                Log.i("dengguochuan", "excuteAnimator: mHideAnimator");

                mHideAnimator.start();
            }

        }else{

            if(mShowAnimator!=null){
                Log.i("dengguochuan", "excuteAnimator: mShowAnimator");
                setVisibility(View.VISIBLE);
                mShowAnimator.start();

            }
        }
    }
    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }
//    public void setmRadius(int value){
//        mRadius=value;
//        mInnerRadius=mRadius-15;
//        lift_line=2*mRadius;
//        normal_line=mRadius*3;
//    }
    public void setAnimatorRadius(int radius){
        mRadius=radius;
        invalidate();
    }
    public void setAnimatorInnerRadius(int radius){
        mInnerRadius=radius;
        invalidate();
    }
    public void addRippleView(){
        mRippleView=new RippleView(this.getContext());
        mRippleView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mRippleView.initAnimitor(mInnerRadius,2*mRadius,100);

        mRippleView.setDirection(DIRECTION.CENTER);
        addView(mRippleView);
    }
    public void  addTextView(TagGroupModel.Tag tag){
       TagTextView tv=new TagTextView(this.getContext());
       tv.setDirection(DIRECTION.valueOf(tag.getDirection()));
       tv.setText(tag.getName());
        addView(tv);
    }
    private void setTagAlpha(float value){
        mTagAlpha=value;
        drawTagAlpha(mTagAlpha);
    }
    private void drawTagAlpha(float mTagAlpha){
        int count=getChildCount();
        for(int i=0;i<count;i++){
            getChildAt(i).setAlpha(mTagAlpha);
        }
    }
    public float getmTagAlpha() {
        return mTagAlpha;
    }
    public float getlinesRatio(){
        return mLinesRatio;
    }
    public  void setLinesRatio(float linesRatio) {
        mLinesRatio = linesRatio;
        invalidate();
    }

    public static final Property<TagViewGroup,Float> TAG_ALPHA=new Property<TagViewGroup, Float>(Float.class,"tagAlpha") {

        @Override
        public Float get(TagViewGroup tagViewGroup) {
           return  tagViewGroup.getmTagAlpha();
        }

        @Override
        public void set(TagViewGroup object, Float value) {
            object.setTagAlpha(value);
        }
    };
    public static final Property<TagViewGroup,Float>LINES_RATIO=new Property<TagViewGroup, Float>(Float.class,"linesRatio") {
        @Override
        public Float get(TagViewGroup tagViewGroup) {
            return tagViewGroup.getlinesRatio();
        }

        @Override
        public void set(TagViewGroup object, Float value) {
            object.setLinesRatio(value);
        }
    };
    public static final Property<TagViewGroup,Integer> TAG_RADIUS=new Property<TagViewGroup, Integer>(Integer.class,"animatorRadius") {
        @Override
        public Integer get(TagViewGroup tagViewGroup) {
            return tagViewGroup.getmRadius();
        }

        @Override
        public void set(TagViewGroup object, Integer value) {
           object.setAnimatorRadius(value);
        }
    };
    public static final Property<TagViewGroup,Integer> TAG_INNER_RADIUS=new Property<TagViewGroup, Integer>(Integer.class,"animatorInnerRadius") {
        @Override
        public Integer get(TagViewGroup tagViewGroup) {
            return tagViewGroup.getmInnerRadius();
        }

        @Override
        public void set(TagViewGroup object, Integer value) {
            object.setAnimatorInnerRadius(value);
        }
    };
}
