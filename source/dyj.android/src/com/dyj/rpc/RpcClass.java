package com.dyj.rpc;

import java.util.HashMap;

import android.R.integer;

public interface RpcClass {
	public HashMap checkLogin(String user_mc,String password);
	public HashMap getNewRwList(byte[] phpSerializer,int pageIndex,int size);
	public HashMap getRwDisp(String rw_dm);
	public HashMap downRw(int user_dm,int rw_dm);
	public HashMap rw_submit(int rw_dm, int sgry_dm,String sg_wd,String sg_jd,
			String wcqk,int is_ghsb,String new_zcbh,String old_jsr,String old_tel,
			String old_cfdd,
			String image_name, byte[] image_data);
	public HashMap rw_submit1(int rw_dm, int sgry_dm,int is_gxdlxx,String sg_wd,String sg_jd,
			String wcqk,int is_ghsb,String new_zcbh,String old_jsr,String old_tel,
			String old_cfdd,
			String image_name, byte[] image_data);
	public HashMap getLsRwList(byte[] phpSerializer, int pageIndex, int size);
	public HashMap myRw(byte[] phpSerializer, int pageIndex, int size);
	public HashMap reDownRw(int user_dm, int rw_dm);
	public HashMap returnRw(int rw_dm);
	public HashMap updatePushUserId(String user_dm,String push_user_id);
	//返回任务统计
	public HashMap getTotalRw(String bm_dm,long begin,long end);
	
	//获取通知列表
	public HashMap getContentList(String bm_dm,int pageIndex ,int size );
	//查看通知详情
	public HashMap getContentInfo(int id);
}
