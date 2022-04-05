package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LikeVO {
    private String likeNo;
    private String memberId;
    private int photoBoardNo;
}
