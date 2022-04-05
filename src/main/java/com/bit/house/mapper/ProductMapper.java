package com.bit.house.mapper;

import com.bit.house.domain.ProductVO;
import com.bit.house.domain.SellerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("Select * from product")
    List<ProductVO> selectAllProduct();

    @Select("SELECT * FROM product WHERE productname LIKE CONCAT('%',#{searchProduct},'%') OR modelName LIKE CONCAT('%',#{searchProduct},'%') OR sellername LIKE CONCAT('%',#{searchProduct},'%')")
    List<ProductVO> selectProduct(@Param("searchProduct") String searchProduct);

    @Select("select * from product where categoryCode LIKE CONCAT('%',#{categoryCode},'%')")
    List<ProductVO> selectProductByCategory(String categoryCode);

    @Select("SELECT c.colorType FROM productoption po join color c on po.colorCode=c.colorCode where productno=#{productNo}")
    List<String> getProductVOByProductColorCode(String productNo);

    @Select("select * from product where productno=#{productNo}")
    ProductVO getProductVOByProductNo(String productNo);

    @Select("select s.sellerName, s.managerTel, s.managerEmail, s.sellerAddr from product p join seller s on p.sellerName=s.sellerName where productno =#{productNo}")
    SellerVO getProductVOBySellerName(String productNo);

    @Select("select r.memberId, p.modelName, ol.colorName, r.reviewContent, r.reviewImg1, r.reviewImg2, r.reviewImg3, ol.orderConfirmDate from product p join orderList ol on p.productNo=ol.productNo join review r on ol.orderNo=r.orderNo where p.productNo=#{productNo}")
    List<ProductVO> getProductReviewByProductNo(String productNo);

    @Select("Select productName from product")
    List<String> selectAllProductJs();

    public void insertProduct(ProductVO productVO);
    List<ProductVO> searchProductListInfo(String sellerId);
    List<ProductVO> searchProductTarget(String productNo, String sellerId);
    public void updateProductInfo(String productNo, String productName, String modelName, int customerPrice, int sellPrice, int purchasePrice, String categoryCode
                                  , String productMainImg, String productSubImg1, String productSubImg2, String productSubImg3, String productExpImg, String productNo2);
}



