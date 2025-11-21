package kr.nesystem.appengine.daemon.service;

import java.lang.reflect.Method;
import java.util.List;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import kr.nesystem.appengine.daemon.common.BaseDaemon;
import kr.nesystem.appengine.daemon.dao.DaemonDao;
import kr.nesystem.appengine.daemon.model.CM_Daemon;

public class DaemonContextListener implements ServletContextListener {
	DaemonDao dao = new DaemonDao();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			List<CM_Daemon> daemons = dao.list(null, null, -1, 0);
			if (daemons != null) {
				CM_Daemon daemon;
				for (int ii=0; ii<daemons.size(); ii++) {
					daemon = daemons.get(ii);
					if ("Y".equals(daemon.getAutoStartYN())) {
						startDaemon(daemon);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			List<CM_Daemon> daemons = dao.list(null, null, -1, 0);
			CM_Daemon daemon;
			for (int ii=0; ii<daemons.size(); ii++) {
				daemon = daemons.get(ii);
				stopDaemon(daemon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isRunning(CM_Daemon item) {
		try {
			if (item.getClassName() == null || item.getClassName().length() == 0) {
				return false;
			}
			Class<?> cls = Class.forName(item.getClassName());
			Method m = cls.getMethod("checkRunning", String.class);
			return (boolean)m.invoke(null, item.getClassName()); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void startDaemon(CM_Daemon item) {
		try {
			if (item.getClassName() == null || item.getClassName().length() == 0) {
				return;
			}
			Class<?> cls = Class.forName(item.getClassName());
			Method m = cls.getMethod("initSingleton", String.class);
			m.invoke(null, item.getClassName()); 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void stopDaemon(CM_Daemon item) {
		try {
			if (item.getClassName() == null || item.getClassName().length() == 0) {
				return;
			}
			Class<?> cls = Class.forName(item.getClassName());
			Method m = cls.getMethod("stopSingleton", String.class);
			m.invoke(null, item.getClassName()); 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BaseDaemon getDaemon(CM_Daemon item) {
		try {
			if (item.getClassName() == null || item.getClassName().length() == 0) {
				return null;
			}
			Class<?> cls = Class.forName(item.getClassName());
			Method m = cls.getMethod("getInstance", String.class);
			return (BaseDaemon)m.invoke(null, item.getClassName()); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
