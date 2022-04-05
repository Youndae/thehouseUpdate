package com.bit.house.domain;

import lombok.Data;

import java.util.List;

@Data
public class AllMemberVO {
    private String userid;
    private String userpw;
    private int authCode;
    private boolean enabled;
    private SocialType socialType;
    private List<AuthVO> authList;

}