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

        /*
            기존 문제점.
                이미지 변경을 하지 않았을때 오류 발생.
                선택되어 있는 이미지가 없는데 그걸 구분해 처리할 코드가 제대로 작성되어 있지 않았기 때문.
                기존에 수정하려 했을때는 단순하게 MultipartHttpServletRequest가 null이면 이미지 업로드를 하지 않는 방법을 택했는데
                애초에 이미지를 새로 등록하지 않아도 null이 아니었기 때문에 조건문에서 구분할 수가 없었음.

            해결방안
                MultipartHttpServletRequest.getFile().getSize로 크기를 확인할 수 있음을 찾았음.
                당연히 이미지를 새로 등록하지 않는다면 size는 0으로 출력.
                조건문에 != 0 으로 구분을 함으로써 이미지 변경 없이 수정하더라도 정상 작동하는것을 확인.

            기존 코드는 아래 주석으로 보관.
         */
        MultipartFile mf = mreq.getFile("uploadFile");
        memberVO.setNickName(request.getParameter("nickName"));
        memberVO.setMemberIntro(request.getParameter("memberIntro"));

        if(mf.getSize() != 0){
            StringBuffer sb = new StringBuffer();

            String oldName = mf.getOriginalFilename();

            String saveName = sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                    .append(UUID.randomUUID().toString())
                    .append(oldName.substring(oldName.lastIndexOf("."))).toString();

            String filePath = request.getSession().getServletContext().getRealPath("image/profileImg/");
            File dest = new File(filePath + saveName);
            mf.transferTo(dest);

            memberVO.setMemberImg("/profileImg/" + saveName);
        }

        myPageMapper.modifyProfile(memberVO);


        /*if(mreq != null){
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


        }*/


    }


}
