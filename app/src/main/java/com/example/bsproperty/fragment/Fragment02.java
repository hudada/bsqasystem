package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.QuestionBean;
import com.example.bsproperty.bean.QuestionInfoBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.InfoStaffActivity;
import com.example.bsproperty.ui.InfoManActivity;
import com.example.bsproperty.utils.DenstityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment02 extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.rb_00)
    RadioButton rb00;
    @BindView(R.id.rb_01)
    RadioButton rb01;
    @BindView(R.id.rb_02)
    RadioButton rb02;
    @BindView(R.id.rb_03)
    RadioButton rb03;
    @BindView(R.id.btn_qe)
    Button btnQe;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    private int state = -1;
    private Fragment02.MyAdapter adapter;
    private ArrayList<QuestionBean> mData = new ArrayList<>();
    private boolean isVisibleToUser;

    @Override
    protected void loadData() {
        if (MyApplication.getInstance().getUserBean() != null &&
                isVisibleToUser) {
            getWebData();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getWebData();
    }

    private void getWebData() {
        mData.clear();
        String key = etText.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            key = "";
        }
        if (MyApplication.getInstance().getUserBean().getLid() == 1) {
            OkHttpTools.sendGet(mContext, ApiManager.GET_QUESTION_LIST_M)
                    .addParams("team",MyApplication.getInstance().getUserBean().getTeam()+"")
                    .addParams("key", key)
                    .addParams("status", state + "")
                    .build()
                    .execute(new BaseCallBack<QuestionInfoBean>(mContext, QuestionInfoBean.class) {
                        @Override
                        public void onResponse(QuestionInfoBean questionInfoBean) {
                            mData = questionInfoBean.getData();
                            adapter.notifyDataSetChanged(mData);
                        }
                    });
        } else {
            OkHttpTools.sendGet(mContext, ApiManager.GET_QUESTION_LIST)
                    .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                    .addParams("key", key)
                    .addParams("status", state + "")
                    .build()
                    .execute(new BaseCallBack<QuestionInfoBean>(mContext, QuestionInfoBean.class) {
                        @Override
                        public void onResponse(QuestionInfoBean questionInfoBean) {
                            mData = questionInfoBean.getData();
                            adapter.notifyDataSetChanged(mData);
                        }
                    });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        loadData();
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        btnBack.setText("");
        tvTitle.setText("问题详情查询");
        rb00.setChecked(true);
        state = -1;
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new Fragment02.MyAdapter(mContext, R.layout.item_fr_staff_02_list, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, final int position) {
                Intent intent;
                if (MyApplication.getInstance().getUserBean().getLid() == 0) {
                    intent = new Intent(getContext(), InfoStaffActivity.class);
                    intent.putExtra("questionBean",mData.get(position));
                } else {
                    intent = new Intent(getContext(), InfoManActivity.class);
                    intent.putExtra("questionBean",mData.get(position));
                }
                startActivity(intent);
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_staff02;
    }


    @OnClick({R.id.rb_00, R.id.rb_01, R.id.rb_02, R.id.rb_03, R.id.btn_qe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_00:
                rb00.setChecked(true);
                state = -1;
                break;
            case R.id.rb_01:
                rb01.setChecked(true);
                state = 0;
                break;
            case R.id.rb_02:
                rb02.setChecked(true);
                state = 1;
                break;
            case R.id.rb_03:
                rb03.setChecked(true);
                state = 2;
                break;
            case R.id.btn_qe:
                getWebData();
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<QuestionBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<QuestionBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, QuestionBean questionBean, int position) {
            holder.setText(R.id.tv_02, DenstityUtils.getTypeStr(questionBean.getType()))
                    .setText(R.id.tv_04, "问题地点：" + questionBean.getAddr())
                    .setText(R.id.tv_05, "问题时间：" + format.format(new Date(questionBean.getTime())))
                    .setText(R.id.tv_06, "问题描述：" + questionBean.getInfo());
            TextView t = (TextView) holder.getView(R.id.tv_03);
            if (questionBean.getStatus() == 0) {
                t.setTextColor(0xff999999);
                holder.setText(R.id.tv_03, "待解决");
            } else if (questionBean.getStatus() == 1) {
                t.setTextColor(0xff35a91e);
                holder.setText(R.id.tv_03, "解决中");
            } else {
                t.setTextColor(0xffe83723);
                holder.setText(R.id.tv_03, "已解决");
            }
        }
    }
}
