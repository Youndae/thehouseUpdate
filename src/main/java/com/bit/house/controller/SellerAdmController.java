package com.bit.house.controller;

import com.bit.house.domain.SellerVO;
import com.bit.house.mapper.SellerAdmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class SellerAdmController {

    @Autowired(required = false)
    SellerAdmMapper sellerAdmMapper;
    //거래처 현황
    @RequestMapping("/sellerManagement")
    public String sellerManagement(Model model) throws Exception{

        model.addAttribute("sellerMng", sellerAdmMapper.sellerManagement());

        return "th/admin/statAdmin/sellerManagement";
    }
    //거래처 정보
    @RequestMapping("/sellerStat/{sellerName}")
    public String sellerStat(@PathVariable String sellerName, Model model) throws Exception{

        model.addAttribute("sellerProduct", sellerAdmMapper.sellerProduct(sellerName));
        model.addAttribute("sellerStat", sellerAdmMapper.sellerStat(sellerName));
        model.addAttribute("productCount", sellerAdmMapper.productCount(sellerName));

        return "th/admin/statAdmin/sellerStat";
    }
    //거래서 상세
    @RequestMapping("/sellerDetail")
    public String sellerDetail(@RequestParam(required = false) String sellerName, Model model) throws Exception{

        model.addAttribute("sellerDetail", sellerAdmMapper.sellerDetail(sellerName));

        return "th/admin/statAdmin/sellerDetail";
    }
    //거래처 판매글
    @RequestMapping("/allSellerProduct")
    public String sellerProduct(@RequestParam(required = false) String sellerProduct, Model model) throws Exception{

        model.addAttribute("product", sellerAdmMapper.allSellerProduct(sellerProduct));
        model.addAttribute("productCount", sellerAdmMapper.productCount(sellerProduct));

        return "th/admin/statAdmin/sellerProduct";
    }
    //업체신청
    @RequestMapping("/applySeller")
    public String applySeller(Model model) throws Exception{

        model.addAttribute("applySeller", sellerAdmMapper.applySeller());

        return "th/admin/statAdmin/applySeller";
    }

    //신청업체 정보
    @RequestMapping("/applySellerDetail/{sellerName}")
    public String applySellerDetail(@PathVariable String sellerName, Model model) throws Exception{

        model.addAttribute("sellerDetail", sellerAdmMapper.sellerDetail(sellerName));

        return "th/admin/statAdmin/applySellerDetail";
    }
    //업체신청 승인
    @RequestMapping("/applyProc")
    public String applyProc(@RequestParam(required = false) String sellerName, SellerVO sellerVO) throws Exception{

        sellerVO.setSellerName(sellerName);

        sellerAdmMapper.applyProc(sellerName);

        return "redirect:/admin/applySeller";
    }
}
