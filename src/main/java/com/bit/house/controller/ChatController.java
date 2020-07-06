package com.bit.house.controller;

import com.bit.house.chattingProcess.ChatRepository;
import com.bit.house.chattingProcess.RedisPublisher;
import com.bit.house.domain.*;
import com.bit.house.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRepository chatRepository;

    @MessageMapping("/message")
    public void message(ChatVO message){

        if(ChatVO.MessageType.ENTER.equals(message.getType())){
            chatRepository.enterChatRoom(message.getChatId());
            message.setMsg(message.getSender()+"님이 입장하셨습니다.");
            redisPublisher.publish(chatRepository.getTopic(message.getChatId()), message);
        } else if(ChatVO.MessageType.LEAVE.equals(message.getType())){
            message.setMsg(message.getSender()+"님이 퇴장하셨습니다.");
            redisPublisher.publish(chatRepository.getTopic(message.getChatId()), message);
            chatRepository.deleteChatRoom(message.getChatId());
        } else{
            if(chatRepository.findRoombyId(message.getChatId()).getCount()==2){
                //채팅시간 set
                SimpleDateFormat currentTime = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
                String time=currentTime.format(System.currentTimeMillis());
                message.setTime(time);
                //채팅메시지 저장
                chatRepository.saveMsg(message);
                redisPublisher.publish(chatRepository.getTopic(message.getChatId()), message);
            } else{
                message.setMsg("상담원이 입장할때까지 잠시만 기다려주세요.");
                redisPublisher.publish(chatRepository.getTopic(message.getChatId()), message);
            }
        }
    }

    @PostMapping("/chat")
    public String createChat(HttpSession session){
        //채팅방 생성
        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        String memberId = memberVO.getMemberId();
        ChatRoomVO vo=chatRepository.createRoom(memberId);
        String chatId=vo.getChatId();

        return "redirect:/enter/"+chatId;
    }

    @GetMapping("/enter/{chatId}")
    public String enterChat(Model model, @PathVariable String chatId){
        //채팅방 입장
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ChatRoomVO vo=chatRepository.findRoombyId(chatId);

        if(String.valueOf(authentication.getAuthorities()).equals("[ROLE_ADMIN]")) {
            //관리자아이디 set
            vo.setAdminId(authentication.getName());
            chatRepository.setAdmin(vo);
        }
        //인원수+1
        int count=vo.getCount();
        vo.setCount(++count);
        chatRepository.addCount(vo);

        model.addAttribute("chatInfo", vo);

        return "th/main/chatting";
    }

    @GetMapping("/admin/chatList")
    public String getChatList(Model model){
        //관리자 채팅리스트
        model.addAttribute("roomLists", chatRepository.findAllRoom());
        return "th/admin/chat/chatList";
    }

    @GetMapping("/admin/chatHistory")
    public String getChatHistory(@ModelAttribute("page") ChatMsgVO vo, Criteria criteria, HttpServletRequest request, Model model) {
        //회원 채팅이력
        String memberId=request.getParameter("memberId");
        //id검색했을때
        if(memberId!=null){
            vo.setMemberId(memberId);
        }
        model.addAttribute("msgLists", chatRepository.getChatMsg(vo, criteria));

        return "th/admin/chat/chatHistory";
    }

}
