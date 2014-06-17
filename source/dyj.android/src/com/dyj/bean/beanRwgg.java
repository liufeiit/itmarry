package com.dyj.bean;

import java.text.DecimalFormat;

import android.util.Log;

import com.dyj.untils.GetTimeUtil;

public class beanRwgg {
	private String Rw_dm;
	private String Cjd_bh;
	private String Fbr_name;
	private String Fbr_mc;
	private String Fbr_tel;
	private String Rw_lx;
	private String Gzqk;
	private String Cjsj;
	private String Tjsj;
	private String Fbr_dm;
	private String Rwzt_dm;
	private String Rwzt_mc;
	
	private String Yhbh;
	private String Yhmc;
	private String Gddw;
	private String Azdz;
	private String Zddz;
	private String Zcbh;
	private String Htrl;
	private String Dbzch;
	private String Lxr;
	private String Dh;
	private String Dh1;
	private String Zbmklx;
	private String Cjb_jd;
	private String Cjd_wd;
	private String has_rwda;
	private String Wcqx;
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
	public String getFbr_dm() {
		return Fbr_dm;
	}
	public void setFbr_dm(String fbr_dm) {
		Fbr_dm = fbr_dm;
	}
	public String getRwzt_dm() {
		return Rwzt_dm;
	}
	public void setRwzt_dm(String rwzt_dm) {
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
	public String getCjsj() {
		//Log.d("cjsj is :",this.Cjsj+"");
		//return this.Cjsj;
		return GetTimeUtil.getTime(Integer.parseInt(this.Cjsj));
	}
	public int getCjsj_i(){
		return Integer.parseInt(this.Cjsj);
	}
	public void setCjsj(String cjsj) {
		Cjsj = cjsj;
	}
	public String getTjsj() {
		return GetTimeUtil.getTime(Integer.parseInt(this.Tjsj));
	}
	public int getTjsj_i(){
		return Integer.parseInt(this.Tjsj);
	}
	public void setTjsj(String tjsj) {
		Tjsj = tjsj;
	}
	public String getHas_rwda() {
		return has_rwda;
	}
	public void setHas_rwda(String has_rwda) {
		this.has_rwda = has_rwda;
	}
	public String getWcqx() {
		if(0==Integer.parseInt(this.Wcqx)){
			return "-";
		}
		else{
		return GetTimeUtil.getTime(Integer.parseInt(this.Wcqx));
		}
	}
	public void setWcqx(String wcqx) {
		this.Wcqx = wcqx;
	}
	
	
	

}
