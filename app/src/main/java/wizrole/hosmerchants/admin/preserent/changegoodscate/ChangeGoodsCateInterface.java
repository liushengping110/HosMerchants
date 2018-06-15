package wizrole.hosmerchants.admin.preserent.changegoodscate;

import wizrole.hosmerchants.admin.model.changegoodscate.ChangeGoodsCateBack;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 */

public interface ChangeGoodsCateInterface {

    void ChangeGoodsCateSucc(ChangeGoodsCateBack changeGoodsCateBack);
    void ChangeGoodsCateFail(String msg);
}
