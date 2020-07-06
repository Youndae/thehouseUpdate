package com.bit.house.domain;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;


@Setter
@Getter
@NoArgsConstructor
@ToString
public class BasketVO {
    private int basketNo;
    private String memberId;
    private String productNo;
    private String productColor;
    private int sellPrice;
    private String productName;
    private String productMainImg;
    private String modelName;
    private int qty;
    private int count;
    private int totalPrice;

    @Autowired(required = false)
    private ProductVO productVO;

    @Autowired(required = false)
    private ProductOptionVO productOptionVO;

    @Autowired(required = false)
    private ColorVO colorVO;

    public BasketVO(String memberId, String productNo, String productColor){
        this.memberId = memberId;
        this.productNo = productNo;
        this.productColor = productColor;
    }

}
