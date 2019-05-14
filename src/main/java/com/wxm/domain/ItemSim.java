package com.wxm.domain;

import lombok.Data;

@Data
public class ItemSim {
	public ItemSim(){}
	public ItemSim(int fromItemId,int toItemId){
		this.fromItemId = fromItemId;
		this.toItemId = toItemId;
	}
	
	private int fromItemId;
	private int toItemId;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemSim other = (ItemSim) obj;
		if (fromItemId != other.fromItemId)
			return false;
		if (toItemId != other.toItemId)
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fromItemId;
		result = prime * result + toItemId;
		return result;
	}
}
