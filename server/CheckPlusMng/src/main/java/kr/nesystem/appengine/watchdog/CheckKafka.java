package kr.nesystem.appengine.watchdog;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;

public class CheckKafka implements ServletContextListener {
	ProcessChecker checker;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		checker = new ProcessChecker();
		checker.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	public void runCommand(String command) {
		
	}
	
	public boolean isKafkaRunning() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("connections.max.idle.ms", 10000);
		properties.put("request.timeout.ms", 5000);
		try (AdminClient client = KafkaAdminClient.create(properties)) {
			ListTopicsResult topics = client.listTopics();
			Set<String> names = topics.names().get();
			if (names.isEmpty()) {
				return false;
			}
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private class ProcessChecker extends Thread {
		public ProcessChecker() {
			super();
			setDaemon(true);
		}
		
		public void run() {
			try {
				boolean isRunning = isKafkaRunning();
				if (isRunning == false) {
					runCommand("");
				}
				Thread.sleep(10000); //1분마다 체크
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
