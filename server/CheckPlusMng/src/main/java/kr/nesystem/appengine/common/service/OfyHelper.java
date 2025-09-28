package kr.nesystem.appengine.common.service;

import com.googlecode.objectify.ObjectifyService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import kr.nesystem.appengine.common.model.CM_Code;
import kr.nesystem.appengine.common.model.CM_User;
import kr.nesystem.appengine.common.model.CM_CodeType;

public class OfyHelper implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ObjectifyService.init();
		ObjectifyService.register(CM_CodeType.class);
		ObjectifyService.register(CM_Code.class);
		ObjectifyService.register(CM_User.class);
		System.out.println("ObjectifyService inited");
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
