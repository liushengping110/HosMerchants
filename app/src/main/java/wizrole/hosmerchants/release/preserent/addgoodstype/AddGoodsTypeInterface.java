package wizrole.hosmerchants.release.preserent.addgoodstype;

import wizrole.hosmerchants.release.model.addgoodstype.AddGoodsTypeBack;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public interface AddGoodsTypeInterface {

    void AddGoodsTypeSucc(AddGoodsTypeBack addGoodsTypeBack);
    void AddGoodsTypeFail(String msg);
}
