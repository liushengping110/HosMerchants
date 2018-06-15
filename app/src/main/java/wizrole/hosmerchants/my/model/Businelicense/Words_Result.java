package wizrole.hosmerchants.my.model.Businelicense;

import com.google.gson.annotations.SerializedName;


/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class Words_Result {

    @SerializedName("单位名称")
    public EntityName entityName;

    @SerializedName("法人")
    public LegalPersion legalPersion;

    @SerializedName("地址")
    public Address address;

    @SerializedName("有效期")
    public ValidityPeriod validityPeriod;

    @SerializedName("证件编号")
    public IDNumber idNumber;

    @SerializedName("社会信用代码")
    public CreditCode creditCode;


    public EntityName getEntityName() {
        return entityName;
    }

    public LegalPersion getLegalPersion() {
        return legalPersion;
    }

    public Address getAddress() {
        return address;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public IDNumber getIdNumber() {
        return idNumber;
    }

    public CreditCode getCreditCode() {
        return creditCode;
    }
}
