package wizrole.hosmerchants.order.model.orderlist;

import java.util.List;

/**
 * Created by liushengping on 2018/1/17/017.
 * 何人执笔？
 */

public class OrderList {

    public String OrderStatus ;// 		订单状态
    public String OrderHeader ;// 		订单人头像
    public String OrderName	;//	订单人姓名
    public String OrderTel	;		//订单人手机号码
    public String OrderTime	;	//订单时间
    public String OrderAddress	;	//订单地址
    public String OrderDistrTime	;	//订单配送时间
    public List<OrderContent> OrderContent;//		订单内容

    public String getOrderHeader() {
        return OrderHeader;
    }

    public void setOrderHeader(String orderHeader) {
        OrderHeader = orderHeader;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public void setOrderTel(String orderTel) {
        OrderTel = orderTel;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public void setOrderDistrTime(String orderDistrTime) {
        OrderDistrTime = orderDistrTime;
    }

    public void setOrderContent(List<wizrole.hosmerchants.order.model.orderlist.OrderContent> orderContent) {
        OrderContent = orderContent;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getOrderName() {
        return OrderName;
    }

    public String getOrderTel() {
        return OrderTel;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public String getOrderDistrTime() {
        return OrderDistrTime;
    }

    public List<wizrole.hosmerchants.order.model.orderlist.OrderContent> getOrderContent() {
        return OrderContent;
    }
}
