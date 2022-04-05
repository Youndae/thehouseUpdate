package com.bit.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/*
채팅 메시지 저장 vo
 */
@ToString
@Setter
@Getter
@NoArgsConstructor
public class ChatVO implements Serializable {
    private static final long serialVersionUID=6494678977089006639L;

    public enum MessageType {
        ENTER, LEAVE, TALK
    }

    private MessageType type;
    private String chatId;
    private String sender;
    private String msg;
    private String time;
}
