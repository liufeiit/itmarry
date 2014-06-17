package com.guide.geek.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import com.guide.geek.utils.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Window;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressLint("NewApi")
public class AppConfig {

	private final static String APP_CONFIG = "config";

	public final static String TEMP_TWEET = "temp_tweet";
	public final static String TEMP_TWEET_IMAGE = "temp_tweet_image";
	public final static String TEMP_MESSAGE = "temp_message";
	public final static String TEMP_COMMENT = "temp_comment";
	public final static String TEMP_POST_TITLE = "temp_post_title";
	public final static String TEMP_POST_CATALOG = "temp_post_catalog";
	public final static String TEMP_POST_CONTENT = "temp_post_content";

	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	public final static String CONF_COOKIE = "cookie";
	public final static String CONF_ACCESSTOKEN = "accessToken";
	public final static String CONF_ACCESSSECRET = "accessSecret";
	public final static String CONF_EXPIRESIN = "expiresIn";
	public final static String CONF_LOAD_IMAGE = "perf_loadimage";
	public final static String CONF_SCROLL = "perf_scroll";
	public final static String CONF_HTTPS_LOGIN = "perf_httpslogin";
	public final static String CONF_VOICE = "perf_voice";
	public final static String CONF_CHECKUP = "perf_checkup";
	
	public final static String APP_PATH_NAME = "Geek";
	public final static String APP_PATH_ROOT = Environment.getExternalStorageDirectory()+ File.separator+ APP_PATH_NAME + File.separator;
	
	public final static String SAVE_IMAGE_PATH = "save_image_path";
	@SuppressLint("NewApi")
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory()+ File.separator+ APP_PATH_NAME + File.separator;
			
	private Context mContext;
	private static AppConfig appConfig;

	
	public static String API_URL = "http://116.255.197.213/t/t/index.php/";
	  
//	/*-- MENU菜单选项下标 --*/
//	public static final int ITEM_SEARCH = 0;// 搜索
//	public static final int ITEM_FILE_MANAGER = 1;// 文件管理
//	public static final int ITEM_DOWN_MANAGER = 2;// 下载管理
//	public static final int ITEM_FULLSCREEN = 3;// 全屏
//	public static final int ITEM_MORE = 11;// 菜单
	
	/*-- MENU菜单选项下标 --*/
	public static final int ITEM_LOGOUT = 0;// 注销
	public static final int ITEM_EXIT = 1;// 退出

	/*-- Toolbar底部菜单选项下标--*/
	public static final int TOOLBAR_ITEM_PAGEHOME = 0;// 首页
	public static final int TOOLBAR_ITEM_MESSAGE = 1;// 聊天
	public static final int TOOLBAR_ITEM_FRIEND = 2;// 好友
	public static final int TOOLBAR_ITEM_RECOGNIZE = 3;// 识脸
	public static final int TOOLBAR_ITEM_AROUND = 4;// 附近
//	/** 菜单图片 **/
//	public static final int[] menu_image_array = { R.drawable.menu_return, R.drawable.menu_quit
//		};
	/** 菜单文字 **/
	public static final String[] menu_name_array = { "注销", "退出"
	};
//		"加入书签", "分享页面", "退出", "夜间模式", "刷新", "更多" };
//	/** 菜单图片 **/
//	public static final int[] menu_image_array = { R.drawable.menu_search,
//			R.drawable.menu_filemanager, R.drawable.menu_downmanager,
//			R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
//			R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
//			R.drawable.menu_sharepage, R.drawable.menu_quit,
//			R.drawable.menu_nightmode, R.drawable.menu_refresh,
//			R.drawable.menu_more };
//	/** 菜单文字 **/
//	public static final String[] menu_name_array = { "搜索", "文件管理", "下载管理", "全屏", "网址", "书签",
//			"加入书签", "分享页面", "退出", "夜间模式", "刷新", "更多" };
//	/** 菜单图片2 **/
//	public static final int[] menu_image_array2 = { R.drawable.menu_auto_landscape,
//			R.drawable.menu_penselectmodel, R.drawable.menu_page_attr,
//			R.drawable.menu_novel_mode, R.drawable.menu_page_updown,
//			R.drawable.menu_checkupdate, R.drawable.menu_checknet,
//			R.drawable.menu_refreshtimer, R.drawable.menu_syssettings,
//			R.drawable.menu_help, R.drawable.menu_about, R.drawable.menu_return };
//	/** 菜单文字2 **/
//	public static  final String[] menu_name_array2 = { "自动横屏", "笔选模式", "阅读模式", "浏览模式", "快捷翻页",
//			"检查更新", "检查网络", "定时刷新", "设置", "帮助", "关于", "返回" };
//
//	/** 底部菜单图片 **/
//	public static final int[] menu_toolbar_image_array = { 
//			R.drawable.topic,
//			R.drawable.chat,
//			R.drawable.friend, R.drawable.recognize,
//			R.drawable.recommend };
	/** 底部菜单文字 **/
	public static final String[] menu_toolbar_name_array = { "动态", "聊天", "好友", "识脸", "推荐" };

	public static final String FILE_DIR="/sdcard/DCIM/";

	public static  int UID = 0;
	
	public static AppConfig getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig();
			appConfig.mContext = context;
		}
		return appConfig;
	}
	/**
	 * 全局通用配置
	 */
	public static void initCommon() {
		
	}
	/**
	 * 获取Preference设置
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 是否加载显示文章图片
	 */
	public static boolean isLoadImage(Context context) {
		return getSharedPreferences(context).getBoolean(CONF_LOAD_IMAGE, true);
	}

	public String getCookie() {
		return get(CONF_COOKIE);
	}

	public void setAccessToken(String accessToken) {
		set(CONF_ACCESSTOKEN, accessToken);
	}

	public String getAccessToken() {
		return get(CONF_ACCESSTOKEN);
	}

	public void setAccessSecret(String accessSecret) {
		set(CONF_ACCESSSECRET, accessSecret);
	}

	public String getAccessSecret() {
		return get(CONF_ACCESSSECRET);
	}

	public void setExpiresIn(long expiresIn) {
		set(CONF_EXPIRESIN, String.valueOf(expiresIn));
	}

	public long getExpiresIn() {
		return StringUtils.toLong(get(CONF_EXPIRESIN));
	}



	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	public Properties get() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator
					+ APP_CONFIG);

			props.load(fis);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}

	private void setProps(Properties p) {
		FileOutputStream fos = null;
		try {
			// 把config建在files目录下
			// fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

			// 把config建在(自定义)app_config的目录下
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File conf = new File(dirConf, APP_CONFIG);
			fos = new FileOutputStream(conf);

			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}
}
