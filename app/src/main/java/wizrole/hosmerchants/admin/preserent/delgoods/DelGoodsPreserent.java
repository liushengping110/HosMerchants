package wizrole.hosmerchants.admin.preserent.delgoods;

import wizrole.hosmerchants.admin.model.delgoods.DelGoodsBack;
import wizrole.hosmerchants.admin.model.delgoods.DelGoodsBackInterface;
import wizrole.hosmerchants.admin.model.delgoods.DelGoodsHttp;

/**
 * Created by liushengping on 2018/1/8/008.
 * 何人执笔？
 */

public class DelGoodsPreserent implements DelGoodsBackInterface{

    public DelGoodsInterface delGoodsInterface;
    public DelGoodsHttp delGoodsHttp;
    public DelGoodsPreserent(DelGoodsInterface delGoodsInterface){
        this.delGoodsInterface=delGoodsInterface;
        delGoodsHttp=new DelGoodsHttp(this);
    }

    public void delGoodsHttp(String CommodityId){
        delGoodsHttp.delGoodsHttp(CommodityId);
    }

    @Override
    public void Succ(Object o) {
        delGoodsInterface.getDelGoodsSucc((DelGoodsBack)o);
    }

    @Override
    public void Fail(String msg) {
        delGoodsInterface.getDelGoodsFail(msg);
    }
}
