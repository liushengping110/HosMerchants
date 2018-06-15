package wizrole.hosmerchants.my.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.merchants.model.getsecondtypename.TwoTypeList;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.my.model.storecate.StoreTypeList;
import wizrole.hosmerchants.view.CustTextView;

/**
 * Created by 54727 on 2018/4/2.
 * 月嫂护工信息页面
 */

public class NursingWorkActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.text_s_two_cate)TextView text_s_two_cate;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.text_s_cate)TextView text_s_cate;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.img_s_logo)ImageView img_s_logo;
    @BindView(R.id.img_s_alp)ImageView img_s_alp;
    @BindView(R.id.img_s_wx)ImageView img_s_wx;
    @BindView(R.id.edit_s_name)EditText edit_s_name;
    @BindView(R.id.edit_s_tel)EditText edit_s_tel;
    @BindView(R.id.edit_s_add)EditText edit_s_add;
    @BindView(R.id.btn_up_sinfor)Button btn_up_sinfor;
    @BindView(R.id.list_secondtype)CustTextView list_secondtype;
    public Dialog dialog;
    public StoreList storeList;
    public StoreTypeList storeTypeList;//商户一级分类
    private ImagePicker imagePicker;
    public List<TwoTypeList> twoTypeLists=new ArrayList<>();//选中的二级分类
    public static final int DRIVE_REQUSR_CODE=6666;//二级分类
    public static final int DRIVE_RESULT_CODE=6667;//二级分类
    public Intent intent;
    @Override
    protected int getLayout() {
        return R.layout.activity_nursingwork;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        storeList=(StoreList) getIntent().getSerializableExtra("storeinfor");
        storeTypeList=(StoreTypeList) getIntent().getSerializableExtra("storeTypeList");
        initUI(storeList);
    }

    public void initUI(StoreList s){
        text_s_cate.setText(storeTypeList.getStoreTypeName());
        if (s==null){//添加
            text_title.setText("添加店铺");
        }else {//查看或修改
            text_title.setText("店铺资料");
            text_right.setText("修改");
        }
    }

    @Override
    protected void setListener() {
        text_s_two_cate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.text_s_two_cate://二级分类
                if(twoTypeLists.size()>0){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("sel_secondType",(Serializable) twoTypeLists);
                    intent=new Intent(NursingWorkActivity.this,StoreSecondCateActivity.class);
                    intent.putExtra("store_type",storeTypeList.getStoreTypeName());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,DRIVE_REQUSR_CODE);
                }else{
                    intent=new Intent(NursingWorkActivity.this,StoreSecondCateActivity.class);
                    intent.putExtra("store_type",storeTypeList.getStoreTypeName());
                    startActivityForResult(intent,DRIVE_REQUSR_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==DRIVE_REQUSR_CODE){//二级分类回掉
            if(resultCode==DRIVE_RESULT_CODE){
                twoTypeLists =(List<TwoTypeList>) data.getSerializableExtra("secondtype");
                if(twoTypeLists!=null){
                    setSecondTypeView(twoTypeLists);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 设置二级分类名
     * @param secondTypeView
     */
    public void setSecondTypeView(List<TwoTypeList> secondTypeView){
        if(list_secondtype.getChildCount()>0){
            list_secondtype.removeAllViews();
        }
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,90);
        params.setMargins(20,10,20,10);
        for (int a=0;a<secondTypeView.size();a++){
            TextView textView=new TextView(NursingWorkActivity.this);
            textView.setText(secondTypeView.get(a).getTwoTypeName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(13);
            textView.setPadding(20,10,20,10);
            textView.setMinWidth(120);
            textView.setLayoutParams(params);
            textView.setTextColor(getResources().getColor(R.color.shenhui));
            textView.setBackgroundColor(getResources().getColor(R.color.qrea_bg));
            list_secondtype.addView(textView);
        }
    }

}
