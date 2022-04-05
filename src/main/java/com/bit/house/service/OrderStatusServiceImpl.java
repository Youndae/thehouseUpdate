package com.bit.house.service;

import com.bit.house.domain.OrderStatusVO;
import com.bit.house.mapper.OrderStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusServiceImpl implements OrderStatusService{

    @Autowired
    OrderStatusMapper orderStatusMapper;

    @Override
    public List<OrderStatusVO> searchOrderStatus() {


        return orderStatusMapper.searchOrderStatus();
    }

    @Override
    public void changeOrderStatus(String orderCode, String orderNo) {

        orderStatusMapper.changeOrderStatus(orderCode, orderNo);
    }
}
