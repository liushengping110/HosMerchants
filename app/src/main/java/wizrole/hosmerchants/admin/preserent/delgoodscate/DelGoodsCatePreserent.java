package wizrole.hosmerchants.admin.preserent.delgoodscate;

import wizrole.hosmerchants.admin.model.delgoodscate.DelGoodsCateBack;
import wizrole.hosmerchants.admin.model.delgoodscate.DelGoodsCateBackInterface;
import wizrole.hosmerchants.admin.model.delgoodscate.DelGoodsCateHttp;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 */

public class DelGoodsCatePreserent implements DelGoodsCateBackInterface{

    public DelGoodsCateInterface delGoodsCateInterface;
    public DelGoodsCateHttp delGoodsCateHttp;

    public DelGoodsCatePreserent(DelGoodsCateInterface delGoodsCateInterface){
        this.delGoodsCateInterface=delGoodsCateInterface;
        delGoodsCateHttp=new DelGoodsCateHttp(this);
    }
    public void delGoodsCateName(String StoreNo ,String TypeName){
        delGoodsCateHttp.delGoodsCateName(StoreNo,TypeName);
    }

    @Override
    public void Succ(Object o) {
        delGoodsCateInterface.delGoodsCateSucc((DelGoodsCateBack)o);
    }

    @Override
    public void Fail(String msg) {
        delGoodsCateInterface.delGoodsCateFail(msg);
    }
}
