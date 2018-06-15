package wizrole.hosmerchants.my.preserent.changepassword;

import wizrole.hosmerchants.my.model.changepassword.ChangePassBack;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 */

public interface ChangePassInterface {

    void getNewPassSucc(ChangePassBack changePassBack);
    void getNewPassFail(String msg);
}
