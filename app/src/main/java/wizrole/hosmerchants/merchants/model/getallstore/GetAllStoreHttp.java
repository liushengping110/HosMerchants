package wizrole.hosmerchants.merchants.model.getallstore;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class GetAllStoreHttp {
    public GetAllStoreBackInterface getAllStoreBackInterface;
    public GetAllStoreHttp(GetAllStoreBackInterface getAllStoreBackInterface){
        this.getAllStoreBackInterface=getAllStoreBackInterface;
    }
    public void getAllStore(String StoreType,String OrderType,int page ){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y016");
            object.put("StoreType",StoreType);
            object.put("PageNo",page );
            object.put("OrderType",OrderType );
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getAllStoreBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        getAllStoreBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        MyStoreInforBack back=new MyStoreInforBack();
                        back=gson.fromJson(o.toString(),MyStoreInforBack.class);
                        if(back.getResultCode().equals("0")){
                            getAllStoreBackInterface.Succ(back);
                        }else{
                            if(back.getResultContent().equals("服务器ERROR")){
                                getAllStoreBackInterface.Fail("");
                            }else{
                                getAllStoreBackInterface.Fail("null");
                            }
                        }
                    }
                }
            });
        }catch (JSONException e){
            getAllStoreBackInterface.Fail("");
        }
    }
}
