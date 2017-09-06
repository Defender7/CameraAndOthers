package demo.optimizel.dn.com.myqqc60.EditTextView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by dengguochuan on 2017/8/15.
 */

public class MyEditText extends EditText {


    private TextChangListener listener;
    public MyEditText(Context context) {
        super(context);
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if(listener!=null){

        listener.onTextChanged(this);
        }
    }
    public interface TextChangListener{
        void onTextChanged(MyEditText i);
    }
    public void setTextChangListener(TextChangListener listener) {
        this.listener = listener;
    }
}
