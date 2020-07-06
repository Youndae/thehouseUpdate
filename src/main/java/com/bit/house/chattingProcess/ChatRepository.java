package com.bit.house.chattingProcess;

import com.bit.house.domain.*;
import com.bit.house.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChatRepository {

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private static final String CHAT_ROOMS="CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoomVO> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;
    private ListOperations<String, Object> msgList;

    @Autowired(required = false)
    ChatMapper chatMapper;

    @PostConstruct
    public void init() {
        opsHashChatRoom=redisTemplate.opsForHash();
        msgList=redisTemplate.opsForList();
        topics=new HashMap<>();
    }

    public List<ChatRoomVO> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoomVO findRoombyId(String chatId) {
        return opsHashChatRoom.get(CHAT_ROOMS, chatId);
    }

    public ChatRoomVO setAdmin(ChatRoomVO chatRoomVO) {
        opsHashChatRoom.put(CHAT_ROOMS, chatRoomVO.getChatId(), chatRoomVO);
        return opsHashChatRoom.get(CHAT_ROOMS, chatRoomVO.getChatId());
    }

    public ChatRoomVO createRoom(String memberId) {
        //채팅방생성-채팅방공유를 위해 redis hash에 저장
        ChatRoomVO chatRoomVO=ChatRoomVO.create(memberId);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoomVO.getChatId(), chatRoomVO);
        return chatRoomVO;
    }

    public void enterChatRoom(String chatId) {
        //채팅방 입장-redis에 topic을 만들고 pub/sub 통신을 위한 리스너 설정
        ChannelTopic topic=topics.get(chatId);
        if(topic==null){
            topic=new ChannelTopic(chatId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(chatId, topic);
        }
    }

    public ChannelTopic getTopic(String chatId) {
        return topics.get(chatId);
    }

    public void deleteChatRoom(String chatId){
        ChannelTopic topic=topics.get(chatId);
        redisMessageListener.removeMessageListener(redisSubscriber, topic);
        topics.remove(chatId);
        ChatRoomVO chatInfo=opsHashChatRoom.get(CHAT_ROOMS, chatId);

        //데이터 전처리
        StringBuilder msgData=new StringBuilder();
        if(msgList.size(chatId)!=0){
            msgData.append(msgList.range(chatId,0,-1));
            String stringMsg=msgData.toString();
            String msg=stringMsg.substring(1, stringMsg.length()-1);
            //레디스에 저장된 채팅메시지 디비에 저장
            chatMapper.insertMsg(chatInfo.getChatId(), chatInfo.getMemberId(), msg, chatInfo.getTime());
            //레디스에 저장된 채팅내역 삭제
            msgList.trim(chatId,-1,0);
        }
        //레디스에 저장된 채팅정보 삭제
        opsHashChatRoom.delete(CHAT_ROOMS, chatId);
    }

    public ChatRoomVO addCount(ChatRoomVO chatRoomVO){
        //채팅방 인원수+1
        opsHashChatRoom.put(CHAT_ROOMS, chatRoomVO.getChatId(), chatRoomVO);
        return opsHashChatRoom.get(CHAT_ROOMS, chatRoomVO.getChatId());
    }

    public void saveMsg(ChatVO vo){
        //저장할 데이터 전처리
        String msg=vo.getMsg().replaceAll(",", "");
        //레디스에 채팅 메시지 저장
        msgList.rightPush(vo.getChatId(), vo.getSender());
        msgList.rightPush(vo.getChatId(), msg);
        msgList.rightPush(vo.getChatId(), vo.getTime()+"<br>");
    }

    public List<ChatMsgVO> getChatMsg(ChatMsgVO vo, Criteria criteria){

        List<ChatMsgVO> vos=chatMapper.selectChatMsg(vo);
        //페이징
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCri(criteria);
        pageMaker.setTotalCount(chatMapper.countList(vo));
        vo.setPageMaker(pageMaker);

        //데이터 전처리
        for(ChatMsgVO chatMsgVO:vos){
            String msg=chatMsgVO.getMsg().replace(",", "");
            chatMsgVO.setMsg(msg);
        }
        return vos;
    }
}
