package wizrole.hosmerchants.my.preserent.login;

import wizrole.hosmerchants.my.model.login.LoginBack;
import wizrole.hosmerchants.my.model.login.LoginBackInterface;
import wizrole.hosmerchants.my.model.login.LoginHttp;

/**
 * Created by liushengping on 2017/11/29/029.
 * 何人执笔？
 */

public class LoginPreserent implements LoginBackInterface {

    public LoginInterface LoginInterface;
    public LoginHttp loginHttp;
    public LoginPreserent(LoginInterface LoginInterface){
        this.LoginInterface = LoginInterface;
        loginHttp=new LoginHttp(this);
    }

    public void Http(String name ,String password){
            loginHttp.Login(name,password);
    }

    @Override
    public void Succ(Object o) {
        LoginInterface.getDataSucc((LoginBack) o);
    }

    @Override
    public void Fail(String msg) {
        LoginInterface.getDataFail(msg);
    }
}
