package wizrole.hosmerchants.merchants.model.getsecondtypename;

import java.io.Serializable;

/**
 * Created by liushengping on 2018/1/28.
 * 何人执笔？
 */

public class TwoTypeList implements Serializable{

    public String TwoTypeId;
    public String TwoTypeName;
    public boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getTwoTypeId() {
        return TwoTypeId;
    }

    public String getTwoTypeName() {
        return TwoTypeName;
    }
}
