package com.bit.house.service;

import com.bit.house.domain.OrderListProVO;
import com.bit.house.mapper.OrderListProMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderListProServiceImpl implements OrderListProService {

    @Autowired
    OrderListProMapper orderListProMapper;

    @Override
    public List<OrderListProVO> searchOrderList(String sellerId) {

        return orderListProMapper.searchOrderList(sellerId);
    }

    @Override
    public List<OrderListProVO> searchOrderStatus(String orderCode, String sellerId) {

        return orderListProMapper.searchOrderStatus(orderCode, sellerId);
    }
}
