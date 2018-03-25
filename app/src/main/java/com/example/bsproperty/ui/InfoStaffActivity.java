package com.example.bsproperty.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.QuestionBean;
import com.example.bsproperty.bean.ReplyBean;
import com.example.bsproperty.bean.ReplyInfoBean;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.fragment.FragmentManager01;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.DenstityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class InfoStaffActivity extends BaseActivity {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.tv_02)
    TextView tv02;
    @BindView(R.id.tv_03)
    TextView tv03;
    @BindView(R.id.tv_04)
    TextView tv04;
    @BindView(R.id.tv_05)
    TextView tv05;
    @BindView(R.id.tv_06)
    TextView tv06;
    @BindView(R.id.ima_01)
    ImageView ima01;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    @BindView(R.id.lin_reply)
    LinearLayout linReply;
    private QuestionBean questionBean;
    private UserBean userBean;
    private InfoStaffActivity.MyAdapter adapter;
    private ArrayList<ReplyBean> mData = new ArrayList<>();

    @OnClick({R.id.btn_back, R.id.btn_right, R.id.ima_01, R.id.rb_01, R.id.rb_02, R.id.btn_re})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_right:
                if (btnRight.getText().equals("评分")) {
                    Intent intent = new Intent(mContext, StaffStarActivity.class);
                    intent.putExtra("data", questionBean);
                    startActivityForResult(intent, 521);
                } else if (btnRight.getText().equals("删除")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("删除")
                            .setMessage("是否确认删除问题？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OkHttpTools.sendPost(mContext, ApiManager.POST_QUESTION_DEL)
                                            .addParams("id", questionBean.getId() + "")
                                            .build()
                                            .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                                @Override
                                                public void onResponse(BaseResponse baseResponse) {
                                                    showToast("删除成功");
                                                    finish();
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;
            case R.id.ima_01:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 521 && resultCode == RESULT_OK) {
            questionBean.setScore((double) data.getFloatExtra("score", 0));
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        userBean = MyApplication.getInstance().getUserBean();
        linReply.setVisibility(View.GONE);
        tvTitle.setText("问题详情");
        questionBean = (QuestionBean) getIntent().getSerializableExtra("questionBean");
        if (questionBean != null && questionBean.getStatus() == 0) {
            btnRight.setText("删除");
        } else if (questionBean != null && questionBean.getStatus() == 1) {
            btnRight.setText("");
        } else {
            btnRight.setText("评分");
        }
        tv02.setText(DenstityUtils.getTypeStr(questionBean.getType()));
        if (questionBean.getStatus() == 0) {
            tv03.setTextColor(0xff999999);
            tv03.setText("(待解决)");
        } else if (questionBean.getStatus() == 1) {
            tv03.setTextColor(0xff35a91e);
            tv03.setText("(解决中)");
        } else {
            tv03.setTextColor(0xffe83723);
            tv03.setText("(已解决)");
        }
        tv04.setText("问题地点：" + questionBean.getAddr());
        tv05.setText("问题时间：" + format.format(new Date(questionBean.getTime())));
        tv06.setText("问题描述：" + questionBean.getInfo());
        Glide.with(mContext).load(ApiManager.GET_QUESTION_IMG + questionBean.getImg()).into(ima01);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new InfoStaffActivity.MyAdapter(mContext, R.layout.item_recomment, mData);
        rvList.setAdapter(adapter);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_info;
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendGet(mContext, ApiManager.GET_REPLAY_LIST)
                .addParams("qid", questionBean.getId() + "")
                .build()
                .execute(new BaseCallBack<ReplyInfoBean>(mContext, ReplyInfoBean.class) {
                    @Override
                    public void onResponse(ReplyInfoBean replyInfoBean) {
                        mData = replyInfoBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    private class MyAdapter extends BaseAdapter<ReplyBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<ReplyBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ReplyBean replyBean, int position) {
            holder.setText(R.id.tv_02, format.format(new Date(replyBean.getTime())))
                    .setText(R.id.tv_03, replyBean.getInfo());
        }
    }
}
