package com.uid939948.Controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 静态页面接口
 */
@Controller
@Lazy
public class LoginController {
    @RequestMapping("/index")
    public String show() {
        return "index";
    }

}
