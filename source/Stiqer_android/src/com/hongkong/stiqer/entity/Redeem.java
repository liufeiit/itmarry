package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Redeem implements Serializable{

	private static final long serialVersionUID = 1L;

    private String  store_id;
    private String  store_name;
    
    private String  promo_id;
    private String  promo_des;
    private String  trans_id;
   
    private int     error_code;
    private String  error_msg;
    
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getPromo_id() {
		return promo_id;
	}
	public void setPromo_id(String promo_id) {
		this.promo_id = promo_id;
	}
	public String getPromo_des() {
		return promo_des;
	}
	public void setPromo_des(String promo_des) {
		this.promo_des = promo_des;
	}
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
    
    
    
}