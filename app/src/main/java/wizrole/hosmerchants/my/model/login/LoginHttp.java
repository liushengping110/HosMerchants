package wizrole.hosmerchants.my.model.login;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.HttpStatusCallBack;
import wizrole.hosmerchants.util.http.OkPotting;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/11/29/029.
 * 何人执笔？
 */

public class LoginHttp {

    public LoginBackInterface anInterface;
    public LoginHttp(LoginBackInterface anInterface1){
        this.anInterface=anInterface1;
    }
    public void  Login(String name,String password){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y003");
            object.put("LoginName", name);
            object.put("LoginPass", password);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    anInterface.Fail("");
                }
        //我debug  你看看   这就成功了 地址都没变 就是这样添加的   baseUr

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        anInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        LoginBack loginBack=new LoginBack();
                        loginBack=gson.fromJson(o.toString(),LoginBack.class);
                        if(loginBack.getResultCode().equals("0")){
                            anInterface.Succ(loginBack);
                        }else{
                            anInterface.Fail(loginBack.getResultContent());
                        }
                    }
                }
            });
        }catch (JSONException e){
            anInterface.Fail("");
        }
    }
}
