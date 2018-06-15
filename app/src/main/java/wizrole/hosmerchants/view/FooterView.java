package wizrole.hosmerchants.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;

import wizrole.hosmerchants.R;

/**
 * Created by liushengping on 2018/1/19/019.
 * 何人执笔？
 */

public class FooterView extends LinearLayout implements IFooterCallBack {
    public Context content;

    public FooterView(Context context) {
        super(context);
        initView(context);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    public TextView text_load;
    public ProgressBar progress_bar;
    private boolean showing = true;
    public View view;
    public void initView(Context content){
        view= LayoutInflater.from(content).inflate(R.layout.footer_view,this);
        progress_bar=(ProgressBar)view.findViewById(R.id.progress_bar);
        text_load=(TextView) view.findViewById(R.id.text_load);
    }

    @Override
    public void callWhenNotAutoLoadMore(XRefreshView xRefreshView) {

    }

    @Override
    public void onStateReady() {
        text_load.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
        text_load.setText("下拉即可加载更多");
    }

    @Override
    public void onStateRefreshing() {
        text_load.setVisibility(View.VISIBLE);
        text_load.setText("加载中");
        progress_bar.setVisibility(View.VISIBLE);
        show(true);
    }

    @Override
    public void onReleaseToLoadMore() {
        text_load.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
        text_load.setText("松手即可加载更多");
    }

    @Override
    public void onStateFinish(boolean hidefooter) {
        text_load.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.INVISIBLE);
        if(hidefooter){
            text_load.setText("加载成功");
        }else{
            text_load.setText("加载失败");
        }

    }

    @Override
    public void onStateComplete() {
        text_load.setText("没有更多数据了");
        text_load.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void show(boolean show) {
        if (show == showing) {
            return;
        }
        showing = show;
        LayoutParams lp = (LayoutParams) view
                .getLayoutParams();
        lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
        view.setLayoutParams(lp);
    }

    @Override
    public boolean isShowing() {
        return false;
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
