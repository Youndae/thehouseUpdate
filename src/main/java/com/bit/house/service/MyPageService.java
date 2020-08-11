package com.bit.house.service;


import com.bit.house.domain.MemberVO;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface MyPageService {
    void deleteNote(List<String> msgNum) throws Exception;
    void profilemodify(MultipartHttpServletRequest mreq, HttpServletRequest request, MemberVO memberVO) throws Exception;
}
