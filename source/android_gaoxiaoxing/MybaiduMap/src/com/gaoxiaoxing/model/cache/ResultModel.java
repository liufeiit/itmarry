package com.gaoxiaoxing.model.cache;

public class ResultModel<Re> {
	Re r=null;
	
	public ResultModel(Re re){
		r=re;
	}
	
	public Re getResult(){
		return this.r;
	}
	
}
