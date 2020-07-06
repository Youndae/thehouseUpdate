package com.bit.house.mapper;

import com.bit.house.domain.BasketVO;
import com.bit.house.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BasketMapper {
    public List<BasketVO> getNonMemberBasketList(List<String> list,List<String> list2);

    public List<BasketVO> getArray(String[] hoho2);

    public BasketVO getBasket(String userId);

    public void insertBasketMember(BasketVO basketVO);

    public List<BasketVO> getMemberBasketList(String memberId);

    public void deletememberBasket(List<BasketVO> basketVOList);




}
