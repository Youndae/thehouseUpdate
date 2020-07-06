package com.bit.house.service;

import com.bit.house.domain.MemberVO;
import com.bit.house.mapper.MemberInfoMapper;
import com.bit.house.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    MemberInfoMapper memberInfoMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void insertMember(MemberVO memberVO) {

        memberMapper.insertMember(memberVO);
    }

    @Override
    public void insertSocialToMember(MemberVO memberVO) {

        memberMapper.insertSocialToMember(memberVO);
    }

    @Override
    public MemberVO searchMember(String id) {

        return memberMapper.searchMember(id);
    }

    @Override
    public void updatePW(MemberVO memberVO) {
        memberVO.setUserpw(passwordEncoder.encode(memberVO.getUserpw()));
        memberInfoMapper.updateMemberPassword(memberVO);
    }

    /*@Override
    public void updateMember(MemberVO memberVO*//*, String userpw*//*) {



    }*/


}