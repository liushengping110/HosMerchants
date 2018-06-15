package wizrole.hosmerchants.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.BaseActivity;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.login.LoginBack;
import wizrole.hosmerchants.my.preserent.login.LoginInterface;
import wizrole.hosmerchants.my.preserent.login.LoginPreserent;
import wizrole.hosmerchants.my.view.LoginActivity;
import wizrole.hosmerchants.util.SharedPreferenceUtil;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2017/12/15/015.
 * 何人执笔？
 */

public class SplashActivity extends AppCompatActivity implements LoginInterface {

    @BindView(R.id.img_splash)ImageView img_splash;
    public AlphaAnimation animation;
    public LoginPreserent preserent=new LoginPreserent(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        anitomStart();
        login();
        setListener();
    }

    public void anitomStart(){
        ImageLoading.common(SplashActivity.this,R.drawable.splash,img_splash,R.drawable.splash);
        animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(4000);
        img_splash.setAnimation(animation);
        animation.start();
    }

    /**
     * 登录
     */
    public void login(){
        String tel=SharedPreferenceUtil.getUserTel(SplashActivity.this);
        String pass=SharedPreferenceUtil.getUserPassW(SplashActivity.this);
        if(!tel.equals("")&&!pass.equals("")){
            Toast.makeText(SplashActivity.this,"登录中",Toast.LENGTH_LONG).show();
            preserent.Http(tel,pass);
        }
    }
    public void setListener() {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                releaseImageViewResouce(img_splash);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 释放资源
     * @param imageView
     */
    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap=null;
            }
        }
        System.gc();
    }


    public LoginBack loginback;
    @Override
    public void getDataSucc(LoginBack loginBack) {
        loginback=loginBack;
        if(loginback.getResultCode().equals("0")){
            SharedPreferenceUtil.saveID(SplashActivity.this,loginback.getHostNo());//保存主键id
            SharedPreferenceUtil.saveLoginState(SplashActivity.this,1);//保存登录状态
            SharedPreferenceUtil.saveUserName(SplashActivity.this,loginback.getHostName());//保存用户姓名
            SharedPreferenceUtil.saveHeaderImg(SplashActivity.this, Constant.ip+loginback.getHostAvatar());//用户头像
            SharedPreferenceUtil.saveInforComplete(SplashActivity.this,loginback.getInfoSign());//保存信息完善程度
        }
    }

    @Override
    public void getDataFail(String msg) {
        SharedPreferenceUtil.saveLoginState(SplashActivity.this,2);//保存登录状态
    }
}
