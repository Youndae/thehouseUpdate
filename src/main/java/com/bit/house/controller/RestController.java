package com.bit.house.controller;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;/*
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @PostMapping("/yearGraphAjax")

    public Object yearAjaxGraph(String quantity, String year) {
        //List<통계domain> house통계 = userDAO.get통계();
        //ObjectMapper mapper = new ObjectMapper();
        //String jsonText;
        //jsonText = mapper.writeValueAsString(house통계 );
        // return jsonText  Map으로 보낼지 어떻게 보낼지
        ArrayList<String> graph2ArrList = new ArrayList<String>();
        System.out.println("ajax ===> Quantity : "+ quantity + " year ====> " + year);
        //Service.getSelectedYear(quantity);
        return graph2ArrList;
    }

    @PostMapping("/monthGraphAjax")
    public Object monthAjaxGraph(String quantity, String year) {
        ArrayList<String> graph2ArrList = new ArrayList<String>();
        System.out.println("ajax ===> Quantity : "+ quantity + " year ====> " + year);
        //Service.getSelectedYear(quantity);
        return graph2ArrList;
    }

}
*/