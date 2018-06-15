package wizrole.hosmerchants.my.preserent.login;

import wizrole.hosmerchants.my.model.login.LoginBack;

/**
 * Created by liushengping on 2017/11/29/029.
 * 何人执笔？
 */

public interface LoginInterface {

    void getDataSucc(LoginBack loginBack);
    void getDataFail(String msg);
}
