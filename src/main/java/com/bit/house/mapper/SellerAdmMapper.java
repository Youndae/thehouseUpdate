package com.bit.house.mapper;

import com.bit.house.domain.ProductVO;
import com.bit.house.domain.SellerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SellerAdmMapper {
    //거래처 현황
    public List<SellerVO> sellerManagement() throws Exception;
    //거래처 현황페이지 판매글(4개)
    public List<ProductVO> sellerProduct(String sellerName) throws Exception;
    //거래처 판매글 개수
    int productCount(String sellerName) throws Exception;
    //거래처 정보
    public SellerVO sellerStat(String sellerName) throws Exception;
    //거래처 상세
    public SellerVO sellerDetail(String sellerName) throws Exception;
    //해당 거래처 판매글 전체목록
    public List<ProductVO> allSellerProduct(String sellerName) throws Exception;
    //신청 업체 목록
    public List<SellerVO> applySeller() throws Exception;
    //업체 신청 승인
    public void applyProc(String sellerName) throws Exception;


}
