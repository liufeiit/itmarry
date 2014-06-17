package com.dyj.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dyj.bean.beanRwgg;
import com.dyj.db.bean.Rw;
import com.dyj.db.bean.Rwda;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "dyj.db";
	private static final int DATABASE_VERSION = 9;

	private Dao<Rw, Integer> rwDao = null;
	private Dao<Rwda, Integer> rwdaDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTable(connectionSource, Rw.class);
			TableUtils.createTable(connectionSource, Rwda.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases",
					e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		// TODO Auto-generated method stub
		try {
			TableUtils.dropTable(connectionSource, Rw.class, true);
			TableUtils.dropTable(connectionSource, Rwda.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(),
					"Unable to upgrade database from version " + oldVer
							+ " to new " + newVer, e);
		}
		

	}

	public Dao<Rw, Integer> getRwDao() throws SQLException {
		if (rwDao == null) {
			rwDao = getDao(Rw.class);
		}
		return rwDao;
	}
	public Dao<Rwda, Integer> getRwDaDao() throws SQLException {
		if (rwdaDao == null) {
			rwdaDao = getDao(Rwda.class);
		}
		return rwdaDao;
	}
	
	//下载任务数据到本地数据库
	public void down_rw(beanRwgg br){
		Dao<Rw, Integer> rwDao;
		try {
			Log.d("msg","写入数据库开始");
			rwDao = this.getRwDao();
			//先删除旧数据
			
			DeleteBuilder<Rw, Integer> deleteBulder=rwDao.deleteBuilder();
			deleteBulder.where().eq("rw_dm", br.getRw_dm());
			PreparedDelete<Rw> pd=deleteBulder.prepare();
			rwDao.delete(pd);
			Rw rwBean = new Rw();
			rwBean.setRw_dm(br.getRw_dm());
			rwBean.setAzdz(br.getAzdz());
			rwBean.setCjb_jd(br.getCjb_jd());
			rwBean.setCjd_bh(br.getCjd_bh());
			rwBean.setCjd_wd(br.getCjd_wd());
			rwBean.setCjsj(br.getCjsj_i());
			rwBean.setDbzch(br.getDbzch());
			rwBean.setDh(br.getDh());
			rwBean.setDh1(br.getDh1());
			rwBean.setFbr_dm(br.getFbr_dm());
			rwBean.setFbr_mc(br.getFbr_mc());
			rwBean.setFbr_name(br.getFbr_name());
			rwBean.setFbr_tel(br.getFbr_tel());
			rwBean.setGddw(br.getGddw());
			rwBean.setGzqk(br.getGzqk());
			rwBean.setHtrl(br.getHtrl());
			rwBean.setLxr(br.getLxr());
			rwBean.setRw_lx(br.getRw_lx());
			rwBean.setRwzt_dm(6);
			rwBean.setRwzt_mc(br.getRwzt_mc());
			//rwBean.setTjsj(br.getTjsj_i());
			rwBean.setYhbh(br.getYhbh());
			rwBean.setYhmc(br.getYhmc());
			rwBean.setZbmklx(br.getZbmklx());
			rwBean.setZcbh(br.getZcbh());
			rwBean.setZddz(br.getZddz());
			rwBean.setWcqx(br.getWcqx());
			rwDao.create(rwBean);
			Log.d("msg","写入数据库结束");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e("error",e.getMessage());
			e.printStackTrace();
		}
		
		
		
	}

}
