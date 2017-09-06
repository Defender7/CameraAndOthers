package demo.optimizel.dn.com.myqqc60.AccessCamera.AccessAnimationView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import demo.optimizel.dn.com.myqqc60.R;

/**
 * Created by dengguochuan on 2017/8/3.
 */

public class AccessActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_activity);
        RelativeLayout rl= (RelativeLayout) findViewById(R.id.menu_rl);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        AccessView view=new AccessView(this);
        view.create();
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.TRANSPARENT);
        view.setAlpha(0.9f);
        rl.addView(view);
        view.startAnimation();
    }
}
