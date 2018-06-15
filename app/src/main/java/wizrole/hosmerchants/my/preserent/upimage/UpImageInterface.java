package wizrole.hosmerchants.my.preserent.upimage;

import wizrole.hosmerchants.my.model.upimage.UpImageBack;

/**
 * Created by liushengping on 2017/12/12/012.
 * 何人执笔？
 */

public interface UpImageInterface {

    void getImageSucc(UpImageBack upImageBack);
    void getImageFail(String msg);
}
