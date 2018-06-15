package wizrole.hosmerchants.merchants.view;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.SingleStoreFragAdapter;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.base.MyApplication;
import wizrole.hosmerchants.merchants.model.getsecondtypename.GetSecondTypeNameBack;
import wizrole.hosmerchants.merchants.model.getsecondtypename.TwoTypeList;
import wizrole.hosmerchants.merchants.preserent.getsecondtypename.GetSecondTypeNameInterface;
import wizrole.hosmerchants.merchants.preserent.getsecondtypename.GetSecondTypeNamePreserent;
import wizrole.hosmerchants.merchants.view.fragment.Fragment_SingleStoreType;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2018/1/27.
 * 何人执笔？
 * 单个类型的
 */

public class SingleStoreTypeActivity extends BaseActivity implements GetSecondTypeNameInterface{
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.tab_layout)TabLayout tab_layout;
    @BindView(R.id.vp_content)ViewPager viewPager;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.lin_content)LinearLayout lin_content;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_err_agagin_center)TextView text_err_agagin_center;
    public List<Fragment> fragments;
    public List<TwoTypeList> tabList;
    public String type;
    public Dialog dialog;
    public GetSecondTypeNamePreserent getSecondTypeNamePreserent= new GetSecondTypeNamePreserent(this);
    @Override
    protected int getLayout() {
        return R.layout.activity_singlestoretype;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        type=getIntent().getStringExtra("store_type");
        text_title.setText(type);
        getTabLayoutName(type);//获取TabLayout的名字
        reflex();
    }
    public void getTabLayoutName(String type_num){
        dialog= LoadingDailog.createLoadingDialog(SingleStoreTypeActivity.this,"加载中");
        getSecondTypeNamePreserent.GetSecondTypeName(type_num);
    }

    public void setViewPager(){
        fragments=new ArrayList<Fragment>();
        for(int i=0;i<tabList.size();i++){
            if(i==0){
                fragments.add(Fragment_SingleStoreType.newInstance(1,false));
            }else{
                fragments.add(Fragment_SingleStoreType.newInstance(i+1,true));
            }
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        SingleStoreFragAdapter adapter=new SingleStoreFragAdapter(tabList,fragments,supportFragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tab_layout.setupWithViewPager(viewPager);
    }
    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getSecondTypeNameSucc(GetSecondTypeNameBack getSecondTypeNameBack) {
        tabList=getSecondTypeNameBack.getTwoTypeList();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getSecondTypeNameFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(1);
        }else{
            handler.sendEmptyMessage(2);
        }
    }
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://成功
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    setViewPager();
                    initTab();
                    lin_content.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    break;
                case 1://网络错误
                    lin_content.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(SingleStoreTypeActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.try_again));
                    text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.white));
                    text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                    LoadingDailog.closeDialog(dialog);
                    break;
                case 2://无二级分类名
                    lin_content.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(SingleStoreTypeActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.null_data));
                    text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.huise));
                    text_err_agagin_center.setBackgroundResource(R.color.white);
                    LoadingDailog.closeDialog(dialog);
                    break;
            }
        }
    };

    //设置taLayout的下划线宽度
    public void reflex(){
        tab_layout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tab_layout,10,10);
            }
        });
    }
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /***
     * 设置TabLayout的字体样式
     */
    public void initTab(){
    for (int i = 0; i < tabList.size(); i++) {
        TabLayout.Tab tab = tab_layout.getTabAt(i);
        if (tab != null) {
            tab.setCustomView(getTabView(i));
        }
    }
    updateTabTextView(tab_layout.getTabAt(tab_layout.getSelectedTabPosition()), true);
    tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            updateTabTextView(tab, true);

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            updateTabTextView(tab, false);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });
}

    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this).inflate(R.layout.single_store_tab, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(tabList.get(currentPosition).getTwoTypeName());
        return view;
    }

    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {
        if (isSelect) {
            for (int i = 0; i < tabList.size(); i++) {
                TabLayout.Tab all_tab = tab_layout.getTabAt(i);
                if (all_tab != null) {
                    TextView tabSelect = (TextView) all_tab.getCustomView().findViewById(R.id.tab_item_textview);
                    tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    tabSelect.setAlpha(0.5f);
                }
            }
            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setText(tab.getText());
            tabSelect.setAlpha(1.0f);
        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setText(tab.getText());
            tabUnSelect.setAlpha(0.6f);

        }
    }
}
