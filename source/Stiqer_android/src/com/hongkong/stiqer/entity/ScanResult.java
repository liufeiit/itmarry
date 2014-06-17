package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class ScanResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6248325093359852703L;
	private int       trans_type;
	private String    trans_id;
	private String    trans_store_id;
	private String    trans_store_name;
	private String    trans_des;
	private int       trans_stiqer_num;
	private int       trans_egg_num;
	private int       total_stiqer_num;
	private int       total_egg_num;
	private int       error_code;
	private int       new_level;
	private int       new_class;
	private String    error_msg;

	public int getNew_level() {
		return new_level;
	}
	public void setNew_level(int new_level) {
		this.new_level = new_level;
	}
	public int getNew_class() {
		return new_class;
	}
	public void setNew_class(int new_class) {
		this.new_class = new_class;
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
	public int getTrans_type() {
		return trans_type;
	}
	public void setTrans_type(int trans_type) {
		this.trans_type = trans_type;
	}
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public String getTrans_store_id() {
		return trans_store_id;
	}
	public void setTrans_store_id(String trans_store_id) {
		this.trans_store_id = trans_store_id;
	}
	public String getTrans_store_name() {
		return trans_store_name;
	}
	public void setTrans_store_name(String trans_store_name) {
		this.trans_store_name = trans_store_name;
	}
	public String getTrans_des() {
		return trans_des;
	}
	public void setTrans_des(String trans_des) {
		this.trans_des = trans_des;
	}
	public int getTrans_stiqer_num() {
		return trans_stiqer_num;
	}
	public void setTrans_stiqer_num(int trans_stiqer_num) {
		this.trans_stiqer_num = trans_stiqer_num;
	}
	public int getTrans_egg_num() {
		return trans_egg_num;
	}
	public void setTrans_egg_num(int trans_egg_num) {
		this.trans_egg_num = trans_egg_num;
	}
	public int getTotal_stiqer_num() {
		return total_stiqer_num;
	}
	public void setTotal_stiqer_num(int total_stiqer_num) {
		this.total_stiqer_num = total_stiqer_num;
	}
	public int getTotal_egg_num() {
		return total_egg_num;
	}
	public void setTotal_egg_num(int total_egg_num) {
		this.total_egg_num = total_egg_num;
	}

}
