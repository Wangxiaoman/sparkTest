package com.wxm.service.spark;

import static org.apache.spark.sql.functions.col;

import java.io.Serializable;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkDataSetDemo {
	public static void main(String[] args) {
		// configure spark
		SparkSession spark = SparkSession.builder().appName("Read JSON File to DataSet").master("local[2]")
				.getOrCreate();

		// Java Bean (data class) used to apply schema to JSON data
		Encoder<Employee> employeeEncoder = Encoders.bean(Employee.class);

		String jsonPath = "/Users/xiaomanwang/tool/hadoop/hdfs/tmp/student.json";

		// read JSON file to Dataset
		Dataset<Employee> ds = spark.read().json(jsonPath).as(employeeEncoder);
		System.out.println(ds.count());
		ds.show();
		ds.printSchema();
		ds.select("name").show();
		ds.filter(col("age").gt(21)).show();
		
		String jsonPath1 = "/Users/xiaomanwang/tool/hadoop/hdfs/tmp/student1.json";
		
		Dataset<Row> ds1 = spark.read().json(jsonPath);
		Dataset<Row> ds2 = spark.read().json(jsonPath1);
		Dataset<Row> union = ds1.union(ds2);
		union.show();
		
		spark.conf().set("spark.sql.crossJoin.enabled", "true");
		Dataset<Row> joiner = ds1.join(ds2);
		joiner.show();
		
		spark.stop();
	}
	
}

class Employee implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public int age;
}
