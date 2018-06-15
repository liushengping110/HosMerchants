package wizrole.hosmerchants.admin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.my.view.MyStoreListActivity;
import wizrole.hosmerchants.util.SharedPreferenceUtil;

/**
 * Created by liushengping on 2017/11/27/027.
 * 何人执笔？
 * 商家fragment页面
 */

public class Fragment_Admin extends Fragment implements View.OnClickListener{

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public View  view;
    public LinearLayout lin_store,lin_foods,lin_store_change;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_admin,null);
            initUI();
            setListener();
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
        lin_store=(LinearLayout)view.findViewById(R.id.lin_store);
        lin_foods=(LinearLayout)view.findViewById(R.id.lin_foods);
        lin_store_change=(LinearLayout)view.findViewById(R.id.lin_store_change);
    }
    public void setListener(){
        lin_store.setOnClickListener(this);
        lin_foods.setOnClickListener(this);
        lin_store_change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_store://店铺预览
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    if(SharedPreferenceUtil.getInforComplete(getActivity()).equals("0")){
                        Intent intent=new Intent(getActivity(),MyStoreListActivity.class);
                        intent.putExtra("type","storelook");
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(),"请先完善我的资料和我的店铺",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"当前您暂未登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.lin_store_change://店铺修改
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    Intent intent=new Intent(getActivity(),MyStoreListActivity.class);
                    intent.putExtra("type","fragment_me");
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"当前您暂未登录",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.lin_foods://商品管理
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    if(SharedPreferenceUtil.getInforComplete(getActivity()).equals("0")){
                        Intent intent=new Intent(getActivity(),GoodsInforMenuActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(),"请先完善我的资料和我的店铺",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"当前您暂未登录",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
