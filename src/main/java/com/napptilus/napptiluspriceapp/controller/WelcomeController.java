package com.napptilus.napptiluspriceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {

    @GetMapping("welcome")
    public ModelAndView ModelAndView() {
        ModelAndView mv = new ModelAndView("welcome");
        return mv;
    }
}
