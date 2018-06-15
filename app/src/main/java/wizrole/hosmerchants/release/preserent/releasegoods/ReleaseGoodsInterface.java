package wizrole.hosmerchants.release.preserent.releasegoods;

import wizrole.hosmerchants.release.model.releasegoods.ReleaseGoodsBack;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public interface ReleaseGoodsInterface {

    void ReleaseGoodsSucc(ReleaseGoodsBack releaseGoodsBack);
    void ReleaseGoodsFail(String msg);
}
