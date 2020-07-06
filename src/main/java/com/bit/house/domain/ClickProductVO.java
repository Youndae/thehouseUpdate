package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
public class ClickProductVO {
    private int clickProductNo;
    private String memberId;
    private String productNo;
    private Date clickDate;
    private int clickCount;
}
