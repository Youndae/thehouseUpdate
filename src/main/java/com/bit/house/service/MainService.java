package com.bit.house.service;

import com.bit.house.domain.productQtyVO;

import java.util.List;

public interface MainService {
    List<productQtyVO> selectBestList();
}
