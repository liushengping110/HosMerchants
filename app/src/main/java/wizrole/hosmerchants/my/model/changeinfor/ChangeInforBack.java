package wizrole.hosmerchants.my.model.changeinfor;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 * 修改个人信息
 */

public class ChangeInforBack {

    public String ResultCode;
    public String ResultContent;
    public String InfoSign;//信息标志(1-个人信息未完成 店铺完成  , 2-个人完成,店铺未完成  , 3- 个人未完成,店铺未完成  , 0-两个都完成)

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public String getInfoSign() {
        return InfoSign;
    }
}
