package com.wxm.service.spark;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.spark_project.guava.base.Objects;

import scala.Tuple2;

public class SparkRddDemo {
	public static void main(String[] args) {
		String fileName = "/Users/xiaomanwang/tool/hadoop/words.txt";
		SparkConf conf = new SparkConf().setAppName("Word Count").setMaster("local[2]").set("spark.executor.memory",
				"1g");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(fileName);
		JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(s.split(" ")).iterator());
		System.out.println(words.collect());
		
		Function<String,Boolean> filter = x -> (!Objects.equal(x, "test"));
		JavaRDD<String> filterWords = words.filter(filter);
		System.out.println(filterWords.collect());
		
		JavaPairRDD<String, Integer> map = filterWords.mapToPair(word -> new Tuple2<>(word, 1)).reduceByKey((a, b) -> a + b);
		System.out.println(map.collectAsMap());
		
		map.saveAsTextFile("/Users/xiaomanwang/tool/hadoop/hdfs/tmp/tt");
		sc.close();
	}
}
