package com.yigu.openbacktable.activity.intent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.common.api.OrderApi;
import com.yigu.common.application.AppContext;
import com.yigu.common.result.MapiHistoryResult;
import com.yigu.common.util.DPUtil;
import com.yigu.common.util.DateUtil;
import com.yigu.common.util.RequestExceptionCallback;
import com.yigu.common.util.RequestPageCallback;
import com.yigu.common.widget.MainToast;
import com.yigu.openbacktable.R;
import com.yigu.openbacktable.adapter.intent.IntentListAdapter;
import com.yigu.openbacktable.base.BaseActivity;
import com.yigu.openbacktable.base.RequestCode;
import com.yigu.openbacktable.interfaces.RecyOnItemClickListener;
import com.yigu.openbacktable.util.ControllerUtil;
import com.yigu.openbacktable.widget.BestSwipeRefreshLayout;
import com.yigu.openbacktable.widget.DividerListItemDecoration;
import com.yigu.openbacktable.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntentListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefreshLayout)
    BestSwipeRefreshLayout swipRefreshLayout;

    List<MapiHistoryResult> mList;
    IntentListAdapter mAdapter;

    private Integer pageIndex = 0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    String dateStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_list);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {
        center.setText("订单列表");
        back.setImageResource(R.mipmap.back);
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(4), getResources().getColor(R.color.divider_line)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new IntentListAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        //控制时间范围
        Calendar calendar = Calendar.getInstance();

        //通过日历获取下一天日期
        calendar.setTime(new Date());

        dateStr = DateUtil.getInstance().date2YMD_H(calendar.getTime());

    }

    private void initListener(){
        swipRefreshLayout.setOnRefreshListener(new BestSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(IntentListActivity.this, IntentDetailActivity.class);
                intent.putExtra("item",mList.get(position));
                startActivityForResult(intent,RequestCode.intent_list);
            }
        });
    }

    private void load() {
        OrderApi.getSaleslist(this, userSP.getUserBean().getUSER_ID(), "1", dateStr, pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiHistoryResult>>() {
            @Override
            public void success(Integer isNext, List<MapiHistoryResult> success) {
                swipRefreshLayout.setRefreshing(false);
                ISNEXT = isNext;
                if (success.isEmpty())
                    return;
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                swipRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
            }
        });
    }

    private void loadNext() {
        if (ISNEXT != null && ISNEXT == 0) {
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            pageIndex = 0;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode== RequestCode.intent_list){
                refreshData();
            }
        }
    }
}
