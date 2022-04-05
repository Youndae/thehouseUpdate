package com.bit.house.controller;

import com.bit.house.domain.*;
import com.bit.house.mapper.PhotoBoardMapper;
import com.bit.house.service.PhotoBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PhotoBoardController {

    @Autowired(required = false)
    PhotoBoardMapper photoBoardMapper;

    @Autowired(required = false)
    PhotoBoardService photoBoardService;

    //사진 메인
    @RequestMapping("/communityMain")
    private String communityMain(Model model, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        
        model.addAttribute("member", memberVO);
        model.addAttribute("likerank", photoBoardMapper.communityMain());

        return "th/photoBoard/photoBoardMain";
    }
    //사진 목록
    @RequestMapping("/photoBoardList")
    private String photoBoardList(Model model, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        int i = 0;

        model.addAttribute("member", memberVO);
        model.addAttribute("photoList", photoBoardMapper.selectPhotoList(i));

        return "th/photoBoard/photoBoardList";
    }
    //사진 등록
    @RequestMapping("/photoInsert")
    private String insertAsk(Model model) throws Exception{

        model.addAttribute("areaCode", photoBoardMapper.area());
        model.addAttribute("houseCode", photoBoardMapper.house());
        model.addAttribute("styleCode", photoBoardMapper.style());
        model.addAttribute("placeCode", photoBoardMapper.place());

        return "th/photoBoard/photoBoardInsert";
    }

    @RequestMapping("/photoInsertProc")
    private String insertPhotoProc(PhotoBoardVO photoBoardVO,
                                   MultipartHttpServletRequest mtf, HttpSession session, HttpServletRequest request) throws Exception{

        List<String> photoImgArray = new ArrayList<String>();
        List<MultipartFile> fileList = mtf.getFiles("files");

        String filePath = request.getSession().getServletContext().getRealPath("image/board/photoboard/");

        for(MultipartFile mf : fileList){

            StringBuffer sb = new StringBuffer();

            String originFileName = mf.getOriginalFilename();

            String saveName =  sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                                .append(UUID.randomUUID().toString())
                                .append(originFileName.substring(originFileName.lastIndexOf("."))).toString();


            String saveFile = filePath + saveName;

            photoImgArray.add(saveName);

            mf.transferTo(new File(saveFile));

        }

        int filesSize=photoImgArray.size();
        if( filesSize !=0){
            int i=1;
            switch (filesSize ){
                case 5 :
                    photoBoardVO.setPhotoImg5( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 4 :
                    photoBoardVO.setPhotoImg4( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 3 :
                    photoBoardVO.setPhotoImg3( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 2 :
                    photoBoardVO.setPhotoImg2( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 1 :
                    photoBoardVO.setPhotoImg1( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
            }
        }


        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        photoBoardVO.setMemberId(memberVO.getMemberId());
        photoBoardVO.setPhotoContent(request.getParameter("photoContent"));
        photoBoardVO.setAreaCode(request.getParameter("areaCode"));
        photoBoardVO.setHouseCode(request.getParameter("houseCode"));
        photoBoardVO.setStyleCode(request.getParameter("styleCode"));
        photoBoardVO.setPlaceCode(request.getParameter("placeCode"));

        photoBoardMapper.insertPhoto(photoBoardVO);


        return "redirect:/photoBoardList";
    }
    //사진 상세
    @RequestMapping("/photodetail/{photoBoardNo}")
    private String photoDetail(@PathVariable int photoBoardNo, Model model, HttpSession session) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        PhotoBoardVO detail=photoBoardMapper.photoDetail(photoBoardNo);

        try{
            model.addAttribute("member", photoBoardMapper.myProfileImg(memberVO.getMemberId()));
            model.addAttribute("likestat", photoBoardMapper.likeStat(memberVO.getMemberId(), photoBoardNo));
            model.addAttribute("scrapstat", photoBoardMapper.scrapStat(memberVO.getMemberId(), photoBoardNo));
        }catch (NullPointerException e){

        }

        model.addAttribute("photodetail", detail);
        model.addAttribute("userphoto", photoBoardMapper.userPhoto(detail.getMemberId()));
        model.addAttribute("photoComment", photoBoardMapper.photoComment(photoBoardNo));
        model.addAttribute("commentCount", photoBoardMapper.commentCount(photoBoardNo));


        return "th/photoBoard/photoBoardDetail";
    }

   /*
    photoDetail과 합침.
    //사진 상세
    @RequestMapping("/photodetailnonmem/{photoBoardNo}")
    private String photoDetailNonMem(@PathVariable int photoBoardNo, Model model) throws Exception{



        PhotoBoardVO detail=photoBoardMapper.photoDetail(photoBoardNo);

        model.addAttribute("photodetail", detail);
        model.addAttribute("userphoto", photoBoardMapper.userPhoto(detail.getMemberId()));
        model.addAttribute("photoComment", photoBoardMapper.photoComment(photoBoardNo));
        model.addAttribute("commentCount", photoBoardMapper.commentCount(photoBoardNo));


        return "th/photoBoard/photoBoardDetailNonMem";
    }*/

    //사진 수정
    @RequestMapping("/photoupdate/{photoBoardNo}")
    private String photoUpdate(@PathVariable int photoBoardNo, Model model) throws Exception{

        model.addAttribute("photodetail", photoBoardMapper.photoDetail(photoBoardNo));
        model.addAttribute("areaCode", photoBoardMapper.area());
        model.addAttribute("houseCode", photoBoardMapper.house());
        model.addAttribute("styleCode", photoBoardMapper.style());
        model.addAttribute("placeCode", photoBoardMapper.place());

        return "th/photoBoard/photoBoardUpdate";
    }


    //수정페이지에 보내는 이미지
    @RequestMapping("/photoBoardUpdateImg")
    @ResponseBody
    public ResponseEntity<List<PhotoBoardVO>> photoBoardUpdateImg(int photoBoardNo){

        System.out.println(photoBoardNo);

        return new ResponseEntity<>(photoBoardMapper.photoBoardUpdateImg(photoBoardNo), HttpStatus.OK);
    }

    @RequestMapping("/photoupdateProc")
    private String photoUpdateProc(PhotoBoardVO photoBoardVO,
                                   MultipartHttpServletRequest mtf, HttpServletRequest request) throws Exception{




        photoBoardService.photoUpdateProc(photoBoardVO, mtf, request);



        /*
            기존코드.
            수정된 코드는 PhotoBoardServiceImpl에.

        String[] delOldPhotos = request.getParameterValues("delOldPhoto");

        for(int i = 0; i < delOldPhotos.length; i++){
            System.out.println("OldPhoto Num : " + delOldPhotos[i]);
        }

        System.out.println("for End");






        List<String> photoImgArray = new ArrayList<String>();
        List<MultipartFile> fileList = mtf.getFiles("files");

        String filePath = request.getSession().getServletContext().getRealPath("image/board/photoboard/");

        for(MultipartFile mf : fileList){

            StringBuffer sb = new StringBuffer();

            String originFileName = mf.getOriginalFilename();

            String saveName =  sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                    .append(UUID.randomUUID().toString())
                    .append(originFileName.substring(originFileName.lastIndexOf("."))).toString();

            String saveFile = filePath + saveName;

            photoImgArray.add(saveName);

            mf.transferTo(new File(saveFile));

        }

        int filesSize=photoImgArray.size();
        if( filesSize !=0){
            int i=1;
            switch (filesSize ){
                case 5 :
                    photoBoardVO.setPhotoImg5( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 4 :
                    photoBoardVO.setPhotoImg4( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 3 :
                    photoBoardVO.setPhotoImg3( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 2 :
                    photoBoardVO.setPhotoImg2( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
                    i++;
                case 1 :
                    photoBoardVO.setPhotoImg1( "/board/photoboard/"+photoImgArray.get(filesSize-i)   );
            }
        }




        photoBoardVO.setMemberId(memberVO.getMemberId());
        photoBoardVO.setPhotoContent(request.getParameter("photoContent"));
        photoBoardVO.setAreaCode(request.getParameter("areaCode"));
        photoBoardVO.setHouseCode(request.getParameter("houseCode"));
        photoBoardVO.setStyleCode(request.getParameter("styleCode"));
        photoBoardVO.setPlaceCode(request.getParameter("placeCode"));

        photoBoardMapper.updatePhoto(photoBoardVO);

        return "redirect:/photoBoardList";*/

        return "redirect:/photoBoardList";
    }
    //사진 삭제
    @RequestMapping("/photodelete/{photoBoardNo}")
    private String photoDelete(@PathVariable int photoBoardNo) throws Exception{

        photoBoardMapper.deletePhoto(photoBoardNo);

        return "redirect:/photoBoardList";
    }



    //좋아요
    @RequestMapping("/like")
    @ResponseBody
    private void like(LikeVO likeVO, HttpSession session, @RequestParam("photoBoardNo") int photoBoardNo, String likeNo) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        likeNo = memberVO.getMemberId() + photoBoardNo;

        likeVO.setMemberId(memberVO.getMemberId());
        likeVO.setLikeNo(likeNo);
        likeVO.setPhotoBoardNo(photoBoardNo);

        photoBoardMapper.like(likeVO);

        photoBoardMapper.likeCount(photoBoardNo);

    }
    //좋아요 취소
    @RequestMapping("/nonlike")
    @ResponseBody
    private void cancellike(LikeVO likeVO, HttpSession session, @RequestParam("photoBoardNo") int photoBoardNo) throws Exception{

        MemberVO memberVo = (MemberVO) session.getAttribute("memberVO");

        likeVO.setMemberId(memberVo.getMemberId());
        likeVO.setPhotoBoardNo(photoBoardNo);

        photoBoardMapper.cancelLike(likeVO);

        photoBoardMapper.likeCountSub(photoBoardNo);

    }
    //스크랩
    @RequestMapping("/scrap")
    @ResponseBody
    private void scrap(ScrapVO scrapVO, HttpSession session, @RequestParam("photoBoardNo") int photoBoardNo) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        scrapVO.setMemberId(memberVO.getMemberId());
        scrapVO.setPhotoBoardNo(photoBoardNo);

        photoBoardMapper.scrap(scrapVO);

        photoBoardMapper.scrapCount(photoBoardNo);

    }
    //스크랩취소
    @RequestMapping("/nonscrap")
    @ResponseBody
    private void cancelscrap(ScrapVO scrapVO, HttpSession session, @RequestParam("photoBoardNo") int photoBoardNo) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        scrapVO.setMemberId(memberVO.getMemberId());
        scrapVO.setPhotoBoardNo(photoBoardNo);

        photoBoardMapper.cancelScrap(scrapVO);

        photoBoardMapper.scrapCountSub(photoBoardNo);

    }
    //댓글작성
    @RequestMapping("/insertPhotoComment")
    @ResponseBody
    private void insertComment(@RequestParam("commentContent") String commentContent, CommentVO commentVO, HttpSession session, @RequestParam("photoBoardNo") int photoBoardNo) throws Exception{

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");

        commentVO.setMemberId(memberVO.getMemberId());
        commentVO.setCommentContent(commentContent);
        commentVO.setPhotoBoardNo(photoBoardNo);

        photoBoardMapper.insertPhotoComment(commentVO);

    }
    //무한스크롤
    @GetMapping("/photoScrollDown")
    @ResponseBody
    public Map<String, List<PhotoBoardVO>> photoScrollDown(String idx){

        System.out.println("photo Scroll Down idx : " + idx);

        int index = Integer.parseInt(idx) * 12;

        Map<String, List<PhotoBoardVO>> scrollList=new HashMap<String, List<PhotoBoardVO>>();
        scrollList.put("photoList", photoBoardMapper.selectPhotoList(index));

        return scrollList;
    }

}
