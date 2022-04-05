package com.bit.house.domain;

import lombok.Data;

@Data
public class OrderListProVO {

    private String orderNo;
    private String productNo;
    private String productName;
    private String sellerId;
    private String sellerName;
    private String memberId;
    private String colorName;
    private int orderQty;
    private int totalPrice;
    private int usedPoint;
    private String recipient;
    private String orderAddr;
    private String orderDate;
    private String payCode;
    private String payType;
    private String orderCode;
    private String orderType;

}
