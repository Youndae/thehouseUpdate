package com.bit.house.service;

import com.bit.house.domain.PhotoBoardVO;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface PhotoBoardService {

    void photoUpdateProc(PhotoBoardVO photoBoardVO, MultipartHttpServletRequest mtf, HttpServletRequest request) throws Exception;

    void photoInsertProc(PhotoBoardVO photoBoardVO, MultipartHttpServletRequest mtf, HttpServletRequest request, HttpSession session) throws Exception;

    void deletePhotoProc(int photoBoardNo, int photoCount, HttpServletRequest request) throws Exception;
}
