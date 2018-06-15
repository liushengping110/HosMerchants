package wizrole.hosmerchants.merchants.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.andview.refreshview.XRefreshView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.activity.MainActivity;
import wizrole.hosmerchants.merchants.view.MyStoreViewActivity;
import wizrole.hosmerchants.adapter.MerchantsListAdapter;
import wizrole.hosmerchants.adapter.MerchantsVPAdapter;
import wizrole.hosmerchants.adapter.ViewpNetErrAdapter;
import wizrole.hosmerchants.base.MyApplication;
import wizrole.hosmerchants.merchants.model.viewpager.ViewPagerBack;
import wizrole.hosmerchants.merchants.preserent.getallstore.GetAllStoreInterface;
import wizrole.hosmerchants.merchants.preserent.getallstore.GetAllStorePreserent;
import wizrole.hosmerchants.merchants.preserent.viewpager.ViewPagerInterface;
import wizrole.hosmerchants.merchants.preserent.viewpager.ViewPagerPreserent;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.ui.Point;
import wizrole.hosmerchants.ui.pop.PopDissListener;
import wizrole.hosmerchants.ui.pop.PopupWindowPotting;
import wizrole.hosmerchants.util.DensityUtil;
import wizrole.hosmerchants.util.image.ImageLoading;
import wizrole.hosmerchants.view.CustListView;
import wizrole.hosmerchants.view.FooterView;
import wizrole.hosmerchants.view.HeaderView;
import wizrole.hosmerchants.view.LoadingView;
import wizrole.hosmerchants.view.MyScroview;
import wizrole.hosmerchants.view.UserFooterView;
import wizrole.hosmerchants.view.UserHeaderView;

/**
 * Created by liushengping on 2017/11/27/027.
 * 何人执笔？
 * 商家fragment页面
 */

public class Fragment_Merchants extends Fragment implements View.OnClickListener,ViewPagerInterface
,GetAllStoreInterface,PopDissListener {

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public View  view,view_over;
    public LinearLayout lin_content,lin_wifi_err,lin_cyms,lin_scbl,lin_tpyp,lin_sgsx,lin_xhhh,lin_cycx,lin_yshg,lin_xdth;
    public ImageView img_net_err;//网络无连接图片
    public TextView text_search,text_err_agagin_center;
    public TextView tabl_one;
    public TextView tabl_two;
    public ImageView img_one_bottom,img_to_top;
    public ImageView  img_two_bottom;
    public RelativeLayout rel_tab1_one;
    public RelativeLayout rel_tab1_two;
    public CustListView recy_view;
    public RefreshLayout refreshView;
    public LoadingView loadingView,loadView_cener;
    public ViewPager viewPager;
    public List<String> strings;//图片轮播网络
    public List<Integer> integers;//图片轮播默认
    public ViewPagerPreserent ViewPagerpreserent=new ViewPagerPreserent(this);
    public GetAllStorePreserent getAllStorePreserent=new GetAllStorePreserent(this);
    private LinearLayout group;//小圆点容器
    public Point point;
    private MerchantsListAdapter adapter;
    public int page=1;
    public ViewFlipper view_flipper;
    /*************/
    MyScroview myscroview;
    LinearLayout main_tab2;// 在MyScrollView里面的购买布局
    LinearLayout main_tab1;
    LinearLayout tab_mian;
    public boolean scrollTotop=false;//滑动到顶端标记位
    public GetView getView;
    public interface GetView {
        void getView(LinearLayout linearLayout_one, LinearLayout linearLayout_two);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getView=(GetView)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            if (Build.VERSION.SDK_INT >= 21) {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.bule));
            }
            view=inflater.inflate(R.layout.fragment_merchants,null);
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
    public boolean first=false;//程序第一次加载
    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if(!isLoadData){
            //加载数据操作
            initUI();
            textScroll();
            setListener();
            initViewPager();
            getData("",tabl_two.getText().toString(),page);
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


    protected void initUI() {
        all_storeLists=new ArrayList<StoreList>();
        view_over=(View) view.findViewById(R.id.view_over);
        img_to_top=(ImageView) view.findViewById(R.id.img_to_top);
        text_search=(TextView)view.findViewById(R.id.text_search);
        img_net_err=(ImageView)view.findViewById(R.id.img_net_err);
        text_err_agagin_center=(TextView)view.findViewById(R.id.text_err_agagin_center);
        recy_view=(CustListView)view.findViewById(R.id.recy_view);
        lin_cyms=(LinearLayout)view.findViewById(R.id.lin_cyms);
        lin_cycx=(LinearLayout)view.findViewById(R.id.lin_cycx);
        lin_xdth=(LinearLayout)view.findViewById(R.id.lin_xdth);
        lin_yshg=(LinearLayout)view.findViewById(R.id.lin_yshg);
        lin_xhhh=(LinearLayout)view.findViewById(R.id.lin_xhhh);
        lin_sgsx=(LinearLayout)view.findViewById(R.id.lin_sgsx);
        lin_tpyp=(LinearLayout)view.findViewById(R.id.lin_tpyp);
        lin_scbl=(LinearLayout)view.findViewById(R.id.lin_scbl);
        lin_wifi_err=(LinearLayout)view.findViewById(R.id.lin_wifi_err);
        lin_content=(LinearLayout)view.findViewById(R.id.lin_content);
        refreshView=(RefreshLayout) view.findViewById(R.id.xrefreshview);
        loadView_cener=(LoadingView)view.findViewById(R.id.loadView_cener);
        loadView_cener.addBitmap(R.drawable.icon_chicken);
        loadView_cener.addBitmap(R.drawable.icon_flower);
        loadView_cener.addBitmap(R.drawable.icon_orange);
        loadView_cener.addBitmap(R.drawable.icon_ufo);
        loadView_cener.addBitmap(R.drawable.icon_pear);
        loadView_cener.setShadowColor(Color.LTGRAY);
        loadView_cener.setDuration(700);
        loadView_cener.start();
        /*顶部viewPager*/
        viewPager=(ViewPager)view.findViewById(R.id.view_pager);
        group=(LinearLayout) view.findViewById(R.id.viewGroup);
        rel_tab1_one=(RelativeLayout)view.findViewById(R.id.rel_tabl_one);
        rel_tab1_two=(RelativeLayout)view.findViewById(R.id.rel_tabl_two);
        //滚动
        view_flipper=(ViewFlipper)view.findViewById(R.id.view_flipper);
        //加载刷新
        refreshView.setRefreshHeader(new UserHeaderView(getActivity()));
        refreshView.setRefreshFooter(new UserFooterView(getActivity()));
        refreshView.setHeaderHeightPx(300);
        refreshView.setFooterHeightPx(80);
        setNetErr();
        /*******标题置顶*******/
        tabl_one=(TextView)view.findViewById(R.id.tab1_one);
        tabl_two=(TextView)view.findViewById(R.id.tabl_two);
        img_one_bottom=(ImageView)view.findViewById(R.id.img_one_bottom);
        img_two_bottom=(ImageView)view.findViewById(R.id.img_two_bottom);
        myscroview=(MyScroview)view.findViewById(R.id.myscroview);
        main_tab2=(LinearLayout)view.findViewById(R.id.main_tab2);// 在MyScrollView里面的购买布局
        main_tab1=(LinearLayout)view.findViewById(R.id.main_tab1);
        tab_mian=(LinearLayout)view.findViewById(R.id.tab_mian);
        myscroview.setOnScrollListener(new MyScroview.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY >= ((MainActivity)getActivity()).getTopHeight()) {
                    if (tab_mian.getParent() != main_tab1) {
                        main_tab2.removeView(tab_mian);
                        main_tab1.addView(tab_mian);
                        //滑动到顶端
                        scrollTotop=true;
                    }
                } else {
                    scrollTotop=false;
                    if (tab_mian.getParent() != main_tab2) {
                        main_tab1.removeView(tab_mian);
                        main_tab2.addView(tab_mian);
                    }
                }
                if (scrollY<3000){
                    img_to_top.setVisibility(View.INVISIBLE);
                }else{
                    if(scrollY>scroll_null){
                        img_to_top.setVisibility(View.INVISIBLE);
                    }else {
                        img_to_top.setVisibility(View.VISIBLE);
                    }
                }
                scroll_null=scrollY;
            }
        });
        getView.getView(main_tab1,main_tab2);
    }
    public int scroll_null=0;
        /**
         * 设置滚动公告
         */
        public void textScroll() {
            List<String> hosNews=new ArrayList<>();
            hosNews.add("欢迎黄焖鸡米饭（解放南路店）入驻住院生活服务平台");
            hosNews.add("欢迎沙县小吃（凤鸣路店）入驻住院生活服务平台");
            hosNews.add("欢迎必胜客（奎山小区店）入驻住院生活服务平台");
            hosNews.add("欢迎肯德基（康居小区店）入驻住院生活服务平台");
            hosNews.add("欢迎李先生（四院店）入驻住院生活服务平台");
            for(int i=0;i<=hosNews.size();i+=2){
                if(i<hosNews.size()){
                    LayoutInflater inflater=LayoutInflater.from(getActivity());
                    View layout=(LinearLayout) inflater.inflate(R.layout.merchants_flipper_item,null);
                    TextView text_one=(TextView)layout.findViewById(R.id.vf_one);
                    ImageView img_vf_two=(ImageView)layout.findViewById(R.id.img_vf_two);
                    TextView text_two=(TextView)layout.findViewById(R.id.vf_two);
                    text_one.setText(hosNews.get(i));//设置第一位数据---无论如何进入该if中都满足设置第一位数据
                    if((i+1)<hosNews.size()){//当设置数据刚好只够第二位
                        text_two.setText(hosNews.get(i+1));
                        img_vf_two.setVisibility(View.VISIBLE);
                    }else{
                        img_vf_two.setVisibility(View.INVISIBLE);
                    }
                    view_flipper.addView(layout);
                }
            }
            view_flipper.setInAnimation(getActivity(), R.anim.push_up_in);
            view_flipper.setOutAnimation(getActivity(), R.anim.push_up_out);
            view_flipper.setAutoStart(true);
            view_flipper.startFlipping();
    }


    public void setListener() {
        text_search.setOnClickListener(this);
        rel_tab1_one.setOnClickListener(this);
        rel_tab1_two.setOnClickListener(this);
        text_err_agagin_center.setOnClickListener(this);
        lin_cyms.setOnClickListener(this);
        lin_cycx.setOnClickListener(this);
        lin_xdth.setOnClickListener(this);
        lin_yshg.setOnClickListener(this);
        lin_xhhh.setOnClickListener(this);
        lin_sgsx.setOnClickListener(this);
        lin_tpyp.setOnClickListener(this);
        lin_scbl.setOnClickListener(this);
        img_to_top.setOnClickListener(this);
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                all_storeLists.clear();
                page=1;
                if(tabl_one.getText().toString().equals("商家类型")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData("",tabl_two.getText().toString(),page);
                        }
                    }, 1000);
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData(tabl_one.getText().toString(),tabl_two.getText().toString(),page);
                        }
                    }, 1000);
                }
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<totalNum){
                    page++;
                    getData("",tabl_two.getText().toString(),page);
                }else{
                    Toast.makeText(getActivity(),"已经没有更多数据了",Toast.LENGTH_LONG).show();
                    refreshlayout.finishLoadmoreWithNoMoreData();
                }
            }
        });

        view_flipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_flipper.getCurrentView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if(true){//请求成功后，方可点击
                            int num=view_flipper.getDisplayedChild();
//                            HosNews news=hosNews.get(num);
                        }
                    }
                });
            }
        });
        recy_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                scroll_status=true;//是否回到原点
                if(all_storeLists.size()>0){
                    if(recy_view.getFooterViewsCount()>0){
                        if(position<all_storeLists.size()){
                            Intent intent=new Intent(getActivity(), MyStoreViewActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("storeinfor",all_storeLists.get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }else{//无店铺添加header
                        Intent intent=new Intent(getActivity(), MyStoreViewActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("storeinfor",all_storeLists.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_search:
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                int location[] = new int[2];
                text_search.getLocationOnScreen(location);
                intent.putExtra("x",location[0]);
                intent.putExtra("y",location[1]);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.rel_tabl_one://商家类型
                if(!scrollTotop){//未滑动到顶端
                    myscroview.smoothScrollTo(0,1170);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            img_status=true;
                            initStore();
                            rotateStart(img_one_bottom);
                            view_over.setVisibility(View.VISIBLE);
                            if(changeBg!=null){
                                changeBg.change(true);
                            }
                            store_popupWindowPotting.Show(main_tab1);
                        }
                    },200 );
                }else{//已经折叠的
                    img_status=true;
                    initStore();
                    rotateStart(img_one_bottom);
                    view_over.setVisibility(View.VISIBLE);
                    if(changeBg!=null){
                        changeBg.change(true);
                    }
                    store_popupWindowPotting.Show(main_tab1);
                }
                break;
            case R.id.rel_tabl_two://好评有限
                if(!scrollTotop){//未滑动到顶端
                    myscroview.smoothScrollTo(0,1170);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            img_status=false;
                            initEvelPop();
                            rotateStart(img_two_bottom);
                            view_over.setVisibility(View.VISIBLE);
                            if(changeBg!=null){
                                changeBg.change(true);
                            }
                            evel_popupWindowPotting.Show(main_tab1);
                        }
                    }, 200);
                }else{
                    img_status=false;
                    initEvelPop();
                    rotateStart(img_two_bottom);
                    view_over.setVisibility(View.VISIBLE);
                    if(changeBg!=null){
                        changeBg.change(true);
                    }
                    evel_popupWindowPotting.Show(main_tab1);
                }
                break;
            case R.id.text_err_agagin_center://所有重试
                lin_wifi_err.setVisibility(View.INVISIBLE);
                loadView_cener.setVisibility(View.VISIBLE);
                page=1;
                getData("","",page);
                break;
            case R.id.img_to_top://回顶部
                myscroview.smoothScrollTo(0,0);
                break;
            case R.id.lin_cyms://餐饮美食
                intent=new Intent(getActivity(),SingleStoreTypeActivity.class);
                intent.putExtra("store_type",getString(R.string.cyms));
                startActivity(intent);
                break;
            case R.id.lin_scbl://商超便利
                intent=new Intent(getActivity(),SingleStoreTypeActivity.class);
                intent.putExtra("store_type",getString(R.string.csbl));
                startActivity(intent);
                break;
            case R.id.lin_tpyp://甜品音频
                intent=new Intent(getActivity(),SingleStoreTypeActivity.class);
                intent.putExtra("store_type",getString(R.string.tpyp));
                startActivity(intent);
                break;
            case R.id.lin_sgsx://生鲜水果
                intent=new Intent(getActivity(),SingleStoreTypeActivity.class);
                intent.putExtra("store_type",getString(R.string.sgsx));
                startActivity(intent);
                break;
            case R.id.lin_xhhh://鲜花花卉
                intent=new Intent(getActivity(),SingleStoreTypeActivity.class);
                intent.putExtra("store_type",getString(R.string.xhhh));
                startActivity(intent);
                break;
             case R.id.lin_cycx://出院出行
                intent=new Intent(getActivity(),SingleStoreTypeActivity.class);
                 intent.putExtra("store_type",getString(R.string.cycx));
                startActivity(intent);
                break;
            case R.id.lin_yshg://月嫂护工
                intent=new Intent(getActivity(),SingleStoreTypeActivity.class);
                intent.putExtra("store_type",getString(R.string.yshg));
                startActivity(intent);
                break;
            case R.id.lin_xdth://新店特惠
                Toast.makeText(MyApplication.getContextObject(),"当前暂未开放该功能，敬请期待！",Toast.LENGTH_LONG).show();
                break;
        }
    }


    /**
     * 获取轮播图片
     */
    public void initViewPager(){
        ViewPagerpreserent.getImage();
    }
    /**
     * 获取列表
     */
    public void getData(String content, String OrderType,int page){
        getAllStorePreserent.getAllStore(content,OrderType,page);
    }

    /**
     * 获取广告轮播
     * @param viewPagerBack
     */
    @Override
    public void getImgSucc(ViewPagerBack viewPagerBack) {
        strings=viewPagerBack.getPicList();
        handler.sendEmptyMessage(0);
    }
    @Override
    public void getImgFail(String msg) {
        handler.sendEmptyMessage(1);
    }

    /***
     * 获取所有商家列表
     * @param getAllStoreBack
     */
    public  List<StoreList> storeLists;
    public  List<StoreList> all_storeLists;
    public int totalNum;//最大页码
    @Override
    public void getAllStoreSucc(MyStoreInforBack getAllStoreBack) {
        storeLists=getAllStoreBack.getStoreList();
        totalNum=getAllStoreBack.getTotalNum();
        handler.sendEmptyMessage(3);
    }

    @Override
    public void getAllStoreFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(4);
        }else{
            handler.sendEmptyMessage(5);
        }
    }


    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    setViewPager(strings,null);
                    break;
                case 1:
                    integers=new ArrayList<Integer>();
                    integers.add(R.drawable.viewpager_load_fail);
                    integers.add(R.drawable.viewpager_load_fail);
                    setViewPager(null,integers);
                    break;
                case 3://获取成功
                    if(recy_view.getHeaderViewsCount()==1){
                        recy_view.removeHeaderView(view_load_header);
                    }
                    if(recy_view.getFooterViewsCount()==1){
                        recy_view.removeFooterView(footer_view);
                    }
                    loadView_cener.setVisibility(View.INVISIBLE);
                    lin_content.setVisibility(View.VISIBLE);
                    recy_view.setVisibility(View.VISIBLE);
                    all_storeLists.addAll(storeLists);
                    if(page==1){
                        refreshView.finishRefresh();
                        adapter=new MerchantsListAdapter(getActivity(),all_storeLists, R.layout.recy_merchants_item);
                        recy_view.setAdapter(adapter);
                        recy_view.setFocusable(false);//解决listView上移距离
                        if(!first){
                            myscroview.smoothScrollTo(0,0);
                            first=true;
                        }
                        addFooterView();
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        refreshView.finishLoadmore();
                        adapter.notifyDataSetChanged();
                    }
                    setNetNormal();
                    break;
                case 4://失败
                    if(recy_view.getHeaderViewsCount()==1){
                        recy_view.removeHeaderView(view_load_header);
                    }
                    if(recy_view.getFooterViewsCount()==1){
                        recy_view.removeFooterView(footer_view);
                    }
                    if(page==1){
                        refreshView.finishRefresh();
                        loadView_cener.setVisibility(View.INVISIBLE);
                        ImageLoading.common(getActivity(), R.drawable.net_err,img_net_err, R.drawable.net_err);
                        img_net_err.setVisibility(View.VISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        lin_content.setVisibility(View.INVISIBLE);
                        setNetErr();
                    }else {
                        refreshView.finishLoadmore();
                        setNetNormal();
                    }
                    Toast.makeText(getActivity(), "数据获取失败，请检查网络", Toast.LENGTH_LONG).show();
                    break;
                case 5://无此类别店铺
                    if(recy_view.getHeaderViewsCount()==1){
                        recy_view.removeHeaderView(view_load_header);
                    }
                    if(recy_view.getFooterViewsCount()==1){
                        recy_view.removeFooterView(footer_view);
                    }
                    addNullHeader();
                    setNetErr();
                    break;
            }
        }
    };


    /***
     * 科室弹窗
     * @param list_deparentName
     */
    public PopupWindowPotting store_popupWindowPotting;
    public TextView pop_item_cy;
    public TextView pop_item_cs;
    public TextView pop_item_tp;
    public TextView pop_item_sg;
    public TextView pop_item_xh;
    public TextView pop_item_cx;
    public TextView pop_item_hg;
    public int type_sel;
    public void initStore(){
        if(store_popupWindowPotting==null) {
            store_popupWindowPotting = new PopupWindowPotting(this, 2){
                @Override
                protected int getLayout() {
                    return R.layout.pop_merchants_store;
                }

                @Override
                protected void initUI() {
                    pop_item_cy=(TextView)$(R.id.pop_item_cy);
                    pop_item_cs=(TextView)$(R.id.pop_item_cs);
                    pop_item_tp=(TextView)$(R.id.pop_item_tp);
                    pop_item_sg=(TextView)$(R.id.pop_item_sg);
                    pop_item_xh=(TextView)$(R.id.pop_item_xh);
                    pop_item_cx=(TextView)$(R.id.pop_item_cx);
                    pop_item_hg=(TextView)$(R.id.pop_item_hg);
                }

                @Override
                protected void setListener() {
                    pop_item_cy.setOnClickListener(new ItemClick());
                    pop_item_cs.setOnClickListener(new ItemClick());
                    pop_item_tp.setOnClickListener(new ItemClick());
                    pop_item_sg.setOnClickListener(new ItemClick());
                    pop_item_xh.setOnClickListener(new ItemClick());
                    pop_item_cx.setOnClickListener(new ItemClick());
                    pop_item_hg.setOnClickListener(new ItemClick());
                }

                class  ItemClick implements View.OnClickListener{
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.pop_item_cy:
                                type_sel=1;
                                tabl_one.setText(getString(R.string.cyms));
                                break;
                            case R.id.pop_item_cs:
                                type_sel=2;
                                tabl_one.setText(getString(R.string.csbl));
                                break;
                            case R.id.pop_item_tp:
                                type_sel=3;
                                tabl_one.setText(getString(R.string.tpyp));
                                break;
                            case R.id.pop_item_sg:
                                type_sel=4;
                                tabl_one.setText(getString(R.string.sgsx));
                                break;
                            case R.id.pop_item_xh:
                                type_sel=5;
                                tabl_one.setText(getString(R.string.xhhh));
                                break;
                            case R.id.pop_item_cx:
                                type_sel=6;
                                tabl_one.setText(getString(R.string.cycx));
                                break;
                            case R.id.pop_item_hg:
                                type_sel=7;
                                tabl_one.setText(getString(R.string.yshg));
                                break;
                        }
                        addLoadingHeader();//显示等待动画
                        rotateEnd(img_one_bottom);
                        store_popupWindowPotting.Hide();
                        page=1;
                        if(tabl_one.getText().toString().equals("商家类型")){
                            getData("",tabl_two.getText().toString(),page);
                        }else{
                            getData(tabl_one.getText().toString(),tabl_two.getText().toString(),page);
                        }
                    }
                }
            };
        }
    }
    public PopupWindowPotting evel_popupWindowPotting;
    public TextView pop_item_hp;
    public TextView pop_item_cp;
    public int pj_sel;
    public void initEvelPop(){
        if(evel_popupWindowPotting==null){
            evel_popupWindowPotting=new PopupWindowPotting(this,2) {
                @Override
                protected int getLayout() {
                    return R.layout.pop_merchants_evel;
                }

                @Override
                protected void initUI() {
                    pop_item_cp=(TextView)$(R.id.pop_item_cp);
                    pop_item_hp=(TextView)$(R.id.pop_item_hp);
                }

                @Override
                protected void setListener() {
                    pop_item_hp.setOnClickListener(new ItemClick());
                    pop_item_cp.setOnClickListener(new ItemClick());
                }

                class ItemClick implements View.OnClickListener{
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.pop_item_hp://好评
                                pj_sel=1;
                                tabl_two.setText("好评优先");
                                break;
                            case R.id.pop_item_cp://差评
                                pj_sel=2;
                                tabl_two.setText("差评优先");
                                break;
                        }
                        addLoadingHeader();//显示等待动画
                        rotateEnd(img_two_bottom);
                        evel_popupWindowPotting.Hide();
                        page=1;
                        if(tabl_one.getText().toString().equals("商家类型")){
                            getData("",tabl_two.getText().toString(),page);
                        }else{
                            getData(tabl_one.getText().toString(),tabl_two.getText().toString(),page);
                        }
                    }
                }
            };
        }
    }


    /**
     * 网络连接成功  --失败  广告轮播图
     * @param strings---有网络连接
     * @param integers---无网络连接
     */
    public void setViewPager(List<String> strings,List<Integer> integers){
        if(strings!=null){
            //初始化点 控件
            point=new Point(MyApplication.getContextObject(),strings.size());
            group.addView(point);
            MerchantsVPAdapter hosNewsViewpAdapter =new MerchantsVPAdapter(getActivity(),strings);
            viewPager.setAdapter(hosNewsViewpAdapter);
        }else{
            //初始化点 控件
            point=new Point(MyApplication.getContextObject(),integers.size());
            group.addView(point);
            ViewpNetErrAdapter netErrAdapter=new ViewpNetErrAdapter(getActivity(),integers);
            viewPager.setAdapter(netErrAdapter);
        }
        viewPager.setOnPageChangeListener(viewPagerChangeListener);
        // 自动切换页面功能
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(4000);
                    handlerVp.sendEmptyMessage(0);
                }
            }
        }).start();
    }
    public Handler handlerVp=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                if(strings==null){
                    if (viewPager.getCurrentItem() ==integers.size()-1) {
                        viewPager.setCurrentItem(0);
                    }else{
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }else{
                    if (viewPager.getCurrentItem() ==strings.size()-1 ) {
                        viewPager.setCurrentItem(0);
                    }else{
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }
            }
        }
    };

    public ViewPager.OnPageChangeListener viewPagerChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //根据监听的页面改变当前页对应的小圆点
          point.changePoint(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    public boolean scroll_status=false;
    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            if(!scroll_status){
                myscroview.smoothScrollTo(0,0);
                scroll_status=true;
            }
        }
    }
    public View view_load_header;
    //添加等待动画，筛选请求
    public void addLoadingHeader(){
        if(recy_view.getHeaderViewsCount()==1){
            recy_view.removeHeaderView(view_null_header);
        }
        all_storeLists.clear();
        WindowManager wm = (WindowManager) MyApplication.getContextObject() .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int header_h=height-(DensityUtil.dip2px(MyApplication.getContextObject(),77)+260);
        view_load_header=LayoutInflater.from(MyApplication.getContextObject()).inflate(R.layout.merchants_load_header_view,null);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,header_h);
        view_load_header.setLayoutParams(params);
        loadingView=(LoadingView)view_load_header.findViewById(R.id.loadView);
        loadingView.addBitmap(R.drawable.icon_chicken);
        loadingView.addBitmap(R.drawable.icon_flower);
        loadingView.addBitmap(R.drawable.icon_orange);
        loadingView.addBitmap(R.drawable.icon_ufo);
        loadingView.addBitmap(R.drawable.icon_pear);
        loadingView.setShadowColor(Color.LTGRAY);
        loadingView.setDuration(700);
        loadingView.start();
        recy_view.addHeaderView(view_load_header);
        adapter.notifyDataSetChanged();
        recy_view.setVisibility(View.VISIBLE);
    }
    //添加无数据
    public View view_null_header;
    public void addNullHeader(){
        all_storeLists.clear();
        WindowManager wm = (WindowManager) MyApplication.getContextObject() .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int header_h=height-(307+DensityUtil.dip2px(MyApplication.getContextObject(),51));
        view_null_header=LayoutInflater.from(MyApplication.getContextObject()).inflate(R.layout.merchants_null_header_view,null);
        ImageView img_net_err=(ImageView)view_null_header.findViewById(R.id.img_net_err);
        ImageLoading.common(getActivity(),R.drawable.null_data,img_net_err,R.drawable.null_data);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,header_h);
        view_null_header.setLayoutParams(params);
        if(adapter==null){
            adapter=new MerchantsListAdapter(getActivity(),all_storeLists, R.layout.recy_merchants_item);
            recy_view.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
        recy_view.addHeaderView(view_null_header);
        recy_view.setVisibility(View.VISIBLE);
    }

    public View footer_view;
    public void addFooterView(){
        WindowManager wm = (WindowManager) MyApplication.getContextObject() .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int header_h=height-(307+DensityUtil.dip2px(MyApplication.getContextObject(),51));
        int list_header=getTotalHeightofListView(recy_view);
        if(list_header<header_h){
            footer_view=new View(getActivity());
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,header_h-list_header);
            footer_view.setLayoutParams(params);
            recy_view.addFooterView(footer_view);
        }
    }

    /**
     * 弹窗上的三角形翻转
     */
    public void rotateStart(ImageView imageView) {
        RotateAnimation animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setRepeatCount(0);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
    }

    /**
     * 弹窗上的三角形翻转
     */
    public void rotateEnd(ImageView imageView) {
        RotateAnimation animation = new RotateAnimation(180, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setRepeatCount(0);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
    }
    public boolean img_status;//商家 好评弹窗标记
    @Override
    public void onDiss() {
        if(img_status){
            rotateEnd(img_one_bottom);
        }else{
            rotateEnd(img_two_bottom);
        }
        view_over.setVisibility(View.INVISIBLE);
        if(changeBg!=null){
            changeBg.change(false);
        }
    }

    /**
     * 计算listView的高度
     * @param listView
     * @return
     */
    public  int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
    }

    /*****************网络请求失败时的禁止加载和刷新***********************/
    //禁止加载和刷新
    public void setNetErr(){
        refreshView.setEnableLoadmore(false);
        refreshView.setEnableRefresh(false);
    }
    //支持加载和刷新
    public void setNetNormal(){
        refreshView.setEnableLoadmore(true);
        refreshView.setEnableRefresh(true);
    }


    public ChangeBg changeBg;

    public void setChangeBg(ChangeBg changeBg) {
        this.changeBg = changeBg;
    }

    public interface ChangeBg{
        void change(boolean status);
    }
}
