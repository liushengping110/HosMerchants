package wizrole.hosmerchants.admin.view;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.my.view.MyStoreListActivity;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 * 修改商品信息
 */

public class GoodsInforMenuActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_goods_cate)LinearLayout lin_goods_cate;
    @BindView(R.id.lin_goods_infor)LinearLayout lin_goods_infor;
    @Override
    protected int getLayout() {
        return R.layout.activity_goodsinformenu;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {
        lin_goods_infor.setOnClickListener(this);
        lin_goods_cate.setOnClickListener(this);
        lin_back.setOnClickListener(this);
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.lin_goods_infor://商品信息
                intent=new Intent(GoodsInforMenuActivity.this, MyStoreListActivity.class);
                intent.putExtra("type","storegoodsinfor");
                startActivity(intent);
                break;
            case R.id.lin_goods_cate://商品类别
                intent=new Intent(GoodsInforMenuActivity.this, MyStoreListActivity.class);
                intent.putExtra("type","storegoodscate");
                startActivity(intent);
                break;
        }
    }
}
