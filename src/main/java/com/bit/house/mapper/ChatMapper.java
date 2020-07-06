package com.bit.house.mapper;

import com.bit.house.domain.ChatMsgVO;
import com.bit.house.domain.ChatVO;
import com.bit.house.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {
    void insertMsg(String chatRoomId, String memberId, String msg, String time);
    List<ChatMsgVO> selectChatMsg(ChatMsgVO chatMsgVO);
    int countList(ChatMsgVO chatMsgVO);
}
