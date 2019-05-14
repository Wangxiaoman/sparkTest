package com.wxm.service.juc;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentNavigableMapDemo {
	public static void main(String[] args) {
		ConcurrentNavigableMap<Integer,String> map = new ConcurrentSkipListMap<>(); 
		map.put(1, "1");
		map.put(2, "2");
		map.put(3, "3");
		map.put(4, "4");
		map.put(5, "5");
		map.put(6, "6");
		
		// head tail desc 方法都是通过subMap来实现的
		System.out.println(map.subMap(1, true, 3, true));
		System.out.println(map.headMap(4));
		System.out.println(map.tailMap(5));
		System.out.println(map.descendingMap());
	}
}
