package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.QuestionBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StaffStarActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rb_start)
    RatingBar rbStart;

    private QuestionBean questionBean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("问题得分");
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_staff_star;
    }

    @Override
    protected void loadData() {
        questionBean = (QuestionBean) getIntent().getSerializableExtra("data");
        if (MyApplication.getInstance().getUserBean().getLid() == 0 &&
                questionBean.getStatus() == 2 &&
                questionBean.getScore() <= 0d) {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setText("提交评分");
            rbStart.setIsIndicator(false);
        } else {
            rbStart.setIsIndicator(true);
        }

        rbStart.setRating((float) questionBean.getScore());
    }

    @OnClick({R.id.btn_back, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:
                final float score = rbStart.getRating();
                OkHttpTools.sendPost(mContext, ApiManager.POST_SCORE)
                        .addParams("id", questionBean.getId() + "")
                        .addParams("score", score + "")
                        .build()
                        .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                            @Override
                            public void onResponse(BaseResponse baseResponse) {
                                showToast("评分成功");
                                Intent intent = new Intent();
                                intent.putExtra("score", score);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                break;
        }
    }
}
