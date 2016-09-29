package com.oo.vul.domain;

import java.io.Serializable;
import java.util.List;

public class Rule implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4509775428326343688L;
    
	private String cve;
	private String vulName;
	private String ruleDesc;
	private String judgment;
	private String ignore;
	private String no;
	private List<Condition> conditionList;
	/**  
	 * 获取cve  
	 * @return cve cve  
	 */
	public String getCve() {
		return cve;
	}
	/**  
	 * 设置cve  
	 * @param cve cve  
	 */
	public void setCve(String cve) {
		this.cve = cve;
	}
	/**  
	 * 获取vulName  
	 * @return vulName vulName  
	 */
	public String getVulName() {
		return vulName;
	}
	/**  
	 * 设置vulName  
	 * @param vulName vulName  
	 */
	public void setVulName(String vulName) {
		this.vulName = vulName;
	}
	/**  
	 * 获取ruleDesc  
	 * @return ruleDesc ruleDesc  
	 */
	public String getRuleDesc() {
		return ruleDesc;
	}
	/**  
	 * 设置ruleDesc  
	 * @param ruleDesc ruleDesc  
	 */
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	/**  
	 * 获取conditionList  
	 * @return conditionList conditionList  
	 */
	public List<Condition> getConditionList() {
		return conditionList;
	}
	/**  
	 * 设置conditionList  
	 * @param conditionList conditionList  
	 */
	public void setConditionList(List<Condition> conditionList) {
		this.conditionList = conditionList;
	}
	/**  
	 * 获取judgment  
	 * @return judgment judgment  
	 */
	public String getJudgment() {
		return judgment;
	}
	/**  
	 * 设置judgment  
	 * @param judgment judgment  
	 */
	public void setJudgment(String judgment) {
		this.judgment = judgment;
	}
	/**  
	 * 获取ignore  
	 * @return ignore ignore  
	 */
	public String getIgnore() {
		return ignore;
	}
	/**  
	 * 设置ignore  
	 * @param ignore ignore  
	 */
	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rule [cve=" + cve + ", vulName=" + vulName + ", ruleDesc="
				+ ruleDesc + ", conditionList=" + conditionList + "]";
	}
	/**  
	 * 获取no  
	 * @return no no  
	 */
	public String getNo() {
		return no;
	}
	/**  
	 * 设置no  
	 * @param no no  
	 */
	public void setNo(String no) {
		this.no = no;
	}
}
