package wizrole.hosmerchants.my.model.mystoreinfor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/15/015.
 * 何人执笔？
 */

public class MyStoreInforHttp {

    public MyStoreInforBackInterface myStoreInforBackInterface;
    public MyStoreInforHttp(MyStoreInforBackInterface myStoreInforBackInterface){
        this.myStoreInforBackInterface=myStoreInforBackInterface;
    }

    public void getMyStoreInfor(String id){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y009");
            object.put("HostNo",id);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    myStoreInforBackInterface.Fail(e.toString());
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        myStoreInforBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        MyStoreInforBack myStoreInforBack=new MyStoreInforBack();
                        myStoreInforBack=gson.fromJson(o.toString(),MyStoreInforBack.class);
                        if(myStoreInforBack.getResultCode().equals("0")){
                            myStoreInforBackInterface.Succ(myStoreInforBack);
                        }else{
                            myStoreInforBackInterface.Fail("null");
                        }
                    }
                }
            });
        }catch (JSONException e){
            myStoreInforBackInterface.Fail(e.toString());
        }
    }
}
