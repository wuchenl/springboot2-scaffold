package com.letters7.wuchen.springboot2.utils.web;


import com.letters7.wuchen.springboot2.utils.net.UtilNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * 和Web层相关的实用工具类
 * 
 * @author wuchenl
 * @since 2018-09-13
 */
public class UtilWeb {
	private static Logger logger = LoggerFactory.getLogger(UtilWeb.class);

	/**
	 * 获取当前请求对象
	 * @return
	 */
	public static HttpServletRequest currentRequestInSpring(){
		try{
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		}catch(Exception e){
			return null;
		}
	}

	//web应用绝对路径
	public static String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	}

	public static Map<String, String> getParameters(HttpServletRequest request) {
		Map<String, String> parameters = new HashMap<>();
		Enumeration enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = String.valueOf(enumeration.nextElement());
			parameters.put(name, request.getParameter(name));
		}
		return parameters;
	}

	public static Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<>();
		Enumeration<String> enumeration = request.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}


	//获取请求客户端的真实ip地址
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-host");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if("0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)){
			return UtilNet.LOCALHOST;
		}
		return ip;
	}

	/**
	 * 获取请求体内容
	 */
	public static String getRequestPayload(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			request.setCharacterEncoding("utf8");
			reader = request.getReader();
			//做标记为了reset
			reader.mark(0);
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally{
			if (reader!=null){
				try {
					reader.reset();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 通过Spring获取当前会话的IP
	 * @return IP
	 */
	public static String getCurrentIpBySpring(){
		HttpServletRequest httpServletRequest = currentRequestInSpring();
		if (Objects.nonNull(httpServletRequest)){
			return getIpAddr(httpServletRequest);
		}else {
			return null;
		}
	}
}
