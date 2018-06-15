package wizrole.hosmerchants.merchants.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.FoodDetailAdapter;
import wizrole.hosmerchants.adapter.FoodTypeAdapter;
import wizrole.hosmerchants.admin.view.SingleGoodsInforActivity;
import wizrole.hosmerchants.release.model.getgoodtype.CommodityList;
import wizrole.hosmerchants.release.model.getgoodtype.GoodsTypeBack;
import wizrole.hosmerchants.release.model.getgoodtype.StoreCommodityType;
import wizrole.hosmerchants.release.preserent.getgoodtype.GoodsTypeInterface;
import wizrole.hosmerchants.release.preserent.getgoodtype.GoodsTypePreserent;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Fragment_Goods extends Fragment implements GoodsTypeInterface,FoodTypeAdapter.OnItemClickListener
        ,FoodDetailAdapter.DetailOnItemClick,View.OnClickListener{

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public NestedScrollView nest_scrollview;
    public View view,inc_recyview;
    public ImageView img_net_err;
    public RecyclerView recyclerviewCategory;    //左侧选中的item  view   设置背景
    public  RecyclerView recyclerviewDetail;
    public TextView text_err_msg;
    public TextView text_err_agagin;
    public List<StoreCommodityType> foodTypeList;
    public List<CommodityList> foodDetailList;
    private FoodTypeAdapter foodTypeAdapter;    /**种类适配器*/
    private FoodDetailAdapter foodDetailAdapter;    /**细节适配器*/
    private int oldSelectedPosition = 0;
    private LinearLayoutManager mDetailLayoutManager;
    private LinearLayoutManager mCategoryLayoutManager;
    public GoodsTypePreserent goodsTypePreserent=new GoodsTypePreserent(this);
    public Dialog dialog;
    public String id;
    public AllGoods allGoods;

    public interface AllGoods{
        void getAllGoods(List<CommodityList> commodityLists);
    }

    public AllGoods getAllGoods() {
        return allGoods;
    }

    public void setAllGoods(AllGoods allGoods) {
        this.allGoods = allGoods;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_mystoreview,null);
            Bundle bundle = getArguments();
            id = bundle.getString("id");
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
            getInfor(id);//获取商品
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


    public void initUI(){
        recyclerviewCategory=(RecyclerView)view.findViewById(R.id.recyclerview_category);
        recyclerviewDetail=(RecyclerView)view.findViewById(R.id.recyclerview_detail);
        text_err_msg=(TextView)view.findViewById(R.id.text_err_msg);
        img_net_err=(ImageView) view.findViewById(R.id.img_net_err);
        text_err_agagin=(TextView) view.findViewById(R.id.text_err_agagin);
        inc_recyview=(View) view.findViewById(R.id.inc_recyview);
        nest_scrollview=(NestedScrollView) view.findViewById(R.id.nest_scrollview);

    }

    public void setListener(){
        text_err_agagin.setOnClickListener(this);
    }


    public void getInfor(String No){
        dialog= LoadingDailog.createLoadingDialog(getActivity(),"加载中");
        goodsTypePreserent.getGoodsType(No);
    }
    public GoodsTypeBack back;
    /**
     * 获取所有商品信息
     * @param goodsTypeBack
     */
    @Override
    public void getGoodsTypeSucc(GoodsTypeBack goodsTypeBack) {
        back=goodsTypeBack;
        if(goodsTypeBack.getResultCode().equals("0")){
            List<StoreCommodityType> foodTypeList=goodsTypeBack.getStoreCommodityType();
            getFoodDetailList(foodTypeList);    //添加到细节集合
            handler.sendEmptyMessage(0);
        }else if(goodsTypeBack.getResultCode().equals("2")){
            List<StoreCommodityType> foodTypeList=goodsTypeBack.getStoreCommodityType();
            getFoodDetailList(foodTypeList);    //添加到细节集合
            handler.sendEmptyMessage(2);//有分类-无商品
        }else{//无数据-分类都无
            handler.sendEmptyMessage(1);
        }

    }

    @Override
    public void getGoodsTypeFail(String msg) {
        handler.sendEmptyMessage(3);
    }

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

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://成功
                    inc_recyview.setVisibility(View.VISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    text_err_msg.setVisibility(View.INVISIBLE);
                    img_net_err.setVisibility(View.INVISIBLE);
                    nest_scrollview.setVisibility(View.INVISIBLE);
                    initViews();        //设置数据
                    LoadingDailog.closeDialog(dialog);
                    if(allGoods!=null){
                        allGoods.getAllGoods(foodDetailList);
                    }
                    break;
                case 1://失败-都无数据
                    inc_recyview.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    text_err_msg.setText("当前暂无商品");
                    text_err_msg.setVisibility(View.VISIBLE);
                    ImageLoading.common(getActivity(),R.drawable.null_data,img_net_err,R.drawable.img_loadfail);
                    img_net_err.setVisibility(View.VISIBLE);
                    nest_scrollview.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    Toast.makeText(getActivity(),"当前暂无商品",Toast.LENGTH_LONG).show();
                    break;
                case 2://无数据--有分类
                    initViews();        //设置数据
                    recyclerviewDetail.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    text_err_msg.setText("当前暂无商品");
                    text_err_msg.setVisibility(View.VISIBLE);
                    ImageLoading.common(getActivity(),R.drawable.null_data,img_net_err,R.drawable.img_loadfail);
                    img_net_err.setVisibility(View.VISIBLE);
                    nest_scrollview.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    Toast.makeText(getActivity(),"当前暂无商品",Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    inc_recyview.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.VISIBLE);
                    text_err_msg.setText("数据获取失败，请检查网络");
                    text_err_msg.setVisibility(View.VISIBLE);
                    img_net_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(getActivity(),R.drawable.net_err,img_net_err,R.drawable.img_loadfail);
                    nest_scrollview.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    Toast.makeText(getActivity(),"数据获取失败，请检查网络",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };



    public void onClick(View view){
        switch (view.getId()) {
            case R.id.text_err_agagin://重试
                text_err_agagin.setVisibility(View.INVISIBLE);
                text_err_msg.setVisibility(View.INVISIBLE);
                img_net_err.setVisibility(View.INVISIBLE);
                getInfor(id);//获取商品
                break;
        }
    }


    private void initViews() {
        mDetailLayoutManager = new LinearLayoutManager(getActivity());
        mCategoryLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewCategory.setLayoutManager(mCategoryLayoutManager);
        recyclerviewDetail.setLayoutManager(mDetailLayoutManager);
        /**设置种类适配器*/
        foodTypeAdapter = new FoodTypeAdapter(getActivity(), foodTypeList);
        foodTypeAdapter.setOnItemClickListener(this);
        recyclerviewCategory.setAdapter(foodTypeAdapter);
        /**设置细节适配器*/
        foodDetailAdapter = new FoodDetailAdapter(getActivity(), foodTypeList);
        recyclerviewDetail.setAdapter(foodDetailAdapter);
        foodDetailAdapter.setOnItemClickListener(this);

        // 添加标题
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(foodDetailAdapter);
        recyclerviewDetail.addItemDecoration(headersDecor);

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
                        changeSelected(sort);
                    }else {
                        changeSelected(foodTypeAdapter.getItemCount()-1);
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
                        changeSelected(sort);
                    }else {
                        changeSelected(foodTypeAdapter.getItemCount()-1);
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
    private boolean needMove=false;
    private int movePosition;
    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mDetailLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mDetailLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerviewDetail.scrollToPosition(n);
        }else if ( n <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerviewDetail.getChildAt(n - firstItem).getTop();
            recyclerviewDetail.scrollBy(0, top-dip2px(28));
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerviewDetail.scrollToPosition(n);
            movePosition = n;
            needMove = true;
        }
    }
    /*** 接口实现---左侧FoodType的点击*/
    @Override
    public void onItemClick(int position ) {
        changeSelected(position);
        moveToThisSortFirstItem(position);
    }
    /*****接口实现---右侧item点击******/
    @Override
    public void detailOnItemClick(View view, int position) {
        CommodityList detail=foodDetailList.get(position);
//        showPopupFoodDetail(detail);
//        setWindowAlpha(0.5f);
//        showDetail(back,position);
        showDetail(detail);
        foodDetailAdapter.notifyDataSetChanged();
    }

    /***以下方法实现左右两侧联动**/
    private void moveToThisSortFirstItem(int position) {
        movePosition = 0;
        for(int i=0;i<position;i++){
            movePosition += foodDetailAdapter.getFoodTypeList().get(i).getCommodityList().size();
        }
        moveToPosition(movePosition);
    }
    private void changeSelected(int position) {
        foodTypeList.get(oldSelectedPosition).setSelected(false);
        foodTypeList.get(position).setSelected(true);
        //增加左侧联动
        recyclerviewCategory.scrollToPosition(position);
        oldSelectedPosition = position;
        foodTypeAdapter.notifyDataSetChanged();
    }
    public void showDetail(CommodityList goodsTypeBack){
        Intent intent = new Intent(getActivity(), SingleGoodsInforActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("singlegoods",goodsTypeBack);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        lp = getActivity().getWindow().getAttributes();
        // 重新设定窗体透明度
        lp.alpha = alpha;
        // 重新设定窗体布局参数
        getActivity().getWindow().setAttributes(lp);
    }
}