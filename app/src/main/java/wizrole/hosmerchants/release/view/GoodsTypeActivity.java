package wizrole.hosmerchants.release.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.GoodsTypeAdapter;
import wizrole.hosmerchants.admin.model.changegoodscate.ChangeGoodsCateBack;
import wizrole.hosmerchants.admin.model.delgoodscate.DelGoodsCateBack;
import wizrole.hosmerchants.admin.preserent.changegoodscate.ChangeGoodsCateInterface;
import wizrole.hosmerchants.admin.preserent.changegoodscate.ChangeGoodsCatePreserent;
import wizrole.hosmerchants.admin.preserent.delgoodscate.DelGoodsCateInterface;
import wizrole.hosmerchants.admin.preserent.delgoodscate.DelGoodsCatePreserent;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.release.model.addgoodstype.AddGoodsTypeBack;
import wizrole.hosmerchants.release.model.getgoodtype.GoodsTypeBack;
import wizrole.hosmerchants.release.model.getgoodtype.StoreCommodityType;
import wizrole.hosmerchants.release.preserent.addgoodstype.AddGoodsTypeInterface;
import wizrole.hosmerchants.release.preserent.addgoodstype.AddGoodsTypePreserent;
import wizrole.hosmerchants.release.preserent.getgoodtype.GoodsTypeInterface;
import wizrole.hosmerchants.release.preserent.getgoodtype.GoodsTypePreserent;
import wizrole.hosmerchants.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/12/9/009.
 * 何人执笔？
 * 商品分类列表---添加-删除-修改
 *
 */

public class GoodsTypeActivity extends BaseActivity implements GoodsTypeInterface,View.OnClickListener,AddGoodsTypeInterface
,ChangeGoodsCateInterface,DelGoodsCateInterface{
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.text_err_msg)TextView text_err_msg;
    @BindView(R.id.text_err_agagin)TextView text_err_agagin;
    @BindView(R.id.list_goodstype)ListView list_goodstype;
    public String StoreId;//店铺id
    public String type;//进入类别页面来源
    public Dialog dialog;
    public boolean status;
    public GoodsTypePreserent goodsTypePreserent=new GoodsTypePreserent(this);//获取分类
    public AddGoodsTypePreserent addGoodsTypePreserent=new AddGoodsTypePreserent(this);//添加分类
    public ChangeGoodsCatePreserent changeGoodsCatePreserent=new ChangeGoodsCatePreserent(this);//修改分类
    public DelGoodsCatePreserent delGoodsCatePreserent=new DelGoodsCatePreserent(this);//删除商品分类
    public List<StoreCommodityType> types;//分类列表
    @Override
    protected int getLayout() {
        return R.layout.activity_goodstype;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("商品类别");
        text_right.setText("添加类别");
        type=getIntent().getStringExtra("type");
        StoreId=getIntent().getStringExtra("StoreId");
        switch (type){
            case "releaseType"://发布商品--查看、添加类别
                break;
            case "changeType"://店铺管理--修改删除类别
                break;
        }
        getStoreType(StoreId);
    }

    public void getStoreType(String id){
        dialog= LoadingDailog.createLoadingDialog(GoodsTypeActivity.this,"加载中");
        goodsTypePreserent.getGoodsType(id);
    }

    @Override
    protected void setListener() {
        text_err_agagin.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        text_right.setOnClickListener(this);
        list_goodstype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case "releaseType"://发布商品--查看、添加类别
                        StoreCommodityType type=types.get(position);
                        Intent intent=new Intent();
                        intent.putExtra("CateId",type.getTypeId());
                        intent.putExtra("CateName",type.getTypeName());
                        setResult(1001,intent);
                        finish();
                        break;
                    case "changeType"://店铺管理--修改删除类别
                        String cateId=types.get(position).getTypeId();
                        String cateName=types.get(position).getTypeName();
                        ChangeOrDel(cateId,cateName);
                        break;
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
            case R.id.text_right://添加
                initDialog(1,"","");
                break;
            case R.id.text_err_agagin://重试或者添加
                if(status){//添加
                    initDialog(1,"","");
                }else{//重试
                    getStoreType(StoreId);
                }
                break;
        }
    }

    public GoodsTypeBack typeBack;
    /**
     * 获取商品分类信息列表
     * @param goodsTypeBack
     */
    @Override
    public void getGoodsTypeSucc(GoodsTypeBack goodsTypeBack) {
        if(!goodsTypeBack.getResultCode().equals("1")){
            typeBack=goodsTypeBack;
            handler.sendEmptyMessage(0);
        }else{
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void getGoodsTypeFail(String msg) {
        handler.sendEmptyMessage(2);
    }

    /**
     * 添加商品分类信息
     * @param addGoodsTypeBack
     */
    @Override
    public void AddGoodsTypeSucc(AddGoodsTypeBack addGoodsTypeBack) {
        handler.sendEmptyMessage(3);
    }

    @Override
    public void AddGoodsTypeFail(String msg) {
        handler.sendEmptyMessage(4);
    }

    /**
     * 修改商品分类
     * @param changeGoodsCateBack
     */
    @Override
    public void ChangeGoodsCateSucc(ChangeGoodsCateBack changeGoodsCateBack) {
        handler.sendEmptyMessage(3);
    }

    @Override
    public void ChangeGoodsCateFail(String msg) {
        handler.sendEmptyMessage(5);
    }

    /**
     * 删除商品分类
     * @param delGoodsCateBack
     */
    @Override
    public void delGoodsCateSucc(DelGoodsCateBack delGoodsCateBack) {
        handler.sendEmptyMessage(3);
    }

    @Override
    public void delGoodsCateFail(String msg) {
        handler.sendEmptyMessage(6);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://有分类
                    LoadingDailog.closeDialog(dialog);
                    types=typeBack.getStoreCommodityType();
                    list_goodstype.setVisibility(View.VISIBLE);
                    text_err_msg.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    GoodsTypeAdapter adapter=new GoodsTypeAdapter(GoodsTypeActivity.this,types,R.layout.list_store_item);
                    list_goodstype.setAdapter(adapter);
                    break;
                case 1://无分类
                    status=true;
                    LoadingDailog.closeDialog(dialog);
                    list_goodstype.setVisibility(View.INVISIBLE);
                    text_err_agagin.setText("点此添加");
                    text_err_agagin.setVisibility(View.VISIBLE);
                    text_err_msg.setText("当前暂无商品分类");
                    text_err_msg.setVisibility(View.VISIBLE);
                    break;
                case 2://分类请求失败
                    status=false;
                    LoadingDailog.closeDialog(dialog);
                    list_goodstype.setVisibility(View.INVISIBLE);
                    text_err_agagin.setText("点此重试");
                    text_err_agagin.setVisibility(View.VISIBLE);
                    text_err_msg.setText("网络连接失败，请检查网络");
                    text_err_msg.setVisibility(View.VISIBLE);
                    break;
                case 3://添加成功
                    goodsTypePreserent.getGoodsType(StoreId);//请求一次--刷新
                    break;
                case 4://添加失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("分类添加失败，请检查网络");
                    break;
                case 5://修改失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("分类修改失败，请检查网络");
                    break;
                case 6://删除商品分类失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("分类删除失败，请检查网络");
                    break;
            }
        }
    };

    public AlertDialog edit_dialog;
    /**
     * 添加、、修改 dialog
     * @param dialo_type---1=添加    2=修改
     * @param cateId  要修改的类别id
     */
    public void initDialog(final int dialo_type,final String cateId,final String cateName){
        edit_dialog=new AlertDialog.Builder(this).create();
        edit_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_add_goodstype,null);
        final EditText edit_add_type=(EditText)view.findViewById(R.id.edit_add_type);
        TextView text_add_title=(TextView)view.findViewById(R.id.text_add_title);//温馨提示
        TextView text_add_msg=(TextView)view.findViewById(R.id.text_add_msg);//提示
        TextView dialog_cancle=(TextView)view.findViewById(R.id.dialog_cancle);
        TextView dialog_sure=(TextView)view.findViewById(R.id.dialog_sure);
        switch (dialo_type){
            case 1://添加分类
                text_add_title.setText("添加商品分类：");
                text_add_msg.setText("分类名如：特色、酒水、盖饭等等");
                break;
            case 2://修改
                text_add_title.setText("修改商品分类：");
                text_add_msg.setText("");
                edit_add_type.setText(cateName);
                break;
        }
        dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_dialog.dismiss();
            }
        });
        dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_add_type.getText().toString().length()==0){
                    ToastShow("请输入商品分类名");
                }else{
                    edit_dialog.dismiss();
                    String type_sel=edit_add_type.getText().toString();
                    dialog=LoadingDailog.createLoadingDialog(GoodsTypeActivity.this,"加载中");
                    if(dialo_type==1) {//添加
                        addGoodsTypePreserent.AddGoodsType(StoreId,type_sel);
                    }else{//修改
                        changeGoodsCatePreserent.changeGoodsCateName(cateId,type_sel);
                    }
                }
            }
        });//确定注销监听
        //设置date布局
        edit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        edit_dialog.setView(view);
        edit_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = edit_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        edit_dialog.getWindow().setAttributes(layoutParams);
    }

    /**
     * 修改 删除dialog
     */
    public AlertDialog cd_dialog;
    public void ChangeOrDel(final String cateId,final String cateName){
        cd_dialog=new AlertDialog.Builder(this).create();
        cd_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view_cd= LayoutInflater.from(this).inflate(R.layout.dialog_goods_del_change,null);
        TextView text_change=(TextView)view_cd.findViewById(R.id.text_change);
        TextView text_del=(TextView)view_cd.findViewById(R.id.text_del);
        text_change.setOnClickListener(new View.OnClickListener() {//修改
            @Override
            public void onClick(View v) {
                cd_dialog.dismiss();
                initDialog(2,cateId,cateName);
            }
        });
        text_del.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                cd_dialog.dismiss();
                delCate(cateName);
            }
        });
        //设置date布局
        cd_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd_dialog.setView(view_cd);
        cd_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = cd_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        cd_dialog.getWindow().setAttributes(layoutParams);
    }

    public AlertDialog del_dialog;
    public void delCate(final String typeName){
        del_dialog=new AlertDialog.Builder(this).create();
        del_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view_cd= LayoutInflater.from(this).inflate(R.layout.dialog_exits,null);
        TextView text_message=(TextView)view_cd.findViewById(R.id.text_message);
        TextView text_del=(TextView)view_cd.findViewById(R.id.text_del);
        TextView text_sure=(TextView)view_cd.findViewById(R.id.text_sure);
        TextView text_cancle=(TextView)view_cd.findViewById(R.id.text_cancle);
        text_message.setText("删除该分类，对应分类下的商品也会被删除，是否删除？");
        text_message.setTextColor(getResources().getColor(R.color.colorAccent));
        text_cancle.setOnClickListener(new View.OnClickListener() {//修改
            @Override
            public void onClick(View v) {
                del_dialog.dismiss();

            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                del_dialog.dismiss();
                dialog=LoadingDailog.createLoadingDialog(GoodsTypeActivity.this,"加载中");
                delGoodsCatePreserent.delGoodsCateName(StoreId,typeName);
            }
        });
        //设置date布局
        del_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        del_dialog.setView(view_cd);
        del_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = del_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        del_dialog.getWindow().setAttributes(layoutParams);
    }
}
