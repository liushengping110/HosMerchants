package wizrole.hosmerchants.my.model.regist;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.HttpStatusCallBack;
import wizrole.hosmerchants.util.http.OkPotting;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 */

public class RegistHttp {

    public RegistBacktInterface registBacktInterface;
    public RegistHttp(RegistBacktInterface registBacktInterface) {
        this.registBacktInterface = registBacktInterface;
    }



    public void regist(String tel,String pass,String email){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y001");
            object.put("HostEmail",email);
            object.put("HostPhone",tel);
            object.put("HostPass",pass);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    registBacktInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        registBacktInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        RegistBack registBack=new RegistBack();
                        registBack=gson.fromJson(o.toString(),RegistBack.class);
                        if(registBack.getResultCode().equals("0")){
                            registBacktInterface.Succ(registBack);
                        }else{
                            registBacktInterface.Fail(registBack.getResultContent());
                        }
                    }
                }
            });
        }catch (JSONException e){
            registBacktInterface.Fail("");
        }
    }
}

