package wizrole.hosmerchants.my.preserent.idtoken;

import wizrole.hosmerchants.my.model.idtoken.TokenBack;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public interface IDTokenInterface {

    void getTokenSucc(TokenBack tokenBack);
    void getTokenFail(String msg);
}
