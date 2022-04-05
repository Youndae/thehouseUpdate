package com.bit.house.mapper;

import com.bit.house.domain.ProductVO;
import com.bit.house.domain.RecommenderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommenderMapper {

    List<RecommenderVO> selectClickProductById();

    String selectClickProduct(String memberId);

    ProductVO selectProductDetail(String productNo);

    int selectClickHistory(String memberId, String productNo);

    int insertCount(String memberId, String productNo);

    int updateClickHistory(String memberId, String productNo);

    int updateClickTotalCount(String productNo);

}
