package com.demosix.demosix.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//import org.aspectj.lang.annotation.Aspect;


/**
 * aop
 * 
 */
@Aspect
@Component
public class AutoMobileAspect {
	Logger logger = LoggerFactory.getLogger(AutoMobileAspect.class);

	public AutoMobileAspect() {
	}

	@Pointcut("@annotation(com.demosix.demosix.aop.NoRepeat)")
	public void autoMobileAnnotation() {

	}
	/*
	 * 通过连接点切入
	 */
	@Before("autoMobileAnnotation()")
	public void twi(){
		for (int i = 0; i <6 ; i++) {
			System.err.println ("before=============twi=====================" );
		}
	}




}
