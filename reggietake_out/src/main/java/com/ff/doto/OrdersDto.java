package com.ff.doto;

import com.ff.entity.OrderDetail;
import com.ff.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

    @Override
    public String toString() {
        return "OrdersDto{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", consignee='" + consignee + '\'' +
                ", orderDetails=" + orderDetails +
                super.toString()+'}';
    }
}
