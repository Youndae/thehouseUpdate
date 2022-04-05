package com.bit.house.controller.kakao;
 
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.bit.house.domain.BasketVO;
import com.bit.house.domain.MemberVO;
import com.bit.house.domain.OrderListVO;
import com.bit.house.domain.kakao.KakaoPayApprovalVO;
import com.bit.house.domain.kakao.KakaoPayReadyVO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import lombok.extern.java.Log;
 
@Service
@Log
public class KakaoPay {
	//HttpSession session;
    private static final String HOST = "https://kapi.kakao.com";
    
    private KakaoPayReadyVO kakaoPayReadyVO;
    private KakaoPayApprovalVO kakaoPayApprovalVO;
    
	public String kakaoPayReady(List<OrderListVO> orderListVOList,String memberId) {
		log.info("KakaoPayReady 호출............................................");
        RestTemplate restTemplate = new RestTemplate();
        log.info("orderListVOList" + orderListVOList);
        /*log.info("userId :: " + houseUser.getUserId());
        log.info("payment : 흐" + housePayment);
        log.info("houseProduct : 흐" + houseProduct);*/
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
       // HouseUser houseUser = (HouseUser) session.getAttribute("houseUser");
        headers.add("Authorization", "KakaoAK " + "06ed53af97ab0fb649be890ab5cc6c5d");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");


        int totalQty = 0;
        int totalP = 0;
        for(int i=0;i<orderListVOList.size();i++){
            totalQty += orderListVOList.get(i).getOrderQty();
            totalP += orderListVOList.get(i).getTotalPrice();
        }

        System.out.println("totalQty : "+ totalQty+"totalPrice : "+totalP);



        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "001");
        params.add("partner_user_id", memberId);
        params.add("item_name",orderListVOList.get(0).getProductName()+"외"+Integer.toString(totalQty-1));
        params.add("quantity", Integer.toString(totalQty));
        params.add("total_amount", Integer.toString(totalP));
        params.add("tax_free_amount", "100");
        params.add("approval_url", "http://localhost:8080/kakaoPaySuccess");
        params.add("cancel_url", "http://localhost:8080/kakaoPayCancel");
        params.add("fail_url", "http://localhost:8080/kakaoPaySuccessFail");
 
         HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
 
        try {
            kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyVO.class);
            
            log.info("" + kakaoPayReadyVO);
            
            return kakaoPayReadyVO.getNext_redirect_pc_url();
 
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "/pay";
        
    }
    public KakaoPayApprovalVO kakaoPayInfo(String pg_token,String memberId) {
    	log.info("KakaoPayInfo 메소드 호출 ............................................");
        log.info("KakaoPayInfoVO............................................");
        log.info("-----------------------------");
        RestTemplate restTemplate = new RestTemplate();
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "cc5382cc45ef5108db524d112de5167c");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
 
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", "001");
        params.add("partner_user_id", memberId);
        params.add("pg_token", pg_token);
        
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        
        try {
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalVO.class);
            log.info("" + kakaoPayApprovalVO);
          
            return kakaoPayApprovalVO;
        
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
}