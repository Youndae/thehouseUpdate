package com.bit.house.mapper;

import com.bit.house.domain.OrderListVO;
import com.bit.house.domain.ReviewVO;
import org.apache.ibatis.annotations.*;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface MyOrderListMapper {

    @Select("select ol.orderNo, ol.orderDate, p.productName, p.modelName, ol.orderQty, p.sellerName, ol.totalPrice, os.orderType, ol.orderCode, ol.colorName from orderlist ol join product p on ol.productNo=p.productNo join orderstatus os on ol.ordercode=os.orderCode where memberId=#{memberId}")
    List<OrderListVO> getMyOrderListById(String memberId);

    @Update("update orderlist set orderCode='60', cancelReason=#{orderConfirmReason} where orderNo=#{orderNo}")
    void addReturnReason(OrderListVO orderListVO);

    @Update("update orderlist set orderCode='50', cancelReason=#{cancelReason}, orderCancelDate=sysdate() where orderNo=#{orderNo}")
    void addCancelReason(OrderListVO orderListVO);

    @Insert("insert into review(orderNo, memberId, reviewContent, productRank, reviewImg1, reviewImg2, reviewImg3) values(#{orderNo}, #{memberId}, #{reviewContent}, #{productRank}, #{reviewImg1}, #{reviewImg2}, #{reviewImg3})")
    void addReview(ReviewVO reviewVO);


    @Update("update orderList set orderCode='40', orderConfirmDate=sysdate() where orderNo=#{orderNo}")
    void addConfirmOrderType(OrderListVO orderListVO);

    @Select("select r.orderNo, r.reviewContent, ol.colorName, p.productName, p.modelName, r.reviewImg1, r.reviewImg2, r.reviewImg3 from review r join orderList ol on r.orderNo=ol.orderNo join product p on ol.productNo=p.productNo where r.memberId=#{memberId}")
    List<ReviewVO> showUserReview(String memberId);

    @Delete("delete from review where orderNo=#{orderNo}")
    void deleteReviewById(String orderNo);


}
