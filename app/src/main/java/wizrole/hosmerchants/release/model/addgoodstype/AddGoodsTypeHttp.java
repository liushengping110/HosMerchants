package wizrole.hosmerchants.release.model.addgoodstype;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class AddGoodsTypeHttp {

    public AddGoodsTypeBackInterface addGoodsTypeBackInterface;

    public AddGoodsTypeHttp(AddGoodsTypeBackInterface addGoodsTypeBackInterface){
        this.addGoodsTypeBackInterface=addGoodsTypeBackInterface;
    }

    public void addGoodsTypeHttp(String id,String cateName){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y010");
            object.put("StoreNo",id);
            object.put("TypeName",cateName);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    addGoodsTypeBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        addGoodsTypeBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        AddGoodsTypeBack back=new AddGoodsTypeBack();
                        back=gson.fromJson(o.toString(),AddGoodsTypeBack.class);
                        if(back.getResultCode().equals("0")){
                            addGoodsTypeBackInterface.Succ(back);
                        }else{
                            addGoodsTypeBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            addGoodsTypeBackInterface.Fail("");
        }
    }
}
