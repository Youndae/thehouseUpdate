package com.bit.house.mapper;

import com.bit.house.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotoBoardMapper {
    //사진게시판 메인
    public List<PhotoBoardVO> communityMain() throws Exception;

    //사진게시판 리스트
    public List<PhotoBoardVO> photoBoardList() throws Exception;

    //사진게시판 업로드
    public void insertPhoto(PhotoBoardVO photoBoardVO) throws Exception;

    //사진게시판 수정
    public void updatePhoto(PhotoBoardVO photoBoardVO) throws Exception;

    //사진게시판 삭제
    public int deletePhoto(int photoBoardNo) throws Exception;

    //사진게시판 상세
    public PhotoBoardVO photoDetail(int photoBoardNo) throws Exception;

    //사진게시판 사용자 게시글
    public List<PhotoBoardVO> userPhoto (String memberId) throws Exception;

    //insert area 코드
    public List<PhotoBoardVO> area() throws Exception;

    //insert house 코드
    public List<PhotoBoardVO> house() throws Exception;

    //insert place 코드
    public List<PhotoBoardVO> place() throws Exception;

    //insert style 코드
    public List<PhotoBoardVO> style() throws Exception;

    //좋아요
    public void like(LikeVO likeVO) throws Exception;

    public void cancelLike(LikeVO likeVO) throws Exception;

    public void likeCount(int photoBoardNo) throws Exception;

    public void likeCountSub(int photoBoardNo) throws Exception;

    //좋아요 상태
    int likeStat(String memberId, int photoBoardNo) throws Exception;

    //스크랩
    public void scrap(ScrapVO scrapVO) throws Exception;

    public void cancelScrap(ScrapVO scrapVO) throws Exception;

    public void scrapCount(int photoBoardNo) throws Exception;

    public void scrapCountSub(int photoBoardNo) throws Exception;

    //스크랩 상태
    int scrapStat(String memberId, int photoBoardNo) throws Exception;

    //댓글
    public List<CommentVO> photoComment(int photoBoardNo) throws Exception;

    //댓글 작성
    public void insertPhotoComment(CommentVO commentVO) throws Exception;

    int commentCount(int photoBoardNo) throws Exception;

    //로그인한 사용자 프로필이미지
    public MemberVO myProfileImg(String memberId) throws Exception;

    //사진 게시글 리스트(무한스크롤)
    List<PhotoBoardVO> selectPhotoList();
}
