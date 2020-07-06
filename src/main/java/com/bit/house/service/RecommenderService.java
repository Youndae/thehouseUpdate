package com.bit.house.service;

import com.bit.house.domain.ProductVO;

import java.util.Collection;
import java.util.List;

public interface RecommenderService {
    List<String> selectClickProductById();
    String selectClickProduct(String memberId);
    List<ProductVO> selectProductList(Collection<String> productNos);
    void checkClickHistory(String memberId, String productNo);
    void updateClickTotalCount(String productNo);
}
