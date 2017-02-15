package com.yigu.openbacktable.adapter.intent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yigu.common.result.MapiHistoryResult;
import com.yigu.openbacktable.R;
import com.yigu.openbacktable.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/1/4.
 */
public class IntentListAdapter extends RecyclerView.Adapter<IntentListAdapter.ViewHolder> {

    LayoutInflater inflater;

    private List<MapiHistoryResult> mList;
    RecyOnItemClickListener recyOnItemClickListener;

    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public IntentListAdapter(Context context, List<MapiHistoryResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_intent_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });
        MapiHistoryResult historyResult = mList.get(position);
        holder.addr.setText("送餐地址：" + historyResult.getBz());
        holder.status.setText("未送达");
        holder.date.setText(historyResult.getCreated());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.addr)
        TextView addr;
        @Bind(R.id.status)
        TextView status;
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
