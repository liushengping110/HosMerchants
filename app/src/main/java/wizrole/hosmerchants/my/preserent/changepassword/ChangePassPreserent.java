package wizrole.hosmerchants.my.preserent.changepassword;

import wizrole.hosmerchants.my.model.changepassword.ChangePassBack;
import wizrole.hosmerchants.my.model.changepassword.ChangePassBackInterface;
import wizrole.hosmerchants.my.model.changepassword.ChangePassHttp;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 */

public class ChangePassPreserent implements ChangePassBackInterface{

    public ChangePassInterface changePassInterface;
    public ChangePassHttp changePassHttp;

    public ChangePassPreserent(ChangePassInterface changePassInterface){
        this.changePassInterface=changePassInterface;
        changePassHttp=new ChangePassHttp(this);
    }

    public void change(String id,String pass){
        changePassHttp.ChangePassHttp(id,pass);
    }

    @Override
    public void Succ(Object o) {
        changePassInterface.getNewPassSucc((ChangePassBack)o);
    }

    @Override
    public void Fail(String msg) {
        changePassInterface.getNewPassFail(msg);
    }
}
