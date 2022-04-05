package com.bit.house.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderListMapper {

    public void changeOrderStatus(String orderCode, String orderNo);
}
