package com.bit.house.controller;

import com.bit.house.domain.AllMemberVO;
import com.bit.house.domain.BasketVO;
import com.bit.house.domain.MemberVO;
import com.bit.house.domain.ProductVO;
import com.bit.house.mapper.AdminMapper;
import com.bit.house.mapper.BasketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class BasketController {
    private HttpServletRequest request;


    @Autowired
    AdminMapper adminMapper;

    @Autowired
    BasketMapper basketMapper;



    @GetMapping("/goBasket")
    public String cart(String MemberId, Model model,AllMemberVO allMemberVO, BasketVO basketVO) {
        System.out.println("goBasket!");
        List<AllMemberVO> allMemberVOList = adminMapper.getMember();
        System.out.println(allMemberVOList);
        model.addAttribute("allMemberVOList", allMemberVOList);
        return "nonMemberBasket";
    }

    @GetMapping("/basketPop")
    public String basketPop(HttpServletRequest request) {
        return "th/member/basket/basketPop";
    }

    @RequestMapping(value = "/basketMember", method = RequestMethod.POST)
    public @ResponseBody void basketMem(BasketVO basketVO, String memberId,String productNo, String productColor, String qty){
        System.out.println("id : " + memberId + "pNo : "+productNo + "productColor : " + productColor
        + "Qty : " + qty);
        System.out.println("parse" +
                Integer.parseInt(qty)
        );
        basketVO.setMemberId(memberId);
        basketVO.setProductNo(productNo);
        basketVO.setProductColor(productColor);
        basketVO.setQty(Integer.parseInt(qty));
        basketMapper.insertBasketMember(basketVO);

    }

    @RequestMapping(value = "/basketLocal", method = RequestMethod.POST)
    public @ResponseBody void basketLocal(String[] hoho,HttpSession session, String first) {
        System.out.println("호호 출력"+hoho.getClass()+ "호호길이 : ");
        String[] ab = new String[100];
        int i=0;
        String check = "첫번째";
        System.out.println(hoho.length+"&&");
        System.out.println(first+"입니다"+check+"?"+first.equals(check));

        if(first.equals(check)==true){
            System.out.println("firest는 참입니다");
            List<String> proNo = new ArrayList<>();
            List<String> proColor = new ArrayList<>();
            List<Integer> proQty = new ArrayList<>();
            for (String a : hoho) {

                ab[i] = a;
                i++;
                System.out.println(a);
            }
            proNo.add(ab[0]);
            proColor.add(ab[1]);
            proQty.add(Integer.parseInt(ab[2]));
            session.setAttribute("proNo", proNo);
            session.setAttribute("proColor", proColor);
            session.setAttribute("proQty", proQty);
        }else {
            for (String a : hoho) {

                ab[i] = a;
                i++;
                System.out.println(a);
            }
            List<String> proNo = new ArrayList<>();
            List<String> proColor = new ArrayList<>();
            List<Integer> proQty = new ArrayList<>();
            int j;
            for (j = 0; j < i; j++) {
                System.out.println("ab?:" + ab[j]);
                proNo.add(ab[j].split(",")[0]);
                proColor.add(ab[j].split(",")[1]);
                proQty.add(Integer.parseInt(ab[j].split(",")[2]));
            }
            for (int b = 0; b < j; b++) {
                System.out.println(proNo.get(b));
                System.out.println(proColor.get(b));
                System.out.println(proQty.get(b));
            }
            session.setAttribute("proNo", proNo);
            session.setAttribute("proColor", proColor);
            session.setAttribute("proQty", proQty);
        }

    }

    @GetMapping("/basket")
    public String gobasket(HttpSession session,ProductVO productVO,BasketVO basketVO,Model model){

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        String memberId = "";
        if(memberVO != null) {
            memberId = memberVO.getMemberId();
            System.out.println("memID : " + memberId);
        }
        if(memberId == "") { // 장바구니 클릭시 세션에 저장하고 클라이언트에 저장하기 때문에 그 외의 값은 들어가지 않는다
            List<String> hohoSession1 = new ArrayList<>();
            List<String> hohoSession2 = new ArrayList<>();
            List<Integer> hohoSession3 = new ArrayList<>();

            hohoSession1 = (List<String>) session.getAttribute("proNo");
            hohoSession2 = (List<String>) session.getAttribute("proColor");
            hohoSession3 = (List<Integer>) session.getAttribute("proQty");

            if (hohoSession2 == null) {
                System.out.println("값이 없다");

                return "th/member/basket/basketNull";
            } else {
                //if(userId == null){
                System.out.println("값이 있다");
                //System.out.println(hohoSession1+hohoSession2);
                List<BasketVO> basketVOList = basketMapper.getNonMemberBasketList(hohoSession1,hohoSession2);
                System.out.println("사이즈 " + basketVOList.size());
                for(int i=0; i<basketVOList.size(); i++) {
                    basketVOList.get(i).setQty(hohoSession3.get(i));
                    System.out.println("리스트 i 의 qty입니다"+basketVOList.get(i).getQty());
                }
                System.out.println("리스트:"+basketVOList);
                model.addAttribute("basketList", basketVOList);
                return "th/member/basket/nonMemberBasket";
                //}else{ model.add~ ~~ ~~  return "th/member/basket/basketMember";}
            }
        }else{ // 회원
            System.out.println("회원입니다");
            List<BasketVO> basketMember = basketMapper.getMemberBasketList(memberId);
            if(basketMember != null) {
                System.out.println("basketMem : " + basketMember);
                model.addAttribute("basketList2", basketMember);
                return "th/member/basket/memberBasket";
            }else{
                return "th/member/basket/basketNull";
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delNonmemberBasket")
    public @ResponseBody void delNonmemberBasket(String bool,HttpSession session){
        System.out.println("bool : " + bool);
        if(bool != null){
            session.removeAttribute("proNo");
            session.removeAttribute("proColor");
            session.removeAttribute("proQty");
        }
    }
    @RequestMapping(method = RequestMethod.POST, value = "/delMemberBasket")
    public @ResponseBody void delMemberBasket(String[] productNo, String[] productColor,HttpSession session){

        String memberId = null;

        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        List<BasketVO> basketVOList = new ArrayList<>(productNo.length);


        if(memberVO != null){
            memberId = memberVO.getMemberId();
            for(int i=0; i<productNo.length;i++){
                System.out.println("productNo : " + productNo[i] + "productColor : " + productColor[i]);
                BasketVO basketVO = new BasketVO(memberId,productNo[i],productColor[i]);
                basketVOList.add(basketVO);

            }
        }
        System.out.println("basketList :: " +basketVOList);

        basketMapper.deletememberBasket(basketVOList);
    }



}
