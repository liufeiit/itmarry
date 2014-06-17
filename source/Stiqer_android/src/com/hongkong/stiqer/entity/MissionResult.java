package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class MissionResult implements Serializable{
	
	private static final long serialVersionUID = -6426623685621883496L;
	
	private String    mission_id;
	private String    mission_img;
    private String    redeem_id;
	private String    veri_code;
	private String    veri_qr_img;
	private String    description;
	private String    timestamp;
	private String    uid;
	private int       error_code;
	private String    error_msg;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getMission_id() {
		return mission_id;
	}
	public void setMission_id(String mission_id) {
		this.mission_id = mission_id;
	}
	public String getMission_img() {
		return mission_img;
	}
	public void setMission_img(String mission_img) {
		this.mission_img = mission_img;
	}
	public String getRedeem_id() {
		return redeem_id;
	}
	public void setRedeem_id(String redeem_id) {
		this.redeem_id = redeem_id;
	}
	public String getVeri_code() {
		return veri_code;
	}
	public void setVeri_code(String veri_code) {
		this.veri_code = veri_code;
	}
	public String getVeri_qr_img() {
		return veri_qr_img;
	}
	public void setVeri_qr_img(String veri_qr_img) {
		this.veri_qr_img = veri_qr_img;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
