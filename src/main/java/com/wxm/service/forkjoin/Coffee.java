package com.wxm.service.forkjoin;

import lombok.Data;

@Data	
public class Coffee {
	private int id;
	private String name;
	@Override
	public String toString() {
		return "Coffee [id=" + id + ", name=" + name + "]";
	}
}
