package wizrole.hosmerchants.my.model.checkemail;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class CheckEMailBack {
    public String ResultCode;
    public String ResultContent;//
    public String CheckCode ;// - 发送的验证码
    public String HostNo;

    public String getHostNo() {
        return HostNo;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public String getCheckCode() {
        return CheckCode;
    }
}
