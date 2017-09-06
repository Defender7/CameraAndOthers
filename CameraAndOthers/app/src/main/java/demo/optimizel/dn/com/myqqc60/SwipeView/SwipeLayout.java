package demo.optimizel.dn.com.myqqc60.SwipeView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by dengguochuan on 2017/7/26.
 */

public class SwipeLayout extends FrameLayout{
    private ViewDragHelper mDragHelper;
    private View mBackView;
    private View mFrontView;
    private int mRange;
    private int mHeight;
    private int mWidth;
    private SwipeListener listener;
    private Status status = Status.Close;;

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper=ViewDragHelper.create(this,1.0f,mViewDragCallback);
    }
    public enum Status{
        Close,Open,Draging
    }
    ViewDragHelper.Callback mViewDragCallback=new ViewDragHelper.Callback(){

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            // left
            if (child == mFrontView) {
                if (left > 0) {  //如果前view的左侧大于0，就返回0，不能右滑
                    return 0;
                } else if (left < -mRange) { //如果前view的左侧坐标小于了后view的宽度，就停止左滑
                    return -mRange;
                }
            } else if (child == mBackView) {

                if (left > mWidth) {   //如果后view的左侧大于了前view的宽度，就停止右滑
                    return mWidth;
                } else if (left < mWidth - mRange) {
                    //如果后view的左侧小于了，停止左滑
                    return mWidth - mRange;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            // 传递事件
            if (changedView == mFrontView) {
                //滑动的是前view，就去监听后view的偏移量
                mBackView.offsetLeftAndRight(dx);
            } else if (changedView == mBackView) {
                //如果是滑动的后view，就去监听前view的偏移量
                mFrontView.offsetLeftAndRight(dx);
            }
            disapatchEvent();
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel == 0 && mFrontView.getLeft() < -mRange / 2.0f) {
                open();
            } else if (xvel < 0) {
                open();
            } else {
                close();
            }
        }
    };
    private void disapatchEvent(){
        if(listener!=null){
            listener.onOpening(this);
        }
        Status pre=status;
        status=updateStatus();
        if(pre!=status&&listener!=null){
            if(status==Status.Close){
                listener.closed(this);
            }else if(status==Status.Open){
                listener.opened(this);
            }else if(status==Status.Draging){
                if(pre==Status.Close){
                    listener.onStartOpen(this);
                }else if(status==Status.Close){
                    listener.onStartClose(this);
                }
            }
        }
    }
    private Status updateStatus(){

        int left=mFrontView.getLeft();
        if(left==0){
           return Status.Close;
        }else if(left==-mRange){
            return Status.Open;
        }
        return Status.Draging;
    }
    public void close() {
        close(true);
    }
    public void open() {
        open(true);
    }
    /**
     * 打开
     *
     * @param isSmooth
     */
    public void open(boolean isSmooth) {
        int finalLeft = -mRange;
        if (isSmooth) {
            //开始动画
            if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {

            layoutContent(true);
        }
    }
    private void close(boolean isSmooth){
        int finalLeft = 0;
        if (isSmooth) {
            //开始动画

            if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutContent(false);
        }

    }
    private void layoutContent(boolean isOpen) {
        // 摆放前View
        Rect frontRect = computeFrontViewRect(isOpen);
        mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
        // 摆放后View
        Rect backRect = computeBackViewViaFront(frontRect);
        mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

        // 调整顺序, 把mFrontView前置
        bringChildToFront(mFrontView);
    }
    private Rect computeBackViewViaFront(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left, 0, left + mRange, 0 + mHeight);
    }

    private Rect computeFrontViewRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mRange;
        }
        return new Rect(left, 0, left + mWidth, 0 + mHeight);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutContent(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 当xml被填充完毕时调用
        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mHeight = mFrontView.getMeasuredHeight();
        mWidth = mFrontView.getMeasuredWidth();

        mRange = mBackView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    public interface SwipeListener{
        void opened(SwipeLayout mSwipeLayout);
        void closed(SwipeLayout mSwipeLayout);
        void onStartOpen(SwipeLayout mSwipeLayout);
        void onStartClose(SwipeLayout mSwipeLayout);
        void onOpening(SwipeLayout mSwipeLayout);
    }
    public void setSwipeLister(SwipeListener swipeLister){
        this.listener=swipeLister;
    }
}
