package wizrole.hosmerchants.merchants.preserent.getsecondstore;

import wizrole.hosmerchants.my.model.myinfor.MyInforBack;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;

/**
 * Created by liushengping on 2018/1/31.
 * 何人执笔？
 */

public interface GetSecondStoreInterface {

    void GetSecondStoreSucc(MyStoreInforBack myStoreInforBack);
    void GetSecondStoreFail(String msg);
}
