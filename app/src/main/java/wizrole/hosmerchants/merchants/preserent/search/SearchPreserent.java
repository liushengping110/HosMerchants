package wizrole.hosmerchants.merchants.preserent.search;

import wizrole.hosmerchants.merchants.model.search.SearchBackInterface;
import wizrole.hosmerchants.merchants.model.search.SearchHttp;
import wizrole.hosmerchants.my.model.mystoreinfor.MyStoreInforBack;

/**
 * Created by liushengping on 2018/1/4/004.
 * 何人执笔？
 */

public class SearchPreserent implements SearchBackInterface{

    public SearchInterface searchInterface;
    public SearchHttp searchHttp;
    public SearchPreserent(SearchInterface searchInterface){
        this.searchInterface=searchInterface;
        searchHttp=new SearchHttp(this);
    }

    public void searchGetInfor(String msg){
        searchHttp.SearchGetInfor(msg);
    }
    @Override
    public void Succ(Object o) {
        searchInterface.getSearchInforSucc((MyStoreInforBack) o);
    }

    @Override
    public void Fail(String msg) {
        searchInterface.getSearchInforFail(msg);
    }
}
