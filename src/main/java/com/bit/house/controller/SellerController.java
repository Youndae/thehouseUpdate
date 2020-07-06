package com.bit.house.controller;

import com.bit.house.domain.AllMemberVO;
import com.bit.house.domain.SellerVO;
import com.bit.house.service.AllMemberService;
import com.bit.house.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
public class SellerController {

    @Autowired
    SellerService sellerService;

    @Autowired
    AllMemberService allMemberService;

    @GetMapping("/signupSeller")
    public String signupSeller(){


        return "th/login/signupSeller";

    }

    @PostMapping("/signupSellerToSU")
    public String signupSellerToSU(SellerVO sellerVO){

        sellerService.insertSellerToUser(sellerVO);
        sellerService.insertSeller(sellerVO);


        return "th/login/signupSeller";
    }

    @GetMapping("/testSeller")
    @ResponseBody
    public SellerVO testSeller(@AuthenticationPrincipal Principal principal){

        String id = principal.getName();
        sellerService.searchSeller(id);

        return sellerService.searchSeller(id);
    }

    @GetMapping("/seller/sellerInfo")
    public String sellerInfo(@AuthenticationPrincipal Principal principal, Model model){

        String id = principal.getName();
        SellerVO sellerInfo = sellerService.searchSellerInfo(id);

        model.addAttribute("sellerInfo", sellerInfo);

        return "th/seller/sellerInfo";
    }

    @PostMapping("/seller/changeSellerInfo")
    public String changeSellerInfo(MultipartFile sellerImgUrl, HttpServletRequest request,String sellerName, String sellerRes,
                                   String sellerAddr, String sellerUrl,String sellerImgBefore, @AuthenticationPrincipal Principal principal) throws IOException {

        String uploadFolder = request.getSession().getServletContext().getRealPath("image/seller");

        String sellerImg = sellerImgBefore;
        if (!sellerImgUrl.isEmpty()) {
            sellerImg = "seller/" + sellerImgUrl.getOriginalFilename();
            File saveFile = new File(uploadFolder, sellerImgUrl.getOriginalFilename());
            sellerImgUrl.transferTo(saveFile);
        }
        sellerService.updateSellerInfo(sellerName, sellerRes, sellerImg, sellerAddr, sellerUrl, principal.getName());


        return "redirect:/seller/sellerInfo";
    }


    @PostMapping("/seller/changeSellerManagerInfo")
    public String changeSellerManager(String sellerManager, String managerTel, String managerEmail, @AuthenticationPrincipal Principal principal){

        sellerService.updateSellerManager(sellerManager, managerTel, managerEmail, principal.getName());
        return "redirect:/seller/sellerInfo";
    }

    @GetMapping("/seller/sellerInfoLogin")
    public String sellerInfoLogin(Model model, @AuthenticationPrincipal Principal principal){

        String id = principal.getName();

        AllMemberVO allMemberVO = (AllMemberVO) allMemberService.read(id);
        model.addAttribute("sellerLoginInfo", allMemberVO);

        SellerVO sellerInfo = sellerService.searchSellerInfo(id);
        model.addAttribute("sellerInfo", sellerInfo);

        return "th/seller/sellerInfoLogin";
    }

    @PostMapping("/seller/changeSellerInfoLogin")
    public String changeSellerInfoLogin(String sellerPw, @AuthenticationPrincipal Principal principal){


        sellerService.updateSellerInfoLogin(sellerPw, principal.getName());

        return "redirect:/seller/sellerInfoLogin";
    }

    @GetMapping("/seller/sellerChart")
    public String sellerChart(Model model, @AuthenticationPrincipal Principal principal){

        String id = principal.getName();
        SellerVO sellerInfo = sellerService.searchSellerInfo(id);
        model.addAttribute("sellerInfo", sellerInfo);

        return "th/seller/sellerChart";
    }

}
