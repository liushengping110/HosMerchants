package wizrole.hosmerchants.merchants.preserent.getsecondtypename;

import wizrole.hosmerchants.merchants.model.getsecondtypename.GetSecondTypeNameBack;

/**
 * Created by liushengping on 2018/1/28.
 * 何人执笔？
 */

public interface GetSecondTypeNameInterface {

    void getSecondTypeNameSucc(GetSecondTypeNameBack getSecondTypeNameBack);
    void getSecondTypeNameFail(String msg);
}
