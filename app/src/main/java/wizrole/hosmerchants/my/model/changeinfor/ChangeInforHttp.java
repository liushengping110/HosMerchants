package wizrole.hosmerchants.my.model.changeinfor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.HttpStatusCallBack;
import wizrole.hosmerchants.util.http.OkPotting;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 */

public class ChangeInforHttp {

    public ChangeInforBackInterface changeInforBackInterface;

    public  ChangeInforHttp(ChangeInforBackInterface changeInforBackInterface){
        this.changeInforBackInterface=changeInforBackInterface;
    }

    /**
     * 修改个人信息
     * @param HostName
     * @param HostPhone
     * @param HostEmail
     * @param HostAvatar
     * @param HostNo
     * @param HostIdCard
     * @param HostAddress
     * @param BornDate
     * @param Sex
     * @param Ethnic
     * @param IssuingBody
     * @param Validity
     * @param IdCardName
     */
    public void ChangeInfor(String HostName,String HostPhone,String HostEmail,String HostAvatar,String HostNo
            ,String HostIdCard,String HostAddress,String BornDate,String Sex,String Ethnic,String IssuingBody,String Validity,String IdCardName){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y005");
            object.put("HostName",HostName);
            object.put("HostPhone",HostPhone);
            object.put("HostEmail",HostEmail);
            object.put("HostAvatar",HostAvatar);
            object.put("HostNo",HostNo);
            object.put("HostIdCard",HostIdCard);
            object.put("HostAddress",HostAddress);
            object.put("BornDate",BornDate);
            object.put("Sex",Sex);
            object.put("Ethnic",Ethnic);
            object.put("IssuingBody",IssuingBody);
            object.put("Validity",Validity);
            object.put("IdCardName",IdCardName);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    changeInforBackInterface.Fail(e.toString());
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        changeInforBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        ChangeInforBack inforBack=new ChangeInforBack();
                        inforBack=gson.fromJson(o.toString(),ChangeInforBack.class);
                        if(inforBack.getResultCode().equals("0")){
                            changeInforBackInterface.Succ(inforBack);
                        }else{
                            changeInforBackInterface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            changeInforBackInterface.Fail(e.toString());
        }
    }
}
