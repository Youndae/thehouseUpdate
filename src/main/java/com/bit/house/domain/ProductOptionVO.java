package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ProductOptionVO {
    private String productOptionNo;
    private String productNo;
    private String colorCode;
    private int productQty;
    private int productSellingQty;
    private String colorType;
}
