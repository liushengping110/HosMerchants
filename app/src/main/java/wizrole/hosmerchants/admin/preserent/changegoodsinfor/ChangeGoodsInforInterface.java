package wizrole.hosmerchants.admin.preserent.changegoodsinfor;

import wizrole.hosmerchants.admin.model.changegoodsinfor.ChangeGoodsInforBack;

/**
 * Created by liushengping on 2018/1/8/008.
 * 何人执笔？
 */

public interface ChangeGoodsInforInterface {

    void getChangeGoodsInforSucc(ChangeGoodsInforBack changeGoodsInforBack);
    void getChangeGoodsInforFail(String msg);
}
