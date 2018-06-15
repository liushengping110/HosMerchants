package wizrole.hosmerchants.order.model.orderlist;

import java.util.List;

/**
 * Created by liushengping on 2018/1/17/017.
 * 何人执笔？
 */

public class OrderListBack {

    public String ResultCode;
    public String  ResultContent;
    public List<OrderList> OrderList;


    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public void setResultContent(String resultContent) {
        ResultContent = resultContent;
    }

    public void setOrderList(List<wizrole.hosmerchants.order.model.orderlist.OrderList> orderList) {
        OrderList = orderList;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public String getResultContent() {
        return ResultContent;
    }

    public List<wizrole.hosmerchants.order.model.orderlist.OrderList> getOrderList() {
        return OrderList;
    }
}
