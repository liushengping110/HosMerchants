package wizrole.hosmerchants.my.preserent.upimage;

import wizrole.hosmerchants.my.model.upimage.UpImageBack;
import wizrole.hosmerchants.my.model.upimage.UpImageBackInterface;
import wizrole.hosmerchants.my.model.upimage.UpImageHttp;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 */

public class UpImagePreserent implements UpImageBackInterface{

    public UpImageInterface upImageInterface;
    public UpImageHttp upImageHttp;
    public UpImagePreserent(UpImageInterface upImageInterface){
        this.upImageInterface=upImageInterface;
        upImageHttp=new UpImageHttp(this);
    }
    /**
     * 图片上传
     * @param filepath
     * @param PicType//图片类型--头像  店铺logo 商品详情
     */
    public void HttpUpImage(String filepath,String PicType){
        upImageHttp.UpImageHttp(filepath,PicType);
    }

    @Override
    public void Succ(Object o) {
        upImageInterface.getImageSucc((UpImageBack) o);
    }

    @Override
    public void Fail(String msg) {
        upImageInterface.getImageFail(msg);
    }
}
