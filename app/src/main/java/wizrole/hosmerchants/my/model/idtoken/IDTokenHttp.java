package wizrole.hosmerchants.my.model.idtoken;

import com.google.gson.Gson;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public class IDTokenHttp {

    public IDTokenBackInterface idTokenBackInterface;
    public IDTokenHttp(IDTokenBackInterface idTokenBackInterface){
        this.idTokenBackInterface=idTokenBackInterface;
    }

    public  void getIdToken(){
        RxJavaOkPotting.getInstance(Constant.token_url)
                .Ask("grant_type=client_credentials" + "&client_id=" + Constant.clientId + "&client_secret=" + Constant.clientSecret+"&", new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        idTokenBackInterface.Fail("");
                    }

                    @Override
                    public void onNext(Object o) {
                        if(o.equals(RxJavaOkPotting.NET_ERR)){
                            idTokenBackInterface.Fail("");
                        }else{
                            int result=o.toString().indexOf("error");
                            if(result!=-1){//请求错误
                                idTokenBackInterface.Fail("");
                            }else{//请求成功
                                Gson gson=new Gson();
                                TokenBack token=new TokenBack();
                                token=gson.fromJson(o.toString(),TokenBack.class);
                                idTokenBackInterface.Succ(token);
                            }
                        }
                    }
                });
    }
}
