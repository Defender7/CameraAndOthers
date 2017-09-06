package demo.optimizel.dn.com.myqqc60.TagView.anonimition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.DecelerateInterpolator;

import demo.optimizel.dn.com.myqqc60.TagView.views.TagViewGroup;

/**
 * Created by dengguochuan on 2017/7/31.
 */

public class TagViewGroupAnimation {
    public static Animator getShowTagAnimator(TagViewGroup group){//打开动画  type=1
        AnimatorSet set=new AnimatorSet();
        AnimatorSet cicle=getCicleAnamitor(group);
        set.playSequentially(cicle,getLinesRatio(group,1),getTagAnimator(group,1));
        return set;
    }
    public static Animator getHideTagAnimator(TagViewGroup group){//关闭动画  type=0
        AnimatorSet together=new AnimatorSet();
        AnimatorSet set=new AnimatorSet();
        together.playTogether(getTagAnimator(group,0),getLinesRatio(group,0));
        AnimatorSet cicle=getCicleAnamitor(group);
        set.playSequentially(cicle,together);
        return set;
    }
    private  static AnimatorSet getCicleAnamitor(TagViewGroup group){
        int mRadius=group.getmRadius();
        int mInnerRadius=group.getmInnerRadius();
        AnimatorSet cicle=new AnimatorSet();
        ObjectAnimator radius=ObjectAnimator.ofInt(group,TagViewGroup.TAG_RADIUS,mRadius-10,mRadius+10,mRadius);
        ObjectAnimator innerRadius=ObjectAnimator.ofInt(group,TagViewGroup.TAG_INNER_RADIUS,mInnerRadius-10,mInnerRadius+10,mInnerRadius);
        cicle.playTogether(radius,innerRadius);
        cicle.setDuration(400);
        cicle.setInterpolator(new DecelerateInterpolator());
        return cicle;
    }
    private  static Animator getLinesRatio(final TagViewGroup target,int type){
        ObjectAnimator ob;
        if(type==0){
            ob=ObjectAnimator.ofFloat(target,TagViewGroup.LINES_RATIO,1,0);
           }else if(type==1){
            ob=ObjectAnimator.ofFloat(target,TagViewGroup.LINES_RATIO,0,1);
        }else{
            throw new RuntimeException("动画类型设置错误");
        }

        ob.setDuration(400);
        ob.setInterpolator(new DecelerateInterpolator());
        return ob;
    }
    private  static  Animator getTagAnimator(final TagViewGroup target,int type){
        ObjectAnimator ob1;
        if(type==0){
            ob1=ObjectAnimator.ofFloat(target,TagViewGroup.TAG_ALPHA,1,0);
        }else if(type==1){
           ob1= ObjectAnimator.ofFloat(target,TagViewGroup.TAG_ALPHA,0,1);
        }else{
            throw new RuntimeException("动画类型设置错误");
        }
        ob1.setDuration(400);
        ob1.setInterpolator(new DecelerateInterpolator());
        return ob1;
    }
}
