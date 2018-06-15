package wizrole.hosmerchants.admin.preserent.delgoodscate;

import wizrole.hosmerchants.admin.model.delgoodscate.DelGoodsCateBack;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 */

public interface DelGoodsCateInterface {
    void delGoodsCateSucc(DelGoodsCateBack delGoodsCateBack);
    void delGoodsCateFail(String msg);
}
