package wizrole.hosmerchants.my.model.myinfor;

/**
 * Created by liushengping on 2017/12/13/013.
 * 何人执笔？
 */

public class MyInforBack {
    /**
     * HostIdCard	- 身份证号
     HostAddress	- 地址
     BornDate	- 出生日期
     Sex			- 性别
     Ethnic		- 民族
     IssuingBody	- 签发机关
     Validity	- 有效期
     */
    public String  ResultCode;
    public String ResultContent;
    public String  HostName;//用户名称
    public String HostPhone;//- 用户手机
    public String HostEmail;//- 用户email
    public String HostAvatar;	//- 用户头像
    public String IdCardName;	//- 身份证姓名
    public String HostIdCard;	//- 身份证号
    public String HostAddress;	//- 住址
    public String BornDate;	//- 出生
    public String Sex;	//- 性别
    public String Ethnic;	//- 民族
    public String IssuingBody;	//- 签发机关
    public String Validity;	//- 有效期


    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public String getHostName() {
        return HostName;
    }

    public String getHostPhone() {
        return HostPhone;
    }

    public String getHostEmail() {
        return HostEmail;
    }

    public String getHostAvatar() {
        return HostAvatar;
    }

    public String getIdCardName() {
        return IdCardName;
    }

    public String getHostIdCard() {
        return HostIdCard;
    }

    public String getHostAddress() {
        return HostAddress;
    }

    public String getBornDate() {
        return BornDate;
    }

    public String getSex() {
        return Sex;
    }

    public String getEthnic() {
        return Ethnic;
    }

    public String getIssuingBody() {
        return IssuingBody;
    }

    public String getValidity() {
        return Validity;
    }
}
