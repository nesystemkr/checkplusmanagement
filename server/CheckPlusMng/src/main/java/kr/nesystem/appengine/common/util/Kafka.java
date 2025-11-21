package kr.nesystem.appengine.common.util;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class Kafka {
	static {
		//Kafka로그가 너무 많이 남아서 Kafka는 에러만 처리하도록 추가
//		org.apache.log4j.Logger.getLogger("org").setLevel(org.apache.log4j.Level.ERROR);
//		org.apache.log4j.Logger.getLogger("apache").setLevel(org.apache.log4j.Level.ERROR);
//		org.apache.log4j.Logger.getLogger("kafka").setLevel(org.apache.log4j.Level.ERROR);
	}
	
	public static void push(String topicName, String key, String data) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 64);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 1024 * 1024 * 2);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		try {
			ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, data);
			Future<RecordMetadata> future = producer.send(record);
			future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		producer.close();
	}
	
	public static KafkaConsumer<String, String> getConsumer(String topicName, String groupId) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topicName));
		return consumer;
	}
}
