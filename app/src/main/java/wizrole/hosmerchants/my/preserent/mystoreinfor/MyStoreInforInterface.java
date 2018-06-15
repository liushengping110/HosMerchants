package wizrole.hosmerchants.my.preserent.mystoreinfor;

import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;

/**
 * Created by liushengping on 2017/12/15/015.
 * 何人执笔？
 */

public interface MyStoreInforInterface {

    void getStoreInforSucc(MyStoreInforBack myStoreInforBack);
    void getStoreInforFail(String msg);
}
