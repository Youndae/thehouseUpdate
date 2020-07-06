package com.bit.house.service;

import com.bit.house.domain.ProductOptionVO;
import com.bit.house.domain.ProductVO;

import java.util.List;

public interface ProductService {

    public void insertProduct(ProductVO productVO);
    public void insertProductOption(ProductOptionVO productOptionVO);
    List<ProductVO> searchProductListInfo(String sellerId);
    List<ProductVO> searchProductTarget(String productNo, String sellerId);
    List<ProductOptionVO> searchProductColorList(String productNo, String sellerId);
    public void updateProductInfo(String productNo, String productName, String modelName, int customerPrice, int sellPrice, int purchasePrice, String categoryCode
            , String productMainImg, String productSubImg1, String productSubImg2, String productSubImg3, String productExpImg, String productNo2);
    public void updateProductOption(String productOptionNo, String productNo, String colorCode, int productQty, String productOptionNo2);

}
