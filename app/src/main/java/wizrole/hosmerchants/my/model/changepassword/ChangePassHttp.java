package wizrole.hosmerchants.my.model.changepassword;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 */

public class ChangePassHttp {
    public ChangePassBackInterface changePassBackInterface;
    public ChangePassHttp(ChangePassBackInterface changePassBackInterface){
        this.changePassBackInterface=changePassBackInterface;
    }

    public void ChangePassHttp(String id,String pass){
        JSONObject  object=new JSONObject();
        try {
            object.put("TradeCode","Y006");
            object.put("HostNo",id);
            object.put("HostPass",pass);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    changePassBackInterface.Fail(e.toString());
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        changePassBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        ChangePassBack back=new ChangePassBack();
                        back=gson.fromJson(o.toString(),ChangePassBack.class);
                        if(back.getResultCode().equals("0")){
                            changePassBackInterface.Succ(back);
                        }else{
                            changePassBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            changePassBackInterface.Fail(e.toString());
        }
    }
}
