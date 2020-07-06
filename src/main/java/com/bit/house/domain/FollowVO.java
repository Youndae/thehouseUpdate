package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FollowVO extends MemberVO{
    private String memberId;
    private String followId;
    private String followNo;
}
