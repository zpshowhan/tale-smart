package org.esmart.tale.utils;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component("ApplicationContextUtil")
@Lazy(false)
public class ApplicationContextUtil implements ApplicationContextAware, DisposableBean {
	private static Logger logger = Logger.getLogger(ApplicationContextUtil.class);
	private static ApplicationContext context;
	
	@Override
	public void destroy() throws Exception {
		context=null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context=applicationContext;
	}
	/**
	 * 获取applicationContext
	 * 
	 * @return context
	 */
	public static ApplicationContext getContext() {
		return context;
	}
	
	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @return 实例
	 */
	public static Object getBean(String name) {
		Assert.hasText(name);
		return context.getBean(name);
	}

	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return context.getBean(name, type);
	}
	/**
	 * 获取系统目录
	 * 
	 * @return
	 */
	public static String getRootRealPath(){
		String rootRealPath ="";
		try {
			rootRealPath=getContext().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			logger.warn("获取系统根目录失败");
		}
		return rootRealPath;
	}
	public static void main(String[] args) {
		System.out.println(getRootRealPath());
	}
}
