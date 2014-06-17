package dreajay.android.safe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dreajay.android.safe.R;
import dreajay.android.safe.common.Constants;
import dreajay.android.safe.util.MD5Util;

public class MainActivity extends Activity {
	public static String[] names = new String[] { "手机防盗", "通讯卫士", "软件管理",
			"进场管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	public static int[] icons = new int[] { R.drawable.safe,
			R.drawable.callmsgsafe_selector, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.taskmanager, R.drawable.atools,
			R.drawable.app, R.drawable.sysoptimize,
			R.drawable.callmsgsafe_pressed };

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GridView gv = (GridView) findViewById(R.id.gv_home);
		gv.setAdapter(new GridViewAdapter());
		sp = getSharedPreferences(Constants.SYSTEM_SETTINGS, MODE_PRIVATE);

	}

	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = View.inflate(MainActivity.this, R.layout.item_main,
					null);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.imgv_item_pic);
			imageView.setImageResource(icons[position]);
			TextView textView = (TextView) view.findViewById(R.id.tv_item_name);
			textView.setText(names[position]);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (position) {
					case 0:
						sjfd();
						break;

					default:
						break;
					}

				}
			});

			return view;
		}

		private EditText etpwd;
		private EditText etpwdconf;
		private Button okButton;
		private Button cancelButton;
		public static final String PASSWORD = "password";

		private void sjfd() {
			final String password = sp.getString(PASSWORD, "");

			if (password.equals("")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				View view = View.inflate(MainActivity.this,
						R.layout.dialog_sjfd_config, null);
				builder.setView(view);
				final AlertDialog dialog = builder.create();
				dialog.show();
				etpwd = (EditText) view.findViewById(R.id.tv_sjfd_password);
				etpwdconf = (EditText) view
						.findViewById(R.id.tv_sjfd_password_config);
				okButton = (Button) view.findViewById(R.id.bt_sjfd_ok);
				cancelButton = (Button) view.findViewById(R.id.bt_sjfd_cancel);
				okButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String pwd = etpwd.getText().toString().trim();
						String pwdconf = etpwdconf.getText().toString().trim();
						if (TextUtils.isEmpty(pwd)) {
							Toast.makeText(getApplicationContext(), "密码不能为空!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (TextUtils.isEmpty(pwdconf)) {
							Toast.makeText(getApplicationContext(),
									"确认密码不能为空!", Toast.LENGTH_SHORT).show();
							return;
						}
						if (!pwd.equals(pwdconf)) {
							Toast.makeText(getApplicationContext(), "两次密码不一致!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Editor editor = sp.edit();
						editor.putString(PASSWORD, MD5Util.getMD5(pwd));
						editor.commit();
						dialog.dismiss();
					}
				});
				cancelButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				View view = View.inflate(MainActivity.this,
						R.layout.dialog_sjfd_in, null);
				builder.setView(view);
				final AlertDialog dialog = builder.create();
				dialog.show();
				etpwd = (EditText) view.findViewById(R.id.tv_sjfd_password);
				okButton = (Button) view.findViewById(R.id.bt_sjfd_ok);
				cancelButton = (Button) view.findViewById(R.id.bt_sjfd_cancel);
				okButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String pwd = etpwd.getText().toString().trim();
						if (TextUtils.isEmpty(pwd)) {
							Toast.makeText(getApplicationContext(), "密码不能为空!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (!password.equals(MD5Util.getMD5(pwd))) {
							Toast.makeText(getApplicationContext(), "密码不正确!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						dialog.dismiss();
						Intent intent = new Intent(MainActivity.this,
								SJFDActivity.class);
						startActivity(intent);
					}
				});
				cancelButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}
}
