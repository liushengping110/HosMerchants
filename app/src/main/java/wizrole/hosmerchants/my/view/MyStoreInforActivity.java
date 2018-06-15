package wizrole.hosmerchants.my.view;

import android.app.Activity;
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
import com.baidu.ocr.ui.camera.CameraActivity;
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
import wizrole.hosmerchants.my.model.Businelicense.BusineLicenBack;
import wizrole.hosmerchants.my.model.addstore.AddStoreBack;
import wizrole.hosmerchants.my.model.changestoreinfor.ChangeStoreInforBack;
import wizrole.hosmerchants.my.model.idtoken.TokenBack;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.my.model.storecate.StoreTypeList;
import wizrole.hosmerchants.my.model.upimage.UpImageBack;
import wizrole.hosmerchants.my.preserent.addstore.AddStoreInterface;
import wizrole.hosmerchants.my.preserent.addstore.AddStorePreserent;
import wizrole.hosmerchants.my.preserent.businelicense.BusinLincePreserent;
import wizrole.hosmerchants.my.preserent.businelicense.BusineLicenInterface;
import wizrole.hosmerchants.my.preserent.changestoreinfor.ChangeStoreInforInterface;
import wizrole.hosmerchants.my.preserent.changestoreinfor.ChangeStoreInforPreserent;
import wizrole.hosmerchants.my.preserent.idtoken.IDTokenInterface;
import wizrole.hosmerchants.my.preserent.idtoken.IDTokenPreserent;
import wizrole.hosmerchants.my.preserent.upimage.UpImageInterface;
import wizrole.hosmerchants.my.preserent.upimage.UpImagePreserent;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.dialog.SelectDialog;
import wizrole.hosmerchants.util.image.GlideImageLoader;
import wizrole.hosmerchants.util.image.ImageLoading;
import wizrole.hosmerchants.view.CustTextView;

/**
 * Created by liushengping on 2017/12/13/013.
 * 何人执笔？
 * 我的店铺资料
 */

public class MyStoreInforActivity extends BaseActivity implements View.OnClickListener,IDTokenInterface,BusineLicenInterface
,AddStoreInterface,UpImageInterface,ChangeStoreInforInterface{
    @BindView(R.id.text_s_two_cate)TextView text_s_two_cate;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.text_s_cate)TextView text_s_cate;
    @BindView(R.id.text_s_look)TextView text_s_look;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.img_s_logo)ImageView img_s_logo;
    @BindView(R.id.img_s_zz)ImageView img_s_zz;
    @BindView(R.id.img_s_alp)ImageView img_s_alp;
    @BindView(R.id.img_s_wx)ImageView img_s_wx;
    @BindView(R.id.edit_s_name)EditText edit_s_name;
    @BindView(R.id.edit_s_tel)EditText edit_s_tel;
    @BindView(R.id.edit_s_add)EditText edit_s_add;
    @BindView(R.id.btn_up_sinfor)Button btn_up_sinfor;
    @BindView(R.id.list_secondtype)CustTextView list_secondtype;
    public Dialog dialog;
    public StoreList storeList;
    public List<TwoTypeList> twoTypeLists=new ArrayList<>();//选中的二级分类
    public static final int DRIVE_REQUSR_CODE=6666;//二级分类
    public static final int DRIVE_RESULT_CODE=6667;//二级分类
    public StoreTypeList storeTypeList;//店铺的一级分类
    private ImagePicker imagePicker;
    public static final int REQUEST_CODE_SELECT = 100;
    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;//营业执照拍照请求吗
    public static final int REQUEST_STORECATE_CODE=200;//店铺类别选择请求吗
    public static final int RESULR_STORECATE_CODE=201;//店铺类别选择结果吗
    private ArrayList<ImageItem> images = null;
    public String img_logo_sel="";//店铺logo选择好的图片缓存路径
    public String img_alp_sel="";//支付宝选择好的图片缓存路径
    public String img_wx_sel="";//微信选择好的图片缓存路径
    public int type=0;//店铺logo  --1，支付宝--2，微信--3
    public String path;//营业执照缓存地址
    public String type_name;//店铺类型名
    public String type_id;//店铺类型id
    public IDTokenPreserent idTokenPreserent=new IDTokenPreserent(this);//获取营业执照token
    public BusinLincePreserent businLincePreserent=new BusinLincePreserent(this);//获取营业执照详细信息
    public AddStorePreserent addStorePreserent=new AddStorePreserent(this);//添加店铺
    public UpImagePreserent upImagePreserent=new UpImagePreserent(this);//上传图片
    public ChangeStoreInforPreserent changeStoreInforPreserent=new ChangeStoreInforPreserent(this);
    public boolean changeOrLook=false;//是否是修改状态下
    @Override
    protected int getLayout() {
        return R.layout.activity_mystoreinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        storeList=(StoreList) getIntent().getSerializableExtra("storeinfor");
        storeTypeList=(StoreTypeList) getIntent().getSerializableExtra("storeTypeList");
        initUI(storeList);
    }
    public void initUI(StoreList s){
        if (s==null){//添加
            text_title.setText("添加店铺");
            text_s_cate.setText(storeTypeList.getStoreTypeName());
        }else {//查看或修改
            text_title.setText("店铺资料");
            text_right.setText("修改");
            text_s_look.setText("点击查看营业执照");
            text_s_cate.setText(storeList.getStoreType());
            setView(s);
            new ImageTurn(1,Constant.ip+storeList.getStoreLogoPic()).start();
        }
    }

    /**
     * 开始获取token
     */
    public void getIDToken(){
        dialog=LoadingDailog.createLoadingDialog(MyStoreInforActivity.this,"加载中");
        idTokenPreserent.getIDToken();
    }

    /**
     * 获取营业执照信息
     * @param token--token
     * @param filepath---图片缓存地址
     */
    public void getBusinLicensInfor(String token,String filepath){
        businLincePreserent.getBusinLicenInfor(token,filepath);
    }


    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        btn_up_sinfor.setOnClickListener(this);
        img_s_logo.setOnClickListener(this);
        img_s_alp.setOnClickListener(this);
        img_s_wx.setOnClickListener(this);
        img_s_zz.setOnClickListener(this);
        text_s_two_cate.setOnClickListener(this);
        text_right.setOnClickListener(this);
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                if(changeOrLook){//修改店铺
                    initExitDialog();
                }else if(storeList==null){//添加店铺
                    initExitDialog();
                }else{
                    finish();
                }
                break;
            case R.id.text_right://修改
                ToastShow("现在您可以修改您的店铺信息");
                changeOrLook=true;
                bl_status=true;//设置营业执照已扫描。
                setView(null);
                break;
            case R.id.img_s_logo://店铺logo
                if(storeList==null||changeOrLook){//添加店铺
                    type=1;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{//预览或修改店铺
                    Intent intent=new Intent(MyStoreInforActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStoreLogoPic());
                    startActivity(intent);
                }
                break;
            case R.id.text_s_two_cate://店铺对应下的二级分类选择
                if(twoTypeLists.size()>0){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("sel_secondType",(Serializable) twoTypeLists);
                    intent=new Intent(MyStoreInforActivity.this,StoreSecondCateActivity.class);
                    if(storeTypeList!=null){
                        intent.putExtra("store_type",storeTypeList.getStoreTypeName());//传入一级分类名，获取对应的二级分类名称
                    }else{
                        intent.putExtra("store_type",storeList.getStoreType());//传入一级分类名，获取对应的二级分类名称
                    }
                    intent.putExtras(bundle);
                    startActivityForResult(intent,DRIVE_REQUSR_CODE);
                }else{
                    intent=new Intent(MyStoreInforActivity.this,StoreSecondCateActivity.class);
                    if(storeTypeList!=null){
                        intent.putExtra("store_type",storeTypeList.getStoreTypeName());
                    }else {
                        intent.putExtra("store_type", storeList.getStoreType());
                    }
                    startActivityForResult(intent,DRIVE_REQUSR_CODE);
                }
                break;
            case R.id.img_s_alp://支付宝收款
                if(storeList==null||changeOrLook) {//添加店铺
                    type=2;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(MyStoreInforActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getStorePayPic());
                    startActivity(intent);
                }
                break;
            case R.id.img_s_wx://微信收款
                if(storeList==null||changeOrLook) {//添加店铺
                    type=3;
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Intent intent=new Intent(MyStoreInforActivity.this, ImageLookActivity.class);
                    intent.putExtra("url",Constant.ip+storeList.getWeChatPic());
                    startActivity(intent);
                }
                break;
            case R.id.img_s_zz://营业执照
                if(storeList==null||changeOrLook){//如果是添加店铺或者修改店铺
                    intent = new Intent(MyStoreInforActivity.this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, getSaveFile(getApplication()).getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                    startActivityForResult(intent, REQUEST_CODE_BUSINESS_LICENSE);
                }else{//否则是查看
                    initDialog(2,R.layout.dialog_buinlicen,storeList);
                }
                break;
            case R.id.text_s_cate://店铺类别
                if(text_s_cate.getText().equals("请选择店铺类别")){
                    intent=new Intent(MyStoreInforActivity.this,StoreCateActivity.class);
                    startActivityForResult(intent,REQUEST_STORECATE_CODE);
                }else{
                    intent=new Intent(MyStoreInforActivity.this,StoreCateActivity.class);
                    intent.putExtra("type",text_s_cate.getText().toString());
                    startActivityForResult(intent,REQUEST_STORECATE_CODE);
                }
                break;
            case R.id.btn_up_sinfor://添加保存
                dialog=LoadingDailog.createLoadingDialog(MyStoreInforActivity.this,"提交中");
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


    public TokenBack  back;
    public BusineLicenBack busineLice;
    /**
     * 获取营业执照Token
     * @param tokenBack
     */
    @Override
    public void getTokenSucc(TokenBack tokenBack) {
        back=tokenBack;
        handler.sendEmptyMessage(0);
    }
    @Override
    public void getTokenFail(String msg) {
        handler.sendEmptyMessage(1);
    }
    /**
     * 获取营业执照详细信息
     * @param busineLicenBack
     */
    @Override
    public void getBuinLicenSucc(BusineLicenBack busineLicenBack) {
        busineLice=busineLicenBack;
        handler.sendEmptyMessage(2);
    }

    @Override
    public void getBuinLicenFail(String msg) {
        handler.sendEmptyMessage(3);
    }

    /**
     * 上传图片
     * @param upImageBack
     */
    @Override
    public void getImageSucc(UpImageBack upImageBack) {
        if(type==1){//logo
            img_logo_sel=upImageBack.getImageUrl();
            type=2;
            //先上传图片 1--logo--2 支付宝收款码 3--微信收款码--4--上传所有信息
            upImagePreserent.HttpUpImage(img_alp_sel,Constant.img_pay_logo);
        }else if(type==2){
            img_alp_sel=upImageBack.getImageUrl();
            type=3;
            //先上传图片 1--logo--2 支付宝收款码 3--微信收款码--4--上传所有信息
            upImagePreserent.HttpUpImage(img_wx_sel,Constant.img_pay_logo);
        }else if(type==3){
            img_wx_sel=upImageBack.getImageUrl();
            String id=SharedPreferenceUtil.getID(MyStoreInforActivity.this);
            if(changeOrLook){//修改
                if(busineLice==null){//如果营业执照没修改了
                    changeStoreInforPreserent.changeStoreInfor(
                            storeList.getStoreNo()
                            ,edit_s_name.getText().toString()
                            ,edit_s_add.getText().toString()
                            ,edit_s_tel.getText().toString()
                            ,text_s_cate.getText().toString()
                            ,img_logo_sel
                            ,img_alp_sel
                            ,img_wx_sel
                            ,id
                            ,storeList.getCompanyName()
                            ,storeList.getLegalPerson()
                            ,storeList.getCompanyAddress()
                            ,storeList.getLicenseValidity()
                            ,storeList.getLicenseNo()
                            ,storeList.getCreditCode()
                    );
                }else{//如果营业执照修改了
                    changeStoreInforPreserent.changeStoreInfor(
                            storeList.getStoreNo()
                            ,edit_s_name.getText().toString()
                            ,edit_s_add.getText().toString()
                            ,edit_s_tel.getText().toString()
                            ,text_s_cate.getText().toString()
                            ,img_logo_sel
                            ,img_alp_sel
                            ,img_wx_sel
                            ,id
                            ,busineLice.getWords_result().getEntityName().getWords()
                            ,busineLice.getWords_result().getLegalPersion().getWords()
                            ,busineLice.getWords_result().getAddress().getWords()
                            ,busineLice.getWords_result().getValidityPeriod().getWords()
                            ,busineLice.getWords_result().getIdNumber().getWords()
                            ,busineLice.getWords_result().getCreditCode().getWords()
                    );
                }
            }else{//添加
                addStorePreserent.AddStoreHttp(
                        edit_s_name.getText().toString()
                        ,edit_s_add.getText().toString()
                        ,edit_s_tel.getText().toString()
                        ,text_s_cate.getText().toString()
                        ,img_logo_sel
                        ,img_alp_sel
                        ,img_wx_sel
                        ,id
                        ,busineLice.getWords_result().getEntityName().getWords()
                        ,busineLice.getWords_result().getLegalPersion().getWords()
                        ,busineLice.getWords_result().getAddress().getWords()
                        ,busineLice.getWords_result().getValidityPeriod().getWords()
                        ,busineLice.getWords_result().getIdNumber().getWords()
                        ,busineLice.getWords_result().getCreditCode().getWords());
            }
        }
    }


    @Override
    public void getImageFail(String msg) {
        handler.sendEmptyMessage(6);
    }

    /**
     * 添加店铺
     * @param addStoreBack
     */
    @Override
    public void getAddStoreSucc(AddStoreBack addStoreBack) {
        SharedPreferenceUtil.saveInforComplete(MyStoreInforActivity.this,addStoreBack.getInfoSign());
        handler.sendEmptyMessage(4);
    }

    @Override
    public void getAddStoreFail(String msg) {
        handler.sendEmptyMessage(5);
    }

    /**
     * 修改店铺信息--调用014接口
     * @param changeStoreInforBack
     */

    @Override
    public void getChangeStoreSucc(ChangeStoreInforBack changeStoreInforBack) {
        handler.sendEmptyMessage(7);
    }

    @Override
    public void getChangeStoreFail(String msg) {
        handler.sendEmptyMessage(8);
    }



    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://Token获取成功
                    String token=back.getAccess_token();
                    getBusinLicensInfor(token,path);
                    break;
                case 1://Token获取失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("营业执照获取失败，请检查网络");
                    break;
                case 2://营业执照详细信息获取成功
                    LoadingDailog.closeDialog(dialog);
                    text_s_look.setText("营业执照已上传");
                    initDialog(1,R.layout.dialog_buinlicen,busineLice);
                    break;
                case 3://营业执照详细信息获取失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("营业执照获取失败，请检查网络");
                    break;
                case 4://店铺添加成功(修改)
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("店铺添加成功");
                    setResult(2);
                    finish();
                    break;
                case 5://店铺添加失败（修改）
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("店铺添加失败，请检查网络");
                    break;
                case 6://图片上传发生错误
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("网络连接失败，请检查网络");
                    break;
                case 7:
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("店铺信息修改成功");
                    setResult(2);
                    finish();
                    break;
                case 8:
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("店铺信息修改失败，请检查网络");
                    break;
                case 9://如果修改信息，但是不改变之前选定的图片，就先获取图片的缓存路径转base64
                    if ((int)msg.obj==1){
                        new ImageTurn(2,Constant.ip+storeList.getStorePayPic()).start();
                    }else{
                        new ImageTurn(3,Constant.ip+storeList.getWeChatPic()).start();
                    }
                    break;
            }
        }
    };


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
        }else if(text_s_cate.getText().toString().equals("请选择店铺类别")){
            ToastShow("请选择店铺类别");
            return false;
        }else if(!bl_status){
            ToastShow("请上传营业执照");
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

    public boolean bl_status=false;//是否扫描好营业执照
    public AlertDialog bl_dialog;
    public void initDialog(final int type, int rid, Object object) {
        bl_dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(rid, null);
        bl_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView dialog_bl_add=(TextView)view.findViewById(R.id.dialog_bl_add);
        TextView dialog_bl_fr=(TextView)view.findViewById(R.id.dialog_bl_fr);
        TextView dialog_bl_name=(TextView)view.findViewById(R.id.dialog_bl_name);
        TextView dialog_bl_yxq=(TextView)view.findViewById(R.id.dialog_bl_yxq);
        TextView dialog_bl_no=(TextView)view.findViewById(R.id.dialog_bl_no);
        TextView dialog_bl_crecode=(TextView)view.findViewById(R.id.dialog_bl_crecode);
        TextView dialog_again=(TextView)view.findViewById(R.id.dialog_again);
        TextView dialog_sure=(TextView)view.findViewById(R.id.dialog_sure);
        TextView dialog_only_sure=(TextView)view.findViewById(R.id.dialog_only_sure);
        View dialog_view_bottom = (View) view.findViewById(R.id.dialog_view_bottom);
        switch (type){
            case 1://营业执照请求回调展示信息
                BusineLicenBack back=(BusineLicenBack) object;
                dialog_only_sure.setVisibility(View.GONE);
                dialog_again.setVisibility(View.VISIBLE);
                dialog_view_bottom.setVisibility(View.VISIBLE);
                dialog_sure.setVisibility(View.VISIBLE);
                dialog_bl_name.setText(back.getWords_result().getEntityName().getWords());
                dialog_bl_add.setText(back.getWords_result().getAddress().getWords());
                dialog_bl_crecode.setText(back.getWords_result().getCreditCode().getWords());
                dialog_bl_fr.setText(back.getWords_result().getLegalPersion().getWords());
                dialog_bl_no.setText(back.getWords_result().getIdNumber().getWords());
                dialog_bl_yxq.setText(back.getWords_result().getValidityPeriod().getWords());
                break;
            case 2://查看
                dialog_only_sure.setVisibility(View.VISIBLE);
                dialog_again.setVisibility(View.GONE);
                dialog_sure.setVisibility(View.GONE);
                dialog_view_bottom.setVisibility(View.GONE);
                StoreList storeList=(StoreList) object;
                dialog_bl_name.setText(storeList.getCompanyName());
                dialog_bl_add.setText(storeList.getCompanyAddress());
                dialog_bl_crecode.setText(storeList.getCreditCode());
                dialog_bl_fr.setText(storeList.getLegalPerson());
                dialog_bl_no.setText(storeList.getLicenseNo());
                dialog_bl_yxq.setText(storeList.getLicenseValidity());
                break;
        }
        dialog_only_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bl_dialog.dismiss();
            }
        });
        dialog_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==1){
                    bl_status=false;
                }
                bl_dialog.dismiss();
            }
        });
        dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==1){
                    bl_status=true;
                }
                bl_dialog.dismiss();
            }
        });
        bl_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bl_dialog.setView(view);
        bl_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = bl_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        bl_dialog.getWindow().setAttributes(layoutParams);
    }

    public AlertDialog dialog_exit;

    /**
     * 退出dialog
     */
    public void initExitDialog(){
        dialog_exit=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_exits,null);
        dialog_exit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView text_cancle=(TextView)view.findViewById(R.id.text_cancle);
        TextView text_message=(TextView)view.findViewById(R.id.text_message);
        TextView text_sure=(TextView)view.findViewById(R.id.text_sure);
        text_message.setText("店铺信息尚未保存，是否退出？");
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.dismiss();
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.dismiss();
                finish();
            }
        });//确定注销监听
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
     * 获取商铺信息成功后设置数据
     */
    public void setView(StoreList  s){
        if(s==null){//修改店铺
            edit_s_name.setEnabled(true);
            edit_s_tel.setEnabled(true);
            edit_s_add.setEnabled(true);
            text_s_cate.setEnabled(true);
            text_s_look.setEnabled(true);
            text_s_look.setText("点此修改营业执照");
            btn_up_sinfor.setEnabled(true);
            btn_up_sinfor.setBackground(getResources().getDrawable(R.drawable.login_sel));
            edit_s_name.setTextColor(getResources().getColor(R.color.huise));
            edit_s_tel.setTextColor(getResources().getColor(R.color.huise));
            edit_s_add.setTextColor(getResources().getColor(R.color.huise));
            text_s_cate.setTextColor(getResources().getColor(R.color.huise));
        }else{//查看店铺
            ImageLoading.common(MyStoreInforActivity.this,Constant.ip+s.getStoreLogoPic(),img_s_logo,R.drawable.release_add);
            edit_s_name.setText(s.getStoreName());
            edit_s_tel.setText(s.getStorePhone());
            edit_s_add.setText(s.getStorePlace());
            text_s_cate.setText(s.getStoreType());
            text_s_look.setText("点此查看营业执照详细信息");
            ImageLoading.common(MyStoreInforActivity.this,Constant.ip+s.getStorePayPic(),img_s_alp,R.drawable.release_add);
            ImageLoading.common(MyStoreInforActivity.this,Constant.ip+s.getWeChatPic(),img_s_wx,R.drawable.release_add);
            edit_s_name.setEnabled(false);
            edit_s_tel.setEnabled(false);
            edit_s_add.setEnabled(false);
            text_s_cate.setEnabled(false);
            btn_up_sinfor.setEnabled(false);
            btn_up_sinfor.setBackgroundColor(getResources().getColor(R.color.huise));
            edit_s_name.setTextColor(getResources().getColor(R.color.text_fz));
            edit_s_tel.setTextColor(getResources().getColor(R.color.text_fz));
            edit_s_add.setTextColor(getResources().getColor(R.color.text_fz));
            text_s_cate.setTextColor(getResources().getColor(R.color.text_fz));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==DRIVE_REQUSR_CODE){//二级分类回掉
            if(resultCode==DRIVE_RESULT_CODE){
                twoTypeLists =(List<TwoTypeList>) data.getSerializableExtra("secondtype");
                if(twoTypeLists!=null){
                    setSecondTypeView(twoTypeLists);
                }
            }
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//头像修改
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                        switch (type){
                            case 1:
                                ImageLoading.common(MyStoreInforActivity.this,images.get(0).path,img_s_logo);
                                img_logo_sel=images.get(0).path;
                                break;
                            case 2:
                                ImageLoading.common(MyStoreInforActivity.this,images.get(0).path,img_s_alp);
                                img_alp_sel=images.get(0).path;
                                break;
                            case 3:
                                ImageLoading.common(MyStoreInforActivity.this,images.get(0).path,img_s_wx);
                                img_wx_sel=images.get(0).path;
                                break;
                        }
                }
            }
        }else if (requestCode == REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {//营业执照
            path = getSaveFile(getApplicationContext()).getAbsolutePath();
            getIDToken();
        }else if(requestCode==REQUEST_STORECATE_CODE&&resultCode==RESULR_STORECATE_CODE){//店铺类别回调
            type_name=data.getStringExtra("typeName");
            type_id=data.getStringExtra("typeId");
            if(type_name!=null&&type_id!=null){
                text_s_cate.setText(type_name);
            }
        }
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
            TextView textView=new TextView(MyStoreInforActivity.this);
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

    public void showSel(){
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 直接调起相机
                        Intent intent = new Intent(MyStoreInforActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择
                        Intent intent1 = new Intent(MyStoreInforActivity.this, ImageGridActivity.class);
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
        SelectDialog dialog = new SelectDialog(MyStoreInforActivity.this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!MyStoreInforActivity.this.isFinishing()) {
            dialog.show();
        }
        return dialog;
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
            FutureTarget<File> future = Glide.with(MyStoreInforActivity.this)
                    .load(url)
                    .downloadOnly(800, 800);
            try {
                File cacheFile = future.get();
                String path = cacheFile.getAbsolutePath();
                switch (num){
                    case 1:
                        img_logo_sel=path;
                        handler.obtainMessage(9,num).sendToTarget();
                        break;
                    case 2:
                        img_alp_sel=path;
                        handler.obtainMessage(9,num).sendToTarget();
                        break;
                    case 3:
                        img_wx_sel=path;
                        handler.obtainMessage(9,num).sendToTarget();
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    public  File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "yyzz.jpg");
        return file;
    }

    @Override
    public void onBackPressed() {
        if(changeOrLook){//修改店铺
            initExitDialog();
        }else if(storeList==null){//添加店铺
            initExitDialog();
        }else{
            finish();
        }
    }
}
