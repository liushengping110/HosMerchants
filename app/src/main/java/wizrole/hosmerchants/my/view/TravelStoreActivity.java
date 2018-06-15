package wizrole.hosmerchants.my.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.activity.ImageLookActivity;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.merchants.model.getsecondtypename.TwoTypeList;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.my.model.storecate.StoreTypeList;
import wizrole.hosmerchants.my.model.upimage.UpImageBack;
import wizrole.hosmerchants.my.preserent.upimage.UpImageInterface;
import wizrole.hosmerchants.my.preserent.upimage.UpImagePreserent;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.dialog.SelectDialog;
import wizrole.hosmerchants.util.image.GlideImageLoader;
import wizrole.hosmerchants.util.image.ImageLoading;
import wizrole.hosmerchants.view.CustTextView;

/**
 * Created by liushengping on 2018/1/28.
 * 何人执笔？
 * 出院出行商铺信息页面--与其他商铺不一一致
 * 仅供出院出行
 * 减少复用的数据繁琐性
 * 不使用百度云的证件识别，驾驶证、行驶证、识别准确率太低
 * 车牌识别免用
 */

public class TravelStoreActivity extends BaseActivity implements View.OnClickListener,UpImageInterface {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_s_one_cate)TextView text_s_one_cate;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.text_s_cate)TextView text_s_cate;//二级类目
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.img_s_logo)ImageView img_s_logo;//图标
    @BindView(R.id.img_s_alp)ImageView img_s_alp;//支付宝
    @BindView(R.id.img_s_wx)ImageView img_s_wx;//微信
    @BindView(R.id.img_jsz)ImageView img_s_jsz;//驾驶证
    @BindView(R.id.img_xsz)ImageView img_s_xsz;//行驶证
    @BindView(R.id.img_cp)ImageView img_s_cp;//车牌
    @BindView(R.id.img_cl)ImageView img_s_cl;//车辆
    @BindView(R.id.edit_s_name)EditText edit_s_name;
    @BindView(R.id.edit_s_tel)EditText edit_s_tel;
    @BindView(R.id.edit_s_add)EditText edit_s_add;
    @BindView(R.id.btn_up_sinfor)Button btn_up_sinfor;
    @BindView(R.id.list_secondtype)CustTextView list_secondtype;
    public Dialog dialog;
    public StoreList storeList;//店铺对象
    public StoreTypeList storeTypeList;
    private ImagePicker imagePicker;
    public Intent intent;
    public List<TwoTypeList> twoTypeLists=new ArrayList<>();//选中的二级分类
    public static final int DRIVE_REQUSR_CODE=6666;//二级分类
    public static final int DRIVE_RESULT_CODE=6667;//二级分类
    public String img_jsz_sel="";//驾驶证地址
    public String img_xsz_sel="";//行驶证地址
    public String img_cp_sel="";//车牌地址
    public String img_cl_sel="";//车辆图片
    public String img_logo_sel="";//头像地址
    public String img_alp_sel="";//支付宝
    public String img_wx_sel="";//微信
    private ArrayList<ImageItem> images = null;//选择图片集合
    public int type=0;//店铺logo  --1，支付宝--2，微信--3 ，驾驶证--4，行驶证--5 ，车辆 --6， 车牌--7
    public UpImagePreserent upImagePreserent=new UpImagePreserent(this);//上传图片
    @Override
    protected int getLayout() {
        return R.layout.activity_travelstore;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        storeList=(StoreList) getIntent().getSerializableExtra("storeinfor");
        storeTypeList=(StoreTypeList) getIntent().getSerializableExtra("storeTypeList");
        initUI(storeList);
    }

    public void initUI(StoreList s){
        text_s_one_cate.setText(storeTypeList.getStoreTypeName());//商户一级分类
        if(s==null){//添加出院出行
            text_title.setText("添加商户");
            changeOrLook=true;
            ImageLoading.common(TravelStoreActivity.this,R.drawable.tra_jsz,img_s_xsz,R.drawable.tra_jsz);//行驶证
            ImageLoading.common(TravelStoreActivity.this,R.drawable.tra_jsz,img_s_jsz,R.drawable.tra_jsz);//驾驶证
            ImageLoading.common(TravelStoreActivity.this,R.drawable.tra_car_number,img_s_cp,R.drawable.tra_car_number);//车牌
            ImageLoading.common(TravelStoreActivity.this,R.drawable.tra_car,img_s_cl,R.drawable.tra_car);//车辆
        }else {//查看或修改出院出行
            text_title.setText("商户信息");
            text_right.setText("修改信息");
            new ImageTurn(1, Constant.ip+storeList.getStoreLogoPic()).start();
            new ImageTurn(2,Constant.ip+storeList.getStorePayPic()).start();
            new ImageTurn(3,Constant.ip+storeList.getWeChatPic()).start();
        }
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        img_s_logo.setOnClickListener(this);
        img_s_jsz.setOnClickListener(this);
        img_s_xsz.setOnClickListener(this);
        img_s_cl.setOnClickListener(this);
        img_s_cp.setOnClickListener(this);
        img_s_alp.setOnClickListener(this);
        img_s_wx.setOnClickListener(this);
        text_s_cate.setOnClickListener(this);
        btn_up_sinfor.setOnClickListener(this);
    }
    public boolean changeOrLook=false;//是否是修改状态下 false--查看  true-修改
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.img_s_logo://图标
                if(storeList==null||changeOrLook){//添加或者修改
                    type=1;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(TravelStoreActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStoreLogoPic());
                    startActivity(intent);
                }
                break;
            case R.id.text_s_cate://二级类目
                if(twoTypeLists.size()>0){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("sel_secondType",(Serializable) twoTypeLists);
                    intent=new Intent(TravelStoreActivity.this,StoreSecondCateActivity.class);
                    intent.putExtra("store_type",storeTypeList.getStoreTypeName());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,DRIVE_REQUSR_CODE);
                }else{
                    intent=new Intent(TravelStoreActivity.this,StoreSecondCateActivity.class);
                    intent.putExtra("store_type",storeTypeList.getStoreTypeName());
                    startActivityForResult(intent,DRIVE_REQUSR_CODE);
                }
                break;
            case R.id.img_jsz://驾驶证
                if(storeList==null||changeOrLook){//添加或者修改
                    type=4;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(TravelStoreActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStoreLogoPic());
                    startActivity(intent);
                }
                break;
            case R.id.img_xsz://行驶证
                if(storeList==null||changeOrLook){//添加或者修改
                    type=5;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(TravelStoreActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStoreLogoPic());
                    startActivity(intent);
                }
                break;
            case R.id.img_cl://车辆
                if(storeList==null||changeOrLook){//添加或者修改
                    type=6;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(TravelStoreActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStoreLogoPic());
                    startActivity(intent);
                }
                break;
            case R.id.img_cp://车牌
                if(storeList==null||changeOrLook){//添加或者修改
                    type=7;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(TravelStoreActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStoreLogoPic());
                    startActivity(intent);
                }
                break;
            case R.id.img_s_alp://支付宝
                if(storeList==null||changeOrLook){//添加或者修改
                    type=2;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(TravelStoreActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStorePayPic());
                    startActivity(intent);
                }
                break;
            case R.id.img_s_wx://微信
                if(storeList==null||changeOrLook){//添加或者修改
                    type=3;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(TravelStoreActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getWeChatPic());
                    startActivity(intent);
                }
                break;
            case R.id.btn_up_sinfor://提交
                dialog=LoadingDailog.createLoadingDialog(TravelStoreActivity.this,"提交中");
                if(check()){
                    type=1;
                    //先上传图片 1--logo--2 支付宝收款码 3--微信收款码--4--上传所有信息
                    upImagePreserent.HttpUpImage(img_logo_sel,Constant.img_store_logo);
                }else{
                    LoadingDailog.closeDialog(dialog);
                }
                break;
        }
    }

    public boolean check(){
        if(img_logo_sel.equals("")){
            ToastShow("店铺图标尚未选择");
            return false;
        }else if(edit_s_name.getText().toString().length()==0){
            ToastShow("店铺名称不能为空");
            return false;
        }else if(edit_s_tel.getText().toString().length()==0){
            ToastShow("店铺电话不能为空");
            return false;
        }else if(edit_s_add.getText().toString().length()==0){
            ToastShow("店铺地址不能为空");
            return false;
        }else if(twoTypeLists.size()==0){
            ToastShow("请选择店铺类别，至少选择一种类别");
            return false;
        }else if(img_jsz_sel.equals("")){
            ToastShow("请上传驾驶证件图片");
            return false;
        }else if(img_xsz_sel.equals("")){
            ToastShow("请上传行驶证件图片");
            return false;
        }else if(img_cl_sel.equals("")){
            ToastShow("请上传车辆侧面图片");
            return false;
        }else if(img_cp_sel.equals("")){
            ToastShow("请上传车牌图片");
            return false;
        }else if(img_alp_sel.equals("")){
            ToastShow("请上传支付宝收款码");
            return false;
        }else if(img_wx_sel.equals("")){
            ToastShow("请上传微信收款码");
            return false;
        }else{//添加店铺
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==DRIVE_REQUSR_CODE){//二级分类回掉
            if(resultCode==DRIVE_RESULT_CODE){
                twoTypeLists =(List<TwoTypeList>) data.getSerializableExtra("secondtype");
                if(twoTypeLists!=null){
                    setSecondTypeView(twoTypeLists);
                }
            }
        }else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//图片返回--图标，驾驶、行驶、车牌、 车辆、微信、支付宝
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    switch (type){
                        case 1://图标
                            ImageLoading.common(TravelStoreActivity.this,images.get(0).path,img_s_logo);
                            img_logo_sel=images.get(0).path;
                            break;
                        case 2://支付宝
                            ImageLoading.common(TravelStoreActivity.this,images.get(0).path,img_s_alp);
                            img_alp_sel=images.get(0).path;
                            break;
                        case 3://微信
                            ImageLoading.common(TravelStoreActivity.this,images.get(0).path,img_s_wx);
                            img_wx_sel=images.get(0).path;
                            break;
                        case 4://驾驶证
                            ImageLoading.common(TravelStoreActivity.this,images.get(0).path,img_s_jsz);
                            img_jsz_sel=images.get(0).path;
                            break;
                        case 5://行驶证
                            ImageLoading.common(TravelStoreActivity.this,images.get(0).path,img_s_xsz);
                            img_xsz_sel=images.get(0).path;
                            break;
                        case 6://车辆
                            ImageLoading.common(TravelStoreActivity.this,images.get(0).path,img_s_cl);
                            img_cl_sel=images.get(0).path;
                            break;
                        case 7://车牌
                            ImageLoading.common(TravelStoreActivity.this,images.get(0).path,img_s_cp);
                            img_cp_sel=images.get(0).path;
                            break;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *上传图片
     * @param upImageBack
     */
    @Override
    public void getImageSucc(UpImageBack upImageBack) {
        if(type==1){//logo
            img_logo_sel=upImageBack.getImageUrl();
            type=2;
            upImagePreserent.HttpUpImage(img_alp_sel,Constant.img_pay_logo);
        }else if(type==2){
            img_alp_sel=upImageBack.getImageUrl();
            type=3;
            upImagePreserent.HttpUpImage(img_wx_sel,Constant.img_pay_logo);
        }else if(type==3){//微信
            img_wx_sel=upImageBack.getImageUrl();
            type=4;
            upImagePreserent.HttpUpImage(img_jsz_sel,Constant.img_pay_logo);
        }else if(type==4){//驾驶证
            img_jsz_sel=upImageBack.getImageUrl();
            type=5;
            upImagePreserent.HttpUpImage(img_xsz_sel,Constant.img_pay_logo);
        }else if(type==5){//行驶证
            img_xsz_sel=upImageBack.getImageUrl();
            type=6;
            upImagePreserent.HttpUpImage(img_cl_sel,Constant.img_pay_logo);
        }else if(type==6){//行驶证
            img_cl_sel=upImageBack.getImageUrl();
            type=7;
            upImagePreserent.HttpUpImage(img_cp_sel,Constant.img_pay_logo);
        }else if(type==7){//行驶证
            img_cp_sel=upImageBack.getImageUrl();
            String id= SharedPreferenceUtil.getID(TravelStoreActivity.this);
            if(changeOrLook) {//修改

            }else{//

            }
        }
    }

    @Override
    public void getImageFail(String msg) {

    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://Token获取成功

                    break;
                case 1://Token获取失败
                    LoadingDailog.closeDialog(dialog);
                    break;
                case 2://驾驶证识别成功
                    LoadingDailog.closeDialog(dialog);
                    initIDDialog(0,R.layout.dialog_jsz);
                    break;
                case 3://驾驶证识别失败--断网
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("网络无连接");
                    break;
                case 4://驾驶证识别失败--其他问题
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("驾驶证识别失败，请保证光线、证件摆放整齐、照片裁剪完全等问题");
                    break;
            }
        }
    };

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(false);//设置单选，fasle单选
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //不显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        int radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(radius * 2);
        imagePicker.setFocusHeight(radius * 2);
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }
    public static final int REQUEST_CODE_SELECT = 100;
    public void showSel(){
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 直接调起相机
                        Intent intent = new Intent(TravelStoreActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择
                        Intent intent1 = new Intent(TravelStoreActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        break;
                }
            }
        }, names);
    }

    /**
     * 显示选择dialog
     * @param listener
     * @param names
     * @return
     */
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(TravelStoreActivity.this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!TravelStoreActivity.this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    public AlertDialog dialog_exit;
    public void initIDDialog(int type,int rid){
        dialog_exit=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(rid,null);
        dialog_exit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView text_test=(TextView)view.findViewById(R.id.text_test);
        //设置date布局
        dialog_exit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_exit.setView(view);
        dialog_exit.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = dialog_exit.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog_exit.getWindow().setAttributes(layoutParams);
    }

    /**
     * 设置二级分类名
     * @param secondTypeView
     */
    public void setSecondTypeView(List<TwoTypeList> secondTypeView){
        if(list_secondtype.getChildCount()>0){
            list_secondtype.removeAllViews();
        }
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,90);
        params.setMargins(20,10,20,10);
        for (int a=0;a<secondTypeView.size();a++){
            TextView textView=new TextView(TravelStoreActivity.this);
            textView.setText(secondTypeView.get(a).getTwoTypeName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(13);
            textView.setPadding(20,10,20,10);
            textView.setMinWidth(120);
            textView.setLayoutParams(params);
            textView.setTextColor(getResources().getColor(R.color.shenhui));
            textView.setBackgroundColor(getResources().getColor(R.color.qrea_bg));
            list_secondtype.addView(textView);
        }
    }


    class ImageTurn extends Thread{
        public String url;
        public int num;
        public ImageTurn(int num,String url){
            this.url=url;
            this.num=num;
        }
        @Override
        public void run() {
            super.run();
            FutureTarget<File> future = Glide.with(TravelStoreActivity.this)
                    .load(url)
                    .downloadOnly(800, 800);
            try {
                File cacheFile = future.get();
                String path = cacheFile.getAbsolutePath();
                switch (num){
                    case 1://图标
                        img_logo_sel=path;
                        break;
                    case 2://支付宝
                        img_alp_sel=path;
                        break;
                    case 3://微信
                        img_wx_sel=path;
                        break;
                    case 4://驾驶证
                        img_jsz_sel=path;
                        break;
                    case 5://行驶证
                        img_xsz_sel=path;
                        break;
                    case 6://车辆
                        img_cl_sel=path;
                        break;
                    case 7://车牌
                        img_cp_sel=path;
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
