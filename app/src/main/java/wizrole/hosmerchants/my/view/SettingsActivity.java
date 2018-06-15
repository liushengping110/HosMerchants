package wizrole.hosmerchants.my.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;

/**
 * Created by liushengping on 2018/1/18/018.
 * 何人执笔？
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.frag_my_safe)LinearLayout frag_my_safe;
    @BindView(R.id.frag_my_appversion)LinearLayout frag_my_appversion;
    @BindView(R.id.frag_my_zhuxiao)LinearLayout frag_my_zhuxiao;
    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("设置中心");
    }

    @Override
    protected void setListener() {
        frag_my_safe.setOnClickListener(this);
        frag_my_appversion.setOnClickListener(this);
        frag_my_zhuxiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.frag_my_safe:
                break;
            case R.id.frag_my_appversion:
                break;
            case R.id.frag_my_zhuxiao:
                break;
        }
    }
}
