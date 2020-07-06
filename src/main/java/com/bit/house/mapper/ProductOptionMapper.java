package com.bit.house.mapper;

import com.bit.house.domain.ProductOptionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductOptionMapper {

    public void insertProductOption(ProductOptionVO productOptionVO);
    List<ProductOptionVO> searchProductColorList(String productNo, String sellerId);
    public void updateProductOption(String productOptionNo, String productNo, String colorCode, int productQty, String productOptionNo2);
}
