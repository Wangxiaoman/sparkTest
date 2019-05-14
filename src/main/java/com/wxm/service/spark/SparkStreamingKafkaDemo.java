package com.wxm.service.spark;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.google.common.base.Splitter;

import scala.Tuple2;

public class SparkStreamingKafkaDemo {
	private static Map<String, AtomicInteger> wordCount = new ConcurrentHashMap<>();

	private static void sparkStreamingKafkaWordCount(String brokers, String group, String topics, int threadCount) {

		SparkConf conf = new SparkConf().setAppName("name").setMaster("local[" + threadCount + "]");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));

		JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(jssc,
				LocationStrategies.PreferConsistent(), ConsumerStrategies.<String, String> Subscribe(
						Splitter.on(",").splitToList(topics), getKafkaParams(brokers, group)));

		JavaDStream<String> records = stream.map(record -> record.value());

		records.foreachRDD(rdd -> {
			JavaRDD<String> words = rdd.flatMap(l -> Splitter.on(" ").split(String.valueOf(l)).iterator());
			JavaPairRDD<String, Integer> pairs = words.mapToPair(w -> new Tuple2<String, Integer>(w, 1));
			JavaPairRDD<String, Integer> wordCounts = pairs.reduceByKey((a, b) -> (a + b));
			wordCounts.foreach(tuple -> {
				AtomicInteger v = wordCount.getOrDefault(tuple._1, new AtomicInteger(0));
				v.addAndGet(tuple._2);
				wordCount.put(tuple._1, v);
			});

			System.out.println("word count map:" + wordCount);
		});
		jssc.start();
		
		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Object> getKafkaParams(String brokerList, String groupId) {
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", brokerList);
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", groupId);
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);
		return kafkaParams;
	}

	// kafka打入数据
	// 创建topic: kafka-topics --create --zookeeper localhost:2181
	// --replication-factor 1 --partitions 1 --topic words
	// 查询topic: kafka-topics.sh --list --zookeeper localhost:2181
	// 写入数据:
	public static void main(String[] args) {
		sparkStreamingKafkaWordCount("localhost:9092", "words", "words", 2);
	}
}
