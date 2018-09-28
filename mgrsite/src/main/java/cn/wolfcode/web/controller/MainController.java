package cn.wolfcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台主页
 */
@Controller
public class MainController {
    @RequestMapping("main")
    public String main(){
        return "main";
    }
}
