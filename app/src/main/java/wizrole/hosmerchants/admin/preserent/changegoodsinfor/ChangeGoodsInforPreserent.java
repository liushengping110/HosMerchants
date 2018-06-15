package wizrole.hosmerchants.admin.preserent.changegoodsinfor;

import wizrole.hosmerchants.admin.model.changegoodsinfor.ChangeGoodsInforBack;
import wizrole.hosmerchants.admin.model.changegoodsinfor.ChangeGoodsInforBackInterface;
import wizrole.hosmerchants.admin.model.changegoodsinfor.ChangeGoodsInforHttp;

/**
 * Created by liushengping on 2018/1/8/008.
 * 何人执笔？
 */

public class ChangeGoodsInforPreserent implements ChangeGoodsInforBackInterface{

    public ChangeGoodsInforInterface changeGoodsInforInterface;
    public ChangeGoodsInforHttp changeGoodsInforHttp;
    public ChangeGoodsInforPreserent(ChangeGoodsInforInterface changeGoodsInforInterface){
        this.changeGoodsInforInterface=changeGoodsInforInterface;
        changeGoodsInforHttp=new ChangeGoodsInforHttp(this);
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
        changeGoodsInforHttp.ChangeGoodsInforHttp( CommodityNo ,
                 CommodityName ,
                 CommodityPic,
                 CommodityContent,
                 CommodityAmt,
                 BelongTypeNo);
    }
    @Override
    public void Succ(Object o) {
        changeGoodsInforInterface.getChangeGoodsInforSucc((ChangeGoodsInforBack)o);
    }

    @Override
    public void Fail(String msg) {
        changeGoodsInforInterface.getChangeGoodsInforFail(msg);
    }
}
