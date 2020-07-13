package com.bit.house.service;


import com.bit.house.domain.MemberVO;
import com.bit.house.mapper.PhotoBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
public class PhotoBoardServiceImpl implements PhotoBoardService{

    @Autowired(required = false)
    PhotoBoardMapper photoBoardMapper;

    Model model;

    @Override
    public void detailPhoto(MemberVO memberVO, int photoBoardNo) throws Exception {

        String mem = memberVO.getMemberId();

        System.out.println("service No : "+photoBoardNo);

        if(mem != null){

            /*model.addAttribute("member", photoBoardMapper.myProfileImg(memberVO));
            model.addAttribute("likestat", photoBoardMapper.likeStat(memberVO, photoBoardNo));
            model.addAttribute("scrapstat", photoBoardMapper.scrapStat(memberVO, photoBoardNo));*/
        }else{

        }

    }
}
