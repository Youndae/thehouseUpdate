package com.bit.house.domain.kakao;

import java.util.Date;

import org.springframework.context.annotation.Bean;

import lombok.Data;

@Data
public class KakaoPayApprovalVO {

    @Bean(name = "kakaoPayVO")
    public KakaoPayApprovalVO kakaoPayApprovalVO() {
        return new KakaoPayApprovalVO();
    }

    //response
    private String aid, tid, cid, sid;
    private String item_name, item_code, payload;
    private Integer quantity, tax_free_amount, vat_amount;
    private Date created_at, approved_at;


}