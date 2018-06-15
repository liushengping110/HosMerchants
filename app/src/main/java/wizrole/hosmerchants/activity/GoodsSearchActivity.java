//package wizrole.hosmerchants.activity;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import wizrole.hosmerchants.R;
//import wizrole.hosmerchants.adapter.HostoryEditAdapter;
//import wizrole.hosmerchants.base.BaseActivity;
//import wizrole.hosmerchants.merchants.preserent.search.SearchPreserent;
//import wizrole.hosmerchants.merchants.view.SearchActivity;
//import wizrole.hosmerchants.ui.pop.PopDissListener;
//import wizrole.hosmerchants.ui.pop.PopupWindowPotting;
//
///**
// * Created by liushengping on 2018/1/25.
// * 何人执笔？
// */
//
//public class GoodsSearchActivity extends BaseActivity implements PopDissListener,View.OnClickListener {
//    private boolean finishing;
//    public Dialog dialog;
//    public HostoryEditAdapter adapter;
//    public Handler mHandler;
//    public SearchTask mSearchTesk;
//
//    @BindView(R.id.tv_search_bg)TextView mSearchBGTxt;
//    @BindView(R.id.tv_search)TextView mSearchTxt;
//    @BindView(R.id.tv_hint)EditText mHintTxt;
//    @BindView(R.id.frame_content_bg)LinearLayout mContentFrame;
//    @BindView(R.id.iv_arrow)ImageView mArrowImg;
//    @BindView(R.id.img_del)ImageView img_del;
//    @BindView(R.id.list_histroy)ListView list_histroy;
//    @Override
//    protected int getLayout() {
//        return 0;
//    }
//
//    @Override
//    protected void initData() {
//        ButterKnife.bind(this);
//        mSearchBGTxt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mSearchBGTxt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                performEnterAnimation();
//            }
//        });
//        setView();
//        initPopupWindow();
//        mHandler = new Handler();
//        mSearchTesk = new SearchTask();
//        showSoftInputFromWindow(GoodsSearchActivity.this,mHintTxt);
//    }
//
//    /**
//     * EditText获取焦点并显示软键盘
//     */
//    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//    }
//    public void setView(){
//        list=getHostry();
//        //设置适配器
//        adapter=new HostoryEditAdapter(GoodsSearchActivity.this,list,R.layout.list_item_edithostry);
//        list_histroy.setAdapter(adapter);
//    }
//    public boolean item_status=false;//点击历史记录item标记
//    @Override
//    protected void setListener() {
//        mArrowImg.setOnClickListener(this);
//        mSearchTxt.setOnClickListener(this);
//        img_del.setOnClickListener(this);
//        list_histroy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MerchantsHos hos=list.get(position);
//                if(!hos.getContent().equals("无")){
//                    item_status=true;
//                    dialog=LoadingDailog.createLoadingDialog(SearchActivity.this,"加载中");
//                    searchPreserent.searchGetInfor(hos.getContent());
//                }
//            }
//        });
//        mHintTxt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length() > 0) {
//                    mHandler.removeCallbacks(mSearchTesk);
//                    mHandler.postDelayed(mSearchTesk,300);
//                } else {
//                    mHandler.removeCallbacks(mSearchTesk);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//    @Override
//    public void onDiss() {
//        mSearchTxt.setText("搜索");
//        item_status=false;//点击历史记录不存储
//    }
//    /**
//     * 搜索任务
//     */
//    class SearchTask implements Runnable {
//        @Override
//        public void run() {
//            String msg=mHintTxt.getText().toString();
//            handler.sendEmptyMessage(4);
//            searchPreserent.searchGetInfor(msg);
//        }
//    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_arrow://返回
//                if (!finishing) {
//                    finishing = true;
//                    performExitAnimation();
//                }
//                break;
//            case R.id.tv_search://搜索
//                if (mSearchTxt.getText().toString().equals("取消")) {//取消
//                    popupWindowPotting.Hide();
//                    mHintTxt.setText("");
//                    mSearchTxt.setText("搜索");
//                } else {//搜索
//                    if (mHintTxt.getText().toString().length() == 0) {
//                        ToastShow("请输入要搜索的店铺名或商品");
//                    } else {
//                        //关闭软键盘
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(mHintTxt.getWindowToken(), 0);
//                        dialog = LoadingDailog.createLoadingDialog(SearchActivity.this, "加载中");
//                        searchPreserent.searchGetInfor(mHintTxt.getText().toString());//请求
//                        addDB(mHintTxt.getText().toString());
//                    }
//                }
//                break;
//            case R.id.img_del://删除
//                delHostry();
//                break;
//        }
//    }
//
//        public List<MerchantsHos> list;
//        /***获取存储的集合*/
//        public boolean status=false;//历史纪录集合是为0状态
//        public List<MerchantsHos> getHostry() {
//            if(SearchDisReader.searchInfors(this).size()>0){
//                list=SearchDisReader.searchInfors(this);
//            }else{//数据库为空
//                status=true;
//                list=new ArrayList<MerchantsHos>();
//                MerchantsHos hostry=new MerchantsHos();
//                hostry.setContent("无");
//                list.add(hostry);
//            }
//            return list;
//        }
//
//        private void performEnterAnimation() {
//            float originY = getIntent().getIntExtra("y", 0);
//            int location[] = new int[2];
//            mSearchBGTxt.getLocationOnScreen(location);
//            final float translateY = originY - (float) location[1];
//            //放到前一个页面的位置
//            mSearchBGTxt.setY(mSearchBGTxt.getY() + translateY);
//            mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
//            mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
//            float top = getResources().getDisplayMetrics().density * 20;
//            final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), top);
//            translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
//                    mArrowImg.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
//                    mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
//                    mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
//                }
//            });
//
//            ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
//            scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
//                }
//            });
//
//            ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
//            alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
//                    mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
//                    mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
//                }
//            });
//
//            alphaVa.setDuration(500);
//            translateVa.setDuration(500);
//            scaleVa.setDuration(500);
//
//            alphaVa.start();
//            translateVa.start();
//            scaleVa.start();
//
//        }
//
//        @Override
//        public void onBackPressed() {
//            if (!finishing){
//                finishing = true;
//                performExitAnimation();
//            }
//        }
//
//        private void performExitAnimation() {
//            float originY = getIntent().getIntExtra("y", 0);
//
//            int location[] = new int[2];
//            mSearchBGTxt.getLocationOnScreen(location);
//
//            final float translateY = originY - (float) location[1];
//
//
//            final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY()+translateY);
//            translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
//                    mArrowImg.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
//                    mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
//                    mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
//                }
//            });
//            translateVa.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    finish();
//                    overridePendingTransition(0, 0);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            });
//
//            ValueAnimator scaleVa = ValueAnimator.ofFloat(0.8f, 1f);
//            scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
//                }
//            });
//
//            ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
//            alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
//
//                    mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
//                    mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
//                }
//            });
//
//
//            alphaVa.setDuration(500);
//            translateVa.setDuration(500);
//            scaleVa.setDuration(500);
//
//            alphaVa.start();
//            translateVa.start();
//            scaleVa.start();
//
//        }
//
//        public void delHostry(){
//            if(list.size()>0){
//                if(list.size()>1){
//                    list.clear();
//                    MerchantsHos hos = new MerchantsHos();
//                    hos.setContent("无");
//                    list.add(hos);
//                    adapter.notifyDataSetChanged();
//                    list=SearchDisReader.searchInfors(SearchActivity.this);
//                    for(int i=0;i<list.size();i++){
//                        MerchantsHos hostry=list.get(i);
//                        SearchDisReader.deleteInfors(hostry,SearchActivity.this);
//                    }
//                }else{
//                    if(!list.get(0).getContent().equals("无")){
//                        list.clear();
//                        MerchantsHos hostry=new MerchantsHos();
//                        hostry.setContent("无");
//                        list.add(hostry);
//                        adapter.notifyDataSetChanged();
//                        list=SearchDisReader.searchInfors(SearchActivity.this);
//                        for(int i=0;i<list.size();i++){
//                            MerchantsHos h=list.get(i);
//                            SearchDisReader.deleteInfors(h,SearchActivity.this);
//                        }
//                    }
//                }
//            }
//        }
//
//        /**
//         * 初始化Pop，pop的布局是一个列表
//         */
//        private List<StoreList> mSearchList = new ArrayList<>(); //搜索结果的数据源
//        public ListView searchLv;
//        public TextView text_pop_msg;
//        public SearchShowAdapter search_adapter;
//        public PopupWindowPotting popupWindowPotting;
//        public void initPopupWindow(){
//            if (popupWindowPotting==null){
//                popupWindowPotting=new PopupWindowPotting(SearchActivity.this,1) {
//                    @Override
//                    protected int getLayout() {
//                        return R.layout.search_pop_list;
//                    }
//                    @Override
//                    protected void initUI(){
//                        searchLv = $(R.id.search_list_lv);
//                        text_pop_msg = $(R.id.text_pop_msg);
//                    }
//                    @Override
//                    protected void setListener() {
//                        searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                if(item_status){
//                                    item_status=false;
//                                }else{
//                                    addDB(mSearchList.get(position).getStoreName());
//                                }
//                                ToastShow(mSearchList.get(position).getStoreName());
//                                Intent intent=new Intent(SearchActivity.this, MyStoreViewActivity.class);
//                                Bundle bundle=new Bundle();
//                                bundle.putSerializable("storeinfor",mSearchList.get(position));
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            }
//                        });
//                    }
//                };
//            }
//        }
//        public void showPop(int type){
//            switch (type){
//                case 1:
//                    searchLv.setVisibility(View.VISIBLE);
//                    text_pop_msg.setVisibility(View.INVISIBLE);
//                    if(search_adapter==null){
//                        mSearchList.addAll(storeLists);
//                        search_adapter=new SearchShowAdapter(SearchActivity.this,mSearchList,R.layout.list_store_item);
//                        searchLv.setAdapter(search_adapter);
//                    }else{
//                        mSearchList.clear(); //先清空数据源
//                        mSearchList.addAll(storeLists);
//                        search_adapter.notifyDataSetChanged();
//                    }
//                    break;
//                case 2://无结果
//                    searchLv.setVisibility(View.INVISIBLE);
//                    text_pop_msg.setText("很抱歉，当前搜索无结果");
//                    text_pop_msg.setVisibility(View.VISIBLE);
//                    break;
//                case 3://网络失败
//                    searchLv.setVisibility(View.INVISIBLE);
//                    text_pop_msg.setText("网络连接失败，请检查网络");
//                    text_pop_msg.setVisibility(View.VISIBLE);
//                    break;
//            }
//            popupWindowPotting.Show(mHintTxt);
//            mSearchTxt.setText("取消");
//        }
//
//        public void addDB(String content){
//            if(status){//如果是添加第一条记录，就先删除那个【无】
//                list.clear();
//                status=false;
//            }
//            MerchantsHos hostry=new MerchantsHos();
//            hostry.setContent(content);
//            SearchDisReader.addInfors(hostry,SearchActivity.this);//存储
//            list.add(hostry);
//            adapter.notifyDataSetChanged();//刷新适配器
//
//
////        for (int i=0;i<list.size();i++){
////            if(content.equals(list.get(i).getContent())){
////                return;
////            }else{
////                if((i+1)==list.size()){
////
////                }
////            }
////        }
//        }
//}
