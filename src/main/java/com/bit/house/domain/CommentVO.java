package com.bit.house.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class CommentVO extends MemberVO{
    private String memberId;
    private Date commentupDate;
    private String commentContent;
    private int commentGroupNo;
    private int commentIndent;
    private int commentStep;
    private int photoBoardNo;
    private int askBoardNo;
}
