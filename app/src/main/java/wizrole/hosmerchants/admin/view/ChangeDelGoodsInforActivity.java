package wizrole.hosmerchants.admin.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.ChageOrDelGoodsAdapter;
import wizrole.hosmerchants.admin.model.changegoodsinfor.ChangeGoodsInforBack;
import wizrole.hosmerchants.admin.model.changegoodsinfor.ChangeGoodsInforHttp;
import wizrole.hosmerchants.admin.model.delgoods.DelGoodsBack;
import wizrole.hosmerchants.admin.preserent.changegoodsinfor.ChangeGoodsInforInterface;
import wizrole.hosmerchants.admin.preserent.changegoodsinfor.ChangeGoodsInforPreserent;
import wizrole.hosmerchants.admin.preserent.delgoods.DelGoodsInterface;
import wizrole.hosmerchants.admin.preserent.delgoods.DelGoodsPreserent;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.upimage.UpImageBack;
import wizrole.hosmerchants.my.preserent.upimage.UpImageInterface;
import wizrole.hosmerchants.my.preserent.upimage.UpImagePreserent;
import wizrole.hosmerchants.release.model.getgoodtype.CommodityList;
import wizrole.hosmerchants.release.model.getgoodtype.GoodsTypeBack;
import wizrole.hosmerchants.release.model.getgoodtype.StoreCommodityType;
import wizrole.hosmerchants.release.preserent.getgoodtype.GoodsTypeInterface;
import wizrole.hosmerchants.release.preserent.getgoodtype.GoodsTypePreserent;
import wizrole.hosmerchants.release.view.ReleaseGoodsActivity;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.image.GlideImageLoader;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 * 商品列表列表--商品修改 删除 、添加
 */

public class ChangeDelGoodsInforActivity extends BaseActivity implements View.OnClickListener,GoodsTypeInterface,ChageOrDelGoodsAdapter.DetailOnItemClick,UpImageInterface
,ChangeGoodsInforInterface,DelGoodsInterface{

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.recy_goods_infor)RecyclerView recyclerviewDetail;
    @BindView(R.id.text_err_msg)TextView text_err_msg;
    @BindView(R.id.text_err_agagin)TextView text_err_agagin;
    public boolean goods_status=false;//商品列表请求状态
    public String StoreId;//店铺主键id
    public Dialog dialog;
    public GoodsTypePreserent goodsTypePreserent=new GoodsTypePreserent(this);//获取所有商品信息
    public UpImagePreserent imagePreserent=new UpImagePreserent(this);//上传图片
    public ChangeGoodsInforPreserent changeGoodsInforPreserent=new ChangeGoodsInforPreserent(this);//修改信息
    public DelGoodsPreserent delGoodsPreserent=new DelGoodsPreserent(this);//删除商品
    private LinearLayoutManager mDetailLayoutManager;
    public List<StoreCommodityType> foodTypeList;
    public List<CommodityList> foodDetailList;
    private ChageOrDelGoodsAdapter foodDetailAdapter;    /**细节适配器*/
    private boolean needMove=false;
    private int movePosition;
    @Override
    protected int getLayout() {
        return R.layout.activity_changeordelgoodsinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        StoreId=getIntent().getStringExtra("StoreId");
        text_title.setText("商品列表");
        text_right.setText("添加商品");
        initViews();
        getAllGoods();
    }
    public void getAllGoods(){
        dialog= LoadingDailog.createLoadingDialog(ChangeDelGoodsInforActivity.this,"加载中");
        goodsTypePreserent.getGoodsType(StoreId);
    }

    @Override
    protected void setListener() {
        text_right.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        text_err_agagin.setOnClickListener(this);
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.text_right://添加商品
                intent=new Intent(ChangeDelGoodsInforActivity.this, ReleaseGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.text_err_agagin://添加或重试
                intent=new Intent(ChangeDelGoodsInforActivity.this, ReleaseGoodsActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取店铺内的所有商品
     * @param goodsTypeBack
     */
    @Override
    public void getGoodsTypeSucc(GoodsTypeBack goodsTypeBack) {
        if(goodsTypeBack.getResultCode().equals("0")){
            foodTypeList=goodsTypeBack.getStoreCommodityType();
            getFoodDetailList(foodTypeList);
            handler.sendEmptyMessage(0);
        }else{
            handler.sendEmptyMessage(8);
        }
    }

    @Override
    public void getGoodsTypeFail(String msg) {
        handler.sendEmptyMessage(1);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://请求成功
                    goods_status=true;
                    recyclerviewDetail.setVisibility(View.VISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    text_err_msg.setVisibility(View.INVISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    setView();
                    break;
                case 1://请求失败
                    goods_status=false;
                    recyclerviewDetail.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.VISIBLE);
                    text_err_msg.setVisibility(View.VISIBLE);
                    text_err_msg.setText("数据获取失败，请检查网络");
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("网络连接失败，请检查网络");
                    break;
                case 2://图片上传失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("很抱歉，图片上传发生错误");
                    break;
                case 3://商品信息修改成功
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("商品信息修改成功");
                    popupWindow.dismiss();
                    popupWindow.setAnimationStyle(R.style.PopupAnimation);
                    setWindowAlpha(1.0f);
                    getAllGoods();
                    break;
                case 4://商品信息修改失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("商品信息修改失败");
                    break;
                case 5://修改失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow(err_msg);
                    break;
                case 6://删除成功
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("商品删除成功");
                    getAllGoods();
                    break;
                case 7://删除失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("商品删除失败，请检查网络");
                    break;
                case 8://商品无数据
                    goods_status=true;
                    recyclerviewDetail.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.VISIBLE);
                    text_err_agagin.setText("点此添加");
                    text_err_msg.setVisibility(View.VISIBLE);
                    text_err_msg.setText("当前暂无商品");
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("当前暂无商品");
                    break;
            }
        }
    };

    /**
     * 把所有类别的商品都添加到一个集合中
     * @param foodTypeList
     */
    public void getFoodDetailList(List<StoreCommodityType> foodTypeList) {
        foodDetailList=new ArrayList<CommodityList>();
        this.foodTypeList = foodTypeList;
        for(int i = 0; i< foodTypeList.size(); i++){
            if(foodTypeList !=null){
                foodDetailList.addAll(foodTypeList.get(i).getCommodityList());
            }
        }
    }
    public boolean first=false;
    public void setView(){
        /**设置细节适配器*/
        foodDetailAdapter = new ChageOrDelGoodsAdapter(ChangeDelGoodsInforActivity.this, foodTypeList);
        recyclerviewDetail.setAdapter(foodDetailAdapter);
        foodDetailAdapter.setOnItemClickListener(ChangeDelGoodsInforActivity.this);
        if(first==false){
            first=true;
            mDetailLayoutManager = new LinearLayoutManager(this);
            recyclerviewDetail.setLayoutManager(mDetailLayoutManager);
            // 添加标题
            final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(foodDetailAdapter);
            recyclerviewDetail.addItemDecoration(headersDecor);
        }
    }
    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerviewDetail.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //第一个完全显示的item和最后一个item。
                    int firstVisibleItem = mDetailLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastVisibleItem = mDetailLayoutManager.findLastVisibleItemPosition();
                    //此判断，避免左侧点击最后一个item无响应
                    if(lastVisibleItem != mDetailLayoutManager.getItemCount()-1){
                        int sort = foodDetailAdapter.getSortType(firstVisibleItem);
//                        changeSelected(sort);
                    }else {
//                        changeSelected(foodTypeAdapter.getItemCount()-1);
                    }
                    if(needMove){
                        needMove = false;
                        //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                        int n = movePosition - mDetailLayoutManager.findFirstVisibleItemPosition();
                        if ( 0 <= n && n < recyclerviewDetail.getChildCount()){
                            //获取要置顶的项顶部离RecyclerView顶部的距离
                            int top = recyclerviewDetail.getChildAt(n).getTop()-dip2px(28);
                            //最后的移动
                            recyclerviewDetail.scrollBy(0, top);
                        }
                    }
                }
            });
        }else {
            recyclerviewDetail.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //第一个完全显示的item和最后一个item。
                    int firstVisibleItem = mDetailLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastVisibleItem = mDetailLayoutManager.findLastVisibleItemPosition();
                    //此判断，避免左侧点击最后一个item无响应
                    if(lastVisibleItem != mDetailLayoutManager.getItemCount()-1){
                        int sort = foodDetailAdapter.getSortType(firstVisibleItem);
//                        changeSelected(sort);
                    }else {
//                        changeSelected(foodTypeAdapter.getItemCount()-1);
                    }
                    if(needMove){
                        needMove = false;
                        //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                        int n = movePosition - mDetailLayoutManager.findFirstVisibleItemPosition();
                        if ( 0 <= n && n < recyclerviewDetail.getChildCount()){
                            //获取要置顶的项顶部离RecyclerView顶部的距离
                            int top = recyclerviewDetail.getChildAt(n).getTop()-dip2px(28);
                            //最后的移动
                            recyclerviewDetail.scrollBy(0, top);
                        }
                    }
                }
            });
        }
    }
    @Override
    public void detailOnItemClick(View view, int position,String CateId) {
        cateID=CateId;//获取分类id
        detail=foodDetailList.get(position);
        ChangeOrDel(detail);
    }

    /**
     * 修改 删除dialog
     */
    public AlertDialog cd_dialog;
    public void ChangeOrDel(final CommodityList detail){
        cd_dialog=new AlertDialog.Builder(this).create();
        cd_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view_cd= LayoutInflater.from(this).inflate(R.layout.dialog_goods_del_change,null);
        TextView text_change=(TextView)view_cd.findViewById(R.id.text_change);
        text_change.setText("修改商品信息");
        TextView text_del=(TextView)view_cd.findViewById(R.id.text_del);
        text_del.setText("删除该商品");
        text_change.setOnClickListener(new View.OnClickListener() {//修改
            @Override
            public void onClick(View v) {
                cd_dialog.dismiss();
                setWindowAlpha(0.5f);
                showPopupFoodDetail(detail);
            }
        });
        text_del.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                cd_dialog.dismiss();
                delGoods(detail.getCommodityName(),detail.getCommodityNo());
            }
        });
        //设置date布局
        cd_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd_dialog.setView(view_cd);
        cd_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = cd_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        cd_dialog.getWindow().setAttributes(layoutParams);
    }

    public AlertDialog del_dialog;

    /**
     * 删除商品
     * @param goodsId 商品id
     */
    public void delGoods(final String name,final String goodsId){
        del_dialog=new AlertDialog.Builder(this).create();
        del_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view_cd= LayoutInflater.from(this).inflate(R.layout.dialog_exits,null);
        TextView text_message=(TextView)view_cd.findViewById(R.id.text_message);
        TextView text_sure=(TextView)view_cd.findViewById(R.id.text_sure);
        TextView text_cancle=(TextView)view_cd.findViewById(R.id.text_cancle);
        text_message.setText("是否删除【"+name+"】商品？");
        text_message.setTextColor(getResources().getColor(R.color.colorAccent));
        text_cancle.setOnClickListener(new View.OnClickListener() {//修改
            @Override
            public void onClick(View v) {
                del_dialog.dismiss();
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                del_dialog.dismiss();
                dialog=LoadingDailog.createLoadingDialog(ChangeDelGoodsInforActivity.this,"加载中");
                delGoodsPreserent.delGoodsHttp(goodsId);
            }
        });
        //设置date布局
        del_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        del_dialog.setView(view_cd);
        del_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = del_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        del_dialog.getWindow().setAttributes(layoutParams);
    }


    public String cateID;
    public CommodityList detail;
    public PopupWindow popupWindow;
    public TextView text_img_status;
    public EditText text_name;
    public EditText text_detail_content;
    public  EditText text_price;
    public ImageView img_goods_logo;

    /**
     * 显示商品详情 修改
     * @param detail
     */
    public void showPopupFoodDetail(CommodityList detail){
        View v= LayoutInflater.from(this).inflate(R.layout.dialog_change_fooddetail,null);
        img_goods_logo = (ImageView)v.findViewById(R.id.img_detail_logo) ;
        text_name = (EditText)v.findViewById(R.id.text_detail_name) ;
        text_img_status = (TextView)v.findViewById(R.id.text_img_status) ;
        TextView text_changedel_sure = (TextView)v.findViewById(R.id.text_changedel_sure) ;
        text_detail_content = (EditText)v.findViewById(R.id.text_detail_content) ;
        text_price = (EditText)v.findViewById(R.id.text_detail_price) ;
        RelativeLayout rel_dialog_img = (RelativeLayout)v.findViewById(R.id.rel_dialog_img) ;
        WindowManager manager=getWindowManager();
        int wid=manager.getDefaultDisplay().getWidth();
        int hei=manager.getDefaultDisplay().getHeight();
        popupWindow= new PopupWindow(v,
                wid-150, hei-600, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                popupWindow.setAnimationStyle(R.style.PopupAnimation);
                setWindowAlpha(1.0f);
            }
        });
        ImageLoading.common(ChangeDelGoodsInforActivity.this,Constant.ip+detail.getCommodityPic(),img_goods_logo,R.drawable.img_loadfail);
        text_name.setText(detail.getCommodityName());
        text_price.setText(detail.getCommodityAmt());
        text_detail_content.setText(detail.getCommodityContent());
        rel_dialog_img.setOnClickListener(ImageOnClick);
        text_changedel_sure.setOnClickListener(sureOnClick);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0,0);

    }
    //图片更换监听
    public View.OnClickListener ImageOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initImagePicker();
            Intent intent1 = new Intent(ChangeDelGoodsInforActivity.this, ImageGridActivity.class);
            startActivityForResult(intent1, REQUEST_CODE_SELECT);
        }
    };
    //确定修改监听
    public View.OnClickListener sureOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(text_name.getText().toString().length()==0){
                ToastShow("商品名称不能为空");
            }else if(text_detail_content.getText().toString().length()==0){
                ToastShow("商品介绍不能为空");
            }else if(text_price.getText().toString().length()==0){
                ToastShow("商品价格不能为空");
            }else{
                dialog=LoadingDailog.createLoadingDialog(ChangeDelGoodsInforActivity.this,"加载中");
                if(img_logo.equals("")){
                    changeGoodsInforPreserent.ChangeGoodsInforHttp(
                            detail.getCommodityNo()
                            ,text_name.getText().toString()
                            ,""
                            ,text_detail_content.getText().toString()
                            ,text_price.getText().toString()
                            ,cateID);
                }else{
                    imagePreserent.HttpUpImage(img_logo,Constant.img_goods_infor);
                }
            }
        }
    };

    public static final int REQUEST_CODE_SELECT = 100;
    private ImagePicker imagePicker;
    private ArrayList<ImageItem> images = null;
    private void initImagePicker() {
        if(imagePicker==null){
            imagePicker = ImagePicker.getInstance();
            imagePicker.setMultiMode(false);//设置单选，fasle单选
            imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
            imagePicker.setShowCamera(true);                      //显示拍照按钮
            imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
            imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
            imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
            int radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
            imagePicker.setFocusWidth(radius * 2);
            imagePicker.setFocusHeight(radius * 2);
            imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
            imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
        }
    }

    public String img_logo="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//图片修改
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    img_logo=images.get(0).path;
                    ImageLoading.common(ChangeDelGoodsInforActivity.this,img_logo,img_goods_logo);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *
     CommodityNo 	- 商品主键ID
     CommodityName 	- 商品名称
     CommodityPic 	- 商品图片
     CommodityContent- 商品内容
     CommodityAmt	- 商品价格
     BelongTypeNo	- 所属类别主键ID
     */

    /**
     * 上传图片
     * @param upImageBack
     */
    @Override
    public void getImageSucc(UpImageBack upImageBack) {
        String img_path=upImageBack.getImageUrl();
        changeGoodsInforPreserent.ChangeGoodsInforHttp(
                 detail.getCommodityNo()
                 ,text_name.getText().toString()
                 ,img_path
                 ,text_detail_content.getText().toString()
                 ,text_price.getText().toString()
                 ,cateID);
    }

    @Override
    public void getImageFail(String msg) {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void getChangeGoodsInforSucc(ChangeGoodsInforBack changeGoodsInforBack) {
        handler.sendEmptyMessage(3);
    }

    public String err_msg;
    @Override
    public void getChangeGoodsInforFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(4);
        }else{
            err_msg=msg;
            handler.sendEmptyMessage(5);
        }
    }

    /**
     * 删除商品
     * @param delGoodsBack
     */
    @Override
    public void getDelGoodsSucc(DelGoodsBack delGoodsBack) {
        handler.sendEmptyMessage(6);
    }

    @Override
    public void getDelGoodsFail(String msg) {
        handler.sendEmptyMessage(7);
    }


    /**
     * 根据手机分辨率从dp转成px
     */
    public  int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return(int) (dpValue * scale + 0.5f);
    }
    /**
     * 设置窗体透明度
     *
     * @param alpha
     *            透明度值
     */
    private WindowManager.LayoutParams lp;
    private void setWindowAlpha(float alpha) {
        lp = getWindow().getAttributes();
        // 重新设定窗体透明度
        lp.alpha = alpha;
        // 重新设定窗体布局参数
        getWindow().setAttributes(lp);
    }
}
