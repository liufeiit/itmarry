package com.dyj.db.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class Rw implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5683263669918171030L;
	@DatabaseField(id=true)
	private String Rw_dm;
	@DatabaseField(defaultValue="")   
	private String Cjd_bh;
	@DatabaseField(defaultValue="")  
	private String Fbr_name;
	@DatabaseField(defaultValue="")  
	private String Fbr_mc;
	@DatabaseField(defaultValue="")  
	private String Fbr_tel;
	@DatabaseField(defaultValue="")  
	private String Rw_lx;
	@DatabaseField(defaultValue="") 
	private String Gzqk;
	@DatabaseField 
	private int Cjsj;
	@DatabaseField 
	private int Tjsj;
	@DatabaseField(defaultValue="")  
	private String Fbr_dm;
	@DatabaseField 
	private int Rwzt_dm;
	@DatabaseField(defaultValue="")  
	private String Rwzt_mc;
	
	@DatabaseField(defaultValue="")  
	private String Yhbh;
	@DatabaseField(defaultValue="") 
	private String Yhmc;
	@DatabaseField(defaultValue="") 
	private String Gddw;
	@DatabaseField(defaultValue="")  
	private String Azdz;
	@DatabaseField(defaultValue="")  
	private String Zddz;
	@DatabaseField(defaultValue="")  
	private String Zcbh;
	@DatabaseField(defaultValue="")  
	private String Htrl;
	@DatabaseField(defaultValue="")  
	private String Dbzch;
	@DatabaseField(defaultValue="")  
	private String Lxr;
	@DatabaseField(defaultValue="")  
	private String Dh;
	@DatabaseField(defaultValue="")  
	private String Dh1;
	@DatabaseField(defaultValue="")  
	private String Zbmklx;
	@DatabaseField(defaultValue="")  
	private String Cjb_jd;
	@DatabaseField(defaultValue="")  
	private String Cjd_wd;
	@DatabaseField(defaultValue="")  
	private String wcqx;
	public String getWcqx() {
		return wcqx;
	}

	public void setWcqx(String wcqx) {
		this.wcqx = wcqx;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRw_dm() {
		return Rw_dm;
	}

	public void setRw_dm(String rw_dm) {
		Rw_dm = rw_dm;
	}

	public String getCjd_bh() {
		return Cjd_bh;
	}

	public void setCjd_bh(String cjd_bh) {
		Cjd_bh = cjd_bh;
	}

	public String getFbr_name() {
		return Fbr_name;
	}

	public void setFbr_name(String fbr_name) {
		Fbr_name = fbr_name;
	}

	public String getFbr_mc() {
		return Fbr_mc;
	}

	public void setFbr_mc(String fbr_mc) {
		Fbr_mc = fbr_mc;
	}

	public String getFbr_tel() {
		return Fbr_tel;
	}

	public void setFbr_tel(String fbr_tel) {
		Fbr_tel = fbr_tel;
	}

	public String getRw_lx() {
		return Rw_lx;
	}

	public void setRw_lx(String rw_lx) {
		Rw_lx = rw_lx;
	}

	public String getGzqk() {
		return Gzqk;
	}

	public void setGzqk(String gzqk) {
		Gzqk = gzqk;
	}

	public int getCjsj() {
		return Cjsj;
	}

	public void setCjsj(int cjsj) {
		Cjsj = cjsj;
	}

	public int getTjsj() {
		return Tjsj;
	}

	public void setTjsj(int tjsj) {
		Tjsj = tjsj;
	}

	public String getFbr_dm() {
		return Fbr_dm;
	}

	public void setFbr_dm(String fbr_dm) {
		Fbr_dm = fbr_dm;
	}

	public int getRwzt_dm() {
		return Rwzt_dm;
	}

	public void setRwzt_dm(int rwzt_dm) {
		Rwzt_dm = rwzt_dm;
	}

	public String getRwzt_mc() {
		return Rwzt_mc;
	}

	public void setRwzt_mc(String rwzt_mc) {
		Rwzt_mc = rwzt_mc;
	}

	public String getYhbh() {
		return Yhbh;
	}

	public void setYhbh(String yhbh) {
		Yhbh = yhbh;
	}

	public String getYhmc() {
		return Yhmc;
	}

	public void setYhmc(String yhmc) {
		Yhmc = yhmc;
	}

	public String getGddw() {
		return Gddw;
	}

	public void setGddw(String gddw) {
		Gddw = gddw;
	}

	public String getAzdz() {
		return Azdz;
	}

	public void setAzdz(String azdz) {
		Azdz = azdz;
	}

	public String getZddz() {
		return Zddz;
	}

	public void setZddz(String zddz) {
		Zddz = zddz;
	}

	public String getZcbh() {
		return Zcbh;
	}

	public void setZcbh(String zcbh) {
		Zcbh = zcbh;
	}

	public String getHtrl() {
		return Htrl;
	}

	public void setHtrl(String htrl) {
		Htrl = htrl;
	}

	public String getDbzch() {
		return Dbzch;
	}

	public void setDbzch(String dbzch) {
		Dbzch = dbzch;
	}

	public String getLxr() {
		return Lxr;
	}

	public void setLxr(String lxr) {
		Lxr = lxr;
	}

	public String getDh() {
		return Dh;
	}

	public void setDh(String dh) {
		Dh = dh;
	}

	public String getDh1() {
		return Dh1;
	}

	public void setDh1(String dh1) {
		Dh1 = dh1;
	}

	public String getZbmklx() {
		return Zbmklx;
	}

	public void setZbmklx(String zbmklx) {
		Zbmklx = zbmklx;
	}

	public String getCjb_jd() {
		return Cjb_jd;
	}

	public void setCjb_jd(String cjb_jd) {
		Cjb_jd = cjb_jd;
	}

	public String getCjd_wd() {
		return Cjd_wd;
	}

	public void setCjd_wd(String cjd_wd) {
		Cjd_wd = cjd_wd;
	}
	
	
	
}
