package wizrole.hosmerchants.release.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.upimage.UpImageBack;
import wizrole.hosmerchants.my.preserent.upimage.UpImageInterface;
import wizrole.hosmerchants.my.preserent.upimage.UpImagePreserent;
import wizrole.hosmerchants.my.view.MyStoreListActivity;
import wizrole.hosmerchants.release.model.releasegoods.ReleaseGoodsBack;
import wizrole.hosmerchants.release.preserent.releasegoods.ReleaseGoodsInterface;
import wizrole.hosmerchants.release.preserent.releasegoods.ReleaseGoodsPreserent;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.dialog.SelectDialog;
import wizrole.hosmerchants.util.image.GlideImageLoader;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2017/11/27/027.
 * 何人执笔？
 * 发布商品页面
 */

public class ReleaseGoodsActivity extends BaseActivity implements View.OnClickListener,UpImageInterface ,ReleaseGoodsInterface{
    @BindView(R.id.lin_back)LinearLayout lin_back;//返回
    @BindView(R.id.lin_release_type)LinearLayout lin_release_type;//分类点击
    @BindView(R.id.lin_store_name)LinearLayout lin_store_name;//店铺名
    @BindView(R.id.text_relesae_type)TextView text_relesae_type;//分类名
    @BindView(R.id.text_store_name)TextView text_store_name;//店铺名
    @BindView(R.id.text_title)TextView text_title;//标题栏
    @BindView(R.id.text_relesae_name)EditText text_relesae_name;//商品名
    @BindView(R.id.edit_release_scripe)EditText edit_release_scripe;//商品描述
    @BindView(R.id.edit_release_price)EditText edit_release_price;//商品价格
    @BindView(R.id.img_release_logo)ImageView img_release_logo;//商品图标
    @BindView(R.id.btn_release)Button btn_release;//商品图标
    public static final int REQUEST_CODE_SELECT = 100;//相机-照片回调
    public static final int REQUEST_CODE_SELECT_STORE = 200;//店铺名
    public static final int RESULT_CODE_SELECT_STORE = 201;//店铺名
    public static final int TYPE_REQUEST_CODE=1000;//商品分类requesCode
    public static final int TYPE_RESULT_CODE=1001;//商品分类resultCod
    private ImagePicker imagePicker;
    public Intent intent;
    public Dialog dialog;
    public String StoreId;//所属店铺id
    public String StoreName;//所属店铺名称
    public  String CateId;//所属分类id
    public String CateName;//所属分类名
    public String img_goods="";//商品图片
    public UpImagePreserent upImagePreserent=new UpImagePreserent(this);
    public ReleaseGoodsPreserent releaseGoodsPreserent=new ReleaseGoodsPreserent(this);
    @Override
    protected int getLayout() {
        return R.layout.activity_releasegoods;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_releasegoods);
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("发布商品");
        edit_release_price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        initImagePicker();
    }

    @Override
    protected void setListener() {
        img_release_logo.setOnClickListener(this);
        btn_release.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        lin_release_type.setOnClickListener(this);
        lin_store_name.setOnClickListener(this);
        /**
         * 设置只能输入保留两位小数
         */
        edit_release_price.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2){
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_store_name://选择店铺
                intent=new Intent(ReleaseGoodsActivity.this, MyStoreListActivity.class);
                intent.putExtra("type","release");
                startActivityForResult(intent,REQUEST_CODE_SELECT_STORE);
                break;
            case R.id.lin_release_type://选择分类
                if(text_store_name.getText().toString().equals("请选择所属店铺")){
                    ToastShow("请先选择所属店铺");
                }else{
                    intent=new Intent(ReleaseGoodsActivity.this,GoodsTypeActivity.class);
                    intent.putExtra("StoreId",StoreId);
                    intent.putExtra("type","releaseType");
                    startActivityForResult(intent,TYPE_REQUEST_CODE);
                }
                break;
            case R.id.img_release_logo://商品图标
                if(imagePicker==null){
                    initImagePicker();
                }
                showSel();
                break;
            case R.id.lin_back://返回
                if(exit_dialog==null){
                    initDialog();
                }else{
                    exit_dialog.show();
                }
                break;
            case R.id.btn_release://发布
                dialog=LoadingDailog.createLoadingDialog(ReleaseGoodsActivity.this,"发布中");
                if(check()){
                    upImagePreserent.HttpUpImage(img_goods, Constant.img_goods_infor);
                }else{
                    LoadingDailog.closeDialog(dialog);
                }
                break;
        }
    }

    /**
     * 上传图片回调
     * @param upImageBack
     */
    @Override
    public void getImageSucc(UpImageBack upImageBack) {
        String img_url=upImageBack.getImageUrl();
        releaseGoodsPreserent.releaseGoods(text_relesae_name.getText().toString()
            ,img_url//图片地址
            ,edit_release_scripe.getText().toString()
            ,edit_release_price.getText().toString()
            ,StoreId
            ,CateId);
    }

    @Override
    public void getImageFail(String msg) {
        handler.sendEmptyMessage(0);
    }

    /**
     * 发布商品回调
     * @param releaseGoodsBack
     */
    @Override
    public void ReleaseGoodsSucc(ReleaseGoodsBack releaseGoodsBack) {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void ReleaseGoodsFail(String msg) {
        handler.sendEmptyMessage(1);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://图片上传失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("商品发布失败，请检查网络");
                    break;
                case 1://发布失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("商品发布失败，请检查网络");
                    break;
                case 2://发布成功
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("商品发布成功");
                    finish();
                    break;
            }
        }
    };
    public boolean check(){
        if(text_store_name.getText().toString().equals("请选择所属店铺")){
            ToastShow("请选择所属店铺");
            return false;
        }else if(text_relesae_type.getText().toString().equals("请选择商品类别")){
            ToastShow("请选择商品类别");
            return false;
        }else if(text_relesae_name.getText().toString().length()==0){
            ToastShow("商品名称不能为空");
            return false;
        }else if(edit_release_scripe.getText().toString().length()==0){
            ToastShow("商品描述不能为空");
            return false;
        }else if(img_goods.equals("")) {
            ToastShow("请选择商品图片");
            return false;
        }else if(edit_release_price.getText().toString().length()==0){
            ToastShow("商品价格不能为空");
            return false;
        }else{
            return true;
        }
    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(false);//设置单选，fasle单选
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //不显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }


    private ArrayList<ImageItem> images = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    ImageLoading.common(ReleaseGoodsActivity.this,images.get(0).path,img_release_logo);
                    img_goods=images.get(0).path;//选定图片缓存路径
                }
            }
        }else if(requestCode==REQUEST_CODE_SELECT_STORE&&resultCode==RESULT_CODE_SELECT_STORE){//店铺名
             StoreId=data.getStringExtra("StoreId");
             StoreName=data.getStringExtra("StoreName");
            if (StoreId.equals("")){//未选中
                text_store_name.setText("请选择所属店铺");
            }else{//已选中店铺id
                text_store_name.setText(StoreName);
            }
        }else if(requestCode==TYPE_REQUEST_CODE&&resultCode==TYPE_RESULT_CODE){//商品分类
            CateId=data.getStringExtra("CateId");
            CateName=data.getStringExtra("CateName");
            text_relesae_type.setText(CateName);
        }
    }

    public void showSel(){
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 直接调起相机
                        Intent intent = new Intent(ReleaseGoodsActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择
                        Intent intent1 = new Intent(ReleaseGoodsActivity.this, ImageGridActivity.class);
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
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    public AlertDialog exit_dialog;
    public void initDialog(){
        exit_dialog=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_exits,null);
        exit_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView text_cancle=(TextView)view.findViewById(R.id.text_cancle);
        TextView text_sure=(TextView)view.findViewById(R.id.text_sure);
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit_dialog.dismiss();
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit_dialog.dismiss();
                finish();
            }
        });//确定注销监听
        //设置date布局
        exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exit_dialog.setView(view);
        exit_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = exit_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        exit_dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onBackPressed() {
        if(exit_dialog==null){
            initDialog();
        }else{
            exit_dialog.show();
        }
    }
}
