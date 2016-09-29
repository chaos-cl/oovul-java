package com.oo.vul.domain;

import java.io.Serializable;

public class Condition implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8973811836171942317L;
	public static final String OPER_GREATER=">";
	public static final String OPER_GREATER_OR_EQUAL=">=";
	public static final String OPER_LESS="<";
	public static final String OPER_LESS_OR_EQUAL="<=";
	public static final String OPER_EQUAL="=";
	public static final String NAME_OS="version";
	public static final String NAME_SOFT="soft";
	public static final String NAME_SERIES="series";
	private String name;
	private String value;
	private String oper=OPER_EQUAL;
	/**  
	 * 获取name  
	 * @return name name  
	 */
	public String getName() {
		return name;
	}
	/**  
	 * 设置name  
	 * @param name name  
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**  
	 * 获取value  
	 * @return value value  
	 */
	public String getValue() {
		return value;
	}
	/**  
	 * 设置value  
	 * @param value value  
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**  
	 * 获取oper  
	 * @return oper oper  
	 */
	public String getOper() {
		return oper;
	}
	/**  
	 * 设置oper  
	 * @param oper oper  
	 */
	public void setOper(String oper) {
		this.oper = oper;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Condition [name=" + name + ", value=" + value + ", oper="
				+ oper + "]";
	}
}
