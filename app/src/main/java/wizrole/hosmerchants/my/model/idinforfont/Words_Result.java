package wizrole.hosmerchants.my.model.idinforfont;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public class Words_Result {

    @SerializedName("住址")
    public Address address;

    @SerializedName("出生")
    public BrithDay brithDay;

    @SerializedName("姓名")
    public Name name;

    @SerializedName("公民身份号码")
    public IdNumber idNumber;

    @SerializedName("性别")
    public Sex sex;

    @SerializedName("民族")
    public Ethnic ethnic;


    public Address getAddress() {
        return address;
    }

    public BrithDay getBrithDay() {
        return brithDay;
    }

    public Name getName() {
        return name;
    }

    public IdNumber getIdNumber() {
        return idNumber;
    }

    public Sex getSex() {
        return sex;
    }

    public Ethnic getEthnic() {
        return ethnic;
    }
}
