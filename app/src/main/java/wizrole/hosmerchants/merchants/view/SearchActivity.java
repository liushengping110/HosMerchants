package wizrole.hosmerchants.merchants.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.MerchantsListAdapter;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.merchants.model.search.MerchantsHos;
import wizrole.hosmerchants.merchants.preserent.search.SearchInterface;
import wizrole.hosmerchants.merchants.preserent.search.SearchPreserent;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.ui.pop.CustomPopupWindow;
import wizrole.hosmerchants.ui.pop.PopDissListener;
import wizrole.hosmerchants.util.db.SearchStoreReader;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.image.ImageLoading;
import wizrole.hosmerchants.view.CustTextView;

/**
 * Created by liushengping on 2017/12/7/007.
 * 何人执笔？
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener,SearchInterface,PopDissListener{
    private boolean finishing;
    public Dialog dialog;
    public Handler mHandler;
    public SearchTask mSearchTesk;
    public SearchPreserent searchPreserent=new SearchPreserent(this);

    @BindView(R.id.tv_search_bg)TextView mSearchBGTxt;
    @BindView(R.id.tv_search)TextView mSearchTxt;
    @BindView(R.id.tv_hint)EditText mHintTxt;
    @BindView(R.id.frame_content_bg)LinearLayout mContentFrame;
    @BindView(R.id.iv_arrow)ImageView mArrowImg;
    @BindView(R.id.img_del)ImageView img_del;
    @BindView(R.id.list_histroy)CustTextView list_histroy;
    @Override
    protected int getLayout() {
        return R.layout.activity_store_search;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        mSearchBGTxt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSearchBGTxt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                performEnterAnimation();
            }
        });
        list=getHostry();//获取历史记录
        setView();
        initPop();
        mHandler = new Handler();
        mSearchTesk = new SearchTask();
        showSoftInputFromWindow(SearchActivity.this,mHintTxt);
    }

    public void getSearch(String msg){
        searchPreserent.searchGetInfor(msg);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    public void  setView(){
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,90);
        params.setMargins(20,10,20,10);
        if(list_histroy.getChildCount()>1){
            list_histroy.removeAllViews();
        }
        for (int i=0;i<list.size();i++){
            TextView textView=new TextView(SearchActivity.this);
            textView.setText(list.get(i).getContent());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(13);
            textView.setPadding(20,10,20,10);
            textView.setMinWidth(120);
            textView.setLayoutParams(params);
            textView.setTextColor(getResources().getColor(R.color.shenhui));
            textView.setBackgroundColor(getResources().getColor(R.color.qrea_bg));
            textView.setOnClickListener(new ButtonItem(textView.getText().toString()));
            list_histroy.addView(textView);
        }
    }
    class ButtonItem implements View.OnClickListener{
        public String msg;
        public ButtonItem(String msg){
            this.msg=msg;
        }
        @Override
        public void onClick(View v) {
            if(!msg.equals("无")){
                item_status=true;
                dialog=LoadingDailog.createLoadingDialog(SearchActivity.this,"加载中");
                mHintTxt.setText(msg);
                getSearch(msg);
            }
        }
    }

    public boolean item_status=false;//点击历史记录item标记
    @Override
    protected void setListener() {
        mArrowImg.setOnClickListener(this);
        mSearchTxt.setOnClickListener(this);
        img_del.setOnClickListener(this);
        mHintTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    mHandler.removeCallbacks(mSearchTesk);
                    mHandler.postDelayed(mSearchTesk,300);
                } else {
                    mHandler.removeCallbacks(mSearchTesk);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDiss() {
        mSearchTxt.setText("搜索");
        item_status=false;//点击历史记录不存储
    }

    /**
     * 搜索任务
     */
    class SearchTask implements Runnable {
        @Override
        public void run() {
            String msg=mHintTxt.getText().toString();
            handler.sendEmptyMessage(4);
            getSearch(msg);
        }
    }

    public List<StoreList> storeLists;
    @Override
    public void getSearchInforSucc(MyStoreInforBack myStoreInforBack) {
        storeLists=myStoreInforBack.getStoreList();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getSearchInforFail(String msg) {
        if(msg.equals("null")){
            handler.sendEmptyMessage(1);//店铺搜索无结果
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
                    LoadingDailog.closeDialog(dialog);
                    mSearchTxt.setEnabled(true);
                    showPop(1);
                    break;
                case 1://无结果
                    LoadingDailog.closeDialog(dialog);
                    mSearchTxt.setEnabled(true);
                    showPop(2);
                    ToastShow("当前搜索无结果，请重新输入");
                    break;
                case 2://断网
                    LoadingDailog.closeDialog(dialog);
                    mSearchTxt.setEnabled(true);
                    showPop(3);
                    ToastShow("网络连接失败，请检查网络");
                    break;
                case 4://请求中 禁止点击
                    dialog=LoadingDailog.createLoadingDialog(SearchActivity.this,"加载中");
                    progress_bar.setVisibility(View.VISIBLE);
                    mSearchTxt.setEnabled(false);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_arrow://返回
                if(mPop.isShowing()){
                    mPop.dismiss();
                }else {
                    if (!finishing){
                        finishing = true;
                        performExitAnimation();
                    }
                }
                break;
            case R.id.tv_search://搜索
                if(mSearchTxt.getText().toString().equals("取消")){//取消
                    mPop.dismiss();
                    mHintTxt.setText("");
                    mSearchTxt.setText("搜索");
                }else{//搜索
                    if(mHintTxt.getText().toString().length()==0){
                        ToastShow("请输入要搜索的店铺名或商品");
                    }else{
                        //关闭软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mHintTxt.getWindowToken(), 0) ;
                        dialog= LoadingDailog.createLoadingDialog(SearchActivity.this,"加载中");
                        getSearch(mHintTxt.getText().toString());//请求
                        addDB(mHintTxt.getText().toString());
                    }
                }
                break;
            case R.id.img_del://删除
                delHostry();
                break;
        }
    }

    public List<MerchantsHos> list;
    /***获取存储的集合*/
    public boolean status=false;//历史纪录集合是为0状态
    public List<MerchantsHos> getHostry() {
        if(SearchStoreReader.searchInfors(this).size()>0){
            list= SearchStoreReader.searchInfors(this);
        }else{//数据库为空
            status=true;
            list=new ArrayList<MerchantsHos>();
            MerchantsHos hostry=new MerchantsHos();
            hostry.setContent("无");
            list.add(hostry);
        }
        return list;
    }


    private void performEnterAnimation() {
        float originY = getIntent().getIntExtra("y", 0);
        int location[] = new int[2];
        mSearchBGTxt.getLocationOnScreen(location);
        final float translateY = originY - (float) location[1];
        //放到前一个页面的位置
        mSearchBGTxt.setY(mSearchBGTxt.getY() + translateY);
        mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
        mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
        float top = getResources().getDisplayMetrics().density * 20;
        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), top);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
                mArrowImg.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
                mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        alphaVa.setDuration(500);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        translateVa.start();
        scaleVa.start();

    }

    @Override
    public void onBackPressed() {
        if(mPop.isShowing()){
            mPop.dismiss();
        }else {
            if (!finishing){
                finishing = true;
                performExitAnimation();
            }
        }
    }

    private void performExitAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGTxt.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];


        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY()+translateY);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
                mArrowImg.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
            }
        });
        translateVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(0.8f, 1f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());

                mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });


        alphaVa.setDuration(500);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        translateVa.start();
        scaleVa.start();

    }

    public void delHostry(){
        if(list.size()>0){
            if(list.size()>1){
                list.clear();
                MerchantsHos hos = new MerchantsHos();
                hos.setContent("无");
                list.add(hos);
                setView();
                list= SearchStoreReader.searchInfors(SearchActivity.this);
                for(int i=0;i<list.size();i++){
                    MerchantsHos hostry=list.get(i);
                    SearchStoreReader.deleteInfors(hostry,SearchActivity.this);
                }
            }else{
                if(!list.get(0).getContent().equals("无")){
                    list.clear();
                    MerchantsHos hostry=new MerchantsHos();
                    hostry.setContent("无");
                    list.add(hostry);
                    setView();
                    list= SearchStoreReader.searchInfors(SearchActivity.this);
                    for(int i=0;i<list.size();i++){
                        MerchantsHos h=list.get(i);
                        SearchStoreReader.deleteInfors(h,SearchActivity.this);
                    }
                }
            }
        }
    }

    /**
     * 初始化Pop，pop的布局是一个列表
     */
    private List<StoreList> mSearchList = new ArrayList<>(); //搜索结果的数据源
    public ListView searchLv;
    public TextView text_pop_msg;
    public TextView text_err_agagin;
    public ImageView img_net_err;
    public ProgressBar progress_bar;
    public MerchantsListAdapter search_adapter;

    public void showPop(int type){
        switch (type){
            case 1://搜索成功
                searchLv.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.INVISIBLE);
                text_pop_msg.setVisibility(View.INVISIBLE);
                img_net_err.setVisibility(View.INVISIBLE);
                text_err_agagin.setVisibility(View.INVISIBLE);
                if(search_adapter==null){
                    mSearchList.addAll(storeLists);
                    search_adapter=new MerchantsListAdapter(SearchActivity.this,mSearchList,R.layout.recy_merchants_item);
                    searchLv.setAdapter(search_adapter);
                }else{
                    mSearchList.clear(); //先清空数据源
                    mSearchList.addAll(storeLists);
                    search_adapter.notifyDataSetChanged();
                }
                break;
            case 2://无结果
                progress_bar.setVisibility(View.INVISIBLE);
                searchLv.setVisibility(View.INVISIBLE);
                ImageLoading.common(SearchActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                img_net_err.setVisibility(View.VISIBLE);
                text_err_agagin.setVisibility(View.INVISIBLE);
                text_pop_msg.setText("很抱歉，当前搜索无结果");
                text_pop_msg.setVisibility(View.VISIBLE);
                break;
            case 3://网络失败
                progress_bar.setVisibility(View.INVISIBLE);
                searchLv.setVisibility(View.INVISIBLE);
                text_pop_msg.setVisibility(View.INVISIBLE);
                ImageLoading.common(SearchActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                img_net_err.setVisibility(View.VISIBLE);
                text_err_agagin.setVisibility(View.VISIBLE);
                break;
        }
        showSoftInputFromWindow(SearchActivity.this,mHintTxt);
        mSearchTxt.setText("搜索");
        mPop.showAsDropDown(mHintTxt, 0, 50); //显示搜索联想列表的pop
    }

    /**
     * 初始化Pop，pop的布局是一个列表
     */
    private CustomPopupWindow mPop; //显示搜索联想的pop
    private void initPop() {
        mPop = new CustomPopupWindow.Builder(this)
                .setContentView(R.layout.search_pop_list)
                .setwidth(LinearLayout.LayoutParams.MATCH_PARENT)
                .setheight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setBackgroundAlpha(1f)
                .build();
        progress_bar = (ProgressBar) mPop.getItemView(R.id.progress_bar);
        searchLv = (ListView) mPop.getItemView(R.id.search_list_lv);
        text_pop_msg = (TextView) mPop.getItemView(R.id.text_pop_msg);
        text_err_agagin = (TextView) mPop.getItemView(R.id.text_err_agagin);
        img_net_err = (ImageView) mPop.getItemView(R.id.img_net_err);
        text_err_agagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHintTxt.getText().toString().length()==0){
                    ToastShow("请输入要搜索的店铺名或商品");
                }else{
                    //关闭软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mHintTxt.getWindowToken(), 0) ;
                    dialog= LoadingDailog.createLoadingDialog(SearchActivity.this,"加载中");
                    getSearch(mHintTxt.getText().toString());//请求
                }
            }
        });
        searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(item_status){
                    item_status=false;
                }else{
                    addDB(mSearchList.get(position).getStoreName());
                }
                ToastShow(mSearchList.get(position).getStoreName());
                Intent intent=new Intent(SearchActivity.this, MyStoreViewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("storeinfor",mSearchList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        searchLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //关闭软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mHintTxt.getWindowToken(), 0) ;
                return false;
            }
        });
    }

    public void addDB(String content){
        if(status){//如果是添加第一条记录，就先删除那个【无】
            list.clear();
            status=false;
        }
        if(list.size()==0){
            MerchantsHos hostry=new MerchantsHos();
            hostry.setContent(content);
            SearchStoreReader.addInfors(hostry,SearchActivity.this);//存储
            list.add(hostry);
        }else if(list.size()==1){
            if (!list.get(0).getContent().equals(content)){
                MerchantsHos hostry=new MerchantsHos();
                hostry.setContent(content);
                SearchStoreReader.addInfors(hostry,SearchActivity.this);//存储
                list.add(hostry);
            }
        }else{
            for (int a=0;a<list.size();a++){
                if(a<(list.size()-1)){
                    if(list.get(a).getContent().equals(content)){
                        return;
                    }
                }else{
                    if(!list.get(a).getContent().equals(content)){
                        MerchantsHos hostry=new MerchantsHos();
                        hostry.setContent(content);
                        SearchStoreReader.addInfors(hostry,SearchActivity.this);//存储
                        list.add(hostry);
                    }
                }
            }
        }
        setView();
    }
}
