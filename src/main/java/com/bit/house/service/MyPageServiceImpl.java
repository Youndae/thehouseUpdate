package com.bit.house.service;

import com.bit.house.mapper.MyPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPageServiceImpl implements MyPageService{

    @Autowired(required = false)
    MyPageMapper myPageMapper;

    @Override
    public void deleteNote(List<String> msgNum) throws Exception {
        System.out.println("service Num : "+msgNum);
        for(String msgN:msgNum){
            int msgNo=Integer.parseInt(msgN);
            System.out.println("Service : "+msgNo);
            myPageMapper.deleteNote(msgNo);
        }
    }


}
