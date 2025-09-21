package kr.peelknight.daemon.context;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseDaemon {
	protected static Map<String, BaseDaemon> singletonMap = new HashMap<>();
	public boolean running = false;
	
	public static boolean checkRunning(String className) {
		BaseDaemon singleton = singletonMap.get(className);
		if (singleton == null) {
			return false;
		}
		return singleton.isRunning();
	}
	
	public static void initSingleton(String className) {
		BaseDaemon singleton = singletonMap.get(className);
		if (singleton == null) {
			try {
				Class<?> clazz = Class.forName(className);
				Constructor<?> ctor = clazz.getConstructor();
				singleton = (BaseDaemon)ctor.newInstance();
				singleton.start();
				singletonMap.put(className,  singleton);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void stopSingleton(String className) {
		BaseDaemon singleton = singletonMap.get(className);
		if (singleton == null) {
			return;
		}
		singleton.stop();
		singletonMap.remove(className);
	}
	
	public static synchronized BaseDaemon getInstance(String className) {
		return singletonMap.get(className);
	}
	
	public abstract void start();
	public abstract void stop();
	public boolean isRunning() {
		return running;
	}
}
