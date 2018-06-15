package wizrole.hosmerchants.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.callback.IHeaderCallBack;

import wizrole.hosmerchants.R;

/**
 * Created by liushengping on 2018/1/19/019.
 * 何人执笔？
 */

public class HeaderView extends LinearLayout implements IHeaderCallBack {

    public ElmRefreshView refreshView;
    public View view;

    public HeaderView(Context context) {
        super(context);
        initView(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void initView(Context content){
        view=LayoutInflater.from(content).inflate(R.layout.header_view, this);
        refreshView=(ElmRefreshView)view.findViewById(R.id.view_fresh);
    }

    @Override
    public void onStateNormal() {
//        textView.setText("下拉即可刷新");
//        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateReady() {
//        textView.setText("松手刷新");

    }

    @Override
    public void onStateRefreshing() {
//        textView.setText("正在刷新");
        refreshView.setStatus(ElmRefreshView.STATUS_RUNNING);
    }

    @Override
    public void onStateFinish(boolean success) {
//        textView.setText("刷新成功");
        refreshView.setStatus(ElmRefreshView.STATUS_STOP);
        refreshView.setStatus(ElmRefreshView.STATUS_MOVING);
    }

    /**
     * 获取headerview显示的高度与headerview高度的比例
     *
     * @param 、、offset  移动距离和headerview高度的比例，范围是0~1，0：headerview完全没显示 1：headerview完全显示
     * @param offsetY headerview移动的距离
     */
    @Override
    public void onHeaderMove(double headerMovePercent, int offsetY, int deltaY) {
        if (0.1<headerMovePercent&&headerMovePercent == 1) {
//            textView.setText("松手即可刷新");
        }else{
//            textView.setText("正在刷新");
        }
        final int height = refreshView.getHeight() / 2;
        float offset = (offsetY - height) *1.0f;
        if(offset < 0){
            offset = 0;
        }
        refreshView.setPullPositionChanged(offset / (view.getHeight() - height));
    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }
}
