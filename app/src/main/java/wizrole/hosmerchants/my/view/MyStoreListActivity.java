package wizrole.hosmerchants.my.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.merchants.view.MyStoreViewActivity;
import wizrole.hosmerchants.adapter.MyStoreListAdapter;
import wizrole.hosmerchants.admin.view.ChangeDelGoodsInforActivity;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.my.preserent.mystoreinfor.MyStoreInforInterface;
import wizrole.hosmerchants.my.preserent.mystoreinfor.MyStoreInforPreserent;
import wizrole.hosmerchants.release.view.GoodsTypeActivity;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 * 我的店铺列表
 *  ------个人所属店铺集合
 */

public class MyStoreListActivity extends BaseActivity implements MyStoreInforInterface{
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.list_store)ListView list_store;
    @BindView(R.id.text_err_msg)TextView text_msg;
    @BindView(R.id.text_err_agagin)TextView text_agagin;
    public Dialog dialog;
    public List<StoreList> storelist;
    public boolean status=false;//是否有店铺-是否网络请求失败标志位
    public static final int REQUEST_CODE=1;//添加店铺请求吗--修改店铺请求吗（二者共用）
    public static final int RESULT_CODE=2;//添加店铺结果吗--修改店铺结果吗
    public final int MYSTOREINFOR_RESULT_CODE=13;//添加店铺后，回调我的页面，显示隐藏完善
    public static final int RESULT_CODE_SELECT_STORE = 201;//店铺名
    public MyStoreInforPreserent myStoreInforPreserent = new MyStoreInforPreserent(this);
    public Intent intent;
    public String type;
    @Override
    protected int getLayout() {
        return R.layout.activity_mystorelist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("店铺列表");
        type=getIntent().getStringExtra("type");
        switch (type){
            case "fragment_me"://我的
                text_right.setText("添加店铺");
                break;
            case "release"://发布
                break;
            case "storelook"://店铺预览
                break;
            case "storegoodsinfor"://商品信息
                break;
            case "storegoodscate"://商品类别
                break;
        }
        getMyStoreList();
    }

    /**
     * 获取店铺列表
     */
    public void getMyStoreList(){
        dialog= LoadingDailog.createLoadingDialog(MyStoreListActivity.this,"加载中");
        String id= SharedPreferenceUtil.getID(MyStoreListActivity.this);
        myStoreInforPreserent.getMyStoreInfor(id);
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case "fragment_me"://我的进入--
                        setResult(MYSTOREINFOR_RESULT_CODE);
                        break;
                    case "release"://发布
                        break;
                    case "storelook"://店铺预览
                        break;
                    case "storegoodsinfor"://商品信息
                        break;
                    case "storegoodscate"://商品类别
                        break;
                }
                finish();
            }
        });
        //添加店铺
        text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//先确定以一级类
                Intent intent=new Intent(MyStoreListActivity.this,StoreCateActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        text_agagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status){//无店铺，点击添加
                    Intent intent=new Intent(MyStoreListActivity.this,StoreCateActivity.class);
                    startActivityForResult(intent,REQUEST_CODE);
                }else{//网络请求失败，点击重试
                    text_agagin.setVisibility(View.INVISIBLE);
                    text_msg.setVisibility(View.INVISIBLE);
                    getMyStoreList();
                }
            }
        });
        list_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreList storeList=storelist.get(position);
                Bundle bundle=new Bundle();
                switch (type){
                    case "fragment_me"://我的--进入
                        if(storeList.getStoreType().equals("月嫂护工")){
                            ToastShow("当前【月嫂护工】功能暂未落实接口");
//                            bundle.putSerializable("storeinfor",storeList);
//                            intent=new Intent(MyStoreListActivity.this,NursingWorkActivity.class);
//                            intent.putExtras(bundle);
                        }else if(storeList.getStoreType().equals("出院出行")){
                            ToastShow("当前【出院出行】功能暂未落实接口");
//                            bundle.putSerializable("storeinfor",storeList);
//                            intent=new Intent(MyStoreListActivity.this,TravelStoreActivity.class);
//                            intent.putExtras(bundle);
                        }else{
                            bundle.putSerializable("storeinfor",storeList);
                            intent=new Intent(MyStoreListActivity.this,MyStoreInforActivity.class);
                            intent.putExtras(bundle);
                            startActivityForResult(intent,REQUEST_CODE);
                        }
                        break;
                    case "release"://发布
                        sendStoreId(storeList.getStoreName(),storeList.getStoreNo());//发布商品---提交店铺id
                        break;
                    case "storelook"://店铺预览
                        intent=new Intent(MyStoreListActivity.this,MyStoreViewActivity.class);
                        bundle.putSerializable("storeinfor",storeList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case "storegoodsinfor"://商品信息
                        intent=new Intent(MyStoreListActivity.this,ChangeDelGoodsInforActivity.class);
                        intent.putExtra("StoreId",storeList.getStoreNo());
                        startActivity(intent);
                        break;
                    case "storegoodscate"://商品类别
                        intent=new Intent(MyStoreListActivity.this,GoodsTypeActivity.class);
                        bundle.putString("StoreId",storeList.getStoreNo());//传递店铺id
                        bundle.putString("type","changeType");//传递店铺id
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    public MyStoreInforBack storeInforBack;
    /**
     * 获取店铺列表信息
     * @param myStoreInforBack
     */
    @Override
    public void getStoreInforSucc(MyStoreInforBack myStoreInforBack) {
        storeInforBack=myStoreInforBack;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getStoreInforFail(String msg) {
        if(msg.equals("null")){
            handler.sendEmptyMessage(2);
        }else{
            handler.sendEmptyMessage(1);
        }
    }
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://有店铺
                    text_msg.setVisibility(View.INVISIBLE);
                    text_agagin.setVisibility(View.INVISIBLE);
                    list_store.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    storelist=storeInforBack.getStoreList();
                    MyStoreListAdapter adapter=new MyStoreListAdapter(MyStoreListActivity.this,storelist,R.layout.list_store_item);
                    list_store.setAdapter(adapter);
                    break;
                case 1://网络请求失败
                    status=false;
                    list_store.setVisibility(View.VISIBLE);
                    text_agagin.setVisibility(View.VISIBLE);
                    text_agagin.setText("点此重试");
                    text_msg.setVisibility(View.VISIBLE);
                    text_msg.setText("数据获取失败，请检查网络");
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("数据获取失败，请检查网络");
                    break;
                case 2://无店铺
                    status=true;
                    list_store.setVisibility(View.VISIBLE);
                    text_agagin.setVisibility(View.VISIBLE);
                    text_agagin.setText("点此添加");
                    text_msg.setVisibility(View.VISIBLE);
                    text_msg.setText("当前您暂无店铺");
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("当前您暂无店铺");
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_CODE){//添加店铺后，回调---修改店铺资料回调（都调用）
                getMyStoreList();
            }
        }
    }

    /**
     * 店铺id回调
     * @param StoreId
     * @param StoreName
     */
    public void sendStoreId(String StoreName,String StoreId){
        Intent intent=new Intent();
        intent.putExtra("StoreId",StoreId);
        intent.putExtra("StoreName",StoreName);
        setResult(RESULT_CODE_SELECT_STORE,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        switch (type){
            case "fragment_me"://我的
                setResult(MYSTOREINFOR_RESULT_CODE);//回调主页完成刷新
                break;
            case "release"://发布
                break;
            case "storelook"://店铺预览
                break;
            case "storegoodsinfor"://商品信息
                break;
            case "storegoodscate"://商品类别
                break;
        }
        finish();
    }
}
