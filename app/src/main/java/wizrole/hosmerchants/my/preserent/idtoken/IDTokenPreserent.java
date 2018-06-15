package wizrole.hosmerchants.my.preserent.idtoken;

import wizrole.hosmerchants.my.model.idtoken.IDTokenBackInterface;
import wizrole.hosmerchants.my.model.idtoken.IDTokenHttp;
import wizrole.hosmerchants.my.model.idtoken.TokenBack;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public class IDTokenPreserent implements IDTokenBackInterface{

    public IDTokenInterface idTokenInterface;
    public IDTokenHttp idTokenHttp;
    public IDTokenPreserent(IDTokenInterface idTokenInterface){
        this.idTokenInterface=idTokenInterface;
        idTokenHttp=new IDTokenHttp(this);
    }
    public void getIDToken(){
        idTokenHttp.getIdToken();
    }

    @Override
    public void Succ(Object o) {
        idTokenInterface.getTokenSucc((TokenBack) o);
    }

    @Override
    public void Fail(String msg) {
        idTokenInterface.getTokenFail(msg);
    }
}
