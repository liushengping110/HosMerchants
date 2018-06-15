package wizrole.hosmerchants.my.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.StoreTypeAdapter;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.my.model.storecate.StoreCateBack;
import wizrole.hosmerchants.my.model.storecate.StoreTypeList;
import wizrole.hosmerchants.my.preserent.storecate.StoreCateInterface;
import wizrole.hosmerchants.my.preserent.storecate.StoreCatePreserent;
import wizrole.hosmerchants.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 * 店铺类别页面---一级分类
 */

public class StoreCateActivity extends BaseActivity implements StoreCateInterface{

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_storecate)ListView list_storecate;
    @BindView(R.id.text_err_msg)TextView text_err_msg;
    @BindView(R.id.text_err_agagin)TextView text_err_agagin;
    public boolean status;
    public Dialog dialog;
    public StoreCatePreserent preserent=new StoreCatePreserent(this);
    @Override
    protected int getLayout() {
        return R.layout.activity_storecate;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("商铺类型");
        dialog=LoadingDailog.createLoadingDialog(StoreCateActivity.this,"加载中");
        preserent.getStoreCate();
    }

    public List<StoreTypeList> storeTypeLists;
    @Override
    public void getStoreCareSucc(StoreCateBack storeCateBack) {
        storeTypeLists=storeCateBack.getStoreTypeList();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getStoreCareFail(String msg) {
        handler.sendEmptyMessage(1);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    status=true;
                    LoadingDailog.closeDialog(dialog);
                    list_storecate.setVisibility(View.VISIBLE);
                    text_err_msg.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    StoreTypeAdapter adapter=new StoreTypeAdapter(StoreCateActivity.this,storeTypeLists,R.layout.list_store_item);
                    list_storecate.setAdapter(adapter);
                    break;
                case 1:
                    status=false;
                    LoadingDailog.closeDialog(dialog);
                    list_storecate.setVisibility(View.INVISIBLE);
                    text_err_agagin.setText("点此重试");
                    text_err_agagin.setVisibility(View.VISIBLE);
                    text_err_msg.setText("网络连接失败，请检查网络");
                    text_err_msg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setListener() {
        list_storecate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreTypeList storeTypeList=storeTypeLists.get(position);
                if (storeTypeList.getStoreTypeName().equals("出院出行")){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("storeTypeList",storeTypeList);
                    Intent intent=new Intent(StoreCateActivity.this,TravelStoreActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(storeTypeList.getStoreTypeName().equals("月嫂护工")){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("storeTypeList",storeTypeList);
                    Intent intent=new Intent(StoreCateActivity.this,NursingWorkActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("storeTypeList",storeTypeList);
                    Intent intent=new Intent(StoreCateActivity.this,MyStoreInforActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        text_err_agagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_err_agagin.setVisibility(View.INVISIBLE);
                text_err_msg.setVisibility(View.INVISIBLE);
                dialog=LoadingDailog.createLoadingDialog(StoreCateActivity.this,"加载中");
                preserent.getStoreCate();
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
