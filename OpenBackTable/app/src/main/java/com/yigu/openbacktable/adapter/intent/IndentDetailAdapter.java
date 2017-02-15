package com.yigu.openbacktable.adapter.intent;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yigu.common.result.IndexData;
import com.yigu.common.result.MapiHistoryResult;
import com.yigu.common.util.DebugLog;
import com.yigu.openbacktable.R;
import com.yigu.openbacktable.adapter.pay.PaymentAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/1/4.
 */
public class IndentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int INFO = 0;
    private final static int ITEM = 1;

    LayoutInflater inflater;

    private List<IndexData> mList;

    Context context;

    public IndentDetailAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case INFO:
                return new InfoViewHolder(inflater.inflate(R.layout.layout_indent_detail_info, parent, false));
            case ITEM:
                return new OrderItemViewHolder(inflater.inflate(R.layout.lay_indent_detail_item, parent, false));
            default:
                return new InfoViewHolder(inflater.inflate(R.layout.layout_indent_detail_info, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InfoViewHolder) {
            setInfo((InfoViewHolder) holder, position);
//            ((SliderViewHolder) holder).orderSliderLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        } else if (holder instanceof OrderItemViewHolder) {
            setItem((OrderItemViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "INFO":
                return INFO;
            case "ITEM":
                return ITEM;
            default:
                return INFO;
        }
    }

    private void setInfo(InfoViewHolder viewHolder, int position) {

        MapiHistoryResult mapiHistoryResult = (MapiHistoryResult) mList.get(position).getData();
        viewHolder.title.setText(mapiHistoryResult.getName());
        viewHolder.tel.setText("联系电话：" + mapiHistoryResult.getTel());
        viewHolder.time.setText("下单时间：" + mapiHistoryResult.getCreated());
        viewHolder.addr.setText("送货地址：" + mapiHistoryResult.getBz());


    }

    private void setItem(OrderItemViewHolder viewHolder, int position) {
        List<IndexData> indexDataList = (List<IndexData>) mList.get(position).getData();
        DebugLog.i("indexDataList=>"+indexDataList.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        viewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        PaymentAdapter mAdapter = new PaymentAdapter(context, indexDataList);
        viewHolder.recyclerView.setAdapter(mAdapter);
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.tel)
        TextView tel;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.addr)
        TextView addr;

        public InfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        public OrderItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
