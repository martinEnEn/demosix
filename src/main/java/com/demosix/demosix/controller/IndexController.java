package com.demosix.demosix.controller;


import com.demosix.demosix.aop.NoRepeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Martin on 2019/3/23.
 */
@RestController("index")
@RequestMapping(value = "/")
public class IndexController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);



    @RequestMapping(value = "/",method = RequestMethod.GET)
    @NoRepeat
    private String index(){
        logger.info("logMethod:"+Thread.currentThread().getStackTrace()[1].getMethodName()+"-------------start");

        System.out.println("==========index=========");
        return "index";
    }


    @RequestMapping(value = "/001",method = RequestMethod.GET)
    private String i001(){
        System.out.println("==========i001============");
        return "/index";
    }


    @RequestMapping(value = "/002",method = RequestMethod.GET)
//    @AutoMobileAnnotation
    private String i002(){
        System.out.println("==========i002============");
        return "/index";
    }

}
