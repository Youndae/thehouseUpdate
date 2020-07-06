package com.bit.house.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScrapVO extends PhotoBoardVO{
    private int scrapNo;
    private String memberId;
    private int photoBoardNo;


}
