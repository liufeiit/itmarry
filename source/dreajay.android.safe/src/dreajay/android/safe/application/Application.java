package dreajay.android.safe.application;

/**
 * ��manifest��<application>�м���name="dreajay.android.safe.application.Application"
 * �Ϳ�������������ʹ��Application.getInstance()����ȡӦ�ó���Context��
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
