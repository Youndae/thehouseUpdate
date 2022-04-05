package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class productQtyVO extends ProductVO{
    private String productNo;
    private String sellerName;
    private String productName;
    private int customerPrice;
    private String productMainImg;
    private int totalQty;
    private int ranking;


}
