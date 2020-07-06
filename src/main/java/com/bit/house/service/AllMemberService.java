package com.bit.house.service;

import com.bit.house.domain.AllMemberVO;

public interface AllMemberService {

    AllMemberVO read(String userid);

    public void insertUser(AllMemberVO allMemberVO);

    public void insertSocialToUser(AllMemberVO allMemberVO);

    AllMemberVO searchSocial(String id);
}
