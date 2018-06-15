package wizrole.hosmerchants.merchants.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.MerchantsAdapter;
import wizrole.hosmerchants.adapter.MerchantsListAdapter;
import wizrole.hosmerchants.base.MyApplication;
import wizrole.hosmerchants.merchants.preserent.getallstore.GetAllStoreInterface;
import wizrole.hosmerchants.merchants.preserent.getallstore.GetAllStorePreserent;
import wizrole.hosmerchants.merchants.preserent.getsecondstore.GetSecondStoreInterface;
import wizrole.hosmerchants.merchants.preserent.getsecondstore.GetSecondStorePreserent;
import wizrole.hosmerchants.merchants.view.MyStoreViewActivity;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.util.image.ImageLoading;
import wizrole.hosmerchants.view.LoadingView;
import wizrole.hosmerchants.view.UserFooterView;
import wizrole.hosmerchants.view.UserHeaderView;

/**
 * Created by liushengping on 2018/1/27.
 * 何人执笔？
 * 单个类型商铺分类
 * 如：美食下，二级分类--小吃、烤串等等
 */

public class Fragment_SingleStoreType extends LazyFragment implements GetSecondStoreInterface {

    public static final String INTENT_INT_INDEX="index";
    private int tabIndex;
    public LoadingView loadingView;
    public LinearLayout lin_wifi_err;
    public ImageView img_net_err;
    public RelativeLayout rel_content;
    public TextView text_err_agagin_center;
    public RefreshLayout refreshView;
    public String id;//二级分类名id
    public int page=1;
    public GetSecondStorePreserent getAllStorePreserent=new GetSecondStorePreserent(this);
    public static Fragment_SingleStoreType newInstance(int tabIndex, boolean isLazyLoad) {
        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        Fragment_SingleStoreType fragment = new Fragment_SingleStoreType();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_singlestoretype);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        Bundle bundle = getArguments();
        id= bundle.getString("two_type_id");
        initUI();
        getData(id,page);
        setListener();
    }
    public RecyclerView recy_view;
    public void initUI(){
        recy_view=(RecyclerView)findViewById(R.id.recy_view);
        recy_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadingView=(LoadingView)findViewById(R.id.loadView_cener);
        loadingView.addBitmap(R.drawable.icon_chicken);
        loadingView.addBitmap(R.drawable.icon_flower);
        loadingView.addBitmap(R.drawable.icon_orange);
        loadingView.addBitmap(R.drawable.icon_ufo);
        loadingView.addBitmap(R.drawable.icon_pear);
        loadingView.setShadowColor(Color.LTGRAY);
        loadingView.setDuration(700);
        loadingView.start();
        text_err_agagin_center=(TextView)findViewById(R.id.text_err_agagin_center);
        img_net_err=(ImageView) findViewById(R.id.img_net_err);
        rel_content=(RelativeLayout) findViewById(R.id.rel_content);
        lin_wifi_err=(LinearLayout) findViewById(R.id.lin_wifi_err);
        refreshView=(RefreshLayout) findViewById(R.id.xrefreshview);
        refreshView.setRefreshHeader(new UserHeaderView(getActivity()));
        refreshView.setRefreshFooter(new UserFooterView(getActivity()));
        refreshView.setHeaderHeightPx(300);
        refreshView.setFooterHeightPx(100);
        setNetErr();
    }
    public void getData(String id_type,int page){
        getAllStorePreserent.getSecondStore(id_type,page);
    }

    /***
     * 获取所有商家列表
     * @param getAllStoreBack
     */
    public List<StoreList> storeLists;
    public  List<StoreList> all_storeLists=new ArrayList<>();
    public int totalNum;//最大页码

    @Override
    public void GetSecondStoreSucc(MyStoreInforBack myStoreInforBack) {
        storeLists=myStoreInforBack.getStoreList();
        totalNum=myStoreInforBack.getTotalNum();
        handler.sendEmptyMessage(3);
    }

    @Override
    public void GetSecondStoreFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(4);
        }else{
            handler.sendEmptyMessage(5);
        }
    }

    public MerchantsAdapter adapter;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 3://获取成功
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    rel_content.setVisibility(View.VISIBLE);
                    recy_view.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.INVISIBLE);
                    if(page==1){
                        all_storeLists.clear();
                        all_storeLists.addAll(storeLists);
                        adapter=new MerchantsAdapter(getActivity(),all_storeLists);
                        recy_view.setAdapter(adapter);
                        refreshView.finishRefresh();
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        all_storeLists.addAll(storeLists);
                        adapter.notifyDataSetChanged();
                        refreshView.finishLoadmore();
                    }
                    adapter.setOnItemClickListener(itemOnClick);
                    setNetNormal();
                    break;
                case 4://失败
                    if(page==1){
                        refreshView.finishRefresh();
                        recy_view.setVisibility(View.INVISIBLE);
                        loadingView.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.white));
                        text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                        text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.try_again));
                        ImageLoading.common(MyApplication.getContextObject(),R.drawable.net_err,img_net_err,R.drawable.net_err);
                        setNetErr();
                    }else{
                        refreshView.finishLoadmore();
                        setNetNormal();
                    }
                    Toast.makeText(MyApplication.getContextObject(), "数据获取失败，请检查网络", Toast.LENGTH_LONG).show();
                    break;
                case 5://无此类别店铺
                    setNetErr();
                    recy_view.setVisibility(View.INVISIBLE);
                    loadingView.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.huise));                    text_err_agagin_center.setBackgroundResource(R.color.white);
                    text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.null_data));
                    ImageLoading.common(MyApplication.getContextObject(),R.drawable.null_data,img_net_err,R.drawable.null_data);
                    Toast.makeText(getActivity(), "很抱歉，当前暂无此类店铺", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public void setListener(){
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page=1;
                getData(id,page);
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<totalNum){
                    page++;
                    getData(id,page);
                }else{
                    Toast.makeText(MyApplication.getContextObject(),"已经没有更多数据了",Toast.LENGTH_LONG).show();
                    refreshlayout.finishLoadmoreWithNoMoreData();
                }
            }
        });
        text_err_agagin_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_err_agagin_center.getText().toString().equals(MyApplication.getContextObject().getString(R.string.try_again))){
                    page=1;
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    loadingView.setVisibility(View.VISIBLE);
                    getData(id,page);
                }
            }
        });
    }

    public MerchantsAdapter.OnRecyclerViewItemClickListener itemOnClick=new MerchantsAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int viewType) {
            Intent intent=new Intent(getActivity(), MyStoreViewActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("storeinfor",all_storeLists.get(viewType));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };


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
}
