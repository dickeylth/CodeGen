import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ${package}.domain.Permission;
import ${package}.domain.Role;
import ${package}.domain.User;
import ${package}.service.UserService;


public class DatabaseInit extends HttpServlet implements ServletContextListener {

	/**
	 * 默认序列化UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		WebApplicationContext actx = 
				WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		UserService userService = (UserService) actx.getBean("userService");
		
		if(userService.findUsers().isEmpty() && userService.findRoles().isEmpty() 
				&& userService.findPermissions().isEmpty()){
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword("21232f297a57a5a743894a0e4a801fc3");
			
			Role role = new Role();
			role.setRolename("ROLE_ADMIN");
			
			String[] domains = {<#list domains as domain>"${domain.name}",</#list>""};
			String[] ops = {"manage", "add", "delete"};
			List<Permission> permissions = new LinkedList<Permission>();
			for (String domain : domains) {
				if(!domain.equals("")){
					for (String op : ops) {
						Permission permission = new Permission();
						permission.setPermission(domain + ":" + op);
						userService.addPermission(permission);
						permissions.add(permission);
					}
				}
			}
			
			role.setPermissions(permissions);
			userService.addRole(role);
			
			admin.getRoles().add(role);
			userService.addUser(admin);
			
			System.out.println("---------------数据库初始化完毕--------------");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
}
