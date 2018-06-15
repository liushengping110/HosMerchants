package wizrole.hosmerchants.my.preserent.changeinfor;

import wizrole.hosmerchants.my.model.changeinfor.ChangeInforBack;
import wizrole.hosmerchants.my.model.changeinfor.ChangeInforBackInterface;
import wizrole.hosmerchants.my.model.changeinfor.ChangeInforHttp;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 */

public class ChangeInforPreserent implements ChangeInforBackInterface {

    public ChangeInforInterface changeInforInterface;
    public ChangeInforHttp changeInforHttp;
    public ChangeInforPreserent(ChangeInforInterface changeInforInterface){
        this.changeInforInterface=changeInforInterface;
        changeInforHttp=new ChangeInforHttp(this);
    }


    /**
     * 修改头像-先调用Y007，再调用Y005
     * 修改个人信息--统一调用Y005
     * @param HostName
     * @param HostPhone
     * @param HostEmail
     * @param HostAvatar
     * @param HostNo
     * @param HostIdCard
     * @param HostAddress
     * @param BornDate
     * @param Sex
     * @param Ethnic
     * @param IssuingBody
     * @param Validity
     */
    public void HttpChangeinfor(String HostName,String HostPhone,String HostEmail,String HostAvatar,String HostNo
    ,String HostIdCard,String HostAddress,String BornDate,String Sex,String Ethnic,String IssuingBody,String Validity,String IdCardName){
        changeInforHttp.ChangeInfor(HostName,HostPhone,HostEmail,HostAvatar,HostNo,HostIdCard,HostAddress,BornDate
        ,Sex,Ethnic,IssuingBody,Validity,IdCardName);
    }

    @Override
    public void Succ(Object o) {
        changeInforInterface.getInforSucc((ChangeInforBack) o);
    }

    @Override
    public void Fail(String msg) {
        changeInforInterface.getInforFail(msg);
    }
}
