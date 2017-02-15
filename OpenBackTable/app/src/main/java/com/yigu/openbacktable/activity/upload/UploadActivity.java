package com.yigu.openbacktable.activity.upload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.soundcloud.android.crop.Crop;
import com.yigu.common.api.BasicApi;
import com.yigu.common.api.CommonApi;
import com.yigu.common.api.OrderApi;
import com.yigu.common.result.MapiImageResult;
import com.yigu.common.result.MapiOrderResult;
import com.yigu.common.result.MapiResourceResult;
import com.yigu.common.util.DPUtil;
import com.yigu.common.util.DateUtil;
import com.yigu.common.util.DebugLog;
import com.yigu.common.util.FileUtil;
import com.yigu.common.util.JGJBitmapUtils;
import com.yigu.common.util.RequestCallback;
import com.yigu.common.util.RequestExceptionCallback;
import com.yigu.common.util.RequestPageCallback;
import com.yigu.common.widget.MainToast;
import com.yigu.openbacktable.R;
import com.yigu.openbacktable.base.BaseActivity;
import com.yigu.openbacktable.base.RequestCode;
import com.yigu.openbacktable.widget.BaseItemDialog;
import com.yigu.openbacktable.widget.MainAlertDialog;
import com.yigu.openbacktable.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.typeLL)
    LinearLayout typeLL;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.price)
    EditText price;
    @Bind(R.id.imageLL)
    LinearLayout imageLL;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.tiem_tv)
    TextView tiemTv;

    List<String> strList = new ArrayList<>();

    private List<MapiOrderResult> mList;
    BaseItemDialog typeDialog;

    String typeStr = "";
    List<MapiResourceResult> typeList = new ArrayList<>();
    List<MapiResourceResult> timeList = new ArrayList<>();
    PhotoDialog photoDialog;

    ArrayList<MapiImageResult> imgList;

    String images = "";

    MainAlertDialog dialog;

    String SHOP = "  ";

    TimePickerView pvTime;

    BaseItemDialog baseItemDialog;

    private String timeStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("上架菜品");
        tvRight.setText("确认");
        imgList = new ArrayList<>();
        mList = new ArrayList<>();
        if (typeDialog == null)
            typeDialog = new BaseItemDialog(this, R.style.image_dialog_theme);

        typeList.clear();
        typeList.add(new MapiResourceResult("0", "普通菜"));
        typeList.add(new MapiResourceResult("1", "特色菜"));
        typeDialog.setmList(typeList);

        type.setText(typeList.get(0).getNAME());
        typeStr = typeList.get(0).getZD_ID();

        if (dialog == null) {
            dialog = new MainAlertDialog(this);
            dialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认删除这张图片?");
        }

        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦

        //通过日历获取下一天日期
        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_YEAR, +1);
        pvTime.setTime(calendar.getTime());

        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                dateTv.setText(DateUtil.getInstance().date2YMD_H(date));
            }
        });

        dateTv.setText(DateUtil.getInstance().date2YMD_H(calendar.getTime()));

        if (baseItemDialog == null)
            baseItemDialog = new BaseItemDialog(this, R.style.image_dialog_theme);

    }

    private void initListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SHOP = mList.get(position).getID();
                tiemTv.setText("");
                timeStr = "";
                loadTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeDialog.setDialogItemClickListner(new BaseItemDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                type.setText(typeList.get(position).getNAME());
                typeStr = typeList.get(position).getZD_ID();
            }
        });

        dialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgList.remove(0);
                images = "";
                image.setImageResource(R.mipmap.default_item);
                image.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        baseItemDialog.setDialogItemClickListner(new BaseItemDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                tiemTv.setText(timeList.get(position).getTIME());
                timeStr = timeList.get(position).getTYPE();
            }
        });

    }

    private void loadTime() {
        showLoading();
        OrderApi.getDinnertimeay(this,SHOP, new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if (success.isEmpty())
                    return;
                timeList.clear();
                timeList.addAll(success);
                timeList.add(0, new MapiResourceResult("", "全部"));
                baseItemDialog.setmList(timeList);

//                tiemTv.setText(timeList.get(0).getTIME());
//                timeStr = timeList.get(0).getTYPE();

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void load() {
        showLoading();
        OrderApi.getCanteenlist(this, userSP.getUserBean().getCOMPANY(), "0", "99", new RequestPageCallback<List<MapiOrderResult>>() {
            @Override
            public void success(Integer isNext, List<MapiOrderResult> success) {
                hideLoading();
                if (success.isEmpty())
                    return;
                mList.addAll(success);
                for (int i = 0; i < mList.size(); i++) {
                    strList.add(mList.get(i).getNAME());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (UploadActivity.this, android.R.layout.simple_spinner_item, strList);      //绑定字符串数组
                spinner.setAdapter(adapter);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setSelection(0, true);    //默认选中第一个
                SHOP = mList.get(0).getID();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }


    @OnClick({R.id.back, R.id.typeLL, R.id.imageLL,R.id.tv_right,R.id.image,R.id.date,R.id.ll_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.typeLL:
                typeDialog.showDialog();
                break;
            case R.id.imageLL:
                if (null != imgList && imgList.size() == 0) {
                    if (photoDialog == null)
                        photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                    photoDialog.setImagePath("upload_image.jpg");
                    photoDialog.showDialog();
                }
                break;
            case R.id.tv_right:

                String food = name.getText().toString();
                String count = price.getText().toString();

                if(TextUtils.isEmpty(SHOP)){
                    MainToast.showShortToast("请选择食堂");
                    return;
                }

                if(TextUtils.isEmpty(typeStr)){
                    MainToast.showShortToast("请选择菜品分类");
                    return;
                }

                if(TextUtils.isEmpty(food)){
                    MainToast.showShortToast("请输入菜品名称");
                    return;
                }

                if(TextUtils.isEmpty(count)){
                    MainToast.showShortToast("请输入菜品价格");
                    return;
                }

                if(imgList.isEmpty()){
                    MainToast.showShortToast("请上传菜品图片");
                    return;
                }

                getImages();

                showLoading();
                CommonApi.shelves(this, SHOP, typeStr, food, count, images, dateTv.getText().toString(),timeStr,new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showShortToast("上传成功");
                        spinner.setSelection(0, true);    //默认选中第一个
                        type.setText(typeList.get(0).getNAME());
                        typeStr = typeList.get(0).getZD_ID();
                        name.setText("");
                        price.setText("");
                        imgList.remove(0);
                        images = "";
                        image.setImageResource(R.mipmap.default_item);
                        image.setVisibility(View.GONE);
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(String code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });

                break;
            case R.id.image:
                dialog.show();
                break;
            case R.id.date:
                pvTime.show();
                break;
            case R.id.ll_time:
                baseItemDialog.showDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        DebugLog.i("requestCode=" + requestCode + "resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "upload_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "upload_image_crop.jpg", FileUtil.TYPE_IMAGE);
                        String filename = picture2.getAbsolutePath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                        JGJBitmapUtils.saveBitmap2file(bitmap, filename);
                        if (JGJBitmapUtils.rotateBitmapByDegree(filename, filename, JGJBitmapUtils.getBitmapDegree(filename))) {
                            uploadImage(picture2);
                        } else
                            DebugLog.i("图片保存失败");
                    }
                    break;
            }
        }

    }

    private void getImages() {
        images = "";
        StringBuilder sb = new StringBuilder();
        for (MapiImageResult imageResult : imgList) {
            if (sb.length() != 0)
                sb.append(",");
            sb.append(imageResult.getID());
        }
        images = sb.toString();
    }

    private void uploadImage(File file) {
        showLoading();
        CommonApi.uploadImage(this, file, new RequestCallback<MapiImageResult>() {
            @Override
            public void success(MapiImageResult success) {
                hideLoading();
                if (null != success) {
                    imgList.add(success);
                    image.setVisibility(View.VISIBLE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                    //创建将要下载的图片的URI
                    Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + imgList.get(0).getPATH());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(120), DPUtil.dip2px(120)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(image.getController())
                            .setControllerListener(new BaseControllerListener<ImageInfo>())
                            .build();
                    image.setController(controller);

                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                DebugLog.i(message);
            }
        });
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Uri outUrl = Uri
                .fromFile(FileUtil.createFile(this, "upload_image_crop.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("list", imgList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imgList = (ArrayList<MapiImageResult>) savedInstanceState.getSerializable("list");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != pvTime && pvTime.isShowing()) {
            pvTime.dismiss();
            pvTime = null;
        }
        if (null != baseItemDialog && baseItemDialog.isShowing()) {
            baseItemDialog.dismiss();
            baseItemDialog = null;
        }
    }
}
