package com.bit.house.service;

import com.bit.house.domain.MemberVO;

public interface MemberService {

    public void insertMember(MemberVO memberVO);

    public void insertSocialToMember(MemberVO memberVO);

    public MemberVO searchMember(String id);

    public void updatePW(MemberVO memberVO);



}
