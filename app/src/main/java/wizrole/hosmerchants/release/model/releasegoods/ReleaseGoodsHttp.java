package wizrole.hosmerchants.release.model.releasegoods;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class ReleaseGoodsHttp {

    public ReleaseGoodsBackInferface releaseGoodsBackInferface;

    public ReleaseGoodsHttp(ReleaseGoodsBackInferface releaseGoodsBackInferface){
        this.releaseGoodsBackInferface=releaseGoodsBackInferface;
    }

    /**
     * CommodityName 	- 商品名称
     CommodityPic 	- 商品图片
     CommodityContent- 商品内容
     CommodityAmt	- 商品价格
     BelongStoreNo	- 所属商店主键ID
     BelongTypeNo	- 所属类别主键ID
     * @param CommodityName
     * @param CommodityPic
     * @param CommodityContent
     * @param CommodityAmt
     * @param BelongStoreNo
     * @param BelongTypeNo
     */
    public void releaseGoods(String CommodityName ,String CommodityPic ,String CommodityContent ,
                             String CommodityAmt,String BelongStoreNo,String BelongTypeNo){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y011");
            object.put("CommodityName",CommodityName);
            object.put("CommodityPic",CommodityPic);
            object.put("CommodityContent",CommodityContent);
            object.put("CommodityAmt",CommodityAmt);
            object.put("BelongStoreNo",BelongStoreNo);
            object.put("BelongTypeNo",BelongTypeNo);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    releaseGoodsBackInferface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        releaseGoodsBackInferface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        ReleaseGoodsBack back=new ReleaseGoodsBack();
                        back=gson.fromJson(o.toString(),ReleaseGoodsBack.class);
                        if(back.getResultCode().equals("0")){
                            releaseGoodsBackInferface.Succ(back);
                        }else{
                            releaseGoodsBackInferface.Fail("");
                        }
                    }
                }
            });
        }catch (JSONException e){
            releaseGoodsBackInferface.Fail("");
        }
    }
}
