package wizrole.hosmerchants.my.preserent.businelicense;

import wizrole.hosmerchants.my.model.Businelicense.BusineLicenBack;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public interface BusineLicenInterface {
    void getBuinLicenSucc(BusineLicenBack busineLicenBack);
    void getBuinLicenFail(String msg);
}
