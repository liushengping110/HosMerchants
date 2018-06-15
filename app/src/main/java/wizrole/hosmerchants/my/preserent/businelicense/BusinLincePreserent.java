package wizrole.hosmerchants.my.preserent.businelicense;

import wizrole.hosmerchants.my.model.Businelicense.BusinLiceHttp;
import wizrole.hosmerchants.my.model.Businelicense.BusineLicenBack;
import wizrole.hosmerchants.my.model.Businelicense.BusineLicenBackInterface;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class BusinLincePreserent implements BusineLicenBackInterface{

    public BusineLicenInterface busineLicenInterface;
    public BusinLiceHttp businLiceHttp;

    public BusinLincePreserent(BusineLicenInterface busineLicenInterface){
        this.busineLicenInterface=busineLicenInterface;
        businLiceHttp=new BusinLiceHttp(this);
    }

    public void getBusinLicenInfor(String token,String filepath){
        businLiceHttp.BusinLicHttp(token,filepath);
    }

    @Override
    public void Succ(Object o) {
        busineLicenInterface.getBuinLicenSucc((BusineLicenBack) o);
    }

    @Override
    public void Fail(String msg) {
        busineLicenInterface.getBuinLicenFail(msg);
    }
}
