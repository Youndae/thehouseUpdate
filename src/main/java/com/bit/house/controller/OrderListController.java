package com.bit.house.controller;

import com.bit.house.domain.OrderListProVO;
import com.bit.house.domain.OrderStatusVO;
import com.bit.house.service.OrderListProService;
import com.bit.house.service.OrderListService;
import com.bit.house.service.OrderStatusService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/seller")
public class OrderListController {

    @Autowired
    OrderListProService orderListProService;

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    OrderListService orderListService;

    @PostMapping("/orderListChange")
    public String orderListChange(String[] orderCode, String[] orderNo) {


        for (int i = 0; i < orderCode.length; i++) {
            orderListService.changeOrderStatus(orderCode[i], orderNo[i]);
        }


        return "redirect:/seller/orderList";
    }

    @RequestMapping(value="/orderList")
    public String orderCheck(@AuthenticationPrincipal Principal principal, Model model, String orderCode) {

        String id = principal.getName();

        if(orderCode==null) {
            List<OrderListProVO> orderListProVO = orderListProService.searchOrderList(id);
            model.addAttribute("orderListPro", orderListProVO);
        }else if(Integer.parseInt(orderCode)==0){
                List<OrderListProVO> orderListProVO = orderListProService.searchOrderList(id);
                model.addAttribute("orderListPro", orderListProVO);
        }else {
                List<OrderListProVO> orderListProVO = orderListProService.searchOrderStatus(orderCode, id);
                model.addAttribute("orderListPro", orderListProVO);
                log.info(String.valueOf(orderListProVO));

        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonOrderStatus;
        List<OrderStatusVO> orderStatus = orderStatusService.searchOrderStatus();
        model.addAttribute("orderCheck", orderStatus);

        try {
            jsonOrderStatus = mapper.writeValueAsString(orderStatus);
            model.addAttribute("jsonOrderStatus", jsonOrderStatus);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "th/seller/orderList";
    }
}
