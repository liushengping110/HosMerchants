package wizrole.hosmerchants.my.preserent.myinfor;

import wizrole.hosmerchants.my.model.myinfor.MyInforBack;
import wizrole.hosmerchants.my.model.myinfor.MyInforBackInterface;
import wizrole.hosmerchants.my.model.myinfor.MyInforHttp;

/**
 * Created by liushengping on 2017/12/13/013.
 * 何人执笔？
 */

public class MyInforPreserent implements MyInforBackInterface {
    public MyInforInterface anInterface;
    public MyInforHttp myInforHttp;
    public MyInforPreserent(MyInforInterface anInterface1){
        this.anInterface=anInterface1;
        myInforHttp=new MyInforHttp(this);
    }

    public void HttpGetMyInfor(String id){
        myInforHttp.getMyInfor(id);
    }

    @Override
    public void Succ(Object o) {
        anInterface.getDataSucc((MyInforBack) o);
    }

    @Override
    public void Fail(String msg) {
        anInterface.getDataFail(msg);
    }
}
