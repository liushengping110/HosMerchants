package wizrole.hosmerchants.my.model.idinforfont;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.idinforback.IDInforBack;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;
import wizrole.hosmerchants.util.image.ImageTrun;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public class IDInforHttp {
    public IDInforBackInterface idInforBackInterface;

    public IDInforHttp(IDInforBackInterface idInforBackInterface){
        this.idInforBackInterface = idInforBackInterface;
    }


    /**
     * 获取身份证信息
     * @param side front前  back后
     * @param filePath 图片base64字节码
     */
    public void getMyIDInfor(String token, final String side, String filePath){
        try {
            String base64= ImageTrun.encodeBase64File(filePath,450,300);
            FormBody formBody = new FormBody.Builder()
                    .add("detect_direction","true")
                    .add("id_card_side",side)
                    .add("image",base64)
                    .add("detect_risk","true")
                    .build();
            RxJavaOkPotting.getInstance(Constant.id_url).Ask("access_token=" + token, formBody, new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    idInforBackInterface.Fail(e.toString());
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        idInforBackInterface.Fail("");
                    }else{
                        int err=o.toString().indexOf("error_code");
                        if(err!=-1) {//返回结果包含 error_code--说明扫描失败
                            idInforBackInterface.Fail("fail");
                        }else{//扫描成功
                            if(side.equals("front")){//身份证正面
                                int result = o.toString().indexOf("edit_tool");
                                if(result!= -1){//返回结果包含edit_tool--身份证正面，被编辑过
                                    idInforBackInterface.Fail("edit");
                                }else{//不包含edit_tool--身份证正面--未被编辑
                                    Gson gson=new Gson();
                                    IDInforFont idInforFont =new IDInforFont();
                                    idInforFont =gson.fromJson(o.toString(),IDInforFont.class);
                                    idInforBackInterface.Succ(idInforFont);
                                }
                            }else{//身份证反面
                                int result = o.toString().indexOf("edit_tool");
                                if(result!= -1){//返回结果包含edit_tool--被编辑过
                                    idInforBackInterface.Fail("edit");
                                }else{//不包含edit_tool--未被编辑
                                    Gson gson=new Gson();
                                    IDInforBack idInforBack =new IDInforBack();
                                    idInforBack =gson.fromJson(o.toString(),IDInforBack.class);
                                    idInforBackInterface.Succ(idInforBack);
                                }
                            }
                        }
                    }
                }
            });
        }catch (IOException E){
            idInforBackInterface.Fail("");
        }
    }
}
