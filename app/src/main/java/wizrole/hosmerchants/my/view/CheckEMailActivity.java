package wizrole.hosmerchants.my.view;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.my.model.changepassword.ChangePassBack;
import wizrole.hosmerchants.my.model.checkemail.CheckEMailBack;
import wizrole.hosmerchants.my.preserent.changepassword.ChangePassInterface;
import wizrole.hosmerchants.my.preserent.changepassword.ChangePassPreserent;
import wizrole.hosmerchants.my.preserent.checkemail.CheckEMailInterface;
import wizrole.hosmerchants.my.preserent.checkemail.CheckEMailPreserent;
import wizrole.hosmerchants.util.RegexUtil;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 * 忘记密码--验证邮箱
 */

public class CheckEMailActivity extends BaseActivity  implements View.OnClickListener,CheckEMailInterface,ChangePassInterface{
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_email)LinearLayout lin_email;
    @BindView(R.id.lin_edit_pass)LinearLayout lin_edit_pass;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_check)TextView text_check;
    @BindView(R.id.edit_email)EditText edit_email;
    @BindView(R.id.edit_yzm)EditText edit_yzm;
    @BindView(R.id.edit_pass)EditText edit_pass;
    @BindView(R.id.edit_pass_again)EditText edit_pass_again;
    @BindView(R.id.btn_check)Button btn_check;
    public boolean status=false;

    public CheckEMailPreserent checkEMailPreserent=new CheckEMailPreserent(this);
    public ChangePassPreserent changePassPreserent=new ChangePassPreserent(this);

    @Override
    protected int getLayout() {
        return R.layout.activity_checkemail;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("验证邮箱");
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        text_check.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                if(status){
                    status=false;
                    lin_email.setVisibility(View.VISIBLE);
                    edit_email.setVisibility(View.VISIBLE);
                    lin_edit_pass.setVisibility(View.GONE);
                }else{
                    finish();
                }
                break;
            case R.id.text_check:
                getEmail();
                break;
            case R.id.btn_check:
                if(status){//修改密码
                    changePassword();
                }else{//验证邮箱
                    checckEmail();
                }
                break;
        }
    }

    /**
     * 获取验证邮箱验证码
     */
    public void getEmail(){
        if (edit_email.getText().toString().length()==0){
            ToastShow("邮箱账号不能为空");
        }else if(!RegexUtil.isEmail(edit_email.getText().toString())){
            ToastShow("邮箱格式不正确");
        }else{
            dialog= LoadingDailog.createLoadingDialog(CheckEMailActivity.this,"加载中");
            checkEMailPreserent.getCheckEMail(edit_email.getText().toString());
        }
    }

    /**
     * 验证验证码
     */
    public void checckEmail(){
        if(yzm.equals("")){
            ToastShow("请先获取验证码");
        }else if(edit_yzm.getText().toString().length()==0){
            ToastShow("验证码不能为空");
        }else if(!edit_yzm.getText().toString().equals(yzm)){
            ToastShow("验证码错误，请重新校验");
        }else if(edit_yzm.getText().toString().equals(yzm)){
            ToastShow("验证成功，请修改密码");
            status=true;
            lin_email.setVisibility(View.GONE);
            edit_email.setVisibility(View.GONE);
            lin_edit_pass.setVisibility(View.VISIBLE);
        }else{
            ToastShow("验证错误，请重新校验");
        }
    }
    public void changePassword(){
        if(edit_pass.getText().toString().length()==0){
            ToastShow("密码不能为空");
        }else if(edit_pass_again.getText().toString().length()==0){
            ToastShow("请再次输入密码");
        }else{
            dialog=LoadingDailog.createLoadingDialog(CheckEMailActivity.this,"加载中");
            changePassPreserent.change(HostNo,edit_pass.getText().toString());
        }
    }
    public Dialog dialog;
    public CheckEMailBack checkemailBack;
    public String yzm="";//验证码
    public String HostNo="";//id
    public String err_msg;
    /**
     * 获取发送邮箱的验证码
     * @param checkEMailBack
     */
    @Override
    public void getEmailSucc(CheckEMailBack checkEMailBack) {
        checkemailBack=checkEMailBack;
        handler.sendEmptyMessage(1);
    }

    @Override
    public void getEmailFail(String msg) {
        err_msg=msg;
        handler.sendEmptyMessage(0);
    }

    /**
     * 修改密码回调
     * @param changePassBack
     */
    @Override
    public void getNewPassSucc(ChangePassBack changePassBack) {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void getNewPassFail(String msg) {
        handler.sendEmptyMessage(3);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    LoadingDailog.closeDialog(dialog);
                    if(err_msg.equals("")){
                        ToastShow("网络连接失败，请检查网络");
                    }else{
                        ToastShow(err_msg);
                    }
                    break;
                case 1:
                    yzm=checkemailBack.getCheckCode();
                    HostNo=checkemailBack.getHostNo();
                    LoadingDailog.closeDialog(dialog);
                    timer.start();
                    ToastShow("验证码已发送至邮箱");
                    break;
                case 2://修改密码成功
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("密码修改成功");
                    finish();
                    break;
                case 3://修改密码失败
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("网络连接失败，请检查网络");
                    break;
            }
        }
    };

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            text_check.setText((millisUntilFinished / 1000) + "S");
        }

        @Override
        public void onFinish() {
            text_check.setEnabled(true);
            text_check.setText("获取验证码");
        }
    };

    @Override
    public void onBackPressed() {
        if(status){
            status=false;
            lin_email.setVisibility(View.VISIBLE);
            edit_email.setVisibility(View.VISIBLE);
            lin_edit_pass.setVisibility(View.GONE);
        }else{
            finish();
        }
    }
}
