package wizrole.hosmerchants.merchants.preserent.viewpager;

import wizrole.hosmerchants.merchants.model.viewpager.ViewPagerBack;
import wizrole.hosmerchants.merchants.model.viewpager.ViewPagerBackInterface;
import wizrole.hosmerchants.merchants.model.viewpager.ViewPagerHttp;

/**
 * Created by liushengping on 2017/12/22/022.
 * 何人执笔？
 */

public class ViewPagerPreserent implements ViewPagerBackInterface{

    public ViewPagerInterface viewPagerInterface;
    public ViewPagerHttp viewPagerHttp;

    public ViewPagerPreserent(ViewPagerInterface viewPagerInterface){
        this.viewPagerInterface=viewPagerInterface;
        viewPagerHttp=new ViewPagerHttp(this);
    }

    public void getImage(){
        viewPagerHttp.getImage();
    }
    @Override
    public void Succ(Object o) {
        viewPagerInterface.getImgSucc((ViewPagerBack)o);
    }

    @Override
    public void Fail(String msg) {
        viewPagerInterface.getImgFail(msg);
    }
}
