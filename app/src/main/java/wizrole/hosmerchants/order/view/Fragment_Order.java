package wizrole.hosmerchants.order.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.OrderFragAdapter;
import wizrole.hosmerchants.adapter.OrderListAdapter;
import wizrole.hosmerchants.order.model.orderlist.OrderContent;
import wizrole.hosmerchants.order.model.orderlist.OrderList;
import wizrole.hosmerchants.order.model.orderlist.OrderListBack;
import wizrole.hosmerchants.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/11/27/027.
 * 何人执笔？
 * 商家fragment页面
 */

public class Fragment_Order extends Fragment {

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public View  view;
    @BindView(R.id.tabLayout)TabLayout tabLayout;
    @BindView(R.id.view_pager)ViewPager view_pager;
    @BindView(R.id.pro_main)ProgressBar pro_main;
    @BindView(R.id.lin_content)LinearLayout lin_content;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_order,null);
            initUI();
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
            setView();
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
        ButterKnife.bind(this,view);
    }

    public List<String> tabList=new ArrayList<>();
    public List<Fragment> fragments;
    public void  setView(){
        tabList.add("待付款");
        tabList.add("待配送");
        tabList.add("待送达");
        tabList.add("待评价");
        tabList.add("已完成");
        fragments=new ArrayList<Fragment>();
        for(int i=0;i<5;i++){
            if(i==0){
                fragments.add(Fragment_OrderItem.newInstance(1,false));
            }else{
                fragments.add(Fragment_OrderItem.newInstance(i+1,true));
            }
        }
        FragmentManager supportFragmentManager = getChildFragmentManager();
        OrderFragAdapter adapter=new OrderFragAdapter(tabList,fragments,supportFragmentManager);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(view_pager);
        pro_main.setVisibility(View.INVISIBLE);
        lin_content.setVisibility(View.VISIBLE);
    }
}
