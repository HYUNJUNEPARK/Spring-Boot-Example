package com.mysite.sbb.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    //Root URL
    @GetMapping("/")
    public String root() {
        return "redirect:/question/list"; //redirect: 클라이언트가 요청하면 새로운 URL로 전송
    }

    @GetMapping("/sbb")
    @ResponseBody //@ResponseBody 가 없다면 index 라는 파일을 찾게됨
    public String index() {
        return "index";
    }
}