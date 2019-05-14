package com.wxm.service.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import lombok.Data;

public class Neo4jService {
	private static Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "neo4j1"));

	private static void create(Student s) {
		Session session = driver.session();
		session.run("CREATE (s:Student {name:" + s.getName() + ",id:" + s.getId() + "})");
		session.close();
	}

	private static void createRelation() {
		Session session = driver.session();
		session.run("match (s:Student),(t:Teacher) where s.id=4 create (s)-[r:study]->(t)");
		session.run("match (s:Student),(t:Teacher) where s.id=4 create (t)-[r:teach]->(s)");
		session.close();
	}

	public static void main(String[] args) {
		try {
			create(new Student(5,"xiaoT"));
			createRelation();
		} finally {
			driver.close();
		}
	}
}

@Data
class Student {
	private int id;
	private String name;
	private int age;

	public Student() {
	}

	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}
}

@Data
class Teacher {
	private String name;
}
