package wizrole.hosmerchants.my.preserent.myinfor;

import wizrole.hosmerchants.my.model.myinfor.MyInforBack;

/**
 * Created by liushengping on 2017/12/13/013.
 * 何人执笔？
 */

public interface MyInforInterface {

    void getDataSucc(MyInforBack myInforBack);
    void getDataFail(String msg);
}
