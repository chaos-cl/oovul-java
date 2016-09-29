package com.oo.vul.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
public class XMLUtil {

	final static String ROOT_NODE_NAME = "root";
	public static String ISATTR = "isAttr";

	private static Log log = LogFactory.getLog(XMLUtil.class);

	private static SimpleDateFormat YMDHMS = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

	public static String write(Map<String, Object> dataMap) {
		Element root = new Element(ROOT_NODE_NAME);
		write(root,dataMap);
		Document document = new Document(root);
		XMLOutputter out = new XMLOutputter();
		out.setFormat(Format.getCompactFormat().setEncoding("UTF-8")
			.setIndent("  "));
		return out.outputString(document);
	}
	@SuppressWarnings("unchecked")
	private static void write(Element element,Map<String, Object> children){
		try {
			for (Iterator<Map.Entry<String, Object>> iter = children.entrySet()
					.iterator(); iter.hasNext();) {
				Entry<String, Object> entry = iter.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				boolean isAttr=children.get(ISATTR)!=null?true:false;
				if(ISATTR.equals(key)){
					continue;
				}
				if (value instanceof List<?>) {
					List<Map<String,Object>> dataList = (List<Map<String,Object>>)value;
					Element e=new Element(key);
					for(Map<String,Object> map:dataList){
						write(e,map);
					}
					element.addContent(e);
				}else if(value instanceof Map){
					Element e=new Element(key);
					write(e,(Map<String, Object>)value);
					element.addContent(e);
				}else if (value instanceof Date) {
					String v = YMDHMS.format(value);
					if(isAttr){
						element.setAttribute(key,v);
					}else{
						element.addContent(new Element(key).addContent(v));
					}
				} else {
					String v = "";
					if (value != null) {
						v = value.toString();
					}
					if(isAttr){
						element.setAttribute(key,v);
					}else{
						element.addContent(new Element(key).addContent(v));
					}
				}
			}
		} catch (Exception e) {
			log.error(LogError.getDetailError(e));
		}
	}
/*	private static String toString(Object value) {
		if (value == null) {
			return "";
		}
		if (value instanceof Date) {
			return YMDHMS.format((Date) value);
		}
		return value.toString();
	}*/
}
