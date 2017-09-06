package demo.optimizel.dn.com.myqqc60.SwipeView;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.optimizel.dn.com.myqqc60.R;

/**
 * Created by dengguochuan on 2017/7/26.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private List<String>mData;
    private Context mContext;
    private ViewDragHelper viewDragHelper;
    private List<SwipeLayout> openedItems;
    public MyRecyclerViewAdapter(Context context,List<String>lists){
        this.mContext=context;
        this.mData=lists;
        openedItems=new ArrayList<>();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.tvName.setText(mData.get(position));
        final SwipeLayout swipeLayout= (SwipeLayout) holder.itemView;
        swipeLayout.setSwipeLister(new SwipeLayout.SwipeListener() {
            @Override
            public void opened(SwipeLayout mSwipeLayout) {
                Log.i("dengguochuan", "opened: ");
                openedItems.add(swipeLayout);
            }

            @Override
            public void closed(SwipeLayout mSwipeLayout) {
                Log.i("dengguochuan", "closed: ");
                openedItems.remove(swipeLayout);
            }

            @Override
            public void onStartOpen(SwipeLayout mSwipeLayout) {
                Log.i("dengguochuan", "onStartOpen: ");
                closeAll();
                openedItems.add(mSwipeLayout);
            }

            @Override
            public void onStartClose(SwipeLayout mSwipeLayout) {
                Log.i("dengguochuan", "onStartClose: ");

            }

            @Override
            public void onOpening(SwipeLayout mSwipeLayout) {
                Log.i("dengguochuan", "onOpening: ");

            }

        });

    }
    private void closeAll(){
        if(openedItems.size()==0){
            return ;
        }
        for(SwipeLayout swipe:openedItems){
            swipe.close();
        }
        openedItems.clear();
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }



    static  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvCall, tvDelete;
        TextView tvName;
        TextView point;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCall = (TextView) itemView.findViewById(R.id.tvCall);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            point = (TextView) itemView.findViewById(R.id.point);
        }
    }
}
