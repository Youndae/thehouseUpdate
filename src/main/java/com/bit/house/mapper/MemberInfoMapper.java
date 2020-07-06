package com.bit.house.mapper;

import com.bit.house.domain.AllMemberVO;
import com.bit.house.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberInfoMapper {

    @Update("update member set memberName=#{memberName}, memberEmail=#{memberEmail}, memberTel=#{memberTel}, memberAddr=#{memberAddr}, nickName=#{nickName} where memberId=#{memberId}")
    void updateInfoMemberById(MemberVO memberVO);

    @Update("update allmember set userpw=#{userpw} where userId=#{memberId}")
    void updateMemberPassword(MemberVO memberVO);
}
