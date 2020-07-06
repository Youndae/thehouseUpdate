package com.bit.house.mapper;

import com.bit.house.domain.OrderStatusVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderStatusMapper {

    List<OrderStatusVO> searchOrderStatus();

    public void changeOrderStatus(String orderCode, String orderNo);
}

