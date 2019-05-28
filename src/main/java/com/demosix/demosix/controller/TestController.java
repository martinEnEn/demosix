package com.demosix.demosix.controller;

import com.demosix.demosix.aop.NoRepeat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("test")
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/001",method = RequestMethod.GET)
    @NoRepeat
    private String t001(){
        System.out.println("==========t001=========");
        return "t001";
    }
}
