package wizrole.hosmerchants.my.model.mystoreinfor;

import java.util.List;

/**
 * Created by liushengping on 2017/12/15/015.
 * 何人执笔？
 */

public class MyStoreInforBack {


    public String ResultCode;
    public String ResultContent;
    public List<StoreList> StoreList;
    public int TotalNum;

    public int getTotalNum() {
        return TotalNum;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<wizrole.hosmerchants.my.model.mystoreinfor.StoreList> getStoreList() {
        return StoreList;
    }
}
