package com.bit.house.controller;

import com.bit.house.domain.MemberVO;
import com.bit.house.domain.OrderListVO;
import com.bit.house.domain.ProductOptionVO;
import com.bit.house.domain.ProductVO;
import com.bit.house.mapper.AdminMapper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/admin")
public class StatAdminController {

    @Autowired
    AdminMapper adminMapper;

       //statAdmintPage
    @GetMapping("/statAdmin")
    public String statAdmin() {
        return "th/admin/statAdmin/statAdmin";
    }

    @GetMapping("/productSalesVolume")
    public String graph(Model model) {
        //판매량, 품목
        String sellerName = "가쯔";
        List<OrderListVO> yearlySellerSalesVolume = adminMapper.getYearlySellerSalesVolume(sellerName);

        log.info("yearlySellerSalesVolume :: " +yearlySellerSalesVolume);
        //List<ProductVO> houseProductList = adminMapper.getProduct();
        //List<ProductVO> salesVol = adminMapper.getSalesVolume();
        //log.info("salesVol = "+salesVol);
        List<OrderListVO> spendingPattern = adminMapper.getSpendingPattern();
        List<String> yearList = adminMapper.getYear();
        List<OrderListVO> totalPrice = adminMapper.getTotalPrice();
        log.info("totalPrice : "+ totalPrice.toString());
        log.info("productOptionVOList : " +spendingPattern.toString());
        log.info("안녕하세요 yearList입니다" + yearList);
        ObjectMapper mapper = new ObjectMapper();
        String jsonText;
        String yearlyJson;

        try {
            jsonText = mapper.writeValueAsString(spendingPattern);
            yearlyJson = mapper.writeValueAsString(yearlySellerSalesVolume);

            model.addAttribute("yearlyJson", yearlyJson);
            model.addAttribute("sellerName", sellerName );

            model.addAttribute("jsonText", jsonText);
            model.addAttribute("yearList", yearList);
            log.info(yearlyJson);
            System.out.println("jsonText는 : ? : " + jsonText);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("houseProductList : ? " + productOptionVOList);
        return "th/admin/statAdmin/productSalesVolume";
    }





    @RequestMapping(value = "/yearGraphAjax", method = RequestMethod.POST)
    public @ResponseBody Object yearAjaxGraph(String product, String year,String sellerName) {
        //List<OrderListVO> spendingPattern = adminMapper.getMonthData(year, product);

        //ObjectMapper mapper = new ObjectMapper();
        //String jsonText;
        //jsonText = mapper.writeValueAsString(house통계 );
        // return jsonText  Map으로 보낼지 어떻게 보낼지
        //ArrayList<String> graph2ArrList = new ArrayList<String>();
        System.out.println("ajax ===> productNo : "+ product + " year ====> " + year + "sellerName" + sellerName);
        //Service.getSelectedYear(year);
        List<OrderListVO> monthlySellerSalesVolume = adminMapper.getMonthlySellerSalesVolume(sellerName, year);
        log.info("mothly Sale sadmkldmslkdnsanqlTqdwqwd : "+monthlySellerSalesVolume);

        return monthlySellerSalesVolume;
    }

    @RequestMapping(value = "/monthGraphAjax", method = RequestMethod.POST)
    public @ResponseBody Object monthAjaxGraph( String month, String sellerName, String year) {
        //List<OrderListVO> spendingPattern = adminMapper.getDayData(year,month, product);
        List<OrderListVO> dailySellerSalesVolume = adminMapper.getDailySellerSalesVolume(sellerName, year, month);
        ArrayList<String> graph2ArrList = new ArrayList<String>();
        System.out.println("ajax ===> sellerName : "+ sellerName + " year ====> " + year + " month : " + month);

        return dailySellerSalesVolume;
    }


    
    
    
    //-----------------------------------------statAdmin Page
    
    //회사명 받아오기 resp
    @RequestMapping(value = "/sellerGraphAjax", method = RequestMethod.POST)
    public @ResponseBody Object sellerGraphAjax(String inputSellerName,Model model){
        adminMapper.getYearlySellerSalesVolume(inputSellerName);
        log.info("sellerAjax 호출" + inputSellerName);

        List<OrderListVO> yearlySellerSalesVolume = adminMapper.getYearlySellerSalesVolume(inputSellerName);

        ObjectMapper mapper = new ObjectMapper();
        String jsonText;

        try {
            jsonText = mapper.writeValueAsString(yearlySellerSalesVolume);

            model.addAttribute("jsonText", jsonText);
            System.out.println("jsonText는 : ? : " + jsonText);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //회사 이름으로 찾기
        //adminMapper.getSellerStat(sellerName);

        //전체 판매량 순으로 정렬
        //adminMapper.getSpendingPattern();
        return  inputSellerName;
    }

    @GetMapping("/searchCom")
    public String searchCom(String inputSellerName,Model model,String sellerName){
        log.info("sellerAjax 호출" + sellerName);

        List<OrderListVO> yearlySellerSalesVolume = adminMapper.getYearlySellerSalesVolume(sellerName);

        ObjectMapper mapper = new ObjectMapper();
        String jsonText;

        try {
            jsonText = mapper.writeValueAsString(yearlySellerSalesVolume);

            model.addAttribute("yearlyJson", jsonText);
            model.addAttribute("sellerName",sellerName);
            System.out.println("jsonText는 : ? : " + jsonText);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //회사 이름으로 찾기
        //adminMapper.getSellerStat(sellerName);

        //전체 판매량 순으로 정렬
        //adminMapper.getSpendingPattern();
        return "th/admin/statAdmin/productSalesVolume";
    }
    
    //회원 소비패턴
    @GetMapping("/spendingPattern")
    public String spendingPattern(Model model){
        // 가장 많이 구매한 회원 목록 (당일)
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String time1 = format1.format(time);
        System.out.println(time1);

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

        Date currentTime = Calendar.getInstance().getTime();
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
        System.out.println(year + "/" + month + "/" + day );


        //List<ProductVO> houseProductList = adminMapper.getProduct();
        //List<ProductVO> salesVol = adminMapper.getSalesVolume();
        //log.info("salesVol = "+salesVol);
        
        //log.info("spendingPattern : "+spendingPattern.toString());

        List<OrderListVO> userPurchaseVolume = adminMapper.getUserPurchaseVolume();
        log.info("userPurchaseVolume : "+userPurchaseVolume.toString());

        //List<String> yearList = adminMapper.getYear();
        //List<OrderListVO> totalPrice = adminMapper.getTotalPrice();
        //log.info("totalPrice : "+ totalPrice.toString());
        //log.info("productOptionVOList : " +spendingPattern.toString());
        //log.info("안녕하세요 yearList입니다" + yearList);

        ObjectMapper mapper = new ObjectMapper();
        String jsonText;

        try {
            jsonText = mapper.writeValueAsString(userPurchaseVolume);
            model.addAttribute("jsonText", jsonText);
            System.out.println("jsonText는 : ? : " + jsonText);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("houseProductList : ? " + productOptionVOList);


        return "th/admin/statAdmin/spendingPattern";
    }
    
    //회원정보
    @GetMapping("/memberInfo")
    public String memberInfo(Model model , MemberVO memberVO){
        List<MemberVO> memberVOList = (List<MemberVO>) adminMapper.getUserInfo();
        log.info("memberVO : " + memberVOList.toString());
        model.addAttribute("userInfo" , memberVOList);

        //이름, 아이디, 주소, 이메일
        // memberName, memberId, memberAddr,memberEmail

        //  주문량, 주문 총액
        // sum(orderQty2), sum(totalPrice2)

        return "th/admin/statAdmin/memberInfo";
    }
    
    //회원 주문내역
    @GetMapping("/memberOrderList")
    public String memberOrderList(Model model, OrderListVO orderListVO){
        List<OrderListVO> orderListVOList = adminMapper.getOrderList();
        log.info("orderListVOList : " + orderListVOList.toString());
        model.addAttribute("orderList" , orderListVOList);

        return "th/admin/statAdmin/memberOrderList";
    }

    //------------------------------ 제품 관리



    @RequestMapping(value = "/userDateStat", method = RequestMethod.POST)
    public @ResponseBody Object userDateStat(Model model, String date1, String date2) {
        List<OrderListVO> userDateStat = adminMapper.getUserDateStat(date1, date2);
        log.info("info : " + userDateStat);

        ObjectMapper mapper = new ObjectMapper();
        String jsonText;

        try {
            jsonText = mapper.writeValueAsString(userDateStat);
            System.out.println("jsonText는 : ? : " + jsonText);
            return jsonText;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("houseProductList : ? " + productOptionVOList);

        return "";
    }

    @GetMapping("/productManagement")
    public String productManagement(Model model){
        List<ProductVO> productVOList = (List<ProductVO>) adminMapper.getProductManagement();
        model.addAttribute("productVOList", productVOList);
        return "th/admin/statAdmin/productManagement";
    }

    @GetMapping("/popularityProduct")
    public String popularityProduct(Model model){
        List<OrderListVO> orderListVOList = (List<OrderListVO>) adminMapper.getPopularity();
        model.addAttribute("orderListVOList", orderListVOList);
        return "th/admin/statAdmin/popularityProduct";
    }

}
