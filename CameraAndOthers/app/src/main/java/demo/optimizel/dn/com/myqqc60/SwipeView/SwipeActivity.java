package demo.optimizel.dn.com.myqqc60.SwipeView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import demo.optimizel.dn.com.myqqc60.R;

public class SwipeActivity extends AppCompatActivity {
    private MyRecycleView myRecycler;
    private List<String> mLists=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRecycler= (MyRecycleView) findViewById(R.id.recycler);
        initData();
        initRecycle();
        initAdapter();
    }
    private void  initAdapter(){
        MyRecyclerViewAdapter adapter=new MyRecyclerViewAdapter(this,mLists);
        myRecycler.setAdapter(adapter);
    }
    private void initRecycle(){
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setItemAnimator(new DefaultItemAnimator());
        myRecycler.addItemDecoration(new MyItemDecoration(this, LinearLayout.VERTICAL));
    }
    private void initData(){
        for(int i=0;i<20;i++){
            mLists.add("item [ "+i+" ]");
        }
    }

}
