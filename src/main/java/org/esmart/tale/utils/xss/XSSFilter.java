package org.esmart.tale.utils.xss;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class XSSFilter
 * 对于博客内容部分的提交可以关闭这里，使用XSSUtils中的方法来过滤提交
 */
//@WebFilter(filterName="XSSFilter",urlPatterns={"/*"})
public class XSSFilter implements Filter {

    /**
     * Default constructor. 
     */
    public XSSFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		XSSRequestWrapper xssRequest=new XSSRequestWrapper((HttpServletRequest) request);
		//设置cookie为HTTPOnly，防止js获取cookie
//		((HttpServletResponse)response).setHeader("Set-Cookie","cookiename=value; Path=/;Domain=domainvalue;Max-Age=seconds;HTTPOnly");
		chain.doFilter(xssRequest, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
