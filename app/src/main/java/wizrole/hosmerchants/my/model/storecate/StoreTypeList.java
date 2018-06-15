package wizrole.hosmerchants.my.model.storecate;

import java.io.Serializable;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class StoreTypeList implements Serializable{

    public String StoreTypeId;		//- 类别ID
    public String StoreTypeName;	//- 类别名称

    public String getStoreTypeId() {
        return StoreTypeId;
    }

    public String getStoreTypeName() {
        return StoreTypeName;
    }
}
