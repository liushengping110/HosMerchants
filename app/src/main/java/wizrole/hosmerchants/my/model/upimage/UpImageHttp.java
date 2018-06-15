package wizrole.hosmerchants.my.model.upimage;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;
import wizrole.hosmerchants.util.image.ImageTrun;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 */

public class UpImageHttp {

    public UpImageBackInterface upImageBackInterface;

    public UpImageHttp(UpImageBackInterface upImageBackInterface){
        this.upImageBackInterface=upImageBackInterface;
    }

    public void UpImageHttp(String filepath,String PicType){
        JSONObject object=new JSONObject();
        try {
            String base64= ImageTrun.encodeBase64File(filepath,800,800);
            object.put("TradeCode", "Y007");
            object.put("Base64Pic", base64);
            object.put("PicType", PicType);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    upImageBackInterface.Fail("网络连接失败，请检查网络");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        upImageBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        UpImageBack upImageBack=new UpImageBack();
                        upImageBack=gson.fromJson(o.toString(),UpImageBack.class);
                        if(upImageBack.getResultCode().equals("0")){
                            upImageBackInterface.Succ(upImageBack);
                        }else{
                            upImageBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            upImageBackInterface.Fail("");
        }catch (IOException e){
            upImageBackInterface.Fail("");
        }
    }
}
