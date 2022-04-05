package com.bit.house.service;

import com.bit.house.domain.ProductOptionVO;
import com.bit.house.domain.ProductVO;
import com.bit.house.mapper.ProductMapper;
import com.bit.house.mapper.ProductOptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductOptionMapper productOptionMapper;


    @Override
    public void insertProduct(ProductVO productVO) {

        productMapper.insertProduct(productVO);
    }

    @Override
    public void insertProductOption(ProductOptionVO productOptionVO) {

        productOptionMapper.insertProductOption(productOptionVO);
    }

    @Override
    public List<ProductVO> searchProductListInfo(String sellerId){

        return productMapper.searchProductListInfo(sellerId);
    }

    @Override
    public List<ProductVO> searchProductTarget(String productNo, String sellerId) {

        return productMapper.searchProductTarget(productNo, sellerId);
    }

    @Override
    public List<ProductOptionVO> searchProductColorList(String productNo, String sellerId) {

        return productOptionMapper.searchProductColorList(productNo, sellerId);
    }

    @Override
    public void updateProductInfo(String productNo, String productName, String modelName, int customerPrice, int sellPrice, int purchasePrice, String categoryCode
            , String productMainImg, String productSubImg1, String productSubImg2, String productSubImg3, String productExpImg, String productNo2) {

        productMapper.updateProductInfo(productNo, productName, modelName, customerPrice, sellPrice, purchasePrice, categoryCode, productMainImg,
                productSubImg1, productSubImg2, productSubImg3, productExpImg, productNo2);
    }

    @Override
    public void updateProductOption(String productOptionNo, String productNo, String colorCode, int productQty, String productOptionNo2) {

        productOptionMapper.updateProductOption(productOptionNo, productNo, colorCode, productQty, productOptionNo2);
    }
}
