package wizrole.hosmerchants.admin.preserent.delgoods;

import wizrole.hosmerchants.admin.model.delgoods.DelGoodsBack;

/**
 * Created by liushengping on 2018/1/8/008.
 * 何人执笔？
 */

public interface DelGoodsInterface {

    void getDelGoodsSucc(DelGoodsBack delGoodsBack);
    void getDelGoodsFail(String msg);
}
