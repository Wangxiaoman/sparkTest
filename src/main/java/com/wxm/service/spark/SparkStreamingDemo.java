package com.wxm.service.spark;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.google.common.base.Splitter;

import scala.Tuple2;

public class SparkStreamingDemo {
	private static Map<String, AtomicInteger> wordCount = new ConcurrentHashMap<>();
	private static JavaStreamingContext jssc;
	
	private static void sparkStreamingWordCount(String name,String host,int port,int threadCount){
		SparkConf conf = new SparkConf().setAppName("name").setMaster("local["+threadCount+"]");
		jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		// connect host port
		JavaReceiverInputDStream<String> lines = jssc.socketTextStream(host, port);
		
		JavaDStream<String> words = lines.flatMap(l -> Splitter.on(" ").split(l).iterator());
		JavaPairDStream<String, Integer> pairs = words.mapToPair(w ->new Tuple2<String,Integer>(w,1));
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((a,b) -> (a+b));
		wordCounts.print();
		
		// 将统计的数据写入map中
		wordCounts.foreachRDD((VoidFunction<JavaPairRDD<String, Integer>>) pairRDD -> {
			pairRDD.foreach((VoidFunction<Tuple2<String, Integer>>) tuple -> {
				AtomicInteger v = wordCount.getOrDefault(tuple._1, new AtomicInteger(0));
				v.addAndGet(tuple._2);
				wordCount.put(tuple._1,v);
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
	
	// nc -lk 9999
	// 通过nc向端口9999打入数据
	public static void main(String[] args) {
		sparkStreamingWordCount("wordCount", "localhost", 9999, 2);
	}
}
