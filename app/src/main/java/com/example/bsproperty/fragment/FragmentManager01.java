package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.QuestionBean;
import com.example.bsproperty.bean.QuestionInfoBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.InfoManActivity;
import com.example.bsproperty.ui.InfoStaffActivity;
import com.example.bsproperty.utils.DenstityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by yezi on 2018/1/27.
 */

public class FragmentManager01 extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    private FragmentManager01.MyAdapter adapter;
    private ArrayList<QuestionBean> mData=new ArrayList<>();
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private boolean isVisibleToUser;

    @Override
    protected void loadData() {

    }

    private void loadWebData() {
        mData.clear();

        OkHttpTools.sendGet(mContext, ApiManager.GET_QUESTION_LIST_M)
                .addParams("team",MyApplication.getInstance().getUserBean().getTeam()+"")
                .addParams("status", "-2")
                .build()
                .execute(new BaseCallBack<QuestionInfoBean>(mContext, QuestionInfoBean.class) {
                    @Override
                    public void onResponse(QuestionInfoBean questionInfoBean) {
                        mData = questionInfoBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWebData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnBack.setText("");
        tvTitle.setText("未完成问题");
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new FragmentManager01.MyAdapter(mContext, R.layout.item_fr_staff_02_list, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, final int position) {
                Intent intent=new Intent(getContext(), InfoManActivity.class);
                intent.putExtra("questionBean",mData.get(position));
                startActivity(intent);
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_manage01;
    }

    private class MyAdapter extends BaseAdapter<QuestionBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<QuestionBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, QuestionBean questionBean, int position) {
            holder.setText(R.id.tv_02, DenstityUtils.getTypeStr(questionBean.getType()))
                    .setText(R.id.tv_04,"问题地点："+questionBean.getAddr())
                    .setText(R.id.tv_05,"问题时间："+format.format(new Date(questionBean.getTime())))
                    .setText(R.id.tv_06,"问题描述："+questionBean.getInfo());
            TextView t= (TextView) holder.getView(R.id.tv_03);
            if (questionBean.getStatus()==0){
                t.setTextColor(0xff999999);
                holder.setText(R.id.tv_03,"待解决");
            }else if (questionBean.getStatus()==1){
                t.setTextColor(0xff35a91e);
                holder.setText(R.id.tv_03,"解决中");
            }else{
                t.setTextColor(0xffe83723);
                holder.setText(R.id.tv_03,"已解决");
            }
        }
    }
}
