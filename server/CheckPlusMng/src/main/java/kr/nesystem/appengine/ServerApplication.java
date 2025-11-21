package kr.nesystem.appengine;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class ServerApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		// register resources and features
		classes.add(MultiPartFeature.class);
		return classes;
	}
}
