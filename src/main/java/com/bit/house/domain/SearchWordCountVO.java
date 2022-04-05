package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
public class SearchWordCountVO {
    private int searchNo;
    private String memberId;
    private String searchWord;
    private Date searchDate;
    private int searchCount;


}
