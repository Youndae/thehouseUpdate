package com.bit.house.controller.kakao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bit.house.domain.*;
import com.bit.house.domain.kakao.KakaoPayApprovalVO;
import com.bit.house.mapper.AdminMapper;
import org.nd4j.nativeblas.Nd4jCpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import lombok.Setter;
import lombok.extern.java.Log;

@Log
@Controller
public class KakaoPayController {
    @Autowired(required = false)
    AdminMapper adminMapper;
    
	@Autowired(required = false)
    KakaoPayApprovalVO kakaoInfo;
	
	/*@Autowired
	HouseUser houseUser;

	@Autowired
	UserDAO userDAO;*/
	
    @Setter(onMethod_ = @Autowired)
    private KakaoPay kakaopay;


    @GetMapping("/kakaoPay")
    public void kakaoPayGet(Model model) {
    	log.info("KakaoPayGet 호출............................................");
    	log.info("PayGet에서 호출되는지 ? : ");
    	/*model.addAttribute("housePayment", userDAO.getPayment());
		model.addAttribute("houseProduct", userDAO.getProduct());
		log.info("model payment : "+ userDAO.getPayment());*/
    }
    /*@ModelAttribute("houseProduct") HouseProduct houseProduct,@ModelAttribute("housePayment") HousePayment housePayment*/
    // 서버에서 값을 보내주면 @ModelAttribute 써서 받아와서 씀
    // form ~post 
    // <div> 안에 <a href> button false js에서 선택한게 있으면 submit 아니면 되돌아가기
    @PostMapping("/kakaoPay")
    public String kakaoPay(HttpSession session, String[] productNo,
                           int[] orderQty, String[] colorName,String[] productName,
                           int[] totalPrice,String recipient,String receivedAt, String receivedAtDetail) { // 리스트로?

        //log.info(productNo.length + " " + orderQty.length+"" + ""+totalPrice.length + "@#@@##@");

        log.info("kakaoPay post 호출............................................");
        int totalP = 0;
        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        String memberId;
        int orderQ = 0;
        List<OrderListVO> orderListVOList;
        if(productNo != null){
        orderListVOList = new ArrayList<>(productNo.length);
        }else {
           orderListVOList = new ArrayList<>(1);
        }
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



if(orderQty != null){
        for(int i=0; i<orderQty.length;i++){
            orderQ += orderQty[i];
        }
}



        if(memberVO != null){
            memberId = memberVO.getMemberId();
        }else{
            memberId="unknown";
        }
if(productNo != null) {
    for (int i = 0; i < productNo.length; i++) {

        if (0 <= result2 && result2 < 10) {
            No = "000" + Integer.toString(result2 + i + 1);
        } else if (10 <= result2 && result2 < 100) {
            No = "00" + Integer.toString(result2 + i + 1);
        } else if (100 <= result2 && result2 < 1000) {
            No = "0" + Integer.toString(result2 + i + 1);
        } else if (1000 <= result2 && result2 < 10000) {
            No = "" + Integer.toString(result2 + i + 1);
        }

        System.out.println(memberId + "" + colorName[i]);

        OrderListVO orderListVO = new OrderListVO();

        if(productNo.length == 1){
            orderListVO.setOrderQty(1);
            orderListVO.setTotalPrice(1);
        }
        System.out.println(orderListVO.getOrderQty() + "토프 : " + orderListVO.getTotalPrice());

        orderListVO.setOrderNo(today + "-" + No);
        orderListVO.setMemberId(memberId);
        orderListVO.setColorName(colorName[i]);
        //orderListVO.setModelName(productName[i]);
        orderListVO.setProductNo(productNo[i]);
        orderListVO.setProductName(productName[i]);
        orderListVO.setOrderQty(orderQty[i]);
        orderListVO.setTotalPrice(totalPrice[i]);
        orderListVO.setRecipient(recipient);
        orderListVO.setOrderAddr(receivedAt + receivedAtDetail);
        orderListVO.setOrderDate(sqlToday);
        orderListVO.setPayCode(1);
        orderListVO.setOrderCode(10);
        System.out.println("set전에" + orderListVO + orderListVOList);
        orderListVOList.add(i, orderListVO);
        System.out.println("after orderListVOList : " + orderListVOList);
        totalP += totalPrice[i];
        System.out.println(i + "번쨰 : " + productNo[i]);
    }
}

        session.setAttribute("orderListVOList",orderListVOList);

        return "redirect:" + kakaopay.kakaoPayReady(orderListVOList,memberId);
 
    }
    //List<OrderListVO> orderListVO = new ArrayList<OrderListVO>();


    /*, HouseProduct houseProduct, List<HouseUser> houseUser*/
    @GetMapping("/kakaoPaySuccess")
    public String kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model,HttpSession session) {
        log.info("kakaoPaySuccess get 호출............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token);
        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        System.out.println("is null?"+memberVO);
        String memberId;

        List<OrderListVO> orderListVOList = (List<OrderListVO>) session.getAttribute("orderListVOList");


        if(memberVO != null){
            //memberId = memberVO.getMemberId();
            adminMapper.insertMemberOrderList(orderListVOList);
            memberId = orderListVOList.get(0).getMemberId();
            System.out.println(memberId);
        }else{
            adminMapper.insertNonMemberOrderList(orderListVOList);
            memberId = "unknown";
            System.out.println("비회원"+memberId);
        }


        kakaoInfo = kakaopay.kakaoPayInfo(pg_token, memberId);

        //이렇게 하는 이유
        log.info("kakaoInfo : " + kakaoInfo);
        System.out.println("//////////");
        System.out.println("kakaoInfo : " + kakaoInfo);
        model.addAttribute("kakaoInfo", kakaoInfo);

        if(memberVO == null){
            session.removeAttribute("proNo");
            session.removeAttribute("proColor");
            session.removeAttribute("proQty");
        }
        session.removeAttribute("orderListVOList");

        //session.getAttribute(pg_token);

        // 원래 있던거 model.addAttribute("info", kakaopay.kakaoPayInfo(pg_token, pg_token));
        
        //수정 return "redirect:/myPage";
        if(memberVO != null){
        return  "redirect:/member/order_list";
        }else{
            return "redirect:/";
        }
    }
    
}