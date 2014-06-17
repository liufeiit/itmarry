package com.gaoxiaoxing.model.cache;

public class ApplyCard {
	
	private String proposer;
	private String request;
	
	public ApplyCard(String clsName){
		proposer=clsName;
	}
	
	public void writeRequest(String msg){
		request=msg;
	}
	
}
