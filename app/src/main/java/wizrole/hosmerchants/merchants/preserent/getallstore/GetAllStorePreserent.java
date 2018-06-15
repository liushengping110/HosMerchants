package wizrole.hosmerchants.merchants.preserent.getallstore;

import wizrole.hosmerchants.merchants.model.getallstore.GetAllStoreBackInterface;
import wizrole.hosmerchants.merchants.model.getallstore.GetAllStoreHttp;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class GetAllStorePreserent implements GetAllStoreBackInterface{

    public GetAllStoreInterface getAllStoreInterface;
    public GetAllStoreHttp getAllStoreHttp;

    public GetAllStorePreserent(GetAllStoreInterface getAllStoreInterface){
        this.getAllStoreInterface=getAllStoreInterface;
        getAllStoreHttp=new GetAllStoreHttp(this);
    }

    public void getAllStore(String StoreType,String OrderType,int page ){
        getAllStoreHttp.getAllStore(StoreType,OrderType, page );
    }

    @Override
    public void Succ(Object o) {
        getAllStoreInterface.getAllStoreSucc((MyStoreInforBack)o);
    }

    @Override
    public void Fail(String msg) {
        getAllStoreInterface.getAllStoreFail(msg);
    }
}
