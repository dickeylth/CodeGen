/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dickey.domain;

/**
 *
 * @author 铁行
 */
public class RefClassPo{
    private String refClass;
    private String refType;
    
	public String getRefClass() {
		return refClass;
	}
	public void setRefClass(String refClass) {
		this.refClass = refClass;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	
	
	public RefClassPo(String refClass, String refType) {
		super();
		this.refClass = refClass;
		this.refType = refType;
	}
	
	@Override
	public String toString() {
		return "RefClassPo [refClass=" + refClass + ", refType=" + refType
				+ "]";
	}
    
}
