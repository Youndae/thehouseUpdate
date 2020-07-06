package com.bit.house.mapper;

import com.bit.house.domain.OrderListProVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderListProMapper {

    List<OrderListProVO> searchOrderList(String sellerId);
    List<OrderListProVO> searchOrderStatus(String orderCode, String sellerId);
}
