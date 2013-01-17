/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dickey.test;

import com.dickey.domain.DomainPo;
import com.dickey.domain.PropertyPo;
import com.dickey.domain.RefDomainPo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author 铁行
 */
public class GenDomain {
	//Table元数据信息类集合
	private List<DomainPo> domainPos = new LinkedList<>();
	//包名
	public static String pkg = "com.codegen";
	
	/**
	 * 解析db.xml到表元数据的domain中
	 * @throws DocumentException
	 */
    public void dbXmlParse() throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File("src\\domain.xml"));
        
        //获取根节点tables
        Element root = document.getRootElement();
        //获取子节点列表table
        @SuppressWarnings("unchecked")
		List<Element> domains = root.elements("domain");
        for (Element domain : domains) {
            String domainName = (String) domain.elementText("name");
            String domainCnName = (String) domain.elementText("name_cn");
            
            List<PropertyPo> propertyPos = new LinkedList<>();
            DomainPo domainPo = new DomainPo(domainName, domainCnName, propertyPos);
            boolean isRefUserflag = false;
            
            //遍历columns
            @SuppressWarnings("unchecked")
			List<Element> properties = domain.element("properties").elements("property");
            for (Element property : properties) {
            	
                String propName = property.elementText("name");
                String propCnName = property.elementText("name_cn");
                String propType = property.elementText("type");
                PropertyPo propertyPo = new PropertyPo(propName, propCnName, propType);
                
                //处理是否主键/非空/唯一/集合
                String bool = property.elementText("pk");
                if(bool != null){
                	propertyPo.setPk(Boolean.valueOf(bool));
                	propertyPo.setType("String");
                }
                bool = property.elementText("nullable");
                if(bool != null){
                	propertyPo.setNullable(bool);
                }
                bool = property.elementText("unique");
                if(bool != null){
                	propertyPo.setUnique(bool);
                }
                bool = property.elementText("plural");
                if(bool != null){
                	propertyPo.setPlural(Boolean.valueOf(bool));
                }
                       
                //处理外键关联
                Element propRef = property.element("ref");
                if(propRef != null && !propRef.getStringValue().equals("false")){
                    String refDomain = propRef.elementText("ref_domain");
                    String refType = propRef.elementText("ref_type");
                    String refDisplayProp = propRef.elementText("ref_display_property");
                    RefDomainPo refDomainPo = new RefDomainPo(refDomain, refType, refDisplayProp);
                    
                    //处理一对一&多对多连接表
                    if(refType.equals("one-to-one") || refType.equals("many-to-many")){
                    	String joinTable = (domainName.compareTo(refDomain) > 0) ? domainName + "_" + refDomain : refDomain + "_" + domainName;
                    	String joinColumn = domainName + "_id";
                    	String inverseJoinColumn = refDomain + "_id";
                    	refDomainPo.setJoinTable(joinTable);
                    	refDomainPo.setJoinColumn(joinColumn);
                    	refDomainPo.setInverseJoinColumn(inverseJoinColumn);
                    }
                    
                    //处理变量名规范，对关联类型属性变量名进行转换
                    //首字母小写
                    refDomain = refDomain.substring(0, 1).toLowerCase().concat(refDomain.substring(1));
                    if(propertyPo.isPlural()){	
                    	propertyPo.setName(refDomain + "s");
                    }else{
                    	propertyPo.setName(refDomain);
                    }
                    
                    propertyPo.setRefDomainPo(refDomainPo);
                }
                
                domainPo.getProperties().add(propertyPo);
                if(propType != null && propType.equals("User")){
                	isRefUserflag = true;
                }
                
            }
            domainPo.setUserRelated(isRefUserflag);
            //System.out.println(domainPo);
            domainPos.add(domainPo);
        }
        
    }
    
    /**
     * 合并表元数据与模板文件
     * @param args
     * @throws IOException 
     * @throws TemplateException 
     * @throws DocumentException
     */
    public void mergeDomainXml() throws IOException, TemplateException{
    	//Freemarker初始化工作
    	Configuration conf = new Configuration();
    	conf.setDirectoryForTemplateLoading(new File("src/templates"));
    	
    	Map<String, Object> root;
    	for (DomainPo domainPo : this.domainPos) {
    		root = new HashMap<String, Object>();
        	root.put("package", GenDomain.pkg);
        	root.put("domain", domainPo);
        	//使用Configuration实例来加载指定模板
        	Template domainTemplate = conf.getTemplate("domain.ftl");
        	//合并domain模板
        	//domainTemplate.process(root, new OutputStreamWriter(System.out));
        	//System.out.println();
        	
        	
        	//加载dao模板
        	Template daoTemplate = conf.getTemplate("dao.ftl");
        	//合并dao模板
        	//daoTemplate.process(root, new OutputStreamWriter(System.out));
        	//System.out.println();
        	
        	//加载daoImpl模板
        	Template daoImplTemplate = conf.getTemplate("daoImpl.ftl");
        	//合并dao模板
        	//daoImplTemplate.process(root, new OutputStreamWriter(System.out));
        	//System.out.println();
		}
    	
    	//加载service模板
    	root = new HashMap<String, Object>();
    	root.put("package", GenDomain.pkg);
    	root.put("domains", this.domainPos);
    	Template serviceTemplate = conf.getTemplate("service.ftl");
    	//合并service模板
    	serviceTemplate.process(root, new OutputStreamWriter(System.out));
    	System.out.println();
    }
    
    
    public static void main(String[] args) throws DocumentException, IOException, TemplateException{

    	GenDomain genDomain = new GenDomain();
    	genDomain.dbXmlParse();
    	genDomain.mergeDomainXml();
    	
    }
}
