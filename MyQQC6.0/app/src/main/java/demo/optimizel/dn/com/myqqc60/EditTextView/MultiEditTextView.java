package demo.optimizel.dn.com.myqqc60.EditTextView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

import demo.optimizel.dn.com.myqqc60.EditTextView.MyEditText.TextChangListener;

/**
 * Created by dengguochuan on 2017/8/15.
 */

public class MultiEditTextView extends ViewGroup {
    private StringBuilder myInput;
    private int size=3;//有几个输入编辑框
    private Context mContext;
    public MultiEditTextView(Context context) {
        this(context,null);
    }
    public MultiEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initEdit(size);//添加进来size个编辑框
        myInput=new StringBuilder(size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for(int i=0;i<size;i++) {
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        this.layout(left,top,right,bottom);
        for(int i=0;i<size;i++){
            EditText child= (EditText) getChildAt(i);
//            Log.i("deng", "onLayout: "+(left)+"===top==="+top+"==child.getMeasuredWidth()  "+child.getMeasuredWidth());
            int i1=left+(child.getMeasuredWidth()+20)*i;
            child.layout(i1,top+10,child.getMeasuredWidth()+i1,bottom-10);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<size;i++){
            EditText child= (EditText) getChildAt(i);
            child.draw(canvas);
        }
    }

    private void initEdit(final int size){
        for(int i=0;i<size;i++){
            final MyEditText view=new MyEditText(mContext);
            view.setWidth(100);
            view.setTextChangListener(new TextChangListener(){
                @Override
                public void onTextChanged(MyEditText myEdit) {
                    myInput.append(myEdit.getText());
                    Log.i("deng", "onTextChanged: "+myInput.toString());
                   int index=findIndex(myEdit);
                    Log.i("deng", "index: "+index);
                    if(index+1!=size){
                       getChildAt(index+1).requestFocus();
                    }else{

                    }
                }

            });
            view.setBackgroundColor(Color.WHITE);
            this.addView(view);
        }
    }
    private int findIndex(MyEditText current){
        for(int i=0;i<getChildCount();i++){
            if(current==getChildAt(i)){
                return i;
            }
        }
        return 0;
    }
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
