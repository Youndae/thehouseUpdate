package com.bit.house.service;

import com.bit.house.domain.MemberVO;
import com.bit.house.mapper.MyPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

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

    @Override
    public void profilemodify(MultipartHttpServletRequest mreq, HttpServletRequest request, MemberVO memberVO) throws Exception {

        System.out.println("///////////////////////////service//////////////////////");

        if(mreq != null){
            StringBuffer sb = new StringBuffer();

            MultipartFile mf = mreq.getFile("uploadFile");

            String oldName = mf.getOriginalFilename();
            String saveName = sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                    .append(UUID.randomUUID().toString())
                    .append(oldName.substring(oldName.lastIndexOf("."))).toString();

            String filePath = request.getSession().getServletContext().getRealPath("image/profileImg/");
            File dest = new File(filePath+saveName);
            mf.transferTo(dest);


            memberVO.setMemberImg("/profileImg/"+saveName);


        }


    }


}
