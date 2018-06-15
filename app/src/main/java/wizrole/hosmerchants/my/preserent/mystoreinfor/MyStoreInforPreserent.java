package wizrole.hosmerchants.my.preserent.mystoreinfor;

import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBackInterface;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforHttp;

/**
 * Created by liushengping on 2017/12/15/015.
 * 何人执笔？
 */

public class MyStoreInforPreserent implements MyStoreInforBackInterface{
    public MyStoreInforInterface myStoreInforInterface;
    public MyStoreInforHttp myStoreInforHttp;
    public MyStoreInforPreserent(MyStoreInforInterface myStoreInforInterface){
        this.myStoreInforInterface=myStoreInforInterface;
        myStoreInforHttp=new MyStoreInforHttp(this);
    }

    public void getMyStoreInfor(String id){
        myStoreInforHttp.getMyStoreInfor(id);
    }
    @Override
    public void Succ(Object o) {
        myStoreInforInterface.getStoreInforSucc((MyStoreInforBack) o);
    }

    @Override
    public void Fail(String msg) {
        myStoreInforInterface.getStoreInforFail(msg);
    }
}
