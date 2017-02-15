package com.yigu.openbacktable.activity.intent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.common.api.OrderApi;
import com.yigu.common.result.IndexData;
import com.yigu.common.result.MapiHistoryResult;
import com.yigu.common.result.MapiOrderResult;
import com.yigu.common.result.MapiResourceResult;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;
import com.yigu.common.widget.MainToast;
import com.yigu.openbacktable.R;
import com.yigu.openbacktable.adapter.intent.IndentDetailAdapter;
import com.yigu.openbacktable.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntentDetailActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.allPrice)
    TextView allPrice;
    @Bind(R.id.deel)
    TextView deel;

    List<IndexData> mList = new ArrayList<>();
    private final static String INFO = "INFO";
    private final static String ITEM = "ITEM";
    IndentDetailAdapter mAdapter;

    MapiHistoryResult mapiHistoryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_detail);
        ButterKnife.bind(this);
        if(null!=getIntent())
            mapiHistoryResult = (MapiHistoryResult) getIntent().getSerializableExtra("item");
        if(null!=mapiHistoryResult){
            initView();
            load();
        }
    }

    private void initView() {

        center.setText("订餐详情");
        back.setImageResource(R.mipmap.back);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new IndentDetailAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        allPrice.setText("共"+mapiHistoryResult.getPrice()+"元");

    }

    private void load(){
        OrderApi.getSalesdetailslist(this, mapiHistoryResult.getId(), new RequestCallback<List<MapiOrderResult>>() {
            @Override
            public void success(List<MapiOrderResult> success) {
                if(success.isEmpty())
                    return;
                mList.add(new IndexData(0, INFO,mapiHistoryResult));
                List<IndexData> indexDataList = new ArrayList<>();
                int count = 0;
                if(success!=null&&success.size()>0){
                    for(int i=0;i<success.size();i++){
                        indexDataList.add(new IndexData(count++, "item", success.get(i)));
                        indexDataList.add(new IndexData(count++, "divider", new Object()));
                    }
                }


                mList.add(new IndexData(1, ITEM, indexDataList));
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.back, R.id.deel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.deel:
                showLoading();
                OrderApi.sendto(this, mapiHistoryResult.getId(), new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showShortToast("完成");
                        setResult(RESULT_OK);
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });

                break;
        }
    }
}
