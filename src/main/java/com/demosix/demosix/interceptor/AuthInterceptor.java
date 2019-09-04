package com.demosix.demosix.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.demosix.demosix.aop.Lock;
import com.demosix.demosix.utils.CommonCacheService;
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
 * 用户登录拦截器
 *  
 */

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger  = LoggerFactory.getLogger(AuthInterceptor.class);
	

	@Autowired
	private CommonCacheService commonCacheService;

	/**
	 *  true表示继续流程（如调用下一个拦截器或处理器） 
	 *  false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("-----------------------拦截器开始执行");
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Lock noRepeat = method.getAnnotation(Lock.class);
		String token = request.getParameter("token");
		System.out.println("----------------------token :"+token);

		String requestURI = request.getRequestURI();
		System.out.println("-------------requestURI:"+requestURI);
		//获取redis分布式锁
		String key = token+requestURI;
		commonCacheService.tryLock(key,token,1000*8);

		logger.info("AuthInterceptor.noLoginAuth(),noLoginAuth : {}", noRepeat);
		System.out.println("-----------------------拦截器执行结束");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		String token = request.getParameter("token");
		System.out.println("----------------------token :"+token);
		String requestURI = request.getRequestURI();
		System.out.println("-------------requestURI:"+requestURI);
		//获取redis分布式锁
		String key = token+requestURI;
		commonCacheService.releaseLock(key,token);
		
	}
	
	/**
     * 鉴权方法2： 默认所有方法都必须判断是否已登录，但对于注解了@NoLonginAuth的方法可以例外
     * 
     * @return
     * @throws IOException
     */
    private boolean needLogin(Method method) {

    	return false;

    }
    
    /**
     * 用户是否已登录 已经登录 返回USER_ID
     */

    
    /**
     * 将对象转换成json格式输出
     */
    private void returnResult(HttpServletResponse response, Object o) throws IOException {
        OutputStream os = response.getOutputStream();
        String json = JSONObject.toJSONString(o);
        os.write(json.getBytes("UTF-8"));
        os.flush();
    }

}
