package wizrole.hosmerchants.merchants.preserent.getallstore;

import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public interface GetAllStoreInterface {
    void getAllStoreSucc(MyStoreInforBack getAllStoreBack);
    void getAllStoreFail(String msg);
}
