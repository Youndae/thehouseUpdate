package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
public class AskBoardVO extends MemberVO{
    private int askBoardNo;
    private String memberId;
    private String askTitle;
    private String askContent;
    private Date askDate;
    private int askGroupNo;
    private int askIndent;
    private int askStep;

}