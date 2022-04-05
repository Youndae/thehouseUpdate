package com.bit.house.service;

import com.bit.house.domain.PhotoBoardVO;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public interface PhotoBoardService {

    void photoUpdateProc(PhotoBoardVO photoBoardVO, MultipartHttpServletRequest mtf, HttpServletRequest request) throws Exception;
}
