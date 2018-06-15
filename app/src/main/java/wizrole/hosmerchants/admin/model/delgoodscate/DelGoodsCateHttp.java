package wizrole.hosmerchants.admin.model.delgoodscate;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 */

public class DelGoodsCateHttp {
    public DelGoodsCateBackInterface delGoodsCateBackInterface;

    public DelGoodsCateHttp(DelGoodsCateBackInterface delGoodsCateBackInterface){
        this.delGoodsCateBackInterface=delGoodsCateBackInterface;
    }
    public void delGoodsCateName(String StoreNo ,String TypeName ){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y017");
            object.put("StoreNo",StoreNo);
            object.put("TypeName",TypeName);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    delGoodsCateBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        delGoodsCateBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        DelGoodsCateBack back=new DelGoodsCateBack();
                        back=gson.fromJson(o.toString(),DelGoodsCateBack.class);
                        if(back.getResultCode().equals("0")){
                            delGoodsCateBackInterface.Succ(back);
                        }else{
                            delGoodsCateBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            delGoodsCateBackInterface.Fail("");
        }

    }
}
