package com.oo.vul.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

@SuppressWarnings("unchecked")
public class DomainCache {

	static Map<String,List<String>> cache=new HashMap<String,List<String>>();
	static{
		SAXBuilder builder=new SAXBuilder();
		try {
			Document doc = builder.build(DomainCache.class.getResourceAsStream("/conf.xml"));
			Element root = doc.getRootElement();
			List<Element> els = root.getChild("domains").getChildren();
			for(Element e:els){
				String name = e.getAttributeValue("name");
				List<Element> children = e.getChildren();
				List<String> list=new ArrayList<String>();
				for(Element ee:children){
					list.add(ee.getTextTrim());
				}
				cache.put(name,list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getDomain(String ip){
		for(Iterator<Map.Entry<String,List<String>>>iter=cache.entrySet().iterator();iter.hasNext();){
			  Entry<String, List<String>> entry = iter.next();
			  String key=entry.getKey();
			  List<String> domainList=entry.getValue();
			  boolean result = IPUtil.filter(ip, domainList);
			  if(result){
				  return key;
			  }
		}
		return "";
	}
	public static void main(String[] args) {
		
	}
}
