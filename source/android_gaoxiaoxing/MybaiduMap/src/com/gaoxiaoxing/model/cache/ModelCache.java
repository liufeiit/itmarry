package com.gaoxiaoxing.model.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ModelCache {
	
	private static Map<String, HashMap<String, Object>> ModelManager;
	private static ModelCache instance;
	
	static{
		ModelManager=new HashMap<String, HashMap<String,Object>>();
	}
	
	private ModelCache(){
		instance=this;
	}
	
	public static ModelCache getInstance(){
		if(instance==null){
			instance=new ModelCache();
		}
		return instance;
	}
	
	public Map getPrivateModel(Object obj){
		return ModelManager.get(obj.getClass().getName());
	}
	
	
}
