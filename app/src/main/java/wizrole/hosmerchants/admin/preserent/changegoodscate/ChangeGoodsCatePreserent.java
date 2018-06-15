package wizrole.hosmerchants.admin.preserent.changegoodscate;

import wizrole.hosmerchants.admin.model.changegoodscate.ChangeGoodsCateBack;
import wizrole.hosmerchants.admin.model.changegoodscate.ChangeGoodsCateBackInterface;
import wizrole.hosmerchants.admin.model.changegoodscate.ChangeGoodsCateHttp;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 */

public class ChangeGoodsCatePreserent implements ChangeGoodsCateBackInterface{

    public ChangeGoodsCateInterface changeGoodsCateInterface;
    public ChangeGoodsCateHttp changeGoodsCateHttp;
    public ChangeGoodsCatePreserent(ChangeGoodsCateInterface changeGoodsCateInterface){
        this.changeGoodsCateInterface=changeGoodsCateInterface;
        changeGoodsCateHttp=new ChangeGoodsCateHttp(this);
    }

    public void changeGoodsCateName(String id,String name){
        changeGoodsCateHttp.changeGoodsCateName(id,name);
    }
    @Override
    public void Succ(Object o) {
        changeGoodsCateInterface.ChangeGoodsCateSucc((ChangeGoodsCateBack)o);
    }

    @Override
    public void Fail(String msg) {
        changeGoodsCateInterface.ChangeGoodsCateFail(msg);
    }
}
