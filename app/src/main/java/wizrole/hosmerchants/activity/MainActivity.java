package wizrole.hosmerchants.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.FragmentAdapter;
import wizrole.hosmerchants.admin.view.Fragment_Admin;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.merchants.view.Fragment_Merchants;
import wizrole.hosmerchants.my.view.Fragment_My;
import wizrole.hosmerchants.order.view.Fragment_Order;
import wizrole.hosmerchants.release.view.ReleaseGoodsActivity;
import wizrole.hosmerchants.util.DensityUtil;
import wizrole.hosmerchants.util.SharedPreferenceUtil;

/**
 * Created by liushengping on 2017/11/27/027.
 * 何人执笔？
 */
public class MainActivity extends BaseActivity implements View.OnClickListener,Fragment_Merchants.GetView ,Fragment_Merchants.ChangeBg{
    @BindView(R.id.admin)RadioButton admin;
    @BindView(R.id.my)RadioButton my;
    @BindView(R.id.merchants)RadioButton merchants;
    @BindView(R.id.order)RadioButton order;
    @BindView(R.id.view_pager)ViewPager view_pager;
    @BindView(R.id.img_release)ImageView img_release;
    @BindView(R.id.view_over)View view_over;
    public List<Fragment> fragments;
    public FragmentAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        initRadioButton();
        initViewPagerView();
    }

    /**
     * 设置RadioButton的宽高
     */
    public void initRadioButton(){
        initDrawable(merchants);
        initDrawable(order);
        initDrawable(admin);
        initDrawable(my);
    }

    /**
     * 设置主页fragment+ViewPager布局
     */
    public void initViewPagerView(){
        fragments=new ArrayList<>();
        Fragment_Merchants fragment_merchants=new Fragment_Merchants();
        fragment_merchants.setChangeBg(this);
        fragments.add(fragment_merchants);
        fragments.add(new Fragment_Order());
        fragments.add(new Fragment_Admin());
        fragments.add(new Fragment_My());
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(4);

    }

    @Override
    protected void setListener() {
        merchants.setOnClickListener(this);
        order.setOnClickListener(this);
        admin.setOnClickListener(this);
        my.setOnClickListener(this);
        img_release.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.merchants://商家
                view_pager.setCurrentItem(0);
                break;
            case R.id.order://订单
                view_pager.setCurrentItem(1);
                break;
            case R.id.admin://管理
                view_pager.setCurrentItem(2);
                break;
            case R.id.my:// 我的
                view_pager.setCurrentItem(3);
                break;
            case R.id.img_release://发布商品页面
                if(SharedPreferenceUtil.getLoginState(MainActivity.this)==1){
                    String status= SharedPreferenceUtil.getInforComplete(MainActivity.this);
                    switch (status){
                        case "0"://0-两个都完成
                            Intent intent=new Intent(MainActivity.this,ReleaseGoodsActivity.class);
                            startActivity(intent);
                            break;
                        case "1"://1-个人信息未完成 店铺完成
                            ToastShow("很抱歉，请先完善个人信息");
                            break;
                        case "2":// 2-个人完成,店铺未完成
                            ToastShow("很抱歉，请先完善店铺信息");
                            break;
                        case "3":// 3- 个人未完成,店铺未完成
                            ToastShow("很抱歉，请先完善个人和店铺信息");
                            break;
                        case ""://未登录
                            ToastShow("很抱歉，当前您暂未登录");
                            break;
                    }
                }else {
                    ToastShow("很抱歉，当前您暂未登录");
                }
                break;
        }
    }

    // 定义是否退出程序的标记
    private boolean isExit = false;
    // 定义接受用户发送信息的handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                // 标记用户不退出状态
                isExit = false;
            }
        }
    };

    // 监听手机的物理按键点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断用户是否点击的是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果isExit标记为false，提示用户再次按键
            if (!isExit) {
                isExit = true;
                ToastShow("再按一次，退出程序");
                // 如果用户没有在2秒内再次按返回键的话，就发送消息标记用户为不退出状态
                mHandler.sendEmptyMessageDelayed(0, 3000);
            }
            // 如果isExit标记为true，退出程序
            else {
                // 退出程序
                finish();
                System.exit(0);
            }
        }
        return false;
    }

    /**
     * 设置RadioButton的宽高
     * @param v
     */
    public void  initDrawable(RadioButton v){
        Drawable drawable = v.getCompoundDrawables()[1];
        drawable.setBounds(0,0, DensityUtil.dip2px(this,25), DensityUtil.dip2px(this,25));
        v.setCompoundDrawables(null,drawable,null,null);
    }

    public  LinearLayout main_tab2;
    public  LinearLayout main_tab1;
    public int topHeight;
    //一定是在此方法中获取布局的实际高度
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            topHeight = main_tab2.getBottom() - main_tab1.getHeight();
        }
    }

    public int getTopHeight() {
        return topHeight;
    }

    @Override
    public void getView(LinearLayout linearLayout_one, LinearLayout linearLayout_two) {
        main_tab1=linearLayout_one;
        main_tab2=linearLayout_two;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Util.isOnMainThread()&&!this.isFinishing()){
            Glide.with(this).pauseRequests();
        }
    }


    @Override
    public void change(boolean status) {
        if(status){
            view_over.setVisibility(View.VISIBLE);
        }else{
            view_over.setVisibility(View.INVISIBLE);
        }
    }
}
