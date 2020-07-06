package com.bit.house.service;

import com.bit.house.domain.SellerVO;

public interface SellerService {
    public void insertSeller(SellerVO sellerVO);
    public void insertSellerToUser(SellerVO sellerVO);
    SellerVO searchSeller(String id);
    SellerVO searchSellerInfo(String sellerId);
    public void updateSellerInfo(String sellerName, String sellerRes, String sellerImg, String sellerAddr, String sellerUrl, String sellerId);
    public void updateSellerManager(String sellerManager, String managerTel, String managerEmail, String sellerId);
    public void updateSellerInfoLogin(String sellerPw, String sellerId);
}
