package kr.nesystem.appengine.messagequeue.context;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.nesystem.appengine.common.model.Model;
import kr.nesystem.appengine.common.util.Kafka;
import kr.nesystem.appengine.daemon.common.BaseDaemon;
import kr.nesystem.appengine.daemon.common.StopThread;

public abstract class ConsumerBaseDaemon extends BaseDaemon {
	private MQConsumerThread consumerThread;
	
	public String __modelClass = "";
	public boolean __handleAsList = false;
	public abstract String getTopicName();
	public abstract String getGroupId();
	
	public abstract void handleItem(Model item);
	public abstract void handleList(List<Model> item);
	
	public ConsumerBaseDaemon(String modelClass) {
		this(modelClass, false);
	}
	
	public ConsumerBaseDaemon(String modelClass, boolean handleAsList) {
		super();
		__modelClass = modelClass;
		__handleAsList = handleAsList;
	}
	
	public void start() {
		consumerThread = new MQConsumerThread();
		consumerThread.start();
	}
	
	public void stop() {
		if (consumerThread != null) {
			consumerThread.setStop(true);
			consumerThread.interrupt();
		}
	}
	
	private class MQConsumerThread extends StopThread {
		public void run() {
			running = true;
			ObjectMapper mapper = new ObjectMapper();
			List<Model> tempList = new ArrayList<>();
			KafkaConsumer<String, String> consumer = Kafka.getConsumer(getTopicName(), getGroupId());
			try {
				while (true) {
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
					if (isStop == true) {
						break;
					}
					tempList.clear();
					for (ConsumerRecord<String, String> record : records) {
						if (__handleAsList == true) {
							tempList.add((Model)mapper.readValue(record.value(), Class.forName(__modelClass)));
						} else  {
							handleItem((Model)mapper.readValue(record.value(), Class.forName(__modelClass)));
						}
					}
					if (__handleAsList == true) {
						handleList(tempList);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				consumer.close();
			}
			running = false;
		}
	}
}
