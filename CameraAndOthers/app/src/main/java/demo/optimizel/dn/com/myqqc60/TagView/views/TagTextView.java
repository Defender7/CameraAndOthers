package demo.optimizel.dn.com.myqqc60.TagView.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import demo.optimizel.dn.com.myqqc60.TagView.DIRECTION;

/**
 * Created by dengguochuan on 2017/7/31.
 */

public class TagTextView extends TextView implements ITagView {
    DIRECTION mDerection;
    public TagTextView(Context context) {
        this(context,null);
    }
    public TagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(Color.WHITE);
        setPadding(10,10,10,10);
        setShadowLayer(10,0,0,Color.BLACK);
    }



    @Override
    public void setDirection(DIRECTION direction) {
        mDerection=direction;
    }

    @Override
    public DIRECTION getDirection() {
        return mDerection;
    }
}
