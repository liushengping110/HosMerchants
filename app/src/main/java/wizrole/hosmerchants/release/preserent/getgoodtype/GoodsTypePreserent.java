package wizrole.hosmerchants.release.preserent.getgoodtype;

import wizrole.hosmerchants.release.model.getgoodtype.GoodsTypeBack;
import wizrole.hosmerchants.release.model.getgoodtype.GoodsTypeBackInterface;
import wizrole.hosmerchants.release.model.getgoodtype.GoodsTypeHttp;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class GoodsTypePreserent implements GoodsTypeBackInterface {
    public GoodsTypeInterface goodsTypeInterface;
    public GoodsTypeHttp goodsTypeHttp;

    public GoodsTypePreserent(GoodsTypeInterface goodsTypeInterface){
        this.goodsTypeInterface=goodsTypeInterface;
        goodsTypeHttp=new GoodsTypeHttp(this);
    }
    public void getGoodsType(String id){
        goodsTypeHttp.getGoodsType(id);
    }

    @Override
    public void Succ(Object o) {
        goodsTypeInterface.getGoodsTypeSucc((GoodsTypeBack)o);
    }

    @Override
    public void Fail(String msg) {
        goodsTypeInterface.getGoodsTypeFail(msg);
    }
}
