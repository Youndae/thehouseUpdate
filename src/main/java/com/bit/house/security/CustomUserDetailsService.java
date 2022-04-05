package com.bit.house.security;

import com.bit.house.domain.AllMemberVO;
import com.bit.house.mapper.AllMemberMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Setter(onMethod_ = {@Autowired})
    private AllMemberMapper allMemberMapper;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        log.warn("Load User By UserName : " + userName);

        // userName means userid
        AllMemberVO allMemberVO = allMemberMapper.read(userName);


        if(!allMemberVO.isEnabled()){
            return null;
        }

        log.warn("queried by member mapper: " + allMemberVO);

        List<GrantedAuthority> authorities = new ArrayList<>();


        return new User(allMemberVO.getUserid(), allMemberVO.getUserpw(), allMemberVO.getAuthList().stream().map(authVO -> new SimpleGrantedAuthority(authVO.getRoleType())).collect(Collectors.toList()));

    }

}

