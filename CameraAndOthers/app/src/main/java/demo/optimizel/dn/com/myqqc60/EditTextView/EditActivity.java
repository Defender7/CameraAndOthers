package demo.optimizel.dn.com.myqqc60.EditTextView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by dengguochuan on 2017/8/15.
 */

public class EditActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
        MultiEditTextView view=new MultiEditTextView(this);
        view.setBackgroundColor(Color.BLUE);

        addContentView(view,params);
    }
}
