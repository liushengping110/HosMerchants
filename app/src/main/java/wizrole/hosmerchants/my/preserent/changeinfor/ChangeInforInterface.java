package wizrole.hosmerchants.my.preserent.changeinfor;

import wizrole.hosmerchants.my.model.changeinfor.ChangeInforBack;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 */

public interface ChangeInforInterface {
    void getInforSucc(ChangeInforBack changeInforBack);
    void getInforFail(String msg);
}
