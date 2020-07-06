package com.bit.house.mapper;

import com.bit.house.domain.ProductVO;
import com.bit.house.domain.productQtyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    List<ProductVO> selectMainList();
    /*List<productQtyVO> selectTotalQty();
    List<productQtyVO> selectQtyByNo();
    productQtyVO selectDetail(String productNo);*/

    List<productQtyVO> selectBestLists();
    List<productQtyVO> selectNewBestLists();
}
