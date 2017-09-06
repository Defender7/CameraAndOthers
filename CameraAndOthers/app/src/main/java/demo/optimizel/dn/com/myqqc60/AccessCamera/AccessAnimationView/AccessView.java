package demo.optimizel.dn.com.myqqc60.AccessCamera.AccessAnimationView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengguochuan on 2017/8/3.
 */

public class AccessView extends SurfaceView implements ValueAnimator.AnimatorUpdateListener {
    private Paint mPaint;

    //背景图的半径
    private int bgRadius;
    //屏幕宽高度
    private int width;
    private int height;
    //子View
    private List<View>mChildViewList;
    private Context mContext;
    //子View的宽高
    private int viewWidth=0;
    private int viewHeight=0;
    //默认背景色
    private int bgColor = Color.argb(100, 244, 244, 244);

    private List<ChildAccessView>childes=new ArrayList<>();

    //画背景时间
    private int  bgTime=400;
    public AccessView(Context context){
        this(context ,null);
    }
    public AccessView(Context context, List<View>child){
        super(context);
        this.mContext=context;
        this.mChildViewList=child;
    }
    public void create(){
        if(mChildViewList!=null){
         setChild();
        }
        init();
    }
    private void setChild(){
        View view=mChildViewList.get(0);
        view.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
        this.viewHeight=view.getMeasuredHeight();
        this.viewWidth=view.getMeasuredWidth();
    }
    private void init(){
        Display display=((Activity)mContext).getWindowManager().getDefaultDisplay();
        width=display.getWidth();
        height=display.getHeight()-getStatusHeight((Activity)mContext);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        buildChildren();
    }
    private void buildChildren(){

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        invalidate();
    }
    private void drawBg(Canvas canvas){

        mPaint.setColor(bgColor);
//        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        Log.i("bgRadius", "drawBg: "+bgRadius);
        canvas.drawCircle(width/2,height*5/3,bgRadius,mPaint);
    }
    ObjectAnimator objectAnimatorBg;
    public void startAnimation(){
        objectAnimatorBg=ObjectAnimator.ofInt(this,"bgRadius",0,height);
        objectAnimatorBg.setDuration(getBgTime());
        objectAnimatorBg.setInterpolator(new AccelerateInterpolator());
        objectAnimatorBg.addUpdateListener(this);
        objectAnimatorBg.start();
    }

    /**
     * 状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        Rect frame = new Rect();

        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int statusHeight = frame.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    public int getBgRadius() {
        return bgRadius;
    }

    public void setBgRadius(int bgRadius) {
        this.bgRadius = bgRadius;
    }
    public int getBgTime() {
        return bgTime;
    }

    public void setBgTime(int bgTime) {
        this.bgTime = bgTime;
    }

}
