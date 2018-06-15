package wizrole.hosmerchants.my.preserent.changestoreinfor;

import wizrole.hosmerchants.my.model.changestoreinfor.ChangeStoreInforBack;

/**
 * Created by liushengping on 2017/12/20/020.
 * 何人执笔？
 */

public interface ChangeStoreInforInterface {
    void getChangeStoreSucc(ChangeStoreInforBack changeStoreInforBack);
    void getChangeStoreFail(String msg);
}
