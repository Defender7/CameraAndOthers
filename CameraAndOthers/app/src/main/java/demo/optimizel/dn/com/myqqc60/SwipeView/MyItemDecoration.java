package demo.optimizel.dn.com.myqqc60.SwipeView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dengguochuan on 2017/7/26.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 采用系统的内置的分割线风格
     */
    private static final int[] attrs = new int[]{android.R.attr.listDivider};
    private final TypedArray mTypedArray;
    //水平方式
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    //垂直方式
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private int mOrientation;
    private Context mContext;
    private Drawable mDivider;

    public MyItemDecoration(Context context,int orientation){
        this.mContext=context;
        setOrientation(orientation);
        mTypedArray=mContext.obtainStyledAttributes(attrs);
        mDivider=mTypedArray.getDrawable(0);
        mTypedArray.recycle();


    }
      private void setOrientation(int orientation){
          if(orientation!=VERTICAL_LIST&&orientation!=HORIZONTAL_LIST)
              throw new IllegalArgumentException("错误的设置方向");
          this.mOrientation=orientation;
      }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if(mOrientation==VERTICAL_LIST){
            drawVerticleList(c,parent);
        }else{

        }
    }
    private void drawVerticleList(Canvas c,RecyclerView parent){
        int left=parent.getPaddingLeft();
        int right=parent.getWidth()-parent.getPaddingRight();
        int count=parent.getChildCount();
        for(int i=0;i<count;i++){
            View view=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) view.getLayoutParams();
            int top=view.getBottom()+params.bottomMargin;
            int bottom=top+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
