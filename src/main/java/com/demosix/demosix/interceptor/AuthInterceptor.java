package com.demosix.demosix.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.demosix.demosix.aop.Lock;
import com.demosix.demosix.utils.CommonCacheService;
import com.demosix.demosix.utils.RedisLockUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * 拦截器
 *  
 */

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger  = LoggerFactory.getLogger(AuthInterceptor.class);

	/**
	 *  true表示继续流程（如调用下一个拦截器或处理器） 
	 *  false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("-----------------------拦截器开始执行");
		boolean flag = getLocy(request, (HandlerMethod) handler);
		System.out.println("-----------------------拦截器执行结束");
		return flag;
	}

	private boolean getLocy(HttpServletRequest request, HandlerMethod handler) {
		HandlerMethod handlerMethod = handler;
		Method method = handlerMethod.getMethod();
		Lock noRepeat = method.getAnnotation(Lock.class);
		String token = request.getParameter("token");
		System.out.println("----------------------token :"+token);
		String requestURI = request.getRequestURI();
		System.out.println("-------------requestURI:"+requestURI);
		//获取redis分布式锁
		String key = token+requestURI;
		return RedisLockUtils.tryLock(key,token,1000*8);


	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

		String token = request.getParameter("token");
		System.out.println("----------------------token :"+token);
		String requestURI = request.getRequestURI();
		System.out.println("-------------requestURI:"+requestURI);
		//获取redis分布式锁
		String key = token+requestURI;
		boolean releaseLock = RedisLockUtils.releaseLock(key, token);
		logger.info("分布式锁key:{},解锁结果:{}",key,releaseLock);
	}

}
