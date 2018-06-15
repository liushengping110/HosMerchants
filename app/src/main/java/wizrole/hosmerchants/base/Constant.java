package wizrole.hosmerchants.base;

/**
 * Created by liushengping on 2017/11/22/022.
 * 何人执笔？
 */

public class Constant {
    //图片添加ip 接口后面怎么会出现？ ？是get带参数  我这边一般就是这样请求的 给这种不符合规范啊
    public static final String ip="http://39.107.75.214/";
    //请求base_url
    public static final String base_Url="http://39.107.75.214:80//order_bg/orderInterface.do?";
    public static final String img_header="img_header";//头像图片类型
    public static final String img_store_logo="img_store_logo";//商户logo图片类型
    public static final String img_pay_logo="img_store_logo";//收款码图片类型
    public static final String img_goods_infor="img_goods_infor";//商品详情图片类型
    //百度云Token获取地
    public static final String token_url="https://aip.baidubce.com/oauth/2.0/token?";
    //身份证验证请求地址（百度云）
    public static final String id_url="https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?";
    //百度云官网获取的  API KEY
    public static final String clientId = "DtkxHpaQd8mbLdSuSlQ3xaeG";
    // 百度云官网获取的 Secret Key
    public static final String clientSecret = "CzNPfNrsdIaaHTuGq6foRxMP3sCj1Ryi";
    //营业执照获取地址（百度云）
    public static final String businLicense_url="https://aip.baidubce.com/rest/2.0/ocr/v1/business_license?";
    //驾驶证获取地址（百度云）
    public static final String jsz_url="https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?";
}
