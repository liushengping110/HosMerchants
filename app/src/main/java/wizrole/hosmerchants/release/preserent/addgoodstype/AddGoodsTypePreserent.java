package wizrole.hosmerchants.release.preserent.addgoodstype;

import wizrole.hosmerchants.release.model.addgoodstype.AddGoodsTypeBack;
import wizrole.hosmerchants.release.model.addgoodstype.AddGoodsTypeBackInterface;
import wizrole.hosmerchants.release.model.addgoodstype.AddGoodsTypeHttp;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class AddGoodsTypePreserent  implements AddGoodsTypeBackInterface{

    public AddGoodsTypeInterface addGoodsTypeInterface;
    public AddGoodsTypeHttp addGoodsTypeHttp;

    public AddGoodsTypePreserent(AddGoodsTypeInterface addGoodsTypeInterface){
        this.addGoodsTypeInterface=addGoodsTypeInterface;
        addGoodsTypeHttp=new AddGoodsTypeHttp(this);
    }

    public void AddGoodsType(String id,String cateName){
        addGoodsTypeHttp.addGoodsTypeHttp(id,cateName);
    }

    @Override
    public void Succ(Object o) {
        addGoodsTypeInterface.AddGoodsTypeSucc((AddGoodsTypeBack) o);
    }

    @Override
    public void Fail(String msg) {
        addGoodsTypeInterface.AddGoodsTypeFail(msg);
    }
}
