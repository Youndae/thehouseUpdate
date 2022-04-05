package com.bit.house.service;

import com.bit.house.domain.OrderStatusVO;

import java.util.List;

public interface OrderStatusService {
    List<OrderStatusVO> searchOrderStatus();
    public void changeOrderStatus(String orderCode, String orderNo);
}
