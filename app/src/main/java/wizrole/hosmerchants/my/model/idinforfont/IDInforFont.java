package wizrole.hosmerchants.my.model.idinforfont;

/**
 * Created by liushengping on 2017/12/14/014.
 * 何人执笔？
 */

public class IDInforFont {

    /**
     *edit_tool  入参detect_risk=true时返回改参数，但是身份证修改测试后，依然不返回，bug
     */


    /**
     * 图像方向，当detect_direction=true时存在。
     - -1:未定义，
     - 0:正向，
     - 1: 逆时针90度，
     - 2:逆时针180度，
     - 3:逆时针270度
     */
    public int direction;
    /**
     * normal-识别正常
     reversed_side-未摆正身份证
     non_idcard-上传的图片中不包含身份证
     blurred-身份证模糊
     over_exposure-身份证关键字段反光或过曝
     unknown-未知状态
     */
    public String image_status;
    /**
     * 输入参数 detect_risk = true 时，
     * 则返回该字段识别身份证类型: normal-正常身份证；
     * copy-复印件；temporary-临时身份证；screen-翻拍；unknow-其他未知情况
     */
    public String risk_type;
    /**
     * 如果参数 detect_risk = true 时，则返回此字段。
     * 如果检测身份证被编辑过，该字段指定编辑软件名称，
     * 如:Adobe Photoshop CC 2014 (Macintosh),如果没有被编辑过则返回值无此参数
     */
    public  long           log_id;//唯一的log id，用于问题定位
    public  int           words_result_num;
    public Words_Result words_result;


    public int getDirection() {
        return direction;
    }

    public String getImage_status() {
        return image_status;
    }

    public String getRisk_type() {
        return risk_type;
    }

    public long getLog_id() {
        return log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public Words_Result getWords_result() {
        return words_result;
    }
}
