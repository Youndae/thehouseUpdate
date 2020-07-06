package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class OrderListVO {
    private String orderNo;
    private String productNo;
    private String memberId;
    private String colorName;
    private int orderQty;
    private int totalPrice;
    private int usedPoint;
    private String recipient;
    private String orderAddr;
    private Date orderDate;
    private String orderType;
    private Date orderConfirmDate;
    private Date orderCancelDate;
    private String orderConfirmReason;
    private String cancelReason;
    private int payCode;
    private int orderCode;

    private String year;
    private String month;
    private String day;

    private String sellerName;
    private String productName;
    private String modelName;

    @Autowired(required = false)
    MemberVO memberVO;
    @Autowired(required = false)
    ProductVO productVO;

}
