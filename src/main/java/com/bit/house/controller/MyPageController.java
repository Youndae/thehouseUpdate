package com.bit.house.controller;

import com.bit.house.domain.FollowVO;
import com.bit.house.domain.MemberVO;
import com.bit.house.domain.MsgVO;
import com.bit.house.mapper.MemberInfoMapper;
import com.bit.house.mapper.MyPageMapper;
import com.bit.house.service.MemberService;
import com.bit.house.service.MyPageService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("member")
public class MyPageController {

    @Autowired(required = false)
    MemberInfoMapper memberInfoMapper;

    @Autowired(required = false)
    MyPageMapper myPageMapper;

    @Autowired(required = false)
    MyPageService myPageService;

    @Autowired
    private MemberService memberService;


    //프로필수정
    @RequestMapping("/modifyProfile")
    private String modifyProfile(MemberVO memberVO, HttpServletRequest request, MultipartHttpServletRequest mreq,
                                 HttpSession session) throws Exception{

        memberVO = (MemberVO) session.getAttribute("memberVO");

        memberVO.setNickName(request.getParameter("nickName"));
        memberVO.setMemberIntro(request.getParameter("memberIntro"));

        StringBuffer sb = new StringBuffer();


        MultipartFile mf = mreq.getFile("uploadFile");

        String oldName = mf.getOriginalFilename();
        String saveName = sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                            .append(UUID.randomUUID().toString())
                            .append(oldName.substring(oldName.lastIndexOf("."))).toString();

        String filePath = request.getSession().getServletContext().getRealPath("image/profileImg/");
        File dest = new File(filePath+saveName);
        mf.transferTo(dest);


        memberVO.setMemberImg("/profileImg/"+saveName);


        myPageMapper.modifyProfile(memberVO);

        return "redirect:/member/settings";
    }
   //팔로워목록
    @RequestMapping("/allFollow/{memberId}")
    private String allFollow(Model model, @PathVariable String memberId, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        model.addAttribute("mem", memberVO.getMemberId());
        model.addAttribute("followCount", myPageMapper.followCount(memberId));
        model.addAttribute("followingCount", myPageMapper.followingCount(memberId));
        model.addAttribute("member", myPageMapper.selectProfile(memberId));
        model.addAttribute("follower", myPageMapper.follower(memberId));
        model.addAttribute("fcount", myPageMapper.followerCount(memberVO.getMemberId(), memberId));

        return "th/member/mypage/profile/allFollow";
    }
    //팔로잉 목록
    @RequestMapping("allFollowing/{memberId}")
    private String allFollowing(Model model, @PathVariable String memberId, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        model.addAttribute("mem", memberVO.getMemberId());
        model.addAttribute("followCount", myPageMapper.followCount(memberId));
        model.addAttribute("followingCount", myPageMapper.followingCount(memberId));
        model.addAttribute("member", myPageMapper.selectProfile(memberId));
        model.addAttribute("following", myPageMapper.following(memberId));
        model.addAttribute("fcount", myPageMapper.followerCount(memberVO.getMemberId(), memberId));

        return "th/member/mypage/profile/allFollowing";
    }
    //팔로우
    @RequestMapping("/follow")
    @ResponseBody
    private String follow(HttpServletRequest request, FollowVO followVO, HttpSession session, @RequestParam("followId") String followId, String followNo) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        followNo = memberVO.getMemberId()+followId;

        followVO.setMemberId(memberVO.getMemberId());
        followVO.setFollowId(followId);
        followVO.setFollowNo(followNo);

        myPageMapper.follow(followVO);

        return "redirect:/member/memberProfile";
    }

    //팔로우취소
    @RequestMapping("/cancelFollow")
    @ResponseBody
    private void cancelFollow(@RequestParam("followId") String followId, HttpSession session, FollowVO followVO) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        followVO.setMemberId(memberVO.getMemberId());
        followVO.setFollowId(followId);

        System.out.println("followId : "+followId);

        myPageMapper.cancelFollow(followVO);


    }
    //내 프로필
    @RequestMapping("/myBoard")
    private String myProfile(Model model, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        model.addAttribute("myphoto", myPageMapper.profilePhoto(memberVO.getMemberId()));
        model.addAttribute("mprofile", myPageMapper.myProfile(memberVO.getMemberId()));
        model.addAttribute("myscrap", myPageMapper.profileScrap(memberVO.getMemberId()));
        model.addAttribute("followCount", myPageMapper.followCount(memberVO.getMemberId()));
        model.addAttribute("followingCount", myPageMapper.followingCount(memberVO.getMemberId()));
        model.addAttribute("photoCount", myPageMapper.photoCount(memberVO.getMemberId()));
        model.addAttribute("scrapCount", myPageMapper.scrapCount(memberVO.getMemberId()));

        return "th/member/mypage/profile/myBoard";
    }

    //사용자 프로필
    @RequestMapping("/memberProfile/{memberId}")
    private String memberProfile(Model model, @PathVariable("memberId") String memberId, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        model.addAttribute("member", memberVO.getMemberId());
        model.addAttribute("memprofile", myPageMapper.myProfile(memberId));
        model.addAttribute("memphoto", myPageMapper.profilePhoto(memberId));
        model.addAttribute("memberscrap", myPageMapper.profileScrap(memberId));
        model.addAttribute("followCount", myPageMapper.followCount(memberId));
        model.addAttribute("followingCount", myPageMapper.followingCount(memberId));
        model.addAttribute("photoCount", myPageMapper.photoCount(memberId));
        model.addAttribute("scrapCount", myPageMapper.scrapCount(memberId));
        model.addAttribute("fcount", myPageMapper.followerCount(memberVO.getMemberId(), memberId));

        return "th/member/mypage/profile/memberProfile";
    }
    //사진 게시글 전체보기
    @RequestMapping("/allPhoto/{memberId}")
    private String allPhoto(Model model, @PathVariable String memberId) throws Exception{

        model.addAttribute("photo", myPageMapper.allPhoto(memberId));

        return "th/member/mypage/profile/allPhoto";
    }

    //스크랩 전체보기
    @RequestMapping("/allScrap/{memberId}")
    private String allScrap(Model model, @PathVariable String memberId) throws Exception{

        model.addAttribute("scrap", myPageMapper.allScrap(memberId));

        return "th/member/mypage/profile/allScrap";
    }

    //보낸쪽지함
    @RequestMapping("/sendNote")
    private String sendNote(Model model, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        model.addAttribute("sendnote", myPageMapper.sendNote(memberVO.getMemberId()));

        return "th/member/mypage/profile/sendNote";
    }

    //받은쪽지함
    @RequestMapping("/receiveNote")
    private String receiveNote(Model model, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        model.addAttribute("receiveNote", myPageMapper.receiveNote(memberVO.getMemberId()));

        return "th/member/mypage/profile/receiveNote";
    }

    //쪽지보내기 폼
    @RequestMapping("/noteSending")
    private String noteSending(Model model, @RequestParam(required = false) String receiveId){

        model.addAttribute("sending", receiveId);

        return "th/member/mypage/profile/noteSending";
    }

    //쪽지보내기
    @RequestMapping("/noteSendingProc")
    private String noteSendingProc(MsgVO msgVO, HttpServletRequest request, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        msgVO.setMemberId(memberVO.getMemberId());
        msgVO.setMsgContent(request.getParameter("msgContent"));
        msgVO.setReceiveId(request.getParameter("receiveId"));

        myPageMapper.noteSending(msgVO);

        return "redirect:/member/receiveNote";
    }

    //쪽지 삭제

    @RequestMapping("/deleteNote")
    @ResponseBody
    private String deleteNote(@RequestParam(required = false) List<String> msgNum) throws Exception{

        myPageService.deleteNote(msgNum);

        return "redirect:/member/sendNote";
    }


    @GetMapping("/settings")
    public String getProfileInfo(HttpSession session, Model model) throws Exception{

        MemberVO member = (MemberVO) session.getAttribute("memberVO");
        model.addAttribute("member", member);
        model.addAttribute("myprofile", myPageMapper.selectProfile(member.getMemberId()));


        return "th/member/mypage/info/myAllInfoSettings";
    }

    @PostMapping("updateInfo")
    public String updateProfileInfo(HttpSession session, MemberVO memberVO) {

        memberInfoMapper.updateInfoMemberById(memberVO);
        session.setAttribute("memberVO", memberVO);
        return "redirect:/member/settings";
    }

    @PostMapping("updatePassword")
    public String updateProfilePassword(MemberVO memberVO) {
        memberService.updatePW(memberVO);
        return "redirect:/member/settings";
    }


}