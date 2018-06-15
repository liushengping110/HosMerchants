package wizrole.hosmerchants.admin.view;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.MoreEvlautionAdapter;
import wizrole.hosmerchants.adapter.MoreEvlautionAdapterTest;
import wizrole.hosmerchants.admin.model.evaluation.CommentList;
import wizrole.hosmerchants.admin.model.evaluation.EvaluationBack;
import wizrole.hosmerchants.admin.preserent.evaluation.EvaluationInterface;
import wizrole.hosmerchants.admin.preserent.evaluation.EvaluationPreserent;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.image.ImageLoading;
import wizrole.hosmerchants.view.FooterView;
import wizrole.hosmerchants.view.HeaderView;
import wizrole.hosmerchants.view.UserFooterView;
import wizrole.hosmerchants.view.UserHeaderView;

/**
 * Created by liushengping on 2018/1/15/015.
 * 何人执笔？
 * 单个商品的所有评价
 */

public class MoreEvlautListActivity extends BaseActivity implements View.OnClickListener,EvaluationInterface {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.recy_evlaution)ListView recy_evlaution;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_err_agagin)TextView text_err_agagin;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public EvaluationPreserent evaluationPreserent=new EvaluationPreserent(this);
    public String CommodityNo;
    public int page=1;
    public Dialog dialog;
    public int TotalNum=0;
    public EvaluationBack evaluation;
    public List<CommentList> commentLists;
    public List<CommentList> final_commentLists;
    public MoreEvlautionAdapterTest adapter;
    public boolean showdialog_status=false;//显示dialog的状态码
    @Override
    protected int getLayout() {
        return R.layout.activity_moreevlautlist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        CommodityNo=getIntent().getStringExtra("CommodityNo");
        text_title.setText("商品评价");
        setView();
        getEvlaution();
    }
    public void setView(){
        final_commentLists=new ArrayList<>();
        setXRefresh();
    }
    public void setXRefresh(){
        refreshView.setRefreshHeader(new UserHeaderView(this));
        refreshView.setRefreshFooter(new UserFooterView(this));
        refreshView.setHeaderHeightPx(300);
        refreshView.setFooterHeightPx(80);
    }

    public void  getEvlaution(){
        if(page==1){//加载更多不需要
            if(!showdialog_status){//初始化--重新加载
                dialog= LoadingDailog.createLoadingDialog(MoreEvlautListActivity.this,"加载中");
                showdialog_status=true;
            }
        }
        evaluationPreserent.getEvaluationScore(CommodityNo,page);
    }
    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        text_err_agagin.setOnClickListener(this);
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page=1;
                getEvlaution();
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<TotalNum){
                    page++;
                    getEvlaution();
                }else{
                    ToastShow("已经没有更多数据了");
                    refreshlayout.finishLoadmoreWithNoMoreData();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.text_err_agagin:
                recy_evlaution.setVisibility(View.INVISIBLE);
                text_err_agagin.setVisibility(View.INVISIBLE);
                img_net_err.setVisibility(View.INVISIBLE);
                showdialog_status=false;
                page=1;
                getEvlaution();
                break;
        }
    }


    @Override
    public void getEvaluationSucc(EvaluationBack evaluationBack) {
        TotalNum=evaluationBack.getTotalNum();
        evaluation=evaluationBack;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getEvaluationFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(1);
        }else{
            //无数据，
            handler.sendEmptyMessage(2);
        }
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    LoadingDailog.closeDialog(dialog);
                    commentLists=evaluation.getCommentList();
                    final_commentLists.addAll(commentLists);
                    if(page==1){
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                        recy_evlaution.setVisibility(View.VISIBLE);
                        text_err_agagin.setVisibility(View.INVISIBLE);
                        img_net_err.setVisibility(View.INVISIBLE);
                        adapter=new MoreEvlautionAdapterTest(MoreEvlautListActivity.this,final_commentLists);
                        recy_evlaution.setAdapter(adapter);
                        refreshView.finishRefresh();
                    }else{
                        adapter.notifyDataSetChanged();
                        refreshView.finishLoadmore();
                    }
                    break;
                case 1://错误
                    if(page==1){
                        refreshView.finishRefresh();
                        LoadingDailog.closeDialog(dialog);
                        recy_evlaution.setVisibility(View.INVISIBLE);
                        text_err_agagin.setText("重新加载");
                        text_err_agagin.setVisibility(View.VISIBLE);
                        ImageLoading.common(MoreEvlautListActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                        img_net_err.setVisibility(View.VISIBLE);
                    }else{
                        refreshView.finishLoadmore();
                    }
                    ToastShow("网络连接失败，请检查网络");
                    break;
                case 2://无数据
                    //不存在该情况
                    break;
            }
        }
    };
}

