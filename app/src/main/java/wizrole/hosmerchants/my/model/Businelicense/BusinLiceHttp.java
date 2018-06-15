package wizrole.hosmerchants.my.model.Businelicense;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;
import wizrole.hosmerchants.util.image.ImageTrun;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class BusinLiceHttp {
    public BusineLicenBackInterface busineLicenBackInterface;
    public BusinLiceHttp(BusineLicenBackInterface busineLicenBackInterface){
        this.busineLicenBackInterface=busineLicenBackInterface;
    }

    public void BusinLicHttp(String token,String filepath){
        try {
            String base64= ImageTrun.encodeBase64File(filepath,450,300);
            FormBody formBody = new FormBody.Builder()
                    .add("image",base64)
                    .build();
            RxJavaOkPotting.getInstance(Constant.businLicense_url).Ask("access_token=" + token, formBody, new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    busineLicenBackInterface.Fail(e.toString());
                }

                @Override
                public void onNext(Object o) {
                    int err=o.toString().indexOf("error_code");
                    if(err!=-1) {//返回结果包含 error_code--说明扫描失败
                        busineLicenBackInterface.Fail("fail");
                    }else{
                        Gson gson=new Gson();
                        BusineLicenBack back=new BusineLicenBack();
                        back=gson.fromJson(o.toString(),BusineLicenBack.class);
                        busineLicenBackInterface.Succ(back);
                    }
                }
            });
        }catch (IOException e){
            busineLicenBackInterface.Fail(e.toString());
        }
    }
}
