package wizrole.hosmerchants.my.preserent.storecate;

import wizrole.hosmerchants.my.model.storecate.StoreCateBack;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public interface StoreCateInterface {
    void getStoreCareSucc(StoreCateBack storeCateBack);
    void getStoreCareFail(String msg);
}
