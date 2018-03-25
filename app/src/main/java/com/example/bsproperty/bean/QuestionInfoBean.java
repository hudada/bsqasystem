package com.example.bsproperty.bean;

import java.util.ArrayList;

/**
 * Created by wdxc1 on 2018/3/24.
 */

public class QuestionInfoBean extends BaseResponse {
    private ArrayList<QuestionBean> data;

    public ArrayList<QuestionBean> getData() {
        return data;
    }

    public void setData(ArrayList<QuestionBean> data) {
        this.data = data;
    }
}
