package wizrole.hosmerchants.admin.model.delgoods;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/8/008.
 * 何人执笔？
 */

public class DelGoodsHttp {

    public DelGoodsBackInterface delGoodsBackInterface;
    public DelGoodsHttp(DelGoodsBackInterface delGoodsBackInterface){
        this.delGoodsBackInterface=delGoodsBackInterface;
    }
    public void delGoodsHttp(String CommodityId){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y020");
            object.put("CommodityId",CommodityId);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    delGoodsBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        delGoodsBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        DelGoodsBack back=new DelGoodsBack();
                        back=gson.fromJson(o.toString(),DelGoodsBack.class);
                        if(back.getResultCode().equals("0")){
                            delGoodsBackInterface.Succ(back);
                        }else{
                            delGoodsBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            delGoodsBackInterface.Fail("");
        }
    }
}
