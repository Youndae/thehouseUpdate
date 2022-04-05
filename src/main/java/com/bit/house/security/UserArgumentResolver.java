package com.bit.house.security;

import com.bit.house.domain.AllMemberVO;
import com.bit.house.domain.MemberVO;
import com.bit.house.domain.SocialType;
import com.bit.house.service.AllMemberService;
import com.bit.house.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.bit.house.domain.SocialType.*;

@Component
@Slf4j
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private AllMemberService allMemberService;
    private MemberService memberService;

    public UserArgumentResolver(AllMemberService allMemberService, MemberService memberService) {
        this.allMemberService = allMemberService;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SocialUser.class) != null && parameter.getParameterType().equals(MemberVO.class);

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
        return getUser(memberVO, session);
    }

    private MemberVO getUser(MemberVO memberVO, HttpSession session) {
        if (memberVO == null) {
            try {
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

                String principal = authentication.getPrincipal().getName();
                Map<String, Object> map = authentication.getPrincipal().getAttributes();
                AllMemberVO convertUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map , principal);
                MemberVO convertMember = convertMember(authentication.getAuthorizedClientRegistrationId(), map, principal);
//
//                MemberVO = MemberService.searchSocial()
                if (memberService.searchMember(convertMember.getUserid()) == null) {

                    allMemberService.insertSocialToUser(convertUser);
                    memberService.insertSocialToMember(convertMember);

                }
//                allMemberVO = allMemberService.searchSocial(convertUser.getUserid());
                memberVO = memberService.searchMember(convertMember.getUserid());
//                session.setAttribute("memberVO", memberVO);
                session.setAttribute("memberVO", memberVO);


            } catch (ClassCastException e) {
                return memberVO;
            }
        }
        return memberVO;
    }

    private AllMemberVO convertUser(String authority, Map<String, Object> map, String principal) {

        if (GOOGLE.isEquals(authority)) return getGoogleUser(map, principal);
        if (KAKAO.isEquals(authority)) return getKaKaoUser(map, principal);
        if (NAVER.isEquals(authority)) return getNaverUser(map, principal);

        return null;
    }



    private MemberVO convertMember(String authority, Map<String, Object> map, String principal) {

        if (GOOGLE.isEquals(authority)) return getGoogleMember(map, principal);
        if (KAKAO.isEquals(authority)) return getKaKaoMember(map, principal);
        if (NAVER.isEquals(authority)) return getNaverMember(map, principal);

        return null;
    }


    private AllMemberVO getGoogleUser(Map<String, Object> map, String principal) {

        AllMemberVO allMemberVO = new AllMemberVO();

        //allMemberVO.setUserid(String.valueOf(map.get("email")));
        allMemberVO.setUserid(principal);
        allMemberVO.setSocialType(SocialType.GOOGLE);

        return allMemberVO;
    }


    private AllMemberVO getKaKaoUser(Map<String, Object> map, String principal) {
        Map<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
        Map<String, String> accountMap = (HashMap<String, String>) map.get("kakao_account");

        AllMemberVO allMemberVO = new AllMemberVO();
        //allMemberVO.setUserid(String.valueOf(accountMap.get("email")));
        allMemberVO.setUserid(principal);
        allMemberVO.setSocialType(SocialType.KAKAO);

        return allMemberVO;
    }

    private AllMemberVO getNaverUser(Map<String, Object> map, String principal) {

        AllMemberVO allMemberVO = new AllMemberVO();
        //allMemberVO.setUserid(String.valueOf(map.get("email")));
        allMemberVO.setUserid(principal);
        allMemberVO.setSocialType(SocialType.NAVER);

        return allMemberVO;
    }



    private MemberVO getGoogleMember(Map<String, Object> map, String principal) {
        MemberVO memberVO = new MemberVO();
        memberVO.setUserid(principal);
        memberVO.setUserEmail(String.valueOf(map.get("email")));
        memberVO.setUserName(String.valueOf(map.get("name")));


        return memberVO;
    }

    private MemberVO getKaKaoMember(Map<String, Object> map, String principal) {
        Map<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
        Map<String, String> accountMap = (HashMap<String, String>) map.get("kakao_account");

        MemberVO memberVO = new MemberVO();
        memberVO.setUserid(principal);
        memberVO.setUserEmail(String.valueOf(accountMap.get("email")));
        memberVO.setUserName(String.valueOf(propertyMap.get("nickname")));

        return memberVO;
    }

    private MemberVO getNaverMember(Map<String, Object> map, String principal) {
        MemberVO memberVO = new MemberVO();
        memberVO.setUserid(principal);
        memberVO.setUserEmail(String.valueOf(map.get("email")));
        memberVO.setUserName(String.valueOf(map.get("name")));

        return memberVO;
    }
}



