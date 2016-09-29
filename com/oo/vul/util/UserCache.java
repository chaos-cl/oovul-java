package com.oo.vul.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

@SuppressWarnings("unchecked")
public class UserCache {

	private static Map<String,String> cache=new HashMap<String,String>();
	static{
		SAXBuilder builder=new SAXBuilder();
		try {
			Document doc = builder.build(DomainCache.class.getResourceAsStream("/conf.xml"));
			Element root = doc.getRootElement();
			List<Element> els = root.getChild("users").getChildren();
			for(Element e:els){
				cache.put(e.getAttributeValue("name"),e.getAttributeValue("value"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getRespUser(String type,String param){
		if("数据库".equals(type)){
			param="DATABASE";
		}else if("中间件".equals(type)){
			param="WAS";
		}else if("网络设备".equals(type)){
			param="NET";
		}else if("安全设备".equals(type)){
			param="SEC";
		}
		if(param!=null){
			param=param.toUpperCase();
		}
		if(cache.get(param)!=null){
			return cache.get(param);
		}
		for(Iterator<Map.Entry<String,String>> iter=cache.entrySet().iterator();iter.hasNext();){
			 Entry<String, String> entry = iter.next();
			 String key = entry.getKey();
			 String value = entry.getValue();
			 if(param!=null&&param.startsWith(key)){
				 return value;
			 }
		}
		return null;
	}
	public static void main(String[] args) {
		
	}
}
