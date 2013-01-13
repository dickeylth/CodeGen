package ${package}.domain.${className};
import java.util.*;
import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ${className} implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/*定义基本数据类型*/
	<#assign baseTypes = ['String', 'int', 'Date', 'boolean']/>
	
	<#list domain.properties as property>
	<#if property.pk = true>
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(generator = "idGenerator")
	@Column(name="${className?lower_case}_id", unique=true, nullable=false)
	<#elseif baseTypes?seq_contains(property.type) >
	@Column(unique=${property.unique}, nullable=${property.nullable})
	<#elseif property.ref != null>
	
	</#if>
	private ${property.type} ${property.name};
	
	public ${property.type} get${property.name?cap_first}(){
		return ${property.name};
	}
	
	public void set${property.name?cap_first}(${property.type} ${property.name}){
		this.${property.name} = ${property.name};
	}
	</#list>
}