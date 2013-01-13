/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dickey.domain;

/**
 *
 * @author 铁行
 */
public class PropertyPo {
	//属性名
    private String name;
    //属性中文名
    private String cnName;
    //属性类型
    private String type;
    //属性主键
    private boolean pk;
    //属性值是否可为空
    private boolean nullable;
    //属性值是否唯一
    private boolean unique;
    //与当前属性关联的类
    private RefClassPo refClassPo;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean pk) {
		this.pk = pk;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	public RefClassPo getRefClassPo() {
		return refClassPo;
	}
	public void setRefClassPo(RefClassPo refClassPo) {
		this.refClassPo = refClassPo;
	}
	
	@Override
	public String toString() {
		return "PropertyPo [name=" + name + ", cnName=" + cnName + ", type="
				+ type + ", pk=" + pk + ", nullable=" + nullable + ", unique="
				+ unique + ", refClassPo=" + refClassPo + "]";
	}
	
	
	public PropertyPo() {
		super();
	}
	
	public PropertyPo(String name, String cnName, String type, boolean pk,
			boolean nullable, boolean unique, RefClassPo refClassPo) {
		super();
		this.name = name;
		this.cnName = cnName;
		this.type = type;
		this.pk = pk;
		this.nullable = nullable;
		this.unique = unique;
		this.refClassPo = refClassPo;
	}
    
    
}
