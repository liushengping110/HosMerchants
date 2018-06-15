package wizrole.hosmerchants.my.preserent.storecate;

import wizrole.hosmerchants.my.model.storecate.StoreCateBack;
import wizrole.hosmerchants.my.model.storecate.StoreCateBackInterface;
import wizrole.hosmerchants.my.model.storecate.StoreCateHttp;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class StoreCatePreserent implements StoreCateBackInterface{
    public StoreCateInterface storeCateInterface;
    public StoreCateHttp storeCateHttp;
    public StoreCatePreserent(StoreCateInterface storeCateInterface){
        this.storeCateInterface=storeCateInterface;
        storeCateHttp=new StoreCateHttp(this);
    }
    public void getStoreCate(){
        storeCateHttp.getStoreCate();
    }

    @Override
    public void Succ(Object o) {
        storeCateInterface.getStoreCareSucc((StoreCateBack)o);
    }

    @Override
    public void Fail(String msg) {
        storeCateInterface.getStoreCareFail(msg);
    }
}
