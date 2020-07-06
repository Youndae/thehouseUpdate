package com.bit.house.controller;

import com.bit.house.domain.*;
import com.bit.house.mapper.AdminMapper;
import com.bit.house.mapper.BasketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
public class PaymentController {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    BasketMapper basketMapper;


    @GetMapping("/goPayment")
    public String memberPayment(HttpSession session, String productNo,Model model,String colorN, String directPayment){
        String memberId = "";
        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        if(directPayment != null) {
            System.out.println("다이렉트 : " + directPayment + directPayment.getClass() + "is true" + directPayment.equals(null));
        }
        if(colorN != null) {
            System.out.println("컬러 : " + colorN.equals("") + colorN.getClass());
        }
        if(colorN == ""){
            return "redirect:/";
        }
        if(memberVO != null){
            if(directPayment == null) {
                memberId = memberVO.getMemberId();
                log.info(memberId);
                List<BasketVO> basketMember = basketMapper.getMemberBasketList(memberId);
                System.out.println("qty :: " + basketMember);
                //session.setAttribute("basketMember", basketMember);
                model.addAttribute("memberBasketList", basketMember);
                return "th/member/payment/memberPayment";
            }else {
                System.out.println("directPayment는 !Null입니다 ");
                System.out.println(productNo+colorN);
                ProductVO productVO = adminMapper.getDirectPayment(productNo,colorN);
                System.out.println("productVO는? : " + productVO);
                model.addAttribute("productVO",productVO);
                return "th/member/payment/memberPayment";
            }
        }else{
            if(directPayment == null) {
                List<String> proNoSession1;
                List<String> proColorSession2;
                List<Integer> proQtySession3;

                //localSessionStorage
                proNoSession1 = (List<String>) session.getAttribute("proNo");
                proColorSession2 = (List<String>) session.getAttribute("proColor");
                proQtySession3 = (List<Integer>) session.getAttribute("proQty");

                List<BasketVO> basketVOList = basketMapper.getNonMemberBasketList(proNoSession1, proColorSession2);
                for (int i = 0; i < basketVOList.size(); i++) {
                    basketVOList.get(i).setQty(proQtySession3.get(i));
                    System.out.println("리스트 i 의 qty입니다" + basketVOList.get(i).getQty());
                }
                System.out.println("리스트:" + basketVOList);
                session.setAttribute("nonMemberBasketVOList", basketVOList);
                model.addAttribute("nonMemberBasketList", basketVOList);
                return "th/member/payment/nonMemberPayment";
            }else {
                System.out.println("directPayment는 !Null입니다 ");
                System.out.println(productNo+colorN);
                ProductVO productVO = adminMapper.getDirectPayment(productNo,colorN);
                System.out.println("productVO는? : " + productVO);
                model.addAttribute("productVO",productVO);
                return "th/member/payment/nonMemberPayment";
            }
        }
    }

    @PostMapping("/directPayment")
    public String directPayment(String productNo,Model model,String productColor){
        System.out.println("TEst ::" + productNo + "productColor : " +productColor);
        model.addAttribute("test", productNo);
        return "th/member/payment/directPayment";
    }




    @GetMapping("/nonMemberPayment")
    public String nonMemberPayment(){

        /*

        List<String> hohoSession2 = new ArrayList<>();
        System.out.println("getAttribute");
        hohoSession2 = (List<String>) session.getAttribute("hoho3");
        System.out.println("호호세션 : "+hohoSession2);
        basketMapper.getNonMemberBasketList(hohoSession2);
        List<BasketVO> basketVOList = basketMapper.getNonMemberBasketList(hohoSession2); // 아무튼 상품정보 가져옴
        model~
 */

        return "th/member/payment/nonMemberPayment";
    }

    @GetMapping("/paymentSuccess")
    public String paymentSuccess(){
        return "th/member/payment/paymentSuccess";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/paymentCom")
    public @ResponseBody void paymentComplete(OrderListVO orderListVO, NonMemberVO nonMemberVO,HttpSession session){
        java.sql.Date sqlToday = new java.sql.Date(System.currentTimeMillis());
        System.out.println("오늘 : ? "+sqlToday);


        SimpleDateFormat orderDateForm, orderNoForm;
        orderDateForm = new SimpleDateFormat("yyyyMMdd");
        String today = orderDateForm.format(sqlToday);

        System.out.println(today + "그리고"+sqlToday);

        String orderCount = adminMapper.getOrderNo(today);
        String result = null;
        if(orderCount == null){
            result = "0000";
        }else {
            result = orderCount.substring(orderCount.length() - 4, orderCount.length());
        }

        System.out.println("parse전 : " +result);
        int result2 = Integer.parseInt(result);
        System.out.println("parse 후 :" + result);
        String No= null;
        if(0<=result2 && result2<10){
            No="000"+Integer.toString(result2+1);
        }else if(10<=result2 && result2<100){
            No="00"+Integer.toString(result2+1);
        }else if(100<=result2 && result2<1000){
            No="0"+Integer.toString(result2+1);
        }else  if(1000<=result2 && result2<10000){
            No=""+Integer.toString(result2+1);
        }
        orderListVO.setOrderNo(today+'-'+No);
        nonMemberVO.setOrderNo(today+'-'+No);
        log.info("paymentCom 호출@@@@");
        System.out.println("@@@@@@@@@@@@@@@@@@@@ orderListVO  :: "+orderListVO+"nonmembervO: "+ nonMemberVO);
        String memberId = "";
        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        if(memberVO != null) {
            adminMapper.insertInicis(orderListVO);
        }else {
            adminMapper.insertInicis(orderListVO);
            adminMapper.insertNonMemTable(nonMemberVO);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveAddr")
    public @ResponseBody String saveAddr(String fullAddr,HttpSession session ){
        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        String memberId = memberVO.getMemberId();
        System.out.println("saveAddr 실행");
        adminMapper.updateMemberAddr(fullAddr,memberId);
        return "ss";
    }
}
