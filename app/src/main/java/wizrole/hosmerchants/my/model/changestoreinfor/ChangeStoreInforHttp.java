package wizrole.hosmerchants.my.model.changestoreinfor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/20/020.
 * 何人执笔？
 */

public class ChangeStoreInforHttp {

    public ChangeStoreInforBackInterface changeStoreInforBackInterface;
    public ChangeStoreInforHttp(ChangeStoreInforBackInterface changeStoreInforBackInterface){
        this.changeStoreInforBackInterface=changeStoreInforBackInterface;
    }
    public void changeStoreInforHttp(String StoreNo,String StoreName,String StorePlace,String StorePhone ,String StoreType
            ,String StoreLogoPic,String StorePayPic ,String WeChatPic,String HostNo ,String CompanyName
            ,String LegalPerson,String CompanyAddress,String LicenseValidity,String LicenseNo
            ,String CreditCode){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y014");
            object.put("StoreNo", StoreNo);
            object.put("StoreName", StoreName);
            object.put("StorePlace", StorePlace);
            object.put("StorePhone", StorePhone);
            object.put("StoreType", StoreType);
            object.put("StoreLogoPic", StoreLogoPic);
            object.put("StorePayPic", StorePayPic);
            object.put("WeChatPic", WeChatPic);
            object.put("HostNo", HostNo);
            object.put("CompanyName", CompanyName);
            object.put("LegalPerson", LegalPerson);
            object.put("CompanyAddress", CompanyAddress);
            object.put("LicenseValidity", LicenseValidity);
            object.put("LicenseNo", LicenseNo);
            object.put("CreditCode", CreditCode);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    changeStoreInforBackInterface.Fail(e.toString());
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        changeStoreInforBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        ChangeStoreInforBack back=new ChangeStoreInforBack();
                        back=gson.fromJson(o.toString(),ChangeStoreInforBack.class);
                        if(back.getResultCode().equals("0")){
                            changeStoreInforBackInterface.Succ(back);
                        }else{
                            changeStoreInforBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            changeStoreInforBackInterface.Fail(e.toString());
        }
    }
}
