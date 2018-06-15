package wizrole.hosmerchants.my.preserent.idinfor;

import wizrole.hosmerchants.my.model.idinforfont.IDInforBackInterface;
import wizrole.hosmerchants.my.model.idinforfont.IDInforHttp;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public class IDInforPreserent implements IDInforBackInterface {

    public IDInforFrontInterface idInforFrontInterface;
    public IDInforHttp idInforHttp;
    public IDInforPreserent(IDInforFrontInterface idInforFrontInterface){
        this.idInforFrontInterface=idInforFrontInterface;
        idInforHttp =new IDInforHttp(this);
    }

    public void HttpGetIDiInfor(String token,String side,String filepath){
        idInforHttp.getMyIDInfor(token,side,filepath);
    }


    @Override
    public void Succ(Object o) {
        idInforFrontInterface.getIDFrontSucc(o);
    }

    @Override
    public void Fail(String msg) {
        idInforFrontInterface.getIDFrontFail(msg);
    }
}
