package wizrole.hosmerchants.my.view;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.my.model.regist.RegistBack;
import wizrole.hosmerchants.my.preserent.regist.RegistInerface;
import wizrole.hosmerchants.my.preserent.regist.RegistPreserent;
import wizrole.hosmerchants.util.RegexUtil;
import wizrole.hosmerchants.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/12/11/011.
 * 何人执笔？
 * 注册页面
 */

public class RegistActivity extends BaseActivity implements View.OnClickListener,RegistInerface{
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.edit_tel)EditText edit_tel;
    @BindView(R.id.edit_password)EditText edit_password;
    @BindView(R.id.edit_password_again)EditText edit_password_again;
    @BindView(R.id.edit_email)EditText edit_email;
    @BindView(R.id.btn_regist)Button btn_regist;
    public RegistPreserent preserent=new RegistPreserent(this);
    public Dialog dialog;
    @Override
    protected int getLayout() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("商户注册");
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
        btn_regist.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.btn_regist://注册
                if(checkEdit()){//验证后
                    dialog= LoadingDailog.createLoadingDialog(RegistActivity.this,"注册中");
                    preserent.Http(edit_tel.getText().toString(),edit_password.getText().toString(),edit_email.getText().toString());
                }
                break;
        }
    }
    public boolean checkEdit(){
        boolean status;
        if(edit_tel.getText().toString().length()==0){
            ToastShow("手机号码不能为空");
            status=false;
        }else if(!RegexUtil.isMobilePhoneNumber(edit_tel.getText().toString())){
            ToastShow("手机号码格式错误");
            status=false;
        }else if(edit_password.getText().toString().length()==0){
            ToastShow("密码不能为空");
            status=false;
        }else if(edit_password_again.getText().toString().length()==0){
            ToastShow("请您再次输入密码");
            status=false;
        }else if(!edit_password.getText().toString().equals(edit_password_again.getText().toString())){
            ToastShow("两次密码不一致");
            status=false;
        }else if(edit_email.getText().toString().length()==0){
            ToastShow("邮箱不能为空");
            status=false;
        }else if(!RegexUtil.isEmail(edit_email.getText().toString())){
            ToastShow("邮箱格式错误");
            status=false;
        }else{
            status=true;
        }
        return status;
    }

    @Override
    public void getDataSucc(RegistBack registBack) {
        handler.sendEmptyMessage(0);
    }

    public String err_msg;
    @Override
    public void getDataFail(String msg) {
        err_msg=msg;
        handler.sendEmptyMessage(1);
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    LoadingDailog.closeDialog(dialog);
                    ToastShow("注册成功");
                    finish();
                    break;
                case 1:
                    LoadingDailog.closeDialog(dialog);
                    if(err_msg.equals("")){
                        ToastShow("注册失败，请检查网络");
                    }else{
                        ToastShow(err_msg);
                    }
                    break;
            }
        }
    };
}
