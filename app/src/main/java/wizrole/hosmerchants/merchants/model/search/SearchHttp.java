package wizrole.hosmerchants.merchants.model.search;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/4/004.
 * 何人执笔？
 */

public class SearchHttp {
    public SearchBackInterface searchBackInterface;
    public SearchHttp(SearchBackInterface searchBackInterface){
        this.searchBackInterface=searchBackInterface;
    }

    public void SearchGetInfor(String msg){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y019");
            object.put("AlikeName",msg);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    searchBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        searchBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        MyStoreInforBack back=new MyStoreInforBack();
                        back=gson.fromJson(o.toString(),MyStoreInforBack.class);
                        if(back.getResultCode().equals("0")){
                            searchBackInterface.Succ(back);
                        }else{
                            searchBackInterface.Fail("null");
                        }
                    }
                }
            });
        }catch (JSONException e){
            searchBackInterface.Fail("");
        }
    }
}
