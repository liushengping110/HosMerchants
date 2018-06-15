package wizrole.hosmerchants.my.model.myinfor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/13/013.
 * 何人执笔？
 */

public class MyInforHttp {
    public MyInforBackInterface anInterface;
    public MyInforHttp(MyInforBackInterface anInterface){
        this.anInterface=anInterface;
    }

    public void getMyInfor(String id){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y008");
            object.put("HostNo",id);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    anInterface.Fail(e.toString());
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        anInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        MyInforBack back=new MyInforBack();
                        back=gson.fromJson(o.toString(),MyInforBack.class);
                        if(back.getResultCode().equals("0")){
                            anInterface.Succ(back);
                        }else{
                            anInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            anInterface.Fail(e.toString());
        }
    }

}
