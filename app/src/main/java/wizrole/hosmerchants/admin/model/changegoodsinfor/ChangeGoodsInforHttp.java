package wizrole.hosmerchants.admin.model.changegoodsinfor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2018/1/8/008.
 * 何人执笔？
 */

public class ChangeGoodsInforHttp {

    public ChangeGoodsInforBackInterface changeGoodsInforInterface;

    public  ChangeGoodsInforHttp(ChangeGoodsInforBackInterface changeGoodsInforInterface){
        this.changeGoodsInforInterface=changeGoodsInforInterface;
    }

    /**
     *
     CommodityNo 	- 商品主键ID
     CommodityName 	- 商品名称
     CommodityPic 	- 商品图片
     CommodityContent- 商品内容
     CommodityAmt	- 商品价格
     BelongTypeNo	- 所属类别主键ID
     */
    public void ChangeGoodsInforHttp(String CommodityNo ,
            String CommodityName ,
            String CommodityPic,
            String CommodityContent,
            String CommodityAmt,
            String BelongTypeNo){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y012");
            object.put("CommodityNo",CommodityNo);
            object.put("CommodityName",CommodityName);
            object.put("CommodityPic",CommodityPic);
            object.put("CommodityContent",CommodityContent);
            object.put("CommodityAmt",CommodityAmt);
            object.put("BelongTypeNo",BelongTypeNo);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    changeGoodsInforInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        changeGoodsInforInterface.Fail("");
                    }else {
                        Gson  gson=new Gson();
                        ChangeGoodsInforBack back=new ChangeGoodsInforBack();
                        back=gson.fromJson(o.toString(), ChangeGoodsInforBack.class);
                        if(back.getResultCode().equals("0")){
                            changeGoodsInforInterface.Succ(back);
                        }else{
                            changeGoodsInforInterface.Fail(back.getResultContent());
                        }
                    }
                }
            });
        }catch (JSONException e){
            changeGoodsInforInterface.Fail("");
        }
    }
}
