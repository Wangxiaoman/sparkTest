package com.wxm.service.forkjoin;

import lombok.Data;

@Data
public class OrderInfo {
	private User user;
	private Food food;
	private Coffee coffee;
	
	public OrderInfo(){
		
	}
	
	public OrderInfo(User user,Food food,Coffee coffee){
		this.user = user;
		this.coffee = coffee;
		this.food = food;
	}

	@Override
	public String toString() {
		return "OrderInfo [user=" + user + ", food=" + food + ", coffee=" + coffee + "]";
	}
	
}
