package com.example.bsproperty.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.BaseActivity;
import com.example.bsproperty.utils.LQRPhotoSelectUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by yezi on 2018/1/27.
 */

public class FragmentStaff01 extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.rb_01)
    RadioButton rb01;
    @BindView(R.id.rb_02)
    RadioButton rb02;
    @BindView(R.id.rb_03)
    RadioButton rb03;
    @BindView(R.id.rb_04)
    RadioButton rb04;
    @BindView(R.id.et_addr)
    EditText etAddr;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.iv_update_img)
    ImageView ivUpdateImg;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private File mFile;
    private int type = 0;

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnBack.setText("");
        tvTitle.setText("新问题提交");
        rb01.setChecked(true);
        type = 0;
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils((BaseActivity) mContext, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                Glide.with(mContext).load(outputUri).into((ImageView) mRootView.findViewById(R.id.iv_update_img));
                mFile = outputFile;
            }
        }, false);
        Date date = new Date(System.currentTimeMillis());
        String[] times = format.format(date).split(" ");
        tvDate.setText(times[0]);
        tvTime.setText(times[1]);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_staff01;
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.rb_01, R.id.rb_02, R.id.rb_03, R.id.rb_04, R.id.tv_date, R.id.tv_time, R.id.iv_update_img, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_01:
                rb01.setChecked(true);
                type = 0;
                break;
            case R.id.rb_02:
                rb02.setChecked(true);
                type = 1;
                break;
            case R.id.rb_03:
                rb03.setChecked(true);
                type = 2;
                break;
            case R.id.rb_04:
                rb04.setChecked(true);
                type = 3;
                break;
            case R.id.tv_date:
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int monthOfYear = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                            tvDate.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                            tvDate.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                            tvDate.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } else {
                            tvDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }
                }, year, monthOfYear, dayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.tv_time:
                final Calendar c2 = Calendar.getInstance();
                final int hour = c2.get(Calendar.HOUR_OF_DAY);
                int minute = c2.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10 && minute < 10) {
                            tvTime.setText("0" + hourOfDay + ":0" + minute + ":00");
                        } else if (hourOfDay >= 10 && minute < 10) {
                            tvTime.setText(hourOfDay + ":0" + minute + ":00");
                        } else if (hourOfDay < 10 && minute >= 10) {
                            tvTime.setText("0" + hourOfDay + ":" + minute + ":00");
                        } else {
                            tvTime.setText(hourOfDay + ":" + minute + ":00");
                        }
                    }
                }, hour, minute, true);
                timePickerDialog.show();
                break;
            case R.id.iv_update_img:
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(new String[]{
                        "拍照选择", "本地相册选择", "取消"
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                PermissionGen.with(FragmentStaff01.this)
                                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        ).request();
                                break;
                            case 1:
                                PermissionGen.needPermission(FragmentStaff01.this,
                                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                );
                                break;
                            case 2:
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.btn_commit:
                String info = etInfo.getText().toString().trim();
                String addr = etAddr.getText().toString().trim();
                if (TextUtils.isEmpty(addr)) {
                    showToast("请输入问题发生地点...");
                    return;
                }
                if (TextUtils.isEmpty(info)) {
                    showToast("请输入问题的相关描述...");
                    return;
                }
                Long time = new Date().getTime();
                try {
                    time = format.parse("" + tvDate.getText().toString() + " " + tvTime.getText().toString()).getTime();
                } catch (ParseException e) {
                    showToast("时间格式错误...");
                    return;
                }
                if (mFile == null) {
                    showToast("请选择图片");
                    return;
                }
                PostFormBuilder postFormBuilder;
                postFormBuilder = OkHttpTools.postFile(mContext, ApiManager.ADD_QUESTION,
                        "file", mFile);
                postFormBuilder.addParams("uid",
                        MyApplication.getInstance().getUserBean().getId() + "")
                        .addParams("time", time + "")
                        .addParams("info", info)
                        .addParams("addr", addr)
                        .addParams("type", type + "")
                        .addParams("team",
                                MyApplication.getInstance().getUserBean().getTeam()+"")
                        .build()
                        .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                            @Override
                            public void onResponse(BaseResponse baseResponse) {
                                showToast("发布成功");
                                etInfo.setText("");
                                etAddr.setText("");
                                mFile = null;
                                ivUpdateImg.setImageResource(R.mipmap.img_add);
                            }
                        });
                break;
        }
    }
}
