package com.wxm.service.forkjoin;

import lombok.Data;

@Data
public class Food {
	private int id;
	private String name;
	@Override
	public String toString() {
		return "Food [id=" + id + ", name=" + name + "]";
	}
}
