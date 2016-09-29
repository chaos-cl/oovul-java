package com.oo.vul.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.oo.vul.FilePath;
import com.oo.vul.domain.Rule;
import com.oo.vul.util.CommonUtil;
import com.oo.vul.util.DomainCache;
import com.oo.vul.util.ExcelUtil;
import com.oo.vul.util.LogError;
import com.oo.vul.util.UserCache;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.header;

public class VulService {

	private static Log log=LogFactory.getLog(VulService.class);
	
	public static void main(String[] args) {
		try {
			log.info("---开始执行---");
			List<List<String>> sheetList = ExcelUtil.getExcelContent(FilePath.EXCEL_IMPORT_FILE_PATH).get(0);
			Map<String, Map<String, String>> params = getParams();
			List<List<String>> dataList=new ArrayList<List<String>>();
			List<String> headerList=new ArrayList<String>();
			dataList.add(headerList);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String now=sdf.format(new Date());
			for(int i=1;i<sheetList.size();i++){
				List<String> row = sheetList.get(i);
				String ip=row.get(0);
				String type=row.get(1);
				String risk=row.get(2);
				String cve=row.get(3);
				String vulName=row.get(4);
				String vulDesc=row.get(5);
				String view=row.get(6);
				List<String> data=new ArrayList<String>();
				data.add(ip);
				data.add(type);
				data.add(risk);
				data.add(cve);
				data.add(vulName);
				data.add(vulDesc);
				data.add(view);
				Map<String, String> map = params.get(ip);
				if(map==null){
					map= new HashMap<String, String>();
				}
				map.put("CVE",cve);
				map.put("IP",ip);
				data.add(now);
				data.add(map.get("SERIES"));
				data.add(map.get("VERSION"));
				data.add(map.get("OPENSSH"));
				data.add(map.get("OPENSSL"));
				data.add(map.get("NTP"));
				data.add(map.get("BIND"));
				data.add(map.get("HPSMH"));
				data.add(map.get("VSFTPD"));
				data.add(map.get("SENDMAIL"));
				data.add(map.get("NSS"));
				data.add(map.get("BIND97"));
				data.add(map.get("JAVA"));
				data.add(UserCache.getRespUser(type,map.get("VERSION")));
				data.add(DomainCache.getDomain(ip));
				data.add("");
				data.add("");
				data.add("");
				String onBeforeMatch=onBeforeMatch(type,cve,vulName);
				if(onBeforeMatch!=null){
					data.add("是");
					data.add(onBeforeMatch);
				}else{
					Rule rule = CommonUtil.matchRule(map);
					if(rule!=null){
						//误报
							data.add(rule.getIgnore()!=null&&!"".equals(rule.getIgnore())?rule.getIgnore():"是");
							data.add(onAfterMatch(rule.getJudgment(),map));
					}else{
						data.add("否");
						data.add("");
					}
				}
				dataList.add(data);
			}
			headerList.add("IP");
			headerList.add("设备类型");
			headerList.add("风险等级");
			headerList.add("CVE编号");
			headerList.add("漏洞名称");
			headerList.add("漏洞描述");
			headerList.add("整改意见");
			headerList.add("发现时间");
			headerList.add("操作系统系列");
			headerList.add("操作系统版本");
			headerList.add("Openssh软件版本");
			headerList.add("Openssl软件版本");
			headerList.add("Ntp软件版本");
			headerList.add("Bind软件版本");
			headerList.add("Hpsmh软件版本");
			//新增几列↓
			headerList.add("vsftpd软件版本");
			headerList.add("sendmail软件版本");
			headerList.add("nss软件版本");
			headerList.add("bind97软件版本");
			headerList.add("java软件版本");
			headerList.add("php软件版本");
			//↑
			headerList.add("设备责任人");
			headerList.add("所属安全域");
			headerList.add("修复情况");
			headerList.add("复测时间");
			headerList.add("复测结果");
			headerList.add("误报情况");
			headerList.add("误报说明");
			ExcelUtil.exportExcel(dataList,FilePath.EXCEL_EXPORT_FILE_PATH);
			log.info("---执行成功---");
		} catch (Exception e) {
			log.error(LogError.getDetailError(e));
		}
	}
	public static Map<String,Map<String,String>> getParams(){
		Map<String,Map<String,String>> params=new HashMap<String, Map<String,String>>();
	    try {
			File file = new File(FilePath.TXT_FILEPATH);
			FileInputStream input = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(reader);
			String line=null;
			while((line=br.readLine())!=null){
				String[] cells = line.split("\\,");
				Map<String,String> param=new HashMap<String, String>();
				String ip=null;
				for(String cell:cells){
					if(cell!=null&&!"".equals(cell)&&cell.indexOf(":")!=-1){
						String key=cell.substring(0,cell.indexOf(":")).toUpperCase().trim();
						String value=cell.substring(cell.indexOf(":")+1).trim();
						if("IP".equals(key.toUpperCase())){
							ip=value;
						}
						if(value!=null&&(value.indexOf("not installed")!=-1||value.startsWith("package "))){
							continue;
						}
						param.put(key, value);
					}
				}
				if(ip!=null){
					params.put(ip,param);
				}
			}
		  input.close();
		  reader.close();
		  br.close();
		} catch (Exception e) {
			log.error(LogError.getDetailError(e));
		}
		return params;
	}
	private static String onBeforeMatch(String type,String cve,String vulName){
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
		//增加中间件逻辑
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
