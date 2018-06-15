package wizrole.hosmerchants.my.model.idinforback;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public class Words_Result {

    @SerializedName("签发日期")
    public IssueDate startDate;

    @SerializedName("签发机关")
    public IssueOrgan issueOrgan;

    @SerializedName("失效日期")
    public FailureDate failureDate;


    public IssueDate getStartDate() {
        return startDate;
    }

    public IssueOrgan getIssueOrgan() {
        return issueOrgan;
    }

    public FailureDate getFailureDate() {
        return failureDate;
    }
}
