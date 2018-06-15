package wizrole.hosmerchants.merchants.model.viewpager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/22/022.
 * 何人执笔？
 */

public class ViewPagerHttp {

    public ViewPagerBackInterface viewPagerBackInterface;
    public ViewPagerHttp(ViewPagerBackInterface viewPagerBackInterface){
        this.viewPagerBackInterface=viewPagerBackInterface;
    }
    public void getImage(){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y999");
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    viewPagerBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(RxJavaOkPotting.NET_ERR.equals(o)){
                        viewPagerBackInterface.Fail("");
                    }else {
                        Gson gson=new Gson();
                        ViewPagerBack back=new ViewPagerBack();
                        back=gson.fromJson(o.toString(),ViewPagerBack.class);
                        if (back.getResultCode().equals("0")){
                            viewPagerBackInterface.Succ(back);
                        }else{
                            viewPagerBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            viewPagerBackInterface.Fail("");
        }
    }
}
