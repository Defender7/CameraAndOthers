package demo.optimizel.dn.com.myqqc60;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.CameraActivity;
import demo.optimizel.dn.com.myqqc60.EditTextView.EditActivity;
import demo.optimizel.dn.com.myqqc60.SwipeView.SwipeActivity;
import demo.optimizel.dn.com.myqqc60.TagView.RippleActivity;

/**
 * Created by dengguochuan on 2017/8/15.
 */

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
    }
    public void goToEditActivity(View view){
        Intent intent=new Intent(this, EditActivity.class);
        startActivity(intent);
    }
    public void goToAccessActivity(View view){
//        Intent intent=new Intent(this, AccessActivity.class);
        Intent intent=new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
    public void goToSwipeActivity(View view){
        Intent intent=new Intent(this, SwipeActivity.class);
        startActivity(intent);
    }
    public void goToRippleActivity(View view){
        Intent intent=new Intent(this, RippleActivity.class);
        startActivity(intent);
    }
}
