package com.bit.house.service;

import com.bit.house.domain.OrderListProVO;

import java.util.List;

public interface OrderListProService {

    List<OrderListProVO> searchOrderList(String sellerId);
    List<OrderListProVO> searchOrderStatus(String orderCode, String sellerId);
}
