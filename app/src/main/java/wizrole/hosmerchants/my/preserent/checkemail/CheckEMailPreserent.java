package wizrole.hosmerchants.my.preserent.checkemail;

import wizrole.hosmerchants.my.model.checkemail.CheckEMailBack;
import wizrole.hosmerchants.my.model.checkemail.CheckEMailBackInterface;
import wizrole.hosmerchants.my.model.checkemail.CheckEMailHttp;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class CheckEMailPreserent implements CheckEMailBackInterface{

    public CheckEMailInterface checkEMailInterface;
    public CheckEMailHttp checkEMailHttp;

    public CheckEMailPreserent(CheckEMailInterface checkEMailInterface){
        this.checkEMailInterface=checkEMailInterface;
        checkEMailHttp=new CheckEMailHttp(this);
    }

    public void getCheckEMail(String email){
        checkEMailHttp.checkEMailHttp(email);
    }

    @Override
    public void Succ(Object o) {
        checkEMailInterface.getEmailSucc((CheckEMailBack)o);
    }

    @Override
    public void Fail(String msg) {
        checkEMailInterface.getEmailFail(msg);
    }
}
