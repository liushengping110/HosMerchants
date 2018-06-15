package wizrole.hosmerchants.my.preserent.checkemail;

import wizrole.hosmerchants.my.model.checkemail.CheckEMailBack;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public interface CheckEMailInterface {
    void getEmailSucc(CheckEMailBack checkEMailBack);
    void getEmailFail(String msg);
}
