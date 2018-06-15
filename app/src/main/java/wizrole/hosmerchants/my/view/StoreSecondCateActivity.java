package wizrole.hosmerchants.my.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.StoreSecondTypeAdapter;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.merchants.model.getsecondtypename.GetSecondTypeNameBack;
import wizrole.hosmerchants.merchants.model.getsecondtypename.TwoTypeList;
import wizrole.hosmerchants.merchants.preserent.getsecondtypename.GetSecondTypeNameInterface;
import wizrole.hosmerchants.merchants.preserent.getsecondtypename.GetSecondTypeNamePreserent;
import wizrole.hosmerchants.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 * 店铺类别页面----二级分类
 */

public class StoreSecondCateActivity extends BaseActivity implements GetSecondTypeNameInterface,View.OnClickListener{

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.text_duoxuan)TextView text_duoxuan;
    @BindView(R.id.text_notice)TextView text_notice;
    @BindView(R.id.list_storecate)ListView list_storecate;
    @BindView(R.id.text_err_msg)TextView text_err_msg;
    @BindView(R.id.text_err_agagin)TextView text_err_agagin;
    public boolean status;
    public Dialog dialog;
    public GetSecondTypeNamePreserent getSecondTypeNamePreserent= new GetSecondTypeNamePreserent(this);
    public String store_type="";
    public StoreSecondTypeAdapter adapter;
    public String msg="";//选中的
    @Override
    protected int getLayout() {
        return R.layout.activity_storecate;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("商铺类型");
        text_right.setText("确定");
        text_duoxuan.setVisibility(View.VISIBLE);
        store_type=getIntent().getStringExtra("store_type");//获取传递的类别：餐饮美食、出院出行等等
        getSelType=(List<TwoTypeList>) getIntent().getSerializableExtra("sel_secondType");//获取传递已经选中的，
        if(store_type.equals(getString(R.string.cyms))){//餐饮美食
            text_notice.setText("请选择您出售的餐饮美食种类");
        }else if(store_type.equals(getString(R.string.csbl))){//商超便利
            text_notice.setText("请选择您出售的商超便利种类");
        }else if(store_type.equals(getString(R.string.tpyp))){//甜品饮品
            text_notice.setText("请选择您出售的甜品饮品种类");
        }else if(store_type.equals(getString(R.string.sgsx))){//水果生鲜
            text_notice.setText("请选择您出售的水果生鲜种类");
        }else if(store_type.equals(getString(R.string.xhhh))){//鲜花花卉
            text_notice.setText("请选择您出售的鲜花品种");
        }else if(store_type.equals(getString(R.string.cycx))){//出院出行
            text_notice.setText("请选择您出行的范围");
        }else{//月嫂护工
            text_notice.setText("请选择您工作的时间周期");
        }
        setGetSecondTypeName(store_type);//获取二级分类
    }

    public void setGetSecondTypeName(String name){
        dialog=LoadingDailog.createLoadingDialog(StoreSecondCateActivity.this,"加载中");
        getSecondTypeNamePreserent.GetSecondTypeName(name);
    }
    public List<TwoTypeList> tabList;
    public List<TwoTypeList> selType=new ArrayList<>();//选中的
    public List<TwoTypeList> getSelType;//上一次选中的
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
            handler.sendEmptyMessage(1);
        }
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    status=true;
                    text_err_msg.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    if(getSelType!=null){//有选中
                            for(int j=0;j<tabList.size();j++){
                                for(int i=0;i<getSelType.size();i++){
                                    if(getSelType.get(i).getTwoTypeName().equals(tabList.get(j).getTwoTypeName())){
                                        tabList.get(j).setSelect(true);
                                        Log.e("---",tabList.get(j).getTwoTypeName());
                                    }
                                }
                            }
                        selType.addAll(getSelType);
                    }
                    adapter=new StoreSecondTypeAdapter(StoreSecondCateActivity.this,tabList,R.layout.list_second_storetype_item);
                    list_storecate.setAdapter(adapter);
                    LoadingDailog.closeDialog(dialog);
                    list_storecate.setVisibility(View.VISIBLE);
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
        text_right.setOnClickListener(this);
        text_err_agagin.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        list_storecate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TwoTypeList twoTypeList=tabList.get(position);
                if(twoTypeList.isSelect()){
                    twoTypeList.setSelect(false);
                    adapter.notifyDataSetChanged();
                    for(int a=0;a<selType.size();a++){
                        if(twoTypeList.getTwoTypeName().equals(selType.get(a).getTwoTypeName())){
                            selType.remove(selType.get(a));
                        }
                    }
                }else{
                    twoTypeList.setSelect(true);
                    adapter.notifyDataSetChanged();
                    if(selType.size()>0){
                        for(int a=0;a<selType.size();a++){
                            if(!twoTypeList.getTwoTypeName().equals(selType.get(a).getTwoTypeName())){
                                selType.add(twoTypeList);
                                return;
                            }
                        }
                    }else{
                        selType.add(twoTypeList);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.text_err_agagin://重试
                text_err_agagin.setVisibility(View.INVISIBLE);
                text_err_msg.setVisibility(View.INVISIBLE);
                setGetSecondTypeName(store_type);
                break;
            case R.id.text_right://确定
//                for (int a=0;a<selType.size();a++){
//                    msg+=selType.get(a).getTwoTypeName()+"|";
//                }
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("secondtype",(Serializable) selType);
                intent.putExtras(bundle);
                setResult(6667,intent);//回掉数据
                finish();
                break;
        }
    }
}
