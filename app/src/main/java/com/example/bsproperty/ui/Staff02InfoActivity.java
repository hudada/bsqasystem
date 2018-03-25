package com.example.bsproperty.ui;

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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.QuestionBean;
import com.example.bsproperty.fragment.FragmentStaff01;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.utils.LQRPhotoSelectUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class Staff02InfoActivity extends BaseActivity {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
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
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private QuestionBean mData;
    private int type;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private File mFile;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("问题修改");
        btnRight.setText("删除");
        btnRight.setVisibility(View.VISIBLE);
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils((BaseActivity) mContext, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                Glide.with(mContext).load(outputUri).into(ivUpdateImg);
                mFile = outputFile;
            }
        }, false);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_staff02_info;
    }

    @Override
    protected void loadData() {
        mData = (QuestionBean) getIntent().getSerializableExtra("data");
        type = mData.getType();
        switch (type){
            case 0:
                rb01.setChecked(true);
                break;
            case 1:
                rb02.setChecked(true);
                break;
            case 2:
                rb03.setChecked(true);
                break;
            case 3:
                rb04.setChecked(true);
                break;
        }
        etAddr.setText(mData.getAddr());
        etInfo.setText(mData.getInfo());
        String[] times = format.format(mData.getTime()).split(" ");
        tvDate.setText(times[0]);
        tvTime.setText(times[1]);
        Glide.with(mContext).load(ApiManager.GET_QUESTION_IMG + mData.getImg()).into(ivUpdateImg);
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

    @OnClick({R.id.btn_back, R.id.btn_right, R.id.rb_01, R.id.rb_02, R.id.rb_03, R.id.rb_04, R.id.tv_date, R.id.tv_time, R.id.iv_update_img, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:

                break;
            case R.id.rb_01:
                rb01.setChecked(true);
                type = 0;
                break;
            case R.id.rb_02:
                rb01.setChecked(true);
                type = 1;
                break;
            case R.id.rb_03:
                rb01.setChecked(true);
                type = 2;
                break;
            case R.id.rb_04:
                rb01.setChecked(true);
                type = 3;
                break;
            case R.id.tv_date:
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int monthOfYear = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

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
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
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
                                PermissionGen.with((BaseActivity)mContext)
                                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        ).request();
                                break;
                            case 1:
                                PermissionGen.needPermission((BaseActivity)mContext,
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
                break;
        }
    }
}
