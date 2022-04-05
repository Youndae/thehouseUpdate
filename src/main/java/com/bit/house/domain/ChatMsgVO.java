package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/*
db에서가져오는 채팅 메시지 vo
 */
@ToString
@Setter
@Getter
@NoArgsConstructor
public class ChatMsgVO extends Criteria{

    private String memberId;
    private String msg;
    private String time;

    private PageMaker pageMaker;
}
