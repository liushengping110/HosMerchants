package wizrole.hosmerchants.my.preserent.regist;

import wizrole.hosmerchants.my.model.regist.RegistBack;

/**
 * Created by liushengping on 2017/12/11/011.
 * 何人执笔？
 * 注册
 */

public interface RegistInerface {

    void getDataSucc(RegistBack registBack);
    void getDataFail(String msg);

}
