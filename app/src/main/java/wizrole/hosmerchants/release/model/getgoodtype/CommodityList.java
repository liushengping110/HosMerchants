package wizrole.hosmerchants.release.model.getgoodtype;

import java.io.Serializable;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 */

public class CommodityList implements Serializable {
    /**
     * commodityNo 	- 商品主键ID
     CommodityName 	- 商品名称
     CommodityPic 	- 商品图片
     CommodityContent- 商品内容
     CommodityAmt	- 商品价格
     */
    public String commodityNo;
    public String CommodityName;
    public String CommodityPic;
    public String CommodityContent;
    public String CommodityAmt;

    public String getCommodityNo() {
        return commodityNo;
    }

    public String getCommodityName() {
        return CommodityName;
    }

    public String getCommodityPic() {
        return CommodityPic;
    }

    public String getCommodityContent() {
        return CommodityContent;
    }

    public String getCommodityAmt() {
        return CommodityAmt;
    }
}
