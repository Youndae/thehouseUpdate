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
public class ProductVO {
    private String productNo;
    private String sellerName;
    private String productName;
    private String modelName;
    private int customerPrice;
    private int sellPrice;
    private int purchasePrice;
    private String categoryCode;
    private Date productCreateDate;
    private Date productUpdateDate;
    private String productMainImg;
    private String productSubImg1;
    private String productSubImg2;
    private String productSubImg3;

    private String productSubImg4;
    private String productExpImg;
    private int clickTotalCount;

   private String memberId;
   private String colorName;
   private String reviewContent;
    private String reviewImg1;
    private String reviewImg2;
    private String reviewImg3;

    private String Year;
    private String Month;
    private String day;

    @Autowired(required = false)
    private ColorVO colorVO;


    private List<String> colorCodeVOList;

    private Date orderConfirmDate;

}
