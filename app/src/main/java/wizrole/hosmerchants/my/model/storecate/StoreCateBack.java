package wizrole.hosmerchants.my.model.storecate;

import java.util.List;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class StoreCateBack {

    public String  ResultCode;
    public String ResultContent;
    public List<StoreTypeList> StoreTypeList;//- 类别信息列表

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<StoreTypeList> getStoreTypeList() {
        return StoreTypeList;
    }
}
