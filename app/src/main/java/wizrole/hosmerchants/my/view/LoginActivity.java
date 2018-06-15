package wizrole.hosmerchants.my.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.login.LoginBack;
import wizrole.hosmerchants.my.preserent.login.LoginPreserent;
import wizrole.hosmerchants.my.preserent.login.LoginInterface;
import wizrole.hosmerchants.util.dialog.LoadingDailog;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2017/11/29/029.
 * 何人执笔？
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements LoginInterface,View.OnClickListener {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_regist)TextView text_regist;
    @BindView(R.id.text_forget_password)TextView text_forget_password;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.btn_login)Button btn_login;
    @BindView(R.id.edit_password)EditText edit_password;
    @BindView(R.id.edit_name)EditText edit_name;
    @BindView(R.id.img_login_logo)ImageView img_login_logo;
    public LoginPreserent preserent=new LoginPreserent(this);
    public Dialog dialog;
    public static final int RESULT_REGIST_CODE=2;//登录结果吗
    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("用户登录");
        Drawable drawable_tel=getResources().getDrawable(R.drawable.regist_tel_left);
        drawable_tel.setBounds(0,0,70,70);
        edit_name.setCompoundDrawables(drawable_tel,null,null,null);
        Drawable drawable_pass=getResources().getDrawable(R.drawable.regist_pass_left);
        drawable_pass.setBounds(0,0,70,70);
        edit_password.setCompoundDrawables(drawable_pass,null,null,null);
        ImageLoading.common(LoginActivity.this,R.mipmap.ic_launcher,img_login_logo,R.mipmap.ic_launcher);
    }

    @Override
    protected void setListener() {
        btn_login.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        text_regist.setOnClickListener(this);
        text_forget_password.setOnClickListener(this);
    }

    public boolean check(){
        if(edit_name.getText().length()==0){
            ToastShow("用户账号不能为空");
            return false;
        }else if(edit_password.getText().length()==0){
            ToastShow("用户密码不能为空");
            return false;
        }else{
            return true;
        }
    }

    public LoginBack loginback;
    public String err_msg;
    @Override
    public void getDataSucc(LoginBack loginBack) {
        loginback=loginBack;
        if(loginback.getResultCode().equals("0")){
            handler.sendEmptyMessage(0);
        }else{
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void getDataFail(String msg) {
        err_msg=msg;
        handler.sendEmptyMessage(1);
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(check()){
                    dialog= LoadingDailog.createLoadingDialog(LoginActivity.this,"登录中");
                    preserent.Http(edit_name.getText().toString(),edit_password.getText().toString());
                }
                break;
            case R.id.text_regist://注册
                intent=new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.text_forget_password://忘记密码
                intent=new Intent(LoginActivity.this,CheckEMailActivity.class);
                startActivity(intent);
                break;
        }
    }
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    SharedPreferenceUtil.saveUserTel(LoginActivity.this,edit_name.getText().toString());
                    SharedPreferenceUtil.saveUserPassW(LoginActivity.this,edit_password.getText().toString());
                    SharedPreferenceUtil.saveID(LoginActivity.this,loginback.getHostNo());//保存主键id
                    SharedPreferenceUtil.saveLoginState(LoginActivity.this,1);//保存登录状态
                    SharedPreferenceUtil.saveUserName(LoginActivity.this,loginback.getHostName());//保存用户姓名
                    SharedPreferenceUtil.saveHeaderImg(LoginActivity.this, Constant.ip+loginback.getHostAvatar());//用户头像
                    SharedPreferenceUtil.saveInforComplete(LoginActivity.this,loginback.getInfoSign());//保存信息完善程度
                    ToastShow("登录成功");
                    Intent intent=new Intent();
                    intent.putExtra("name",loginback.getHostName());
                    intent.putExtra("image",loginback.getHostAvatar());
                    intent.putExtra("infoSign",loginback.getInfoSign());
                    setResult(RESULT_REGIST_CODE,intent);
                    finish();
                    break;
                case 1:
                    LoadingDailog.closeDialog(dialog);
                    if(err_msg.equals("")){
                        ToastShow("登录失败，请检查网络");
                    }else{
                        ToastShow(err_msg);
                    }
                    break;
            }
        }
    };
}
