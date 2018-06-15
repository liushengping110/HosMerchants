package wizrole.hosmerchants.my.model.storecate;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class StoreCateHttp {

    public StoreCateBackInterface storeCateBackInterface;
    public StoreCateHttp(StoreCateBackInterface storeCateBackInterface){
        this.storeCateBackInterface=storeCateBackInterface;
    }
    public void getStoreCate(){
        try{
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y015");
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    storeCateBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        storeCateBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        StoreCateBack  back=new StoreCateBack();
                        back=gson.fromJson(o.toString(),StoreCateBack.class);
                        if(back.getResultCode().equals("0")){
                            storeCateBackInterface.Succ(back);
                        }else{
                            storeCateBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            storeCateBackInterface.Fail("");
        }
    }
}
