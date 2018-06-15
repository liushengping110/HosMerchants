package wizrole.hosmerchants.my.preserent.addstore;

import wizrole.hosmerchants.my.model.addstore.AddStoreBack;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 */

public interface AddStoreInterface {
    void getAddStoreSucc(AddStoreBack addStoreBack);
    void getAddStoreFail(String msg);
}
