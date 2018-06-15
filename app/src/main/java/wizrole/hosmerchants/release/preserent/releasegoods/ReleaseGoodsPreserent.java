package wizrole.hosmerchants.release.preserent.releasegoods;

import wizrole.hosmerchants.release.model.releasegoods.ReleaseGoodsBack;
import wizrole.hosmerchants.release.model.releasegoods.ReleaseGoodsBackInferface;
import wizrole.hosmerchants.release.model.releasegoods.ReleaseGoodsHttp;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class ReleaseGoodsPreserent implements ReleaseGoodsBackInferface{

    public ReleaseGoodsInterface releaseGoodsInterface;

    public ReleaseGoodsHttp releaseGoodsHttp;

    public ReleaseGoodsPreserent(ReleaseGoodsInterface releaseGoodsInterface){
        this.releaseGoodsInterface=releaseGoodsInterface;
        releaseGoodsHttp=new ReleaseGoodsHttp(this);
    }

    public void releaseGoods(String CommodityName ,String CommodityPic ,String CommodityContent ,
                             String CommodityAmt,String BelongStoreNo,String BelongTypeNo){
        releaseGoodsHttp.releaseGoods( CommodityName , CommodityPic , CommodityContent ,
                 CommodityAmt, BelongStoreNo, BelongTypeNo);
    }
    @Override
    public void Succ(Object o) {
        releaseGoodsInterface.ReleaseGoodsSucc((ReleaseGoodsBack)o);
    }

    @Override
    public void Fail(String msg) {
        releaseGoodsInterface.ReleaseGoodsFail(msg);
    }
}
