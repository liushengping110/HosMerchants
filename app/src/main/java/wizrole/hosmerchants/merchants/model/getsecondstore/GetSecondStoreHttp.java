package wizrole.hosmerchants.merchants.model.getsecondstore;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/31.
 * 何人执笔？
 */

public class GetSecondStoreHttp {

    public GetSecondStoreBackInterface getSecondStoreBackInterface;
    public GetSecondStoreHttp(GetSecondStoreBackInterface getSecondStoreBackInterface){
        this.getSecondStoreBackInterface=getSecondStoreBackInterface;
    }


    public void getAllStore(String id,int page ){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y023");
            object.put("TwoTypeId",id);
            object.put("PageNo",page );
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getSecondStoreBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        getSecondStoreBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        MyStoreInforBack back=new MyStoreInforBack();
                        back=gson.fromJson(o.toString(),MyStoreInforBack.class);
                        if(back.getResultCode().equals("0")){
                            getSecondStoreBackInterface.Succ(back);
                        }else{
                            getSecondStoreBackInterface.Fail("null");
                        }
                    }
                }
            });
        }catch (JSONException e){
            getSecondStoreBackInterface.Fail("");
        }
    }
}
