package demo.optimizel.dn.com.myqqc60.SwipeView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dengguochuan on 2017/7/25.
 */

public class MyRecycleView extends RecyclerView {
    private float itemX;
    private float itemY;

    public MyRecycleView(Context context) {
        super(context);
    }
    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        float preX=itemX;
        float preY=itemY;
        itemX=e.getRawX();
        itemY=e.getRawY();
        if(Math.abs(itemX-preX)>Math.abs(itemY-preY)){
            return false;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}
