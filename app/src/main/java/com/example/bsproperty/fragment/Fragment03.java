package com.example.bsproperty.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.utils.DenstityUtils;
import com.example.bsproperty.utils.SpUtils;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment03 extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_limit)
    TextView tvLimit;
    @BindView(R.id.tv_team)
    TextView tvTeam;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.lin_manage)
    LinearLayout linManage;
    @BindView(R.id.btn_esc)
    Button btnEsc;
    private UserBean userBean;

    @Override
    protected void loadData() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (MyApplication.getInstance().getUserBean()!=null){
            userBean=MyApplication.getInstance().getUserBean();
        }else{
            showToast("用户信息读取失败！");
            return;
        }
        btnBack.setText("");
        if (userBean.getLid()==0){
            linManage.setVisibility(View.GONE);
        }else{
            linManage.setVisibility(View.VISIBLE);
            tvScore.setText(userBean.getScore()+"分");
        }
        tvName.setText(userBean.getUserName());
        tvAge.setText(userBean.getAge()+"岁");
        tvSex.setText(userBean.getSex()==0?"女":"男");
        tvTeam.setText(DenstityUtils.getTeamStr(userBean.getTeam()));
        tvTitle.setText("个人中心");
        tvLimit.setText(DenstityUtils.getLimitStr(userBean.getLid()));
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public int getRootViewId() {
        return R.layout.fragment_03;
    }


    @OnClick(R.id.btn_esc)
    public void onViewClicked() {
        if(SpUtils.cleanUserBean(getContext())){
            System.exit(0);
        }
    }
}
