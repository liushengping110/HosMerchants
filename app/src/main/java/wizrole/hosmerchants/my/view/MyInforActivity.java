package wizrole.hosmerchants.my.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.ocr.ui.camera.CameraActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.my.model.changeinfor.ChangeInforBack;
import wizrole.hosmerchants.my.model.myinfor.MyInforBack;
import wizrole.hosmerchants.my.model.idinforback.IDInforBack;
import wizrole.hosmerchants.my.model.idinforfont.IDInforFont;
import wizrole.hosmerchants.my.model.idtoken.TokenBack;
import wizrole.hosmerchants.my.preserent.changeinfor.ChangeInforInterface;
import wizrole.hosmerchants.my.preserent.changeinfor.ChangeInforPreserent;
import wizrole.hosmerchants.my.preserent.myinfor.MyInforInterface;
import wizrole.hosmerchants.my.preserent.myinfor.MyInforPreserent;
import wizrole.hosmerchants.my.preserent.idinfor.IDInforFrontInterface;
import wizrole.hosmerchants.my.preserent.idinfor.IDInforPreserent;
import wizrole.hosmerchants.my.preserent.idtoken.IDTokenInterface;
import wizrole.hosmerchants.my.preserent.idtoken.IDTokenPreserent;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2017/12/13/013.
 * 何人执笔？
 * 我的资料
 */

public class MyInforActivity extends BaseActivity implements View.OnClickListener ,MyInforInterface,IDTokenInterface,IDInforFrontInterface,ChangeInforInterface{
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.btn_up_infor)Button btn_up_infor;
    @BindView(R.id.text_look_front)TextView text_look_front;
    @BindView(R.id.text_look_back)TextView text_look_back;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.img_id_after)ImageView img_id_after;
    @BindView(R.id.img_id_font)ImageView img_id_font;
    @BindView(R.id.edit_name)EditText edit_name;
    @BindView(R.id.edit_tel)EditText edit_tel;
    @BindView(R.id.edit_email)EditText edit_email;
    public MyInforPreserent preserent=new MyInforPreserent(this);//获取个人资料
    public IDTokenPreserent idTokenPreserent=new IDTokenPreserent(this);//获取身份证token
    public IDInforPreserent inforFrontPreserent=new IDInforPreserent(this);//获取身份证信息
    public ChangeInforPreserent changeInforpreserent=new ChangeInforPreserent(this);//修改个人信息
    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;
    public Dialog dialog;
    public Intent intent;
    public boolean changeinfor=false;//是否修改标志位
    @Override
    protected int getLayout() {
        return R.layout.activity_myinfor;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("个人资料");
        text_right.setText("修改");
        initUI();
        getUserInfor();//获取用户信息
    }

    public void initUI(){
        ImageLoading.commonBlurTeans(MyInforActivity.this,R.drawable.my_infor_id_font,img_id_font);
        ImageLoading.commonBlurTeans(MyInforActivity.this,R.drawable.my_infor_id_after,img_id_after);
    }

    public void getUserInfor(){
        dialog= LoadingDailog.createLoadingDialog(MyInforActivity.this,"加载中");
        String id= SharedPreferenceUtil.getID(MyInforActivity.this);
        if(!id.equals("")){
            preserent.HttpGetMyInfor(id);
        }else{
            LoadingDailog.closeDialog(dialog);
            ToastShow("用户资料获取失败");
        }
    }

    @Override
    protected void setListener() {
        img_id_after.setOnClickListener(this);
        img_id_font.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        btn_up_infor.setOnClickListener(this);
        text_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back://返回
                exitsMess();
                break;
            case R.id.text_right://修改
                changeinfor=true;
                ToastShow("您现在可修改个人资料");
                setChangeStatus();
                break;
            case R.id.btn_up_infor://保存
                checkSave();
                break;
            case R.id.img_id_font://身份证前
                if(inforBack==null){
                    getUserInfor();
                }else{
                    if(changeinfor){//允许修改
                        intent = new Intent(MyInforActivity.this, CameraActivity.class);
                        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,getFrontSaveFile(getApplication()).getAbsolutePath());
                        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    }else{//不允许修改--只能查看
                        if(!inforBack.getIdCardName().equals("")){//显示之前上传了的
                            initDialog(3,R.layout.dialog_id_front,inforBack);
                        }else{//如果之前都没上传，直接打开相机
                            ToastShow("请点击修改后，上传身份证件信息");
                        }
                    }
                }
                break;
            case R.id.img_id_after://身份证后
                if(inforBack==null){//网络无连接
                    getUserInfor();//点击刷新
                }else{
                    if(changeinfor){//允许修改--直接打开相机
                        intent = new Intent(MyInforActivity.this, CameraActivity.class);
                        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, getBackSaveFile(getApplication()).getAbsolutePath());
                        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    }else{//不允许修改--只能查看
                        if(!inforBack.getIssuingBody().equals("")){
                            initDialog(4,R.layout.dialog_id_back,inforBack);
                        }else{
                            ToastShow("请点击修改后，上传身份证件信息");
                        }
                    }
                }
                break;
        }
    }

    public MyInforBack inforBack;//我的信息获取实体类
    public TokenBack idTokenack;//身份证Token实体类
    public IDInforFont idInforFont;//身份证正面实体类
    public IDInforBack idInforBack;//身份证反面实体类
    public boolean front=false;//身份证正面是否完善标志位
    public boolean back=false;//身份证反面是否完善标志位
    public ChangeInforBack ChangeInforBack;//修改信息回调实体类
    /**
     * 获取个人资料
     * @param myInforBack
     */
    @Override
    public void getDataSucc(MyInforBack myInforBack) {
        inforBack=myInforBack;
        handler.sendEmptyMessage(0);
    }



    @Override
    public void getDataFail(String msg) {
        handler.sendEmptyMessage(1);
    }

    /**
     * 获取id的token,然后再把token作为参数，获取身份证信息
     * @param tokenBack
     */
    @Override
    public void getTokenSucc(TokenBack tokenBack) {
        idTokenack=tokenBack;
        handler.sendEmptyMessage(2);
    }

    @Override
    public void getTokenFail(String msg) {
        handler.sendEmptyMessage(3);
    }

    /**
     * 获取身份证信息
     * @param o
     * 正反面调同一个接口，返回数据不同
     */
    @Override
    public void getIDFrontSucc(Object o) {
        if(side.equals("front")){
            idInforFont=(IDInforFont) o;
        }else{
            idInforBack=(IDInforBack)o;
        }
        handler.sendEmptyMessage(4);
    }

    @Override
    public void getIDFrontFail(String msg) {
        handler.sendEmptyMessage(5);
    }

    /**
     * 修改个人信息回调
     * @param changeInforBack
     */
    @Override
    public void getInforSucc(ChangeInforBack changeInforBack) {
        ChangeInforBack=changeInforBack;
        if(changeInforBack.getResultCode().equals("0")){
            handler.sendEmptyMessage(6);
        }else{
            handler.sendEmptyMessage(7);
        }
    }

    @Override
    public void getInforFail(String msg) {
        handler.sendEmptyMessage(7);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://获取个人资料
                    LoadingDailog.closeDialog(dialog);
                    setInforView();//设置视图界面
                    break;
                case 1://获取个人资料失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("个人资料获取失败，请检查网络");
                    setInforErrView();
                    break;
                case 2://Token获取成功
                    String token=idTokenack.getAccess_token();
                    //接着就获取身份证信息
                    inforFrontPreserent.HttpGetIDiInfor(token,side,filePath);
                    break;
                case 3://Token获取失败
                    LoadingDailog.closeDialog(dialog);
                    break;
                case 4://身份证信息获取成功
                    LoadingDailog.closeDialog(dialog);
                    if(side.equals("front")){//身份证正面
                        if(idInforFont.getImage_status().equals("normal")&&idInforFont.getRisk_type().equals("normal")){//
                            initDialog(1,R.layout.dialog_id_front,idInforFont);
                        }else{//否则读取失败
                            ToastShow("扫描失败");
                        }
                    }else{//身份证反面
                        if(idInforBack.getImage_status().equals("normal")&&idInforBack.getRisk_type().equals("normal")){//
                            initDialog(2,R.layout.dialog_id_back,idInforBack);
                        }else{//否则读取失败
                            ToastShow("扫描失败");
                        }
                    }
                    break;
                case 5://身份证信息获取失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("扫描失败");
                    break;
                case 6://信息修改成功
                    SharedPreferenceUtil.saveInforComplete(MyInforActivity.this,ChangeInforBack.getInfoSign());
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("个人信息修改成功");
                    Intent intent=new Intent();
                    intent.putExtra("status",ChangeInforBack.getInfoSign());
                    intent.putExtra("name",edit_name.getText().toString());
                    setResult(10,intent);
                    finish();
                    break;
                case 7://信息修改失败
                    SharedPreferenceUtil.saveInforComplete(MyInforActivity.this,ChangeInforBack.getInfoSign());
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("个人信息提交失败，请检查网络后重试");
                    break;
            }
        }
    };

    public String filePath=null;
    public String side="";//拍摄身份证的正反两面标志位
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE_FRONT && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            filePath = getRealPathFromURI(uri);
            side="front";
            getIDToken();
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE_BACK && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            filePath = getRealPathFromURI(uri);
            side="back";
            getIDToken();
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        side="front";
                        filePath =getFrontSaveFile(getApplicationContext()).getAbsolutePath();
                        getIDToken();
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        side="back";
                        filePath =getBackSaveFile(getApplicationContext()).getAbsolutePath();
                        getIDToken();
                    }
                }
            }
        }
    }

    /**
     * 开始获取token
     */
    public void getIDToken(){
        dialog=LoadingDailog.createLoadingDialog(MyInforActivity.this,"加载中");
        idTokenPreserent.getIDToken();
    }

    /**
     * 设置修改界面视图
     */
    public void setChangeStatus(){
        edit_name.setEnabled(true);
        edit_name.setTextColor(getResources().getColor(R.color.huise));
        edit_tel.setEnabled(true);
        edit_tel.setTextColor(getResources().getColor(R.color.huise));
        edit_email.setEnabled(true);
        edit_email.setTextColor(getResources().getColor(R.color.huise));
        text_look_front.setText("点此修改身份证件信息");
        text_look_back.setText("点此修改身份证件信息");
        text_look_front.setTextColor(getResources().getColor(R.color.text_fz));
        text_look_back.setTextColor(getResources().getColor(R.color.text_fz));
        btn_up_infor.setEnabled(true);
        btn_up_infor.setBackground(getResources().getDrawable(R.drawable.login_sel));
    }

    /**
     * 设置个人资料视图
     */
    public void setInforView(){
        if(!inforBack.getHostName().equals("")){
            edit_name.setText(inforBack.getHostName());
        }else{
            edit_name.setText("待完善");
        }
        edit_name.setEnabled(false);
        edit_name.setTextColor(getResources().getColor(R.color.text_fz));
        if(!inforBack.getHostEmail().equals("")){
            edit_email.setText(inforBack.getHostEmail());
        }else{
            edit_email.setText("待完善");
        }
        edit_email.setEnabled(false);
        edit_email.setTextColor(getResources().getColor(R.color.text_fz));
        if(!inforBack.getHostPhone().equals("")){
            edit_tel.setText(inforBack.getHostPhone());
        }else{
            edit_tel.setText("待完善");
        }
        edit_tel.setEnabled(false);
        edit_tel.setTextColor(getResources().getColor(R.color.text_fz));
        btn_up_infor.setEnabled(false);
        btn_up_infor.setBackgroundColor(getResources().getColor(R.color.huise));
       if(!inforBack.getIdCardName().equals("")){
           text_look_back.setText("点击查看身份证反面");
           text_look_front.setText("点击查看身份证正面");
           text_look_front.setTextColor(getResources().getColor(R.color.text_fz));
           text_look_back.setTextColor(getResources().getColor(R.color.text_fz));
           front=true;
           back=true;
       }else{
           text_look_back.setText("点击修改后上传身份证反面");
           text_look_front.setText("点击修改后上传身份证正面");
           text_look_front.setTextColor(getResources().getColor(R.color.text_fz));
           text_look_back.setTextColor(getResources().getColor(R.color.text_fz));
           front=false;
           back=false;
       }
    }

    /**
     * 设置断网视图
     */
    public void setInforErrView(){
        edit_name.setEnabled(false);
        edit_name.setText("网络无连接");
        edit_name.setTextColor(getResources().getColor(R.color.colorAccent));
        edit_email.setEnabled(false);
        edit_email.setText("网络无连接");
        edit_email.setTextColor(getResources().getColor(R.color.colorAccent));
        edit_tel.setEnabled(false);
        edit_tel.setText("网络无连接");
        edit_tel.setTextColor(getResources().getColor(R.color.colorAccent));
        text_look_back.setText("网络无连接，点此重试");
        text_look_back.setTextColor(getResources().getColor(R.color.colorAccent));
        text_look_front.setText("网络无连接，点此重试");
        text_look_front.setTextColor(getResources().getColor(R.color.colorAccent));
        btn_up_infor.setEnabled(false);
        btn_up_infor.setBackgroundColor(getResources().getColor(R.color.huise));
    }

    /**
     * 保存信息
     */
    public void checkSave(){
        if(edit_name.getText().toString().length()==0){
            ToastShow("真实姓名不能为空");
        }else if(edit_tel.getText().toString().length()==0){
            ToastShow("手机号码不能为空");
        }else if(edit_email.getText().toString().length()==0){
            ToastShow("邮箱找回不能为空");
        }else if(!front&&inforBack.getIdCardName().equals("")){
            ToastShow("身份证正面未上传");
        }else if(!back&&inforBack.getIssuingBody().equals("")){
            ToastShow("身份证反面未上传");
        }else{//修改个人信息，统一调用Y005
            dialog=LoadingDailog.createLoadingDialog(MyInforActivity.this,"个人信息提交中");
            String id=SharedPreferenceUtil.getID(MyInforActivity.this);
            if(!front&&!back){//上传之前上传的正反俩面
                changeInfor(id);
            }else if(!front&&back){//上传之前上传的正面 +（之前上传的反面 or 扫描的反面）
                if(idInforBack==null){//和之前上传的反面
                    changeInfor(id);
                }else{//和扫描的反面
                    changeInforTwo(id);
                }
            }else if(front&&!back){//上传之前的反面 +  （之前上传的正面 or 扫描的正面）
                if(idInforFont==null){//和之前上传的正面
                    changeInfor(id);
                }else{//和扫描的正面
                    changeInforThree(id);
                }
            }else if(front&&back){//上传扫描的正反俩面
                if(idInforFont==null&&idInforBack==null){//上传之前的正反俩面
                    changeInfor(id);
                }else if(idInforFont!=null&&idInforBack==null){//扫描的正面 + 之前的反面
                    changeInforThree(id);
                }else if(idInforFont==null&&idInforBack!=null){//之前的正面 + 扫描的反面
                    changeInforTwo(id);
                }else{//上传扫描的正反两面
                    changeInforFour(id);
                }
            }
        }
    }

    /**
     * 上传之前的正反两面
     */
    public void changeInfor(String id){
        changeInforpreserent.HttpChangeinfor(edit_name.getText().toString()
                ,edit_tel.getText().toString()
                ,edit_email.getText().toString()
                ,""
                ,id
                ,inforBack.getHostIdCard()
                ,inforBack.getHostAddress()
                ,inforBack.getBornDate()
                ,inforBack.getSex()
                ,inforBack.getEthnic()
                ,inforBack.getIssuingBody()
                ,inforBack.getValidity()
                ,inforBack.getIdCardName());
    }
    /**
     * 之前的正面
     * 扫描的反面
     */
    public void changeInforTwo(String id){
        changeInforpreserent.HttpChangeinfor(edit_name.getText().toString()
                ,edit_tel.getText().toString()
                ,edit_email.getText().toString()
                ,""
                ,id
                ,inforBack.getHostIdCard()
                ,inforBack.getHostAddress()
                ,inforBack.getBornDate()
                ,inforBack.getSex()
                ,inforBack.getEthnic()
                ,idInforBack.getWords_result().getIssueOrgan().getWords()
                ,idInforBack.getWords_result().getStartDate().getWords()+"--"+idInforBack.getWords_result().getFailureDate().getWords()
                ,inforBack.getIdCardName());
    }

    /**
     * 扫描的正面
     * 之前上传的反面
     */
    public void changeInforThree(String id){
        changeInforpreserent.HttpChangeinfor(edit_name.getText().toString()
                ,edit_tel.getText().toString()
                ,edit_email.getText().toString()
                ,""
                ,id
                ,idInforFont.getWords_result().getIdNumber().getWords()
                ,idInforFont.getWords_result().getAddress().getWords()
                ,idInforFont.getWords_result().getBrithDay().getWords()
                ,idInforFont.getWords_result().getSex().getWords()
                ,idInforFont.getWords_result().getEthnic().getWords()
                ,inforBack.getIssuingBody()
                ,inforBack.getValidity()
                ,inforBack.getIdCardName());
    }

    /**
     * 扫描的反面
     * 扫描的正面
     */
    public void changeInforFour(String id){
        changeInforpreserent.HttpChangeinfor(edit_name.getText().toString()
                ,edit_tel.getText().toString()
                ,edit_email.getText().toString()
                ,""
                ,id
                ,idInforFont.getWords_result().getIdNumber().getWords()
                ,idInforFont.getWords_result().getAddress().getWords()
                ,idInforFont.getWords_result().getBrithDay().getWords()
                ,idInforFont.getWords_result().getSex().getWords()
                ,idInforFont.getWords_result().getEthnic().getWords()
                ,idInforBack.getWords_result().getIssueOrgan().getWords()
                ,idInforBack.getWords_result().getStartDate().getWords()+"--"+idInforBack.getWords_result().getFailureDate().getWords()
                ,idInforFont.getWords_result().getName().getWords());
    }
    public AlertDialog ID_dialog;
    public void initDialog(final int type , int rid, Object object){
        ID_dialog=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(rid,null);
        ID_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //共同使用以下控件
        TextView dialog_only_sure = (TextView) view.findViewById(R.id.dialog_only_sure);
        View dialog_view_bottom = (View) view.findViewById(R.id.dialog_view_bottom);
        TextView dialog_sure = (TextView) view.findViewById(R.id.dialog_sure);
        TextView dialog_again = (TextView) view.findViewById(R.id.dialog_again);
        switch (type){
            case 1://身份证---正面--读取成功
                TextView dialog_name =(TextView) view.findViewById(R.id.dialog_name);
                TextView dialog_sex = (TextView) view.findViewById(R.id.dialog_sex);
                TextView dialog_address = (TextView) view.findViewById(R.id.dialog_address);
                TextView dialog_year = (TextView) view.findViewById(R.id.dialog_year);
                TextView dialog_month = (TextView) view.findViewById(R.id.dialog_month);
                TextView dialog_day = (TextView) view.findViewById(R.id.dialog_day);
                TextView dialog_mz = (TextView) view.findViewById(R.id.dialog_mz);
                TextView dialog_number = (TextView) view.findViewById(R.id.dialog_number);
                IDInforFont font=(IDInforFont) object;
                dialog_name.setText(font.getWords_result().getName().getWords());
                dialog_sex.setText(font.getWords_result().getSex().getWords());
                dialog_mz.setText(font.getWords_result().getEthnic().getWords());
                dialog_address.setText(font.getWords_result().getAddress().getWords());
                dialog_number.setText(font.getWords_result().getIdNumber().getWords());
                String string=font.getWords_result().getBrithDay().getWords();
                String yeay=string.substring(0,4);
                String month=string.substring(4,6);
                String day=string.substring(6,8);
                if(month.indexOf("0")==0){
                    month=month.replace("0","");
                }
                if(day.indexOf("0")==0){
                    day=day.replace("0","");
                }
                dialog_year.setText(yeay);
                dialog_month.setText(month);
                dialog_day.setText(day);
                dialog_only_sure.setVisibility(View.GONE);
                dialog_sure.setVisibility(View.VISIBLE);
                dialog_again.setVisibility(View.VISIBLE);
                dialog_view_bottom.setVisibility(View.VISIBLE);
                break;
            case 2://身份证----反面----读取成功
                IDInforBack back=(IDInforBack) object;
                TextView dialog_yxq = (TextView) view.findViewById(R.id.dialog_yxq);
                TextView dialog_qfjg = (TextView) view.findViewById(R.id.dialog_qfjg);
                dialog_yxq.setText(back.getWords_result().getStartDate().getWords()+"——"+back.getWords_result().getFailureDate().getWords());
                dialog_qfjg.setText(back.getWords_result().getIssueOrgan().getWords());
                dialog_only_sure.setVisibility(View.GONE);
                dialog_sure.setVisibility(View.VISIBLE);
                dialog_again.setVisibility(View.VISIBLE);
                dialog_view_bottom.setVisibility(View.VISIBLE);
                break;
            case 3://身份证---正面--点击预览
                TextView dialog_yl_name =(TextView) view.findViewById(R.id.dialog_name);
                TextView dialog_yl_sex = (TextView) view.findViewById(R.id.dialog_sex);
                TextView dialog_yl_address = (TextView) view.findViewById(R.id.dialog_address);
                TextView dialog_yl_year = (TextView) view.findViewById(R.id.dialog_year);
                TextView dialog_yl_month = (TextView) view.findViewById(R.id.dialog_month);
                TextView dialog_yl_day = (TextView) view.findViewById(R.id.dialog_day);
                TextView dialog_yl_mz = (TextView) view.findViewById(R.id.dialog_mz);
                TextView dialog_yl_number = (TextView) view.findViewById(R.id.dialog_number);
                MyInforBack font_yl=(MyInforBack) object;
                dialog_yl_name.setText(font_yl.getIdCardName());
                dialog_yl_sex.setText(font_yl.getSex());
                dialog_yl_mz.setText(font_yl.getEthnic());
                dialog_yl_address.setText(font_yl.getHostAddress());
                dialog_yl_number.setText(font_yl.getHostIdCard());
                String string_yl=font_yl.getBornDate();
                String year_yl=string_yl.substring(0,4);
                String month_yl=string_yl.substring(4,6);
                String day_yl=string_yl.substring(6,8);
                if(month_yl.indexOf("0")==0){
                    month_yl=month_yl.replace("0","");
                }
                if(day_yl.indexOf("0")==0){
                    day_yl=day_yl.replace("0","");
                }
                dialog_yl_year.setText(year_yl);
                dialog_yl_month.setText(month_yl);
                dialog_yl_day.setText(day_yl);
                dialog_only_sure.setVisibility(View.VISIBLE);
                dialog_sure.setVisibility(View.GONE);
                dialog_again.setVisibility(View.GONE);
                dialog_view_bottom.setVisibility(View.GONE);
                break;
            case 4://身份证----反面----点击预览
                MyInforBack back_yl=(MyInforBack) object;
                TextView dialog_yl_yxq = (TextView) view.findViewById(R.id.dialog_yxq);
                TextView dialog_yl_qfjg = (TextView) view.findViewById(R.id.dialog_qfjg);
                dialog_yl_yxq.setText(back_yl.getValidity());
                dialog_yl_qfjg.setText(back_yl.getIssuingBody());
                dialog_only_sure.setVisibility(View.VISIBLE);
                dialog_sure.setVisibility(View.GONE);
                dialog_again.setVisibility(View.GONE);
                dialog_view_bottom.setVisibility(View.GONE);
                break;
        }
        dialog_only_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID_dialog.dismiss();
            }
        });
        dialog_sure.setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View v) {
                ID_dialog.dismiss();
                if(type==1){
                    front=true;
                    ImageLoading.common(MyInforActivity.this,filePath,img_id_font);
                    filePath=null;
                }else if(type==2){
                    back=true;
                    ImageLoading.common(MyInforActivity.this,filePath,img_id_after);
                    filePath=null;
                }else if(type==3){//查看

                }else{//查看

                }
            }
        });
        dialog_again.setOnClickListener(new View.OnClickListener() {// 重试
            @Override
            public void onClick(View v) {
                ID_dialog.dismiss();
                if(type==1){
                    front=false;
                }else if(type==2){
                    back=false;
                }else if(type==3){//查看

                }else{//查看

                }
            }
        });//确定注销监听
        //设置date布局
        ID_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ID_dialog.setView(view);
        ID_dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = ID_dialog.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ID_dialog.getWindow().setAttributes(layoutParams);
    }


    public AlertDialog dialog_exit;
    public void initExitDialog(String msg){
        dialog_exit=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_exits,null);
        dialog_exit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView text_cancle=(TextView)view.findViewById(R.id.text_cancle);
        TextView text_message=(TextView)view.findViewById(R.id.text_message);
        TextView text_sure=(TextView)view.findViewById(R.id.text_sure);
        text_message.setText(msg);
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.dismiss();
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.dismiss();
                finish();
            }
        });//确定注销监听
        //设置date布局
        dialog_exit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_exit.setView(view);
        dialog_exit.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = dialog_exit.getWindow().getAttributes();
        layoutParams.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog_exit.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onBackPressed() {
        exitsMess();
    }

    /**
     * 退出提示
     */
    public void exitsMess(){
        if(changeinfor){
            initExitDialog("个人资料尚未保存，是否退出？");
        }else{
            finish();
        }
    }


    /**
     * uri转缓存地址
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public  File getFrontSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "front.jpg");
        return file;
    }
    public  File getBackSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "back.jpg");
        return file;
    }
}
