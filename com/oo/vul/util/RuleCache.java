package com.oo.vul.util;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.oo.vul.domain.Condition;
import com.oo.vul.domain.Rule;

public class RuleCache {

         private static  List<Rule> ruleList=null;
         
         public static List<Rule> getRuleList(){
        	 if(ruleList==null){
        		 init();
        	 }
        	 return ruleList;
         }
		@SuppressWarnings("unchecked")
		private static void init(){
			 ruleList=new ArrayList<Rule>();
        	 SAXBuilder builder=new SAXBuilder();
        	 try {
				Document doc = builder.build(DomainCache.class.getResourceAsStream("/conf.xml"));
				Element root = doc.getRootElement();
				List<Element> rules = root.getChild("rules").getChildren();
				for(Element ele:rules){
					Rule rule=new Rule();
					ruleList.add(rule);
					rule.setCve(ele.getAttributeValue("cve"));
					rule.setRuleDesc(ele.getAttributeValue("ruleDesc"));
					rule.setVulName(ele.getAttributeValue("vulName"));
					rule.setJudgment(ele.getAttributeValue("judgment"));
					rule.setIgnore(ele.getAttributeValue("ignore"));
					rule.setNo(ele.getAttributeValue("no"));
					List<Element> conditions = ele.getChildren();
					List<Condition>conditionList=new ArrayList<Condition>();
					rule.setConditionList(conditionList);
					for(Element e:conditions){
						//String type = e.getName();
						String name = e.getAttributeValue("name");
						String value = e.getAttributeValue("value");
						String oper = e.getAttributeValue("oper");
						Condition condition=new Condition();
						condition.setName(name);
						condition.setValue(value);
						if(oper!=null&&!"".equals(oper)){
							condition.setOper(oper);
						}
						conditionList.add(condition);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
         }
         private RuleCache(){
        	 
         }
         public static void main(String[] args) {
			
		}
}
