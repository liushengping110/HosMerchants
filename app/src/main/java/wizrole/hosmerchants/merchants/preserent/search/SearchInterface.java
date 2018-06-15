package wizrole.hosmerchants.merchants.preserent.search;

import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;

/**
 * Created by liushengping on 2018/1/4/004.
 * 何人执笔？
 */

public interface SearchInterface {
    void getSearchInforSucc(MyStoreInforBack myStoreInforBack);
    void getSearchInforFail(String msg);
}
