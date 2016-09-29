package com.oo.vul.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.oo.vul.domain.Rule;

public class OOVulUtil {

	private static Log log=LogFactory.getLog(OOVulUtil.class);
	public static Rule matchHostRule(Map<String,String> params){
		Rule rule = CommonUtil.matchRule(params);
		if(rule!=null){
			rule.setJudgment(onAfterMatch(rule.getJudgment(), params));
		}
		return rule;
	}
	public static String matchDbRule(String type,String cve,String vulName){
		if(type!=null&&(type.indexOf("数据库"))!=-1||type.toUpperCase().indexOf("ORACLE")!=-1){
			if(vulName!=null&&vulName.toUpperCase().indexOf("ORACLE")!=-1){
				if(cve!=null){
					cve=cve.toUpperCase();
					if(cve.matches("CVE\\-\\d{4}\\-.+")){
						if(cve.compareTo("CVE-2016")<0){
							return "数据库是Oracle的,CVE编号年度为2016以下的漏洞为误报("+cve+")";
						}
					}
				}
			}
		}
		//add midware WEBSPHERE logical
		if(vulName!=null&&(vulName.toUpperCase().indexOf("WAS")!=-1||vulName.toUpperCase().indexOf("WEBSPHERE")!=-1)){
			if(cve!=null){
				cve=cve.toUpperCase();
				if(cve.matches("CVE\\-\\d{4}\\-.+")){
					if(cve.compareTo("CVE-2014")<0){
						return "中间件Was的,CVE编号年度为2014以下的漏洞为误报("+cve+")";
					}
				}
			}
		}
		return null;
	}
	protected static String onAfterMatch(String result,Map<String,String> params){
		if(result!=null){
		   Pattern pattern = Pattern.compile("\\{.+\\}");
		   Matcher matcher = pattern.matcher(result);
		   while(matcher.find()){
			   String param=matcher.group();
			   String group = param.replaceAll("\\{|\\}","");
			   String value = params.get(group.toUpperCase());
			   if(value==null){
				   log.error(params.get("IP")+",在匹配到CVE="+params.get("CVE")+"时未取到"+group+".值");
				   value="";
			   }
			   result=result.replace(param,value);
		   }
		}
		return result;
	}
}
