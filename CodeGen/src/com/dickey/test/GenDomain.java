/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dickey.test;

import com.dickey.domain.DomainPo;
import com.dickey.domain.PropertyPo;
import com.dickey.domain.RefClassPo;

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
		List<Element> domains = root.elements("class");
        for (Element domain : domains) {
            String name = (String) domain.elementText("classname");
            String cnName = (String) domain.elementText("classname_cn");
            
            List<PropertyPo> propertyPos = new LinkedList<>();
            DomainPo domainPo = new DomainPo(name, cnName, propertyPos);
            
            //遍历columns
            @SuppressWarnings("unchecked")
			List<Element> properties = domain.element("properties").elements("property");
            for (Element property : properties) {
                String propName = property.elementText("name");
                String propCnName = property.elementText("name_cn");
                String propType = property.elementText("type");
                boolean propPk = Boolean.valueOf(property.elementText("pk"));
                boolean propEmpty = Boolean.valueOf(property.elementText("nullable"));
                boolean propIdx = Boolean.valueOf(property.elementText("unique"));
                
                Element propRef = property.element("ref");
                
                //处理外键关联
                RefClassPo refClassPo = null;
                if(!propRef.getStringValue().equals("false")){
                    String refClass = propRef.elementText("refClass");
                    String refType = propRef.elementText("refType");
                    
                    refClassPo = new RefClassPo(refClass, refType);
                }
                
                PropertyPo propertyPo = new PropertyPo(propName, propCnName, propType, propEmpty, propIdx, propPk, refClassPo);
                domainPo.getProperties().add(propertyPo);
            }
            System.out.println(domainPo);
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
    public void mergeXmlDomain() throws IOException, TemplateException{
    	//Freemarker初始化工作
    	Configuration conf = new Configuration();
    	conf.setDirectoryForTemplateLoading(new File("src/templates"));
    	
    	Map<String, Object> root;
    	for (DomainPo domainPo : this.domainPos) {
    		root = new HashMap<String, Object>();
        	root.put("package", GenDomain.pkg);
        	root.put("domain", domainPo);
        	//使用Configuration实例来加载指定模板
        	Template template = conf.getTemplate("domain.ftl");
        	//合并模板
        	template.process(root, new OutputStreamWriter(System.out));
		}
    	
    }
    
    
    public static void main(String[] args) throws DocumentException, IOException, TemplateException{
    	GenDomain genDomain = new GenDomain();
    	genDomain.dbXmlParse();
    	genDomain.mergeXmlDomain();
    	
    }
}
