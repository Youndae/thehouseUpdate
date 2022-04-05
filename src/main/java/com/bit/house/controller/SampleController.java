package com.bit.house.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class SampleController {


  @GetMapping("/all")
  public String doAll() {

    log.info("do all can access everybody");
    return "th/login/all";
  }

  @GetMapping("/member")
  public String doMember(Authentication auth) {
    log.info(String.valueOf(auth));
    log.info("logined member");

    return "th/login/member";
  }

  @GetMapping("/admin")
  public String doAdmin(Authentication auth) {
    log.info(String.valueOf(auth));
    log.info("admin only");

    return "th/login/admin";
  }
}