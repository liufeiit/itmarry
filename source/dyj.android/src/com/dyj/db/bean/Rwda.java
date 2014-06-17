package com.dyj.db.bean;

import com.j256.ormlite.field.DatabaseField;

public class Rwda {
	private static final long serialVersionUID = -5683263669918171030L;
	@DatabaseField(id=true)
	private String Rwda_dm;
	@DatabaseField  
    private String Rw_dm;
	@DatabaseField  
	private int Sgry_dm;
	@DatabaseField  
	private int Sgbm_dm;
	@DatabaseField  
	private double Sg_wd;
	@DatabaseField  
	private double Sg_jd;
	@DatabaseField  
	private String Wcqk;
	@DatabaseField  
	private int Pjry_dm;
	@DatabaseField  
	private int Pjry_bm_dm;
	@DatabaseField  
	private int is_hg;
	@DatabaseField  
	private int Scsj;
	@DatabaseField  
	private int pjsj;
	
	
	public String getRw_dm() {
		return Rw_dm;
	}
	public void setRw_dm(String rw_dm) {
		Rw_dm = rw_dm;
	}
	public int getSgry_dm() {
		return Sgry_dm;
	}
	public void setSgry_dm(int sgry_dm) {
		Sgry_dm = sgry_dm;
	}
	public int getSgbm_dm() {
		return Sgbm_dm;
	}
	public void setSgbm_dm(int sgbm_dm) {
		Sgbm_dm = sgbm_dm;
	}
	public double getSg_wd() {
		return Sg_wd;
	}
	public void setSg_wd(double sg_wd) {
		Sg_wd = sg_wd;
	}
	public double getSg_jd() {
		return Sg_jd;
	}
	public void setSg_jd(double sg_jd) {
		Sg_jd = sg_jd;
	}
	public String getWcqk() {
		return Wcqk;
	}
	public void setWcqk(String wcqk) {
		Wcqk = wcqk;
	}
	public int getPjry_dm() {
		return Pjry_dm;
	}
	public void setPjry_dm(int pjry_dm) {
		Pjry_dm = pjry_dm;
	}
	public int getPjry_bm_dm() {
		return Pjry_bm_dm;
	}
	public void setPjry_bm_dm(int pjry_bm_dm) {
		Pjry_bm_dm = pjry_bm_dm;
	}
	public int getIs_hg() {
		return is_hg;
	}
	public void setIs_hg(int is_hg) {
		this.is_hg = is_hg;
	}
	public int getScsj() {
		return Scsj;
	}
	public void setScsj(int scsj) {
		Scsj = scsj;
	}
	public int getPjsj() {
		return pjsj;
	}
	public void setPjsj(int pjsj) {
		this.pjsj = pjsj;
	}
	public String getRwda_dm() {
		return Rwda_dm;
	}
	public void setRwda_dm(String rwda_dm) {
		Rwda_dm = rwda_dm;
	}
}
