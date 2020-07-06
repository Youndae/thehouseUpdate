package com.bit.house.service;

import com.bit.house.mapper.OrderListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderListServiceImpl implements OrderListService{

    @Autowired
    OrderListMapper orderListMapper;

    @Override
    public void changeOrderStatus(String orderCode, String orderNo) {

        orderListMapper.changeOrderStatus(orderCode, orderNo);

    }
}
