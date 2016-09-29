package com.oo.vul.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.oo.vul.domain.Condition;
import com.oo.vul.domain.Rule;

public class CommonUtil {

	private static Log log = LogFactory.getLog(CommonUtil.class);

	/**
	 * 
	 * @category 版本号比对
	 * @param version1
	 * @param version2
	 * @return
	 * @createDate 2016-3-2
	 */
	public static Integer compareVersion(String version1, String version2) {
		if (version1 == null || version2 == null) {
			return null;
		}
		version1 = version1.trim();
		version2 = version2.trim();
		if ("".equals(version1) || "".equals(version2)) {
			return null;
		}
		if (version1.equals(version2)) {
			return 0;
		}
		String[] chars1 = splitForCompare(version1);
		String[] chars2 = splitForCompare(version2);
		for(int i=0;i<chars1.length;i++){
			if(chars2.length>i){
				if(chars1.equals(chars2)){
					
				}else if(chars1[i].matches("\\d+")&&chars2[i].matches("\\d+")){
					int rs=Integer.parseInt(chars1[i])-Integer.parseInt(chars2[i]);
					if(rs!=0){
						return rs;
					}
				}else{
					int rs=chars1[i].compareToIgnoreCase(chars2[i]);
					if(rs!=0){
						return rs;
					}
				}
			}
		}
		return version1.compareToIgnoreCase(version2);
	}
    private static String[] splitForCompare(String str){
    	if(str==null){
    		return new String[]{};
    	}
    	char[] chars = str.toCharArray();
    	List<String> list=new ArrayList<String>();
    	StringBuffer sb=new StringBuffer();
    	for(int i=0;i<chars.length;i++){
    		if(i==0||((chars[i]+"").matches("\\d+")&&(chars[i-1]+"").matches("\\d+"))||((chars[i]+"").matches("\\D+")&&(chars[i-1]+"").matches("\\D+"))){
    			sb.append(chars[i]);
    		}else{
    			list.add(sb.toString());
    			sb=new StringBuffer();
    			sb.append(chars[i]);
    		}
    		if(i==chars.length-1){
    			list.add(sb.toString());
    		}
    	}
    	return list.toArray(new String[]{});
    }
	// os series soft
	public static Rule matchRule(Map<String, String> params) {
		List<Rule> ruleList = RuleCache.getRuleList();
		String cve = params.get("CVE");
		if (cve == null || "".equals(cve)) {
			return null;
		}
		for (Rule rule : ruleList) {
			try {
				if (!cve.equals(rule.getCve())) {
					continue;
				}
				List<Condition> conditionList = rule.getConditionList();
				int index = 0;
				for (Condition condition : conditionList) {
					String name = condition.getName();
					String oper = condition.getOper();
					String value = condition.getValue().toUpperCase().trim();
					String target=params.get(name.toUpperCase());
					if(onBeforeMatchRule(rule,params.get(Condition.NAME_SERIES.toUpperCase()))){
						return rule;
					}
					if(target==null){
						log.error("匹配失败,在匹配规则cve="+cve+"时,ip="+params.get("IP")+",未取到"+name+"的值!!!");
						break;
					}
					target=target.toUpperCase().trim();
					if (oper == null || "".equals(oper)||Condition.OPER_EQUAL.equals(oper)) {
/*						if (value.indexOf(target) != -1||(Condition.NAME_OS.toUpperCase().equals(name.toUpperCase())&&target.indexOf(value)!=-1)
								||(Condition.NAME_SERIES.toUpperCase().equals(name.toUpperCase())&&target.indexOf(".")!=-1&&value.indexOf(target.substring(0,target.indexOf(".")))!=-1)) {
							// match
							index++;
						}*/
						if (value.indexOf(target) != -1||(Condition.NAME_OS.toUpperCase().equals(name.toUpperCase())&&target.indexOf(value)!=-1)
								||(Condition.NAME_SERIES.toUpperCase().equals(name.toUpperCase())&&target.indexOf(value)!=-1)) {
							// match
							index++;
						}
					} else if (Condition.OPER_GREATER_OR_EQUAL.equals(oper)) {
						Integer result = compareVersion(target, value);
						if (result != null && result >= 0) {
							index++;
						}
					}
				}
				if (index == conditionList.size()) {
					return rule;
				}
			} catch (Exception e) {
				log.error(rule);
				log.error(LogError.getDetailError(e));
			}
		}
		return null;
	}
    public static boolean onBeforeMatchRule(Rule rule,String series){
    	if(!rule.getRuleDesc().startsWith("影响")||series==null||"".equals(series)){
    		return false;
    	}
    	String ruleDesc=rule.getRuleDesc().replaceAll("[^RrEeDdHhAaTt0-9/]", "");
    	if("".equals(ruleDesc)||ruleDesc.length()<7){
    		return false;
    	}
    	//REDHAT4/5/6/7
    	String strs[]=ruleDesc.replaceAll("[^0-9/]","").split("\\/");
    	for(String str:strs){
    		 series=series.trim().toUpperCase();
    		if(series.indexOf("REDHAT"+str)!=-1){
    			return false;
    		}
    	}
    	return true;
    }
	public static void main(String[] args) {
		System.out.println(CommonUtil.compareVersion("openssh-5.3p1-194", "openssh-5.3p1-94.el6"));
	}
}
