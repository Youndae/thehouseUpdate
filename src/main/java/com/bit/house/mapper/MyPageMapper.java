package com.bit.house.mapper;

import com.bit.house.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyPageMapper {

    //프로필설정
    public MemberVO selectProfile(String memberId) throws Exception;

    //프로필수정
    public void modifyProfile(MemberVO memberVO) throws Exception;

    //팔로워
    public List<FollowVO> follower(String memberId) throws Exception;

    //팔로잉
    public List<FollowVO> following(String memberId) throws Exception;

    //팔로우
    public void follow(FollowVO followVO) throws Exception;

    //팔로우 취소
    public void cancelFollow(FollowVO followVO) throws Exception;

    //내 게시물
    public MemberVO myProfile(String memberId) throws Exception;

    public List<PhotoBoardVO> profilePhoto(String memberId) throws Exception;

    public List<ScrapVO> profileScrap(String memberId) throws Exception;

    //사진 게시글 전체보기
    public List<PhotoBoardVO> allPhoto(String memberId) throws Exception;

    //스크랩 전체보기
    public List<ScrapVO> allScrap(String memberId) throws Exception;

    //보낸쪽지함
    public List<MsgVO> sendNote(String memberId) throws Exception;

    //받은쪽지함
    public List<MsgVO> receiveNote(String memberId) throws Exception;

    //쪽지 보내기
    public void noteSending(MsgVO msgVO) throws Exception;

    //쪽지 삭제
    public int deleteNote(int msgNo) throws Exception;

    //팔로워 카운트
    int followCount(String memberId) throws Exception;

    //팔로잉 카운트
    int followingCount(String memberId) throws Exception;

    //사진 게시글 카운트
    int photoCount(String memberId) throws Exception;

    //스크랩 카운트
    int scrapCount(String memberId) throws Exception;

    //팔로우한 상태인지 확인을 위한 count
    int followerCount(String memberId, String followId) throws Exception;

}
