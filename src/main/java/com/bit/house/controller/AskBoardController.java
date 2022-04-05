package com.bit.house.controller;

import com.bit.house.domain.*;
import com.bit.house.mapper.AskBoardMapper;
import com.bit.house.mapper.MyPageMapper;
import com.bit.house.mapper.PhotoBoardMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Controller
@Slf4j
public class AskBoardController {

    @Autowired(required = false)
    AskBoardMapper askBoardMapper;

    @Autowired(required = false)
    PhotoBoardMapper photoBoardMapper;

    @Autowired(required = false)
    MyPageMapper myPageMapper;
    /*
        askBoardList와 askSearchList 합쳐서 이건 사용안함.

    //게시판 리스트 화면 호출
    @RequestMapping("/askBoardList")
    private String askBoardList(@ModelAttribute("cri") Criteria cri, Model model, HttpSession session) throws Exception {

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        log.info("cri : " + cri.toString());

        model.addAttribute("member", memberVO);
        model.addAttribute("list", askBoardMapper.askBoardList(cri));
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCri(cri);
        pageMaker.setTotalCount(askBoardMapper.listCountCriteria(cri));

        model.addAttribute("pageMaker", pageMaker);


        return "th/askBoard/askBoardList";
    }
    //검색 리스트
    @RequestMapping("/askSearchList")
    private String askSearchList(@ModelAttribute("cri") Criteria cri, @RequestParam(required = false) String keyword, Model model, HttpServletRequest request, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        System.out.println("////////keyword//////"+keyword);


        System.out.println("pageStart : "+cri.getPageStart());


        model.addAttribute("member", memberVO);

        Search search = new Search();
        search.setKeyword(keyword);
        search.setCri(cri);
        search.setTotalCount(askBoardMapper.searchListCountCriteria(cri, keyword));
        model.addAttribute("list", askBoardMapper.searchList(keyword, cri.getPageStart(), cri.getPerPageNum()));
        *//*PageMaker pageMaker = new PageMaker();
        pageMaker.setCri(cri);
        pageMaker.setTotalCount(askBoardMapper.searchListCountCriteria(cri, keyword));*//*

        model.addAttribute("pageMaker", search);

        return "th/askBoard/askBoardSearchList";
    }*/

    @RequestMapping("/askBoardList")
    private String askSearchList(@ModelAttribute("cri") Criteria cri, @RequestParam(required = false) String keyword, Model model, HttpServletRequest request, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        log.info("////////keyword//////"+keyword);

        model.addAttribute("member", memberVO);

        Search search = new Search();
        search.setKeyword(keyword);
        search.setCri(cri);
        search.setTotalCount(askBoardMapper.searchListCountCriteria(cri, keyword));
        model.addAttribute("list", askBoardMapper.searchList(keyword, cri.getPageStart(), cri.getPerPageNum()));

        model.addAttribute("pageMaker", search);

        return "th/askBoard/askBoardList";
    }

    //글 상세페이지
    @RequestMapping("/askdetail/{askBoardNo}")
    private String askDetail(@PathVariable int askBoardNo, Model model, HttpSession session) throws Exception {

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        AskBoardVO detail=askBoardMapper.askDetail(askBoardNo);

        try {
            model.addAttribute("member", photoBoardMapper.myProfileImg(memberVO.getMemberId()));
            model.addAttribute("fcount", myPageMapper.followerCount(memberVO.getMemberId(), detail.getMemberId()));
        }catch (NullPointerException e){
//            e.printStackTrace();
            log.info("anonymous member");
        }
        model.addAttribute("detail", detail);
        model.addAttribute("askComment", askBoardMapper.askComment(askBoardNo));
        model.addAttribute("commentCount", askBoardMapper.askCommentCount(askBoardNo));



        return "th/askBoard/askBoardDetail";
    }

    //비회원 글 상세보기
    @RequestMapping("/askdetailnonmem/{askBoardNo}")
    private String askDetailNonMem(@PathVariable int askBoardNo, Model model) throws Exception {

        AskBoardVO detail=askBoardMapper.askDetail(askBoardNo);

        model.addAttribute("detail", detail);
        model.addAttribute("askComment", askBoardMapper.askComment(askBoardNo));
        model.addAttribute("commentCount", askBoardMapper.askCommentCount(askBoardNo));


        return "th/askBoard/askBoardDetailNonMem";
    }

    //게시글 작성 폼 호출
    @RequestMapping("/askinsert")
    private String insertAsk() {
        return "th/askBoard/askBoardInsert";
    }

    //게시글 작성
    @RequestMapping("/askinsertProc")
    private String insertAskProc(HttpServletRequest request, HttpSession session) throws Exception {

        AskBoardVO askBoardVO = new AskBoardVO();

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        askBoardVO.setAskTitle(request.getParameter("askTitle"));
        askBoardVO.setAskContent(request.getParameter("askContent"));
        askBoardVO.setMemberId(memberVO.getMemberId());

        askBoardMapper.insertAsk(askBoardVO);

        return "redirect:/askBoardList";
    }
    //답글 작성 폼
    @GetMapping("/askreply/{askBoardNo}")
    private String askReply(@PathVariable int askBoardNo, Model model) throws Exception {

        model.addAttribute("detail", askBoardMapper.askDetail(askBoardNo));

        return "th/askBoard/askBoardReplyInsert";
    }
    //답글 작성
    @RequestMapping("/askreplyProc")
    private String askReplyProc(HttpServletRequest request, HttpSession session) throws Exception {

        AskBoardVO askBoardVO = new AskBoardVO();

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        askBoardVO.setAskTitle(request.getParameter("askTitle"));
        askBoardVO.setMemberId(memberVO.getMemberId());
        askBoardVO.setAskContent(request.getParameter("askContent"));
        askBoardVO.setAskGroupNo(Integer.parseInt(request.getParameter("askGroupNo")));
        askBoardVO.setAskIndent(Integer.parseInt(request.getParameter("askIndent")));
        askBoardVO.setAskUpperNo(Integer.parseInt(request.getParameter("askUpperNo")));

//        재귀쿼리로 수정하면서 필요없어짐.
//        askBoardMapper.askReplyUp(askBoardVO);

        askBoardMapper.askReply(askBoardVO);

        return "redirect:/askBoardList";
    }
    //게시글 수정 폼
    @RequestMapping("/askupdate/{askBoardNo}")
    private String askUpdate(@PathVariable int askBoardNo, Model model) throws Exception {

        model.addAttribute("detail", askBoardMapper.askDetail(askBoardNo));

        return "th/askBoard/askBoardUpdate";
    }
    //게시글 수정
    @RequestMapping("/askupdateProc")
    private String askUpdateProc(HttpServletRequest request) throws Exception {

        AskBoardVO askBoardVO = new AskBoardVO();

        askBoardVO.setAskTitle(request.getParameter("askTitle"));
        askBoardVO.setAskContent(request.getParameter("askContent"));
        askBoardVO.setAskBoardNo(Integer.parseInt(request.getParameter("askBoardNo")));

        askBoardMapper.askUpdate(askBoardVO);

        return "redirect:/askdetail/" + request.getParameter("askBoardNo");
    }
    //게시글 삭제
    @RequestMapping("/askdelete/{askBoardNo}")
    private String askDelete(@PathVariable int askBoardNo) throws Exception {

        askBoardMapper.askDelete(askBoardNo);

        return "redirect:/askBoardList";
    }

    // 다중파일업로드 에디터
    @RequestMapping(value = "/file_uploader_html5.do", method = RequestMethod.POST)
    @ResponseBody
    public String multiplePhotoUpload(HttpServletRequest request) {

        StringBuffer sb = new StringBuffer();
        try {

            String oldName = request.getHeader("file-name");

            String filePath = request.getSession().getServletContext().getRealPath("image/board/askboard/");
            System.err.println(filePath);
            String saveName = sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                    .append(UUID.randomUUID().toString())
                    .append(oldName.substring(oldName.lastIndexOf("."))).toString();
            System.err.println(filePath + saveName);
            InputStream is = request.getInputStream();

            OutputStream os = new FileOutputStream(filePath + saveName);
            int numRead;
            byte b[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
            while ((numRead = is.read(b, 0, b.length)) != -1) {
                os.write(b, 0, numRead);
            }
            os.flush();
            os.close();

            sb = new StringBuffer();
            sb.append("&bNewLine=true")
                    .append("&sFileName=").append(oldName)
                    .append("&sFileURL=").append("/uploadImg/board/askboard/").append(saveName);
            System.out.println(sb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //댓글 작성
    @RequestMapping("/insertAskComment")
    @ResponseBody
    private void insertAskComment(@RequestParam("commentContent") String commentContent, CommentVO commentVO, HttpSession session, @RequestParam("askBoardNo") int askBoardNo) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        System.out.println("내용 : "+commentContent);
        System.out.println("글번호 : "+askBoardNo);

        commentVO.setMemberId(memberVO.getMemberId());
        commentVO.setCommentContent(commentContent);
        commentVO.setPhotoBoardNo(askBoardNo);

        askBoardMapper.insertAskComment(commentVO);

    }

}
