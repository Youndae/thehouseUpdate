package com.bit.house.service;


import com.bit.house.domain.MemberVO;

public interface PhotoBoardService {
    void detailPhoto(MemberVO memberVO, int photoBoardNo) throws Exception;
}
