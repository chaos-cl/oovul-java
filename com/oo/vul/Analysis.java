package com.oo.vul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.oo.vul.domain.Condition;
import com.oo.vul.util.ExcelUtil;
import com.oo.vul.util.XMLUtil;

public class Analysis {

	public static void main(String[] args)throws Exception {
		Analysis.analysis();
	}
	public static void analysis()throws Exception{
		List<List<List<String>>> sheetList = ExcelUtil.getExcelContent("C:\\Users\\le.zhang\\Desktop\\安全漏洞误报统计收集表 V0.12.xlsx");
		Map<String,Object> dataMap=new HashMap<String, Object>();
		List<Map<String,Map<String,Object>>> rules=new ArrayList<Map<String,Map<String,Object>>>();
		dataMap.put("rules",rules);
		int index=1;
		index=analysisSheet0(sheetList.get(0),index,rules);
		index=analysisSheet1(sheetList.get(1),index,rules);
		index=analysisSheet2(sheetList.get(2),index,rules);
		index=analysisSheet3(sheetList.get(3),index,rules);
		index=analysisSheet4(sheetList.get(4),index,rules);
		System.out.println(XMLUtil.write(dataMap));
	}
	protected static int analysisSheet0(List<List<String>> sheet,int index,List<Map<String,Map<String,Object>>> rules){
		for(int i=1;i<sheet.size();i++){
			List<String> row = sheet.get(i);
			String os=row.get(2).replaceAll("\\s+","");
			String cve=row.get(3);
			String vulName=row.get(4);
			String ruleDesc=row.get(5);
			Map<String,Object> rule=new LinkedHashMap<String,Object>(); 
			rule.put("no",index);
			rule.put("cve",cve);
			rule.put("vulName",vulName);
			rule.put("ruleDesc",ruleDesc);
			rule.put("judgment",vulName+"("+cve+")|"+ruleDesc+"，{SERIES}");
			rule.put(XMLUtil.ISATTR,true);
			Map<String,Map<String,Object>> ruleMap=new HashMap<String,Map<String,Object>>();
			ruleMap.put("rule",rule);
			rules.add(ruleMap);
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name",Condition.NAME_OS);
			map.put("value",os);
			map.put(XMLUtil.ISATTR,true);
			list.add(map);
			rule.put(Condition.NAME_OS,list);
			index++;
		}
		return index;
	}
	protected static int analysisSheet1(List<List<String>> sheet,int index,List<Map<String,Map<String,Object>>> rules){
		for(int i=1;i<sheet.size();i++){
			List<String> row = sheet.get(i);
			String os=row.get(2).replaceAll("\\s+","");
			String cve=row.get(3);
			String vulName=row.get(4);
			String ruleDesc=row.get(5);
			Map<String,Object> rule=new LinkedHashMap<String,Object>(); 
			rule.put("no",index);
			rule.put("cve",cve);
			rule.put("vulName",vulName);
			rule.put("ruleDesc",ruleDesc);
			rule.put("judgment",vulName+"("+cve+")|"+ruleDesc+"，{SERIES}");
			rule.put(XMLUtil.ISATTR,true);
			Map<String,Map<String,Object>> ruleMap=new HashMap<String,Map<String,Object>>();
			ruleMap.put("rule",rule);
			rules.add(ruleMap);
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name",Condition.NAME_OS);
			map.put("value",os);
			map.put(XMLUtil.ISATTR,true);
			list.add(map);
			rule.put(Condition.NAME_OS,list);
			index++;
		}
		return index;
	}
	protected static int analysisSheet2(List<List<String>> sheet,int index,List<Map<String,Map<String,Object>>> rules){
		for(int i=1;i<sheet.size();i++){
			List<String> row = sheet.get(i);
			//String os=row.get(2).replaceAll("\\s+","");
			String cve=row.get(3);
			String vulName=row.get(4);
			String ruleDesc=row.get(5);
			Map<String,Object> rule=new LinkedHashMap<String,Object>(); 
			rule.put("no",index);
			rule.put("cve",cve);
			rule.put("vulName",vulName);
			rule.put("ruleDesc",ruleDesc);
			rule.put("judgment",vulName+"("+cve+")|"+ruleDesc+"，{SERIES}");
			rule.put(XMLUtil.ISATTR,true);
			Map<String,Map<String,Object>> ruleMap=new HashMap<String,Map<String,Object>>();
			ruleMap.put("rule",rule);
			rules.add(ruleMap);
/*			List<Map<String,Object>> list1=new ArrayList<Map<String,Object>>();
			Map<String,Object> map1=new HashMap<String, Object>();
			map1.put("name",Condition.NAME_OS);
			map1.put("value",os);
			map1.put(XMLUtil.ISATTR,true);
			list1.add(map1);
			rule.put("co",list1);*/
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name",Condition.NAME_SERIES);
			String values=ruleDesc.replaceAll("[^RrEeDdHhAaTt0-9/]", "");
			if(values.indexOf("/")!=-1){
				String[] splits = values.replaceAll("[a-zA-Z]+","").split("\\/");
				String pre=values.replaceAll("(\\d)|\\/","");
				StringBuffer sb=new StringBuffer();
				for(String split:splits){
					sb.append(pre+split+"|");
				}
				values=sb.toString().substring(0,sb.length()-1);
			}
			map.put("value",values);
			map.put(XMLUtil.ISATTR,true);
			//map.put(Condition.OPER_EQUAL ,true);
			list.add(map);
			rule.put(Condition.NAME_SERIES,list);
			index++;
		}
		return index;
	}
	protected static int analysisSheet3(List<List<String>> sheet,int index,List<Map<String,Map<String,Object>>> rules){
		List<String> print=new ArrayList<String>();
		for(int i=1;i<sheet.size();i++){
			List<String> row = sheet.get(i);
			//String os=row.get(2).replaceAll("\\s+","");
			String cve=row.get(3);
			String vulName=row.get(4);
			String ruleDesc=row.get(5);
			String softs=row.get(6);
			if("CVE-2016-0800".equals(cve)){
				System.out.println("23");
			}
			softs=softs.replaceAll("影响.+修复 ","").replaceAll("\\.rpm",".rpm\n");
			String[] splits = softs.split("\\n");
			for(int j=0;j<splits.length;j++){
				String string = splits[j].trim();
				if("".equals(string)||string.indexOf(":")==-1||string.indexOf("修复")!=-1){
					continue;
				}
				String[] split = string.split("\\:");
				Map<String,Object> rule=new LinkedHashMap<String,Object>(); 
				if(splits.length>1){
					rule.put("no",index+"."+(j+1));
				}else{
					rule.put("no",index);
				}
				rule.put("cve",cve);
				rule.put("vulName",vulName);
				rule.put("ruleDesc",ruleDesc);
				rule.put("judgment",vulName+"("+cve+")|"+split[1].trim()+"|"+ruleDesc+"，{"+split[1].trim().split("\\-")[0]+"}");
				rule.put(XMLUtil.ISATTR,true);
				Map<String,Map<String,Object>> ruleMap=new HashMap<String,Map<String,Object>>();
				ruleMap.put("rule",rule);
				rules.add(ruleMap);
				List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("name",Condition.NAME_SERIES);
				String value=split[0].trim();
				if(value.indexOf(" ")!=-1){
					value=value.substring(0,value.indexOf(" "));
				}
				map.put("value",value);
				map.put(XMLUtil.ISATTR,true);
				list.add(map);
				rule.put("series",list);
				List<Map<String,Object>> list3=new ArrayList<Map<String,Object>>();
				Map<String,Object> map3=new HashMap<String, Object>();
				map3.put("name",split[1].trim().split("\\-")[0]);
				if(!print.contains(split[1].trim().split("\\-")[0])){
					print.add(split[1].trim().split("\\-")[0]);
				}
				map3.put("value",split[1].trim());
				map3.put("oper",Condition.OPER_GREATER_OR_EQUAL);
				map3.put(XMLUtil.ISATTR,true);
				list3.add(map3);
				rule.put("soft",list3);
			}
			index++;
		}
		System.out.println(print);
		return index;
	}
	protected static int analysisSheet4(List<List<String>> sheet,int index,List<Map<String,Map<String,Object>>> rules){
		List<String> print=new ArrayList<String>();
		for(int i=1;i<sheet.size();i++){
			List<String> row = sheet.get(i);
			//String os=row.get(2).replaceAll("\\s+","");
			String cve=row.get(3);
			String vulName=row.get(4);
			String ruleDesc=row.get(5);
			String softs=row.get(6);
			String[] splits = softs.split("\\n");
			for(int j=0;j<splits.length;j++){
				String string = splits[j].trim();
				if("".equals(string)){
					continue;
				}
				String[] split = string.split("\\:|\\：");
				Map<String,Object> rule=new LinkedHashMap<String,Object>(); 
				if(splits.length>1){
					rule.put("no",index+"."+(j+1));
				}else{
					rule.put("no",index);
				}
				rule.put("cve",cve);
				rule.put("vulName",vulName);
				rule.put("ruleDesc",ruleDesc);
				rule.put("judgment",vulName+"("+cve+")|"+split[1].trim()+"|"+ruleDesc+"，{HP-UX SECURE SHELL}");
				rule.put(XMLUtil.ISATTR,true);
				Map<String,Map<String,Object>> ruleMap=new HashMap<String,Map<String,Object>>();
				ruleMap.put("rule",rule);
				rules.add(ruleMap);
				List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("name",Condition.NAME_SERIES);
				String value=split[0].trim();
//				if(value.indexOf(" ")!=-1){
//					value=value.substring(0,value.indexOf(" "));
//				}
				map.put("value",value);
				map.put(XMLUtil.ISATTR,true);
				list.add(map);
				rule.put("series",list);
				List<Map<String,Object>> list3=new ArrayList<Map<String,Object>>();
				Map<String,Object> map3=new HashMap<String, Object>();
				map3.put("name","HP-UX Secure Shell".toUpperCase());
				if(!print.contains(split[1].trim().split("\\-")[0])){
					print.add(split[1].trim().split("\\-")[0]);
				}
				map3.put("value",split[1].trim());
				map3.put("oper",Condition.OPER_GREATER_OR_EQUAL);
				map3.put(XMLUtil.ISATTR,true);
				list3.add(map3);
				rule.put("soft",list3);
			}
			index++;
		}
		System.out.println(print);
		return index;
	}
}
