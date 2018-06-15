package wizrole.hosmerchants.my.model.addstore;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 */

public class AddStoreHttp {

    public AddStoreBackInterface addStoreBackInterface;

    public AddStoreHttp(AddStoreBackInterface addStoreBackInterface){
        this.addStoreBackInterface=addStoreBackInterface;
    }

    public void AddStoreHttp(String StoreName,String StorePlace,String StorePhone ,String StoreType
    ,String StoreLogoPic,String StorePayPic ,String WeChatPic,String HostNo ,String CompanyName
    ,String LegalPerson,String CompanyAddress,String LicenseValidity,String LicenseNo
    ,String CreditCode){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y004");
            object.put("StoreName",StoreName);
            object.put("StorePlace",StorePlace);
            object.put("StorePhone",StorePhone);
            object.put("StoreType",StoreType);
            object.put("StoreLogoPic",StoreLogoPic);
            object.put("StorePayPic",StorePayPic);
            object.put("WeChatPic",WeChatPic);
            object.put("HostNo",HostNo);
            object.put("CompanyName",CompanyName);
            object.put("LegalPerson",LegalPerson);
            object.put("CompanyAddress",CompanyAddress);
            object.put("LicenseValidity",LicenseValidity);
            object.put("LicenseNo",LicenseNo);
            object.put("CreditCode",CreditCode);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    addStoreBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        addStoreBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        AddStoreBack back=new AddStoreBack();
                        back=gson.fromJson(o.toString(),AddStoreBack.class);
                        if(back.getResultCode().equals("0")){
                            addStoreBackInterface.Succ(back);
                        }else{
                            addStoreBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            addStoreBackInterface.Fail("");
        }
    }
}
