package wizrole.hosmerchants.my.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.changeinfor.ChangeInforBack;
import wizrole.hosmerchants.my.model.login.LoginBack;
import wizrole.hosmerchants.my.model.upimage.UpImageBack;
import wizrole.hosmerchants.my.preserent.changeinfor.ChangeInforInterface;
import wizrole.hosmerchants.my.preserent.changeinfor.ChangeInforPreserent;
import wizrole.hosmerchants.my.preserent.upimage.UpImageInterface;
import wizrole.hosmerchants.my.preserent.upimage.UpImagePreserent;
import wizrole.hosmerchants.util.MD5Util;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.dialog.SelectDialog;
import wizrole.hosmerchants.util.image.GlideImageLoader;
import wizrole.hosmerchants.util.image.ImageLoading;
import wizrole.hosmerchants.util.image.ImageTrun;

/**
 * Created by liushengping on 2017/11/29/029.
 * 何人执笔？
 */

public class Fragment_My extends Fragment implements View.OnClickListener,ChangeInforInterface,UpImageInterface{

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public View  view;
    public PullToZoomScrollViewEx scrollView;
    public final int REQUERS_CODE=1;//登录回调
    public final int RESULT_CODE=2;
    public final int MYINFOR_REQUEST_CODE=11;//个人资料完善回调
    public final int MYINFOR_RESULT_CODE=10;
    public final int MYSTOREINFOR_REQUEST_CODE=12;//我的店铺完善回调
    public final int MYSTOREINFOR_RESULT_CODE=13;
    public  TextView text_login,text_frag_store_add,text_frag_infor_add;
    public LinearLayout frag_my_infor,frag_my_store,frag_my_collect,frag_my_setting;
    public ImageView iv_zoom,img_header;
    private ImagePicker imagePicker;
    public static final int REQUEST_CODE_SELECT = 100;
    public ChangeInforPreserent preserent=new ChangeInforPreserent(this);
    public UpImagePreserent imagePreserent=new UpImagePreserent(this);
    public Dialog dailog;
    private ArrayList<ImageItem> images = null;
    public String img;
    public Intent intent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_my,null);
            loadViewForCode();
            initUI();
            setListener();
            isCreateView = true;
        }
        return view;
    }

    //此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if(!isLoadData){
            //加载数据操作
            String msg="";
            isLoadData=true;
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint())
            lazyLoad();
    }


    private void loadViewForCode() {
        scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_head_view, null, false);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    public void initUI(){
        scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view);
        text_login= (TextView)scrollView.getPullRootView().findViewById(R.id.tv_user_name);
        text_frag_store_add= (TextView)scrollView.getPullRootView().findViewById(R.id.text_frag_store_add);
        text_frag_infor_add= (TextView)scrollView.getPullRootView().findViewById(R.id.text_frag_infor_add);
        img_header= (ImageView) scrollView.getPullRootView().findViewById(R.id.iv_user_head);
        iv_zoom= (ImageView)scrollView.getPullRootView().findViewById(R.id.iv_zoom);
        frag_my_infor= (LinearLayout)scrollView.getPullRootView().findViewById(R.id.frag_my_infor);
        frag_my_store= (LinearLayout)scrollView.getPullRootView().findViewById(R.id.frag_my_store);
        frag_my_collect= (LinearLayout)scrollView.getPullRootView().findViewById(R.id.frag_my_collect);
        frag_my_setting= (LinearLayout)scrollView.getPullRootView().findViewById(R.id.frag_my_setting);
        if(SharedPreferenceUtil.getLoginState(getActivity())==1){
            String name = SharedPreferenceUtil.getUserName(getActivity());
            text_login.setText(name);
            //设置头像和背景
            String headerImg=SharedPreferenceUtil.getHeaderImg(getActivity());
            if(headerImg.equals("")){//登录未设置头像
                ImageLoading.commonRound(getActivity(),R.drawable.img_header,img_header);
                ImageLoading.commonBlurTeans(getActivity(),R.drawable.header_bg,iv_zoom);
            }else{
                ImageLoading.commonRound(getActivity(),headerImg,img_header);
                ImageLoading.commonBlurTeans(getActivity(),headerImg,iv_zoom);
            }
        }else {
            text_login.setText("登录");
            ImageLoading.commonRound(getActivity(),R.drawable.img_header,img_header);
            ImageLoading.commonBlurTeans(getActivity(),R.drawable.header_bg,iv_zoom);
        }
        //设置提示状态
        String status=SharedPreferenceUtil.getInforComplete(getActivity());
        MyOrStoreInfor(status);
    }

    public void setListener(){
        text_login.setOnClickListener(this);
        img_header.setOnClickListener(this);
        frag_my_store.setOnClickListener(this);
        frag_my_infor.setOnClickListener(this);
        frag_my_setting.setOnClickListener(this);
        frag_my_collect.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUERS_CODE){
            if(resultCode==RESULT_CODE){
                String name=data.getStringExtra("name");
                String image=data.getStringExtra("image");
                String infoSign=data.getStringExtra("infoSign");
                text_login.setText(name);
                ImageLoading.commonRound(getActivity(),Constant.ip+image,img_header);
                ImageLoading.commonBlurTeans(getActivity(),Constant.ip+image,iv_zoom);
                MyOrStoreInfor(infoSign);//是否显示提示完善信息文本
            }
        }else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//头像修改
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    dailog=LoadingDailog.createLoadingDialog(getActivity(),"头像更新中");
                    imagePreserent.HttpUpImage(images.get(0).path, Constant.img_header);
                }
            }
        }else if(requestCode==MYINFOR_REQUEST_CODE){//我的资料完善回调
            if(resultCode==MYINFOR_RESULT_CODE){
                MyOrStoreInfor(data.getStringExtra("status"));
                text_login.setText(data.getStringExtra("name"));
            }
        }else if(requestCode==MYSTOREINFOR_REQUEST_CODE){//店铺资料完善回调
            if(resultCode==MYSTOREINFOR_RESULT_CODE){
                MyOrStoreInfor(SharedPreferenceUtil.getInforComplete(getActivity()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_user_head://头像
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    if(imagePicker==null){
                        initImagePicker();
                    }
                    showSel();
                }else{
                    Toast.makeText(getActivity(),"当前您暂未登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_user_name://用户名
                if(SharedPreferenceUtil.getLoginState(getActivity())!=1){
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivityForResult(intent,REQUERS_CODE);
                }
                break;
            case R.id.frag_my_store://我的店铺
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(),MyStoreListActivity.class);
                    intent.putExtra("type","fragment_me");
                    startActivityForResult(intent,MYSTOREINFOR_REQUEST_CODE);
                }else{
                    Toast.makeText(getActivity(),"当前您暂未登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.frag_my_infor://我的资料
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(),MyInforActivity.class);
                    startActivityForResult(intent,MYINFOR_REQUEST_CODE);
                }else {
                    Toast.makeText(getActivity(),"当前您暂未登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.frag_my_collect://收藏
                Toast.makeText(getActivity(),"收藏",Toast.LENGTH_LONG).show();
                break;
            case R.id.frag_my_setting://设置中心
                intent=new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(false);//设置单选，fasle单选
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //不显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
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
                        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择
                        Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
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
        SelectDialog dialog = new SelectDialog(getActivity(), R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!getActivity().isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 头像修改回调
     * @param changeInforBack
     */
    @Override
    public void getInforSucc(ChangeInforBack changeInforBack) {
        SharedPreferenceUtil.saveInforComplete(getActivity(),changeInforBack.getInfoSign());
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getInforFail(String msg) {
        handler.sendEmptyMessage(1);
    }

    /**
     * 先获取头像获取地址后
     * @param upImageBack
     */
    @Override
    public void getImageSucc(UpImageBack upImageBack) {
        if(upImageBack.getResultCode().equals("0")){
            img=upImageBack.getImageUrl();
            String no=SharedPreferenceUtil.getID(getActivity());
            preserent.HttpChangeinfor("","","",img,no,"","","","","","","","");
        }else{
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void getImageFail(String msg) {
        handler.sendEmptyMessage(1);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ImageLoading.commonRound(getActivity(),Constant.ip+img,img_header);
                    ImageLoading.commonBlurTeans(getActivity(),Constant.ip+img,iv_zoom);
                    SharedPreferenceUtil.saveHeaderImg(getActivity(),Constant.ip+img);//保存头像
                    MyOrStoreInfor(SharedPreferenceUtil.getInforComplete(getActivity()));
                    LoadingDailog.closeDialog(dailog);
                    Toast.makeText(getActivity(),"头像更新成功",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    LoadingDailog.closeDialog(dailog);
                    Toast.makeText(getActivity(),"头像更新失败",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public void MyOrStoreInfor(String infoSign){
        switch (infoSign){//登录返回是否完善个人、店铺信息
            case "0"://0-两个都完成
                text_frag_infor_add.setVisibility(View.INVISIBLE);
                text_frag_store_add.setVisibility(View.INVISIBLE);
                break;
            case "1"://1-个人信息未完成 店铺完成
                text_frag_infor_add.setVisibility(View.VISIBLE);
                text_frag_store_add.setVisibility(View.INVISIBLE);
                break;
            case "2":// 2-个人完成,店铺未完成
                text_frag_infor_add.setVisibility(View.INVISIBLE);
                text_frag_store_add.setVisibility(View.VISIBLE);
                break;
            case "3":// 3- 个人未完成,店铺未完成
                text_frag_infor_add.setVisibility(View.VISIBLE);
                text_frag_store_add.setVisibility(View.VISIBLE);
                break;
            case ""://未登录
                text_frag_infor_add.setVisibility(View.INVISIBLE);
                text_frag_store_add.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
