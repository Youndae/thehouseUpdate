package com.bit.house.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

@Data
public class MemberVO {

    private String memberId;
    private String memberName;
    private String memberTel;
    private String memberAddr;
    private String memberEmail;
    private String deliveryAddr1;
    private String deliveryAddr2;
    private String nickName;
    private String memberImg;
    private Date memberSignUpDate;
    private Date memberUpdateDate;
    private int point;
    private String memberIntro;
    private String userpw;

    private String userid;
    private String userName;
    private String userTel;
    private String userEmail;

    private String orderNo;
    private Date confirmDate;
    private String cancelReason;




    @Autowired(required = false)
    private OrderListVO orderListVO;
    /*@Autowired(required = false)
    OrderListVO orderListVO;*/
}
