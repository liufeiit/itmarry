package dreajay.android.safe.application;

/**
 * 在manifest中<application>中加入name="dreajay.android.safe.application.Application"
 * 就可以在任意类中使用Application.getInstance()来获取应用程序Context了
 * 
 * @author jay
 * 
 */
public class Application extends android.app.Application {
	public static Application INSTANCE;

	public static Application getApplication() {
		return INSTANCE;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;
	}

}
