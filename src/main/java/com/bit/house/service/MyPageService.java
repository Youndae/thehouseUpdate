package com.bit.house.service;


import org.springframework.stereotype.Service;

import java.util.List;


public interface MyPageService {
    void deleteNote(List<String> msgNum) throws Exception;
}
