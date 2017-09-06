package demo.optimizel.dn.com.myqqc60.TagView.views;

import demo.optimizel.dn.com.myqqc60.TagView.DIRECTION;

/**
 * Created by dengguochuan on 2017/7/28.
 */

public interface ITagView {
    //设置Tag的方向
    void setDirection(DIRECTION direction);
    DIRECTION getDirection();
    int getMeasuredWidth();

    int getMeasuredHeight();

    int getTop();

    int getLeft();

    int getRight();

    int getBottom();
    void layout(int left, int top, int right, int bottom);
}
