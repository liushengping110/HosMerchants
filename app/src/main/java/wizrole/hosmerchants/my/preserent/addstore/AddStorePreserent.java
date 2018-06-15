package wizrole.hosmerchants.my.preserent.addstore;

import wizrole.hosmerchants.my.model.addstore.AddStoreBack;
import wizrole.hosmerchants.my.model.addstore.AddStoreBackInterface;
import wizrole.hosmerchants.my.model.addstore.AddStoreHttp;

/**
 * Created by liushengping on 2017/12/19/019.
 * 何人执笔？
 */

public class AddStorePreserent implements AddStoreBackInterface{

    public AddStoreInterface addStoreInterface;
    public AddStoreHttp addStoreHttp;

    public AddStorePreserent(AddStoreInterface addStoreInterface){
        this.addStoreInterface=addStoreInterface;
        addStoreHttp=new AddStoreHttp(this);
    }

    public void AddStoreHttp(String StoreName,String StorePlace,String StorePhone ,String StoreType
            ,String StoreLogoPic,String StorePayPic ,String WeChatPic,String HostNo ,String CompanyName
            ,String LegalPerson,String CompanyAddress,String LicenseValidity,String LicenseNo
            ,String CreditCode){
        addStoreHttp.AddStoreHttp(StoreName,StorePlace,StorePhone,StoreType,StoreLogoPic,StorePayPic
        ,WeChatPic,HostNo,CompanyName,LegalPerson,CompanyAddress,LicenseValidity,LicenseNo,CreditCode);
    }

    @Override
    public void Succ(Object o) {
        addStoreInterface.getAddStoreSucc((AddStoreBack)o);
    }

    @Override
    public void Fail(String msg) {
        addStoreInterface.getAddStoreFail(msg);
    }
}
