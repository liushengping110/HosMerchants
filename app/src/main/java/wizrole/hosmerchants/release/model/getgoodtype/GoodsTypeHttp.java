package wizrole.hosmerchants.release.model.getgoodtype;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class GoodsTypeHttp {

    public GoodsTypeBackInterface goodsTypeBackInterface;

    public GoodsTypeHttp(GoodsTypeBackInterface goodsTypeBackInterface){
        this.goodsTypeBackInterface=goodsTypeBackInterface;
    }

    public void getGoodsType(String id){
        try {
            JSONObject object=new JSONObject();
            object.put("TradeCode","Y013");
            object.put("StoreNo",id);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    goodsTypeBackInterface.Fail("");
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        goodsTypeBackInterface.Fail("");
                    }else{
                        Gson gson=new Gson();
                        GoodsTypeBack back=new GoodsTypeBack();
                        back=gson.fromJson(o.toString(),GoodsTypeBack.class);
                        if(back.getResultCode().equals("0")){//都有数据--由于多处复用，避免ui更新混乱
                            goodsTypeBackInterface.Succ(back);
                        }else if(back.getResultCode().equals("2")){//所有商品为空，有分类
                            goodsTypeBackInterface.Succ(back);
                        }else{//--1--都无
                            goodsTypeBackInterface.Succ(back);
                        }
                    }
                }
            });
        }catch (JSONException e){
            goodsTypeBackInterface.Fail("");
        }
    }
}
