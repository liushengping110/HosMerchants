package wizrole.hosmerchants.admin.model.changegoodscate;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.changestoreinfor.ChangeStoreInforBack;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 */

public class ChangeGoodsCateHttp {
    public ChangeGoodsCateBackInterface changeGoodsCateBackInterface;
    public ChangeGoodsCateHttp(ChangeGoodsCateBackInterface changeGoodsCateBackInterface){
        this.changeGoodsCateBackInterface=changeGoodsCateBackInterface;
    }
    public void changeGoodsCateName(String id,String name){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y018");
            object.put("TypeId",id);
            object.put("TypeName",name);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    changeGoodsCateBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        changeGoodsCateBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        ChangeGoodsCateBack back=new ChangeGoodsCateBack();
                        back=gson.fromJson(o.toString(),ChangeGoodsCateBack.class);
                        if(back.getResultCode().equals("0")){
                            changeGoodsCateBackInterface.Succ(back);
                        }else{
                            changeGoodsCateBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            changeGoodsCateBackInterface.Fail("");
        }
    }
}
