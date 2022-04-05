package com.bit.house.service;

import com.bit.house.domain.AllMemberVO;
import com.bit.house.mapper.AllMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AllMemberServiceImpl implements AllMemberService {

    @Autowired
    private AllMemberMapper allMemberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AllMemberVO read(String userid) {
        return allMemberMapper.read(userid);
    }

    @Override
    public void insertUser(AllMemberVO allMemberVO) {

        allMemberVO.setUserpw(passwordEncoder.encode(allMemberVO.getUserpw()));
        allMemberMapper.insertUser(allMemberVO);
    }

    @Override
    public void insertSocialToUser(AllMemberVO allMemberVO) {
        allMemberMapper.insertSocialToUser(allMemberVO);
    }

    @Override
    public AllMemberVO searchSocial(String id) {

        return allMemberMapper.searchSocial(id);
    }
}
