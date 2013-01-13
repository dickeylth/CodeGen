/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dickey.domain;

import java.util.List;

/**
 *
 * @author 铁行
 */
public class DomainPo {
    private String name;
    private String cnName;
    
    private List<PropertyPo> properties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public List<PropertyPo> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyPo> properties) {
		this.properties = properties;
	}

	public DomainPo(String name, String cnName, List<PropertyPo> properties) {
		super();
		this.name = name;
		this.cnName = cnName;
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "DomainPo [name=" + name + ", cnName=" + cnName
				+ ", properties=" + properties + "]";
	}
    
}
