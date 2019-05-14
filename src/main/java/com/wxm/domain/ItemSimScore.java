package com.wxm.domain;

import lombok.Data;

@Data
public class ItemSimScore {
	public ItemSimScore(){}
	
	public ItemSimScore(int id,double score){
		this.id = id;
		this.score = score;
	}
	
	private int id;
	private double score;
}
