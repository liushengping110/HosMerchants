package wizrole.hosmerchants.my.preserent.changestoreinfor;

import wizrole.hosmerchants.my.model.changestoreinfor.ChangeStoreInforBack;
import wizrole.hosmerchants.my.model.changestoreinfor.ChangeStoreInforBackInterface;
import wizrole.hosmerchants.my.model.changestoreinfor.ChangeStoreInforHttp;

/**
 * Created by liushengping on 2017/12/20/020.
 * 何人执笔？
 */

public class ChangeStoreInforPreserent implements ChangeStoreInforBackInterface{

    public ChangeStoreInforInterface changeStoreInforInterface;

    public ChangeStoreInforHttp changeStoreInforHttp;

    public ChangeStoreInforPreserent(ChangeStoreInforInterface changeStoreInforInterface){
        this.changeStoreInforInterface=changeStoreInforInterface;
        changeStoreInforHttp=new ChangeStoreInforHttp(this);
    }

    public void changeStoreInfor(String StoreNo,String StoreName,String StorePlace,String StorePhone ,String StoreType
            ,String StoreLogoPic,String StorePayPic ,String WeChatPic,String HostNo ,String CompanyName
            ,String LegalPerson,String CompanyAddress,String LicenseValidity,String LicenseNo
            ,String CreditCode){
        changeStoreInforHttp.changeStoreInforHttp(StoreNo,StoreName,StorePlace,StorePhone,StoreType,StoreLogoPic,StorePayPic
                ,WeChatPic,HostNo,CompanyName,LegalPerson,CompanyAddress,LicenseValidity,LicenseNo,CreditCode);
    }

    @Override
    public void Succ(Object o) {
        changeStoreInforInterface.getChangeStoreSucc((ChangeStoreInforBack) o);
    }

    @Override
    public void Fail(String msg) {
        changeStoreInforInterface.getChangeStoreFail(msg);
    }
}
