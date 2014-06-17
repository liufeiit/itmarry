package com.dyj.tabs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.phprpc.util.Cast;

import com.dyj.R;
import com.dyj.adapter.ImageAdapter;
import com.dyj.app.Global;
import com.dyj.db.DatabaseHelper;
import com.dyj.db.bean.Rw;
import com.dyj.db.bean.Rwda;
import com.dyj.dialog.CustomerDialog;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PutRwTab extends Activity {

	private Global global;
	private SharedPreferences userInfo;
	private SharedPreferences rwInfo;
	private DatabaseHelper dataHelper = null;
	private Dao<Rwda, Integer> rwDaDao;
	private TextView rw_dm;
	private String wcqk;
	private ArrayList<Bitmap> zplist = new ArrayList<Bitmap>();
	private LinearLayout line_Zp;
	private Gallery zpGallery;
	private Switch isGhsb,is_gxdlxx;
	private View view_Zcbh, view_Jsr, view_Cfdd, view_Tel;
	private LinearLayout line_Zcbh, line_Jsr, line_Cfdd, line_Tel;
	private Dialog pdialog;
	private int put_rw_dm, sgry_dm, sg_isGhsb = 0,sg_is_gxdlxx=0;
	private String image_name;
	private String sg_wd;
	private String sg_jd;
	private String sg_wcqk;
	private String sg_zcbh;
	private String sg_jsr;
	private String sg_tel;
	private String sg_cfdd;
	private int i = 1;

	private String imagePath;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dataHelper != null) {
			OpenHelperManager.releaseHelper();
			dataHelper = null;
		}
	}

	@Override
	protected void onResume() {
		// TODO �Զ����ɵķ������
		super.onResume();
		handler.post(runnable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userInfo = this.getSharedPreferences("setting", Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		if (dataHelper == null) {
			dataHelper = OpenHelperManager
					.getHelper(this, DatabaseHelper.class);
		}
		try {
			rwDaDao = dataHelper.getRwDaDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentView(R.layout.activity_put_rw_tab);

		rw_dm = (TextView) findViewById(R.id.Rw_dm);
		Intent intent = this.getIntent();
		if (intent.hasExtra("rw_dm")) {
			rw_dm.setText(intent.getStringExtra("rw_dm"));
			// this.global.save("Sg_Rw_dm", intent.getStringExtra("rw_dm"));
		}

		put_rw_dm = Integer.parseInt(rw_dm.getText().toString());

		// ����SharedPreferences

		this.rwInfo = this.getSharedPreferences(put_rw_dm + "",
				Context.MODE_PRIVATE);

		Log.d("wcqk", this.rwInfo.getString("Sg_Wcqk", ""));
		TextView Sg_lbs = (TextView) findViewById(R.id.Sg_lbs);
		Sg_lbs.setText(this.rwInfo.getString("Sg_lbs_Addr", ""));
		TextView Sg_Wcqk = (TextView) findViewById(R.id.Sg_Wcqk);
		Sg_Wcqk.setText(this.rwInfo.getString("Sg_Wcqk", ""));
		TextView Sg_zcbh = (TextView) findViewById(R.id.Sg_zcbh);
		Sg_zcbh.setText(this.rwInfo.getString("Sg_zcbh", ""));
		TextView Sg_jsr = (TextView) findViewById(R.id.Sg_jsr);
		Sg_jsr.setText(this.rwInfo.getString("Sg_jsr", ""));
		TextView Sg_tel = (TextView) findViewById(R.id.Sg_tel);
		Sg_tel.setText(this.rwInfo.getString("Sg_tel", ""));
		TextView Sg_cfdd = (TextView) findViewById(R.id.Sg_cfdd);
		Sg_cfdd.setText(this.rwInfo.getString("Sg_cfdd", ""));

		zpGallery = (Gallery) findViewById(R.id.ZpGallery);
		zpGalleryAdapter = new ImageAdapter(this, zplist);
		zpGallery.setAdapter(zpGalleryAdapter); // gallery���ImageAdapterͼƬ��Դ
		zpGallery.setOnItemClickListener(zpGarllyOnItemClickListener); // gallery���õ��ͼƬ��Դ���¼�

		registerForContextMenu(zpGallery);
		LinearLayout line_Sg_lbs = (LinearLayout) findViewById(R.id.line_Sg_lbs);

		line_Sg_lbs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PutRwTab.this, GetLbsTab.class);
				intent.putExtra("rw_dm", put_rw_dm);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ˢ��

				startActivity(intent);

			}

		});
		//�Ƿ���µ�����Ϣ
		is_gxdlxx=(Switch) findViewById(R.id.is_gxdlxx);
		if (null != this.rwInfo.getString("sg_is_gxdlxx", "")) {
			if ("1".equals(this.rwInfo.getString("sg_is_gxdlxx", ""))) {
				this.is_gxdlxx.setChecked(true);
			}
		}
		this.is_gxdlxx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {

					sg_is_gxdlxx = 1;
				}
				else{
					sg_is_gxdlxx=0;
				}
				rwInfo.edit().putString("sg_is_gxdlxx", sg_is_gxdlxx + "")
				.commit();
			}
			
		});
		line_Zp = (LinearLayout) findViewById(R.id.line_Zp);
		line_Zp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final CharSequence[] items = { "����", "���ֻ����ѡȡ" };
				AlertDialog dlg = new AlertDialog.Builder(PutRwTab.this)
						.setTitle("ѡ����Ƭ")
						.setItems(items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Log.d("which", which + "");
								if (which == 1) {
									Intent getImage = new Intent(
											Intent.ACTION_GET_CONTENT);
									getImage.addCategory(Intent.CATEGORY_OPENABLE);
									getImage.setType("image/jpeg");
									startActivityForResult(getImage, 2);
								} else {
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									Date date = new Date();
									SimpleDateFormat formatter = new SimpleDateFormat(
											"yyyy-MM-dd_HH-mm-ss");
									imagePath = Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ File.separator
											+ File.separator
											+ formatter.format(date) + ".jpg";
									Log.d("path", imagePath);
									intent.putExtra(
											android.provider.MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(imagePath)));
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ˢ��
									startActivityForResult(intent, 1);
								}
							}

						}).create();
				dlg.show();

			}

		});
		LinearLayout line_Wcqk = (LinearLayout) findViewById(R.id.line_Wcqk);
		line_Wcqk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PutRwTab.this, GetWcqkTab.class);
				intent.putExtra("rw_dm", put_rw_dm);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ˢ��
				startActivity(intent);
			}

		});
		this.isGhsb = (Switch) findViewById(R.id.isGhsb);

		this.view_Zcbh = findViewById(R.id.view_Zcbh);
		this.line_Zcbh = (LinearLayout) findViewById(R.id.line_Zcbh);
		this.line_Zcbh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PutRwTab.this, GetZcbhTab.class);
				intent.putExtra("rw_dm", put_rw_dm);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ˢ��
				startActivity(intent);
			}
		});

		this.view_Jsr = findViewById(R.id.view_Jsr);
		this.line_Jsr = (LinearLayout) findViewById(R.id.line_Jsr);
		this.line_Jsr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PutRwTab.this, GetJsrTab.class);
				intent.putExtra("rw_dm", put_rw_dm);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ˢ��
				startActivity(intent);
			}
		});
		this.view_Tel = findViewById(R.id.view_Tel);
		this.line_Tel = (LinearLayout) findViewById(R.id.line_Tel);
		this.line_Tel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PutRwTab.this, GetTelTab.class);
				intent.putExtra("rw_dm", put_rw_dm);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ˢ��
				startActivity(intent);
			}
		});
		this.view_Cfdd = findViewById(R.id.view_Cfdd);
		this.line_Cfdd = (LinearLayout) findViewById(R.id.line_Cfdd);
		this.line_Cfdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PutRwTab.this, getCfddTab.class);
				intent.putExtra("rw_dm", put_rw_dm);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ˢ��
				startActivity(intent);
			}
		});
		if (null != this.rwInfo.getString("sg_isGhsb", "")) {
			if ("1".equals(this.rwInfo.getString("sg_isGhsb", ""))) {
				this.isGhsb.setChecked(true);
				view_Zcbh.setVisibility(View.VISIBLE);
				line_Zcbh.setVisibility(View.VISIBLE);
				view_Jsr.setVisibility(View.VISIBLE);
				line_Jsr.setVisibility(View.VISIBLE);
				view_Tel.setVisibility(View.VISIBLE);
				line_Tel.setVisibility(View.VISIBLE);
				view_Cfdd.setVisibility(View.VISIBLE);
				line_Cfdd.setVisibility(View.VISIBLE);
			}
		}
		
		this.isGhsb
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {

							sg_isGhsb = 1;
							view_Zcbh.setVisibility(View.VISIBLE);
							line_Zcbh.setVisibility(View.VISIBLE);
							view_Jsr.setVisibility(View.VISIBLE);
							line_Jsr.setVisibility(View.VISIBLE);
							view_Tel.setVisibility(View.VISIBLE);
							line_Tel.setVisibility(View.VISIBLE);
							view_Cfdd.setVisibility(View.VISIBLE);
							line_Cfdd.setVisibility(View.VISIBLE);
						} else {
							sg_isGhsb = 0;
							view_Zcbh.setVisibility(View.GONE);
							line_Zcbh.setVisibility(View.GONE);
							view_Jsr.setVisibility(View.GONE);
							line_Jsr.setVisibility(View.GONE);
							view_Tel.setVisibility(View.GONE);
							line_Tel.setVisibility(View.GONE);
							view_Cfdd.setVisibility(View.GONE);
							line_Cfdd.setVisibility(View.GONE);
						}
						rwInfo.edit().putString("sg_isGhsb", sg_isGhsb + "")
								.commit();
						// global.save("sg_isGhsb", sg_isGhsb + "");
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.put_rw_tab, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId())// �õ��������item��itemId
		{
		case R.id.menu_dh:
			Intent ljgh_intent = new Intent(this, LjghTab.class);
			ljgh_intent.putExtra("rw_dm", put_rw_dm);
			//ljgh_intent.putExtra("jd", locData.longitude);
			startActivity(ljgh_intent);
			break;
		case R.id.menu_del:
			final CustomerDialog dialog = new CustomerDialog(this);
			dialog.setMessage("��ȷ���Ƿ��˻�����");
			dialog.setText_btn_ok("ȷ��");
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pdialog = LoadingDialog.createLoadingDialog(PutRwTab.this,
							"�����ύ�˻���������...");
					pdialog.show();
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							RpcHandler rpcHandler = new RpcHandler(
									PutRwTab.this);

							HashMap<String, ?> res;
							res = rpcHandler.returnRw(put_rw_dm);
							boolean result = true;
							if (null != res) {
								if (Integer.parseInt(Cast.cast(
										res.get("errno"), String.class)
										.toString()) != 0)
									result = false;
							} else if (null == res) {
								result = false;
							}
							if (result) {
								Message message = new Message();
								message.what = 101;
								message.obj = result;
								saveHandler.sendMessage(message);
							}
						}

					});
					dialog.dismiss();
					t.start();
				}

			};
			dialog.setOkListener(listener);
			dialog.show();

			break;
		case R.id.menu_save:

			Global global = Global.getInstance();
			// TODO �Զ����ɵķ������
			// RpcHandler rpcHandler = new RpcHandler(getApplicationContext());

			image_name = global.read("user_mc").toString();

			sg_wd = this.rwInfo.getString("Sg_lbs_wd", "");
			sg_jd = this.rwInfo.getString("Sg_lbs_jd", "");
			if (this.is_gxdlxx.isChecked()) {
				sg_is_gxdlxx = 1;
			}
			else{
				sg_is_gxdlxx=0;
			}
			sg_wcqk = this.rwInfo.getString("Sg_Wcqk", "");

			sg_zcbh = this.rwInfo.getString("Sg_zcbh", "");
			sg_jsr = this.rwInfo.getString("Sg_jsr", "");
			sg_tel = this.rwInfo.getString("Sg_tel", "");
			sg_cfdd = this.rwInfo.getString("Sg_cfdd", "");
			/*if ("".equals(sg_wd) || "".equals(sg_jd)) {
				Toast.makeText(PutRwTab.this, "��ѡ�����λ��!", 1).show();
				return true;
			}*/

			if (zplist.size() == 0) {
				Toast.makeText(PutRwTab.this, "�������ֳ���Ƭ!", 1).show();
				return true;
			}

			if (null == sg_wcqk || "".equals(sg_wcqk)) {
				Toast.makeText(PutRwTab.this, "����д������!", 1).show();
				return true;
			}

			if (this.isGhsb.isChecked()) {
				sg_isGhsb = 1;
				if (null == sg_zcbh || "".equals(sg_zcbh)) {
					Toast.makeText(PutRwTab.this, "����д���ʲ����!", 1).show();
					return true;
				}
				if (null == sg_jsr || "".equals(sg_jsr)) {
					Toast.makeText(PutRwTab.this, "����д���ʲ�������!", 1).show();
					return true;
				}
				if (null == sg_cfdd || "".equals(sg_cfdd)) {
					Toast.makeText(PutRwTab.this, "����д���ʲ���ŵص�!", 1).show();
					return true;
				}
			} else {
				sg_isGhsb = 0;
			}
			sgry_dm = Integer.parseInt(global.read("dm_user").toString());

			Log.d("sg_isGhsb", sg_isGhsb + "");
			pdialog = LoadingDialog.createLoadingDialog(PutRwTab.this,
					"�����ϴ�����...");
			pdialog.show();
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					RpcHandler rpcHandler = new RpcHandler(PutRwTab.this);
					Boolean result = true;
					for (Bitmap bitmap : zplist) {

						byte[] zp = bmpToByteArray(bitmap);
						if (null != zp) {
							HashMap<?, ?> res = rpcHandler.rw_submit(put_rw_dm,
									sgry_dm, sg_is_gxdlxx,sg_wd, sg_jd, sg_wcqk, sg_isGhsb,
									sg_zcbh, sg_jsr, sg_tel, sg_cfdd,
									image_name + "-" + i, zp);
							if (null != res) {
								if (Integer.parseInt(Cast.cast(
										res.get("errno"), String.class)
										.toString()) != 0)
									result = false;
							} else if (null == res) {
								result = false;
							}
							i++;
						}
					}
					if (result) {
						Message message = new Message();
						message.what = 1;
						message.obj = result;
						saveHandler.sendMessage(message);
					}

				}
			});
			if (Global.CheckNetwork(PutRwTab.this)) {

				thread.start();
			} else {
				pdialog.dismiss();
			}
			/*
			 * Log.d("sg_isGhsb",sg_isGhsb+""); pdialog.dismiss();
			 */

			/*
			 * TaskPutRw task = new TaskPutRw(getApplicationContext(),
			 * put_rw_dm, sgry_dm, sg_wd, sg_jd, wcqk, image_name, zplist,
			 * global); try { if (task.execute().get()) { Toast.makeText(this,
			 * "�����ύ�ɹ�", 1).show(); } else { Toast.makeText(this, "�����ύʧ��",
			 * 1).show(); } } catch (InterruptedException e) { // TODO �Զ����ɵ�
			 * catch �� e.printStackTrace(); } catch (ExecutionException e) { //
			 * TODO �Զ����ɵ� catch �� e.printStackTrace(); }
			 * 
			 * finish(); break;
			 */
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private Handler saveHandler = new Handler() {
		public void handleMessage(Message msg) {
			Boolean result = true;
			switch (msg.what) {
			case 1:

				result = (Boolean) msg.obj;
				if (result) {
					Dao<Rw, Integer> rwDao;
					try {
						rwDao = dataHelper.getRwDao();
						Rw rw = rwDao.queryForId(put_rw_dm);
						rw.setRwzt_dm(4);
						rwDao.update(rw);
						Log.d("status is", "ɾ��������Ϣ��");
						rwInfo.edit().remove("Sg_Rw_dm").commit();
						rwInfo.edit().remove("sg_is_gxdlxx").commit();
						rwInfo.edit().remove("Sg_lbs_Addr").commit();
						rwInfo.edit().remove("Sg_lbs_wd").commit();
						rwInfo.edit().remove("Sg_lbs_jd").commit();
						rwInfo.edit().remove("Sg_Wcqk").commit();
						rwInfo.edit().remove("sg_isGhsb").commit();
						rwInfo.edit().remove("Sg_zcbh").commit();
						rwInfo.edit().remove("Sg_jsr").commit();
						rwInfo.edit().remove("Sg_cfdd").commit();
						rwInfo.edit().remove("Sg_zp").commit();
						Log.d("status is", "ɾ��������Ϣ������");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// �޸�״̬Ϊ4��ִ��

					Toast toast = Toast.makeText(PutRwTab.this, "�����ύ�ɹ�",
							Toast.LENGTH_SHORT);
					toast.show();

				} else {
					Toast toast = Toast.makeText(PutRwTab.this, "�����ύʧ��",
							Toast.LENGTH_SHORT);
					toast.show();
				}
				pdialog.dismiss();
				finish();
				// IndexActivity.tabHost.setCurrentTabByTag("tab3");
				Intent intent = new Intent(PutRwTab.this, LsRwTab.class);
				startActivity(intent);
				break;
			case 101:
				result = (Boolean) msg.obj;
				if (result) {
					Dao<Rw, Integer> rwDao;
					try {
						// ɾ����������
						rwDao = dataHelper.getRwDao();
						DeleteBuilder<Rw, Integer> deleteBulder = rwDao
								.deleteBuilder();
						deleteBulder.where().eq("rw_dm", put_rw_dm);
						PreparedDelete<Rw> pd = deleteBulder.prepare();
						rwDao.delete(pd);
						// ɾ������
						rwInfo.edit().remove("Sg_Rw_dm").commit();
						rwInfo.edit().remove("Sg_lbs_Addr").commit();
						rwInfo.edit().remove("sg_is_gxdlxx").commit();
						rwInfo.edit().remove("Sg_lbs_wd").commit();
						rwInfo.edit().remove("Sg_lbs_jd").commit();
						rwInfo.edit().remove("Sg_Wcqk").commit();
						rwInfo.edit().remove("sg_isGhsb").commit();
						rwInfo.edit().remove("Sg_zcbh").commit();
						rwInfo.edit().remove("Sg_jsr").commit();
						rwInfo.edit().remove("Sg_cfdd").commit();
						rwInfo.edit().remove("Sg_zp").commit();
						Toast toast = Toast.makeText(PutRwTab.this, "�����˻سɹ�",
								Toast.LENGTH_SHORT);
						toast.show();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					Toast toast = Toast.makeText(PutRwTab.this, "�����˻�ʧ��",
							Toast.LENGTH_SHORT);
					toast.show();
				}
				pdialog.dismiss();
				finish();
				break;
			}

		}
	};

	// Bitmap to byte[]
	public byte[] bmpToByteArray(Bitmap bmp) {
		// Default size is 32 bytes
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	AdapterView.OnItemClickListener zpGarllyOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(PutRwTab.this, "ͼƬ " + (position + 1),
					Toast.LENGTH_SHORT).show();
		}
	};
	private ImageAdapter zpGalleryAdapter;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (1 == requestCode) { // ϵͳ������ش���
			if (resultCode == Activity.RESULT_OK) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
					Log.v("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				FileInputStream fis;
				try {
					fis = new FileInputStream(this.imagePath);
					Bitmap cameraBitmap = BitmapFactory.decodeStream(fis);
					cameraBitmap = compImage(cameraBitmap);
					Log.d("cameraBitmap height", cameraBitmap.getHeight() + "");
					if (cameraBitmap != null) {
						zplist.add(cameraBitmap);
						zpGalleryAdapter.notifyDataSetChanged();
						// ���浽����
						this.rwInfo
								.edit()
								.putString("Sg_zp",
										global.getBitmapListToString(zplist))
								.commit();
						// global.save("Sg_zp",
						// global.getBitmapListToString(zplist));
						// Log.d("zpliststr is",global.getBitmapListToString(zplist));
					} else {
						Toast.makeText(PutRwTab.this, "��Ƭ����!", 1).show();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");

			}
		}
		if (2 == requestCode) { //��ᴦ��
			Uri originalUri = data.getData();
			Log.d("originalUri=============",originalUri.toString());
			Bitmap myBitmap;
			byte[] mContent;
			ContentResolver resolver = getContentResolver();
			try {
				mContent = readStream(resolver.openInputStream(Uri
						.parse(originalUri.toString())));
				myBitmap = getPicFromBytes(mContent, null);
				if (myBitmap != null) {
					zplist.add(compImage(myBitmap));
					zpGalleryAdapter.notifyDataSetChanged();
					// ���浽����
					this.rwInfo
							.edit()
							.putString("Sg_zp",
									global.getBitmapListToString(zplist))
							.commit();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// ����UI

				TextView Sg_lbs = (TextView) findViewById(R.id.Sg_lbs);
				Sg_lbs.setText(rwInfo.getString("Sg_lbs_Addr", ""));

				TextView Sg_Wcqk = (TextView) findViewById(R.id.Sg_Wcqk);
				Sg_Wcqk.setText(rwInfo.getString("Sg_Wcqk", ""));

				TextView Sg_zcbh = (TextView) findViewById(R.id.Sg_zcbh);
				Sg_zcbh.setText(rwInfo.getString("Sg_zcbh", ""));

				TextView Sg_jsr = (TextView) findViewById(R.id.Sg_jsr);
				Sg_jsr.setText(rwInfo.getString("Sg_jsr", ""));

				TextView Sg_tel = (TextView) findViewById(R.id.Sg_tel);
				Sg_tel.setText(rwInfo.getString("Sg_tel", ""));

				TextView Sg_cfdd = (TextView) findViewById(R.id.Sg_cfdd);
				Sg_cfdd.setText(rwInfo.getString("Sg_cfdd", ""));

				// ��ȡ������Ƭ����
				String zpStr = rwInfo.getString("Sg_zp", "");
				if (null != zpStr && "" != zpStr) {

					zplist = global.getStringToBitmapList(rwInfo.getString(
							"Sg_zp", ""));
					// Log.d("zp size is:",zplist.size()+"");
					zpGalleryAdapter = new ImageAdapter(PutRwTab.this, zplist);
					zpGallery.setAdapter(zpGalleryAdapter);
				}
				break;
			}
		};
	};

	private Runnable runnable = new Runnable() {
		public void run() {
			// ������
			handler.sendEmptyMessage(1);
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO �Զ����ɵķ������
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 0, 0, "ɾ��");
		menu.add(0, 1, 0, "ɾ������");

	}

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			// ɾ������
			zplist.remove(info.position);
			rwInfo.edit()
					.putString("Sg_zp", global.getBitmapListToString(zplist))
					.commit();
			zpGalleryAdapter.notifyDataSetChanged();
			break;

		case 1:
			// ɾ��ALL����
			zplist.clear();
			rwInfo.edit().remove("Sg_zp").commit();
			zpGalleryAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

	// ������ѹ��ͼƬ
	private Bitmap compImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// �ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// ����ѹ��50%����ѹ��������ݴ�ŵ�baos��
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// ���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;// �������ø߶�Ϊ800f
		float ww = 480f;// �������ÿ��Ϊ480f
		// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		// return compressImage(bitmap);// ѹ���ñ�����С���ٽ�������ѹ��
		return bitmap;
	}

	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		return bitmap;
	}
}
