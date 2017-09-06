package demo.optimizel.dn.com.myqqc60.TagView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import demo.optimizel.dn.com.myqqc60.R;
import demo.optimizel.dn.com.myqqc60.TagView.anonimition.TagViewGroupAnimation;
import demo.optimizel.dn.com.myqqc60.TagView.data.DataRepo;
import demo.optimizel.dn.com.myqqc60.TagView.views.TagViewGroup;

import static demo.optimizel.dn.com.myqqc60.TagView.data.DataRepo.tagGroupList;

/**
 * Created by dengguochuan on 2017/7/27.
 */

public class RippleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ripple_activity);
        ImageView iv= (ImageView) findViewById(R.id.iv);

//        RippleView rippleView=new RippleView(this);
//        rippleView.setmCenterX(200);
//        rippleView.setmCenterY(200);
//        rippleView.initAnimitor(25,100,100);
//        addContentView(rippleView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        DataRepo.initData();
        int size=tagGroupList.get(0).getTags().size();

        final TagViewGroup group=new TagViewGroup(this);
        group.setHideAinimator(TagViewGroupAnimation.getHideTagAnimator(group));
        group.setShowAinimator(TagViewGroupAnimation.getShowTagAnimator(group));
        group.setmCenterX(200);
        group.setmCenterY(200);
        group.setmRadius(30);
        group.addRippleView();
        addContentView(group,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for(int i=0;i<size;i++){
            group.addTextView(tagGroupList.get(0).getTags().get(i));
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group.excuteAnimator();
            }
        });
    }
}
