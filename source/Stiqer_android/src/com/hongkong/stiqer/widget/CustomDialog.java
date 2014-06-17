package com.hongkong.stiqer.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.CommentListAdapter;
import com.hongkong.stiqer.adapter.FeedListAdapter;
import com.hongkong.stiqer.entity.MissionResult;
import com.hongkong.stiqer.entity.Prom;
import com.hongkong.stiqer.ui.CheckPhoneActivity;
import com.hongkong.stiqer.ui.PSettingActivity;
import com.hongkong.stiqer.utils.Defs;
import com.hongkong.stiqer.utils.Util;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class CustomDialog extends Dialog
{
    
	 Context dContext;
    public CustomDialog(Context context)
    {
        super(context);
        this.dContext = context;
    }
    
    public CustomDialog(Context context, int theme)
    {
        super(context, theme);
    }
    
    public static CustomDialog createProgressDialog(Context context, String message)
    {
        CustomDialog progressDialog = new CustomDialog(context, R.style.CustomDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        progressDialog.setContentView(view);
        TextView txtProgressView = (TextView) view.findViewById(R.id.txtMsg);
        txtProgressView.setText("" + message);
        float dialogWidth = Util.SCREENWIDTH - 2 * context.getResources().getDimension(R.dimen.widget_margin);
        progressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        progressDialog.getWindow().getAttributes().width = (int) dialogWidth;
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    } 
    
    public static CustomDialog createScanDialog (Context context, final DialogListener dialogListener, int stiqer_num, int egg_num){
    	final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_scan, null);
    	dialog.setContentView(view);
    	TextView stiqerText = (TextView) view.findViewById(R.id.stiqer_num);
    	TextView eggText = (TextView) view.findViewById(R.id.egg_num);	
    	stiqerText.setText("+"+stiqer_num);
    	eggText.setText("+"+egg_num);
    	
    	Button btnOk = (Button) view.findViewById(R.id.btn_scan_ok);
    	btnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogListener.showDialog("Share");
				dialog.dismiss();
			}
		});
	     
	     Button btnShare = (Button) view.findViewById(R.id.btn_scan_share);
	     btnShare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	     
    	float dialogWidth = Util.SCREENWIDTH - 2 * context.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
		return dialog;
    }
    
    public static CustomDialog createLoginDialog(Context context,final DialogListener dialogListener)
    {
        CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login, null);
        dialog.setContentView(view);
        Button btnWeibo = (Button) view.findViewById(R.id.btnWeibo);
        Button btnFacebook = (Button) view.findViewById(R.id.btnFacebook);
        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        
        btnWeibo.setOnTouchListener(Util.TouchDark);
        btnFacebook.setOnTouchListener(Util.TouchDark);

        final EditText username = (EditText) view.findViewById(R.id.login_UserName);
        final EditText password = (EditText) view.findViewById(R.id.login_Password);
        
        btnWeibo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogListener.showDialog("weibo");
			}
		});
        btnFacebook.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogListener.showDialog("facebook");
			}
		});
        btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogListener.showDialog("login,"+username.getText().toString()+" , "+password.getText().toString());
			}
		});
        return dialog;
    }
    
    public static CustomDialog createRegDialog(Context context, final DialogListener dialogListener)
    {
        CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_reg, null);
        dialog.setContentView(view);
        
        Button btnWeibo = (Button) view.findViewById(R.id.btnWeibo);
        Button btnFacebook = (Button) view.findViewById(R.id.btnFacebook);
        Button btReg = (Button) view.findViewById(R.id.btnRegister);
        
        btnWeibo.setOnTouchListener(Util.TouchDark);
        btnFacebook.setOnTouchListener(Util.TouchDark);
        
        final EditText username = (EditText) view.findViewById(R.id.reg_UserName);
        final EditText password = (EditText) view.findViewById(R.id.reg_Password);
        
        btnWeibo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogListener.showDialog("weibo");
			}
		});
        btnFacebook.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogListener.showDialog("facebook");
			}
		});
        btReg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogListener.showDialog("reg,"+username.getText().toString()+" , "+password.getText().toString());
				
			}
		});
        return dialog;
    }

	public static CustomDialog createPromDialog(Context context, final String pid, String prom_des, String image_uri, final DialogListener listener,int is_one) {
		final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_prom, null);
    	dialog.setContentView(view);
    	ImageView image = (ImageView) view.findViewById(R.id.prom_image);
    	
    	Picasso.with(context).load(image_uri).into(image);
    	TextView des = (TextView) view.findViewById(R.id.prom_des);
    	des.setText(prom_des);
    	
    	LinearLayout btn_save = (LinearLayout) view.findViewById(R.id.btn_save);
    	btn_save.setOnTouchListener(Util.TouchDark);
    	btn_save.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				listener.showDialog("save"+","+pid);
				dialog.dismiss();
			}
    	});
        if(is_one == 1){
        	btn_save.setVisibility(View.GONE);
    	}
    	LinearLayout btn_redeem = (LinearLayout) view.findViewById(R.id.btn_redeem);
    	btn_redeem.setOnTouchListener(Util.TouchDark);
    	btn_redeem.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				listener.showDialog("redeem"+","+pid);
				dialog.dismiss();
			}
    	});
    	
    	float dialogWidth = Util.SCREENWIDTH - 2 * context.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	
		return dialog;
	}

	public static CustomDialog createImageDialog(Context context, Bitmap bitmap, final DialogListener listener) {
		final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_showimage, null);
    	dialog.setContentView(view);
    	
    	ImageView image_bitmap = (ImageView) view.findViewById(R.id.image_bitmap);
    	Button image_cancel = (Button) view.findViewById(R.id.image_cancel);
    	Button image_delete = (Button) view.findViewById(R.id.image_delete);
    	
    	image_bitmap.setImageBitmap(bitmap);
    	image_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
    	image_delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				listener.showDialog("");
				dialog.dismiss();
			}
		});
		return dialog;
	}

	public static CustomDialog createMessageDialog(Context context, final DialogListener listener, String to_username,String profile_img) {
		final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
    	dialog.setContentView(view);
    	
    	final EditText txt = (EditText) view.findViewById(R.id.messge_con);
    	Button message_cancel = (Button) view.findViewById(R.id.message_cancel);
    	Button message_send = (Button) view.findViewById(R.id.message_send);
    	ImageView m_avatar = (ImageView) view.findViewById(R.id.m_avatar);
    	Picasso.with(context).load(profile_img).into(m_avatar);
    	
    	TextView msg_tosb = (TextView) view.findViewById(R.id.msg_tosb);
    	msg_tosb.setText("@"+to_username);
    	message_cancel.setOnTouchListener(Util.TouchDark);
    	message_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
    	
    	message_send.setOnTouchListener(Util.TouchDark);
    	message_send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				listener.showDialog(txt.getText().toString());
				dialog.dismiss();
			}
		});
    	
    	float dialogWidth = Util.SCREENWIDTH - context.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	
		return dialog;
	}

	public static CustomDialog createAvatarDialog(final Context context,final DialogListener avatarDialogListener) {
		final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_avatar, null);
    	dialog.setContentView(view);

    	Button btn_change = (Button) view.findViewById(R.id.ad_btn_change);
    	Button btn_view = (Button) view.findViewById(R.id.ad_btn_view);
    	Button btn_setting = (Button) view.findViewById(R.id.ad_btn_setting);
    	btn_change.setOnTouchListener(Util.TouchDark);
    	btn_view.setOnTouchListener(Util.TouchDark);
    	btn_setting.setOnTouchListener(Util.TouchDark);
    	
    	btn_change.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				avatarDialogListener.showDialog("picture");
				dialog.dismiss();
			}
		});
    	btn_view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				avatarDialogListener.showDialog("show");
				dialog.dismiss();
			}
		});
    	btn_setting.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent i = new Intent(context,PSettingActivity.class);
				context.startActivity(i);
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - context.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	
		return dialog;
	}

	public static CustomDialog createBigImageDialog(Context context,String smallUri, String uri) {
		final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_big_image, null);
    	dialog.setContentView(view);

    	ImageView big_image = (ImageView) view.findViewById(R.id.big_image);
    	if(uri.equals("")){
    		Picasso.with(context).load(R.drawable.logo).into(big_image);
    	}else{
    		Picasso.with(context).load(smallUri).into(big_image);
        	Picasso.with(context).load(uri).into(big_image);
    	}
    	//Picasso.with(context).load(uri).placeholder(R.drawable.enlarge_loading).into(big_image);
    	big_image.setOnTouchListener(Util.TouchDark);
    	big_image.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
    	
    	dialog.getWindow().getAttributes().width = (int) Util.SCREENWIDTH;
    	dialog.getWindow().getAttributes().height = (int) Util.SCREENHEIGHT;
    	return dialog;
	}

	public static CustomDialog createSettingDialog(Context mContext, final DialogListener Listener,int is_pulling, int is_pushing) {
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_setting, null);
    	dialog.setContentView(view);
    	
    	final ToggleButton toggle1 = (ToggleButton) view.findViewById(R.id.toggle1);
    	final ToggleButton toggle2 = (ToggleButton) view.findViewById(R.id.toggle2);
    	
    	if(is_pulling == 1){
    		toggle1.setChecked(true);
    	}
    	if(is_pushing == 1){
    		toggle2.setChecked(true);
    	}
    	
    	Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
    	btnCancel.setOnTouchListener(Util.TouchDark);
    	btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
    	
    	Button btnOk = (Button) view.findViewById(R.id.btn_ok);
    	btnOk.setOnTouchListener(Util.TouchDark);
    	btnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int new_receive, new_seen;
				if(toggle1.isChecked()){new_receive=1;}else{new_receive=0;}
				if(toggle2.isChecked()){new_seen=1;}else{new_seen=0;}
				Listener.showDialog(new_receive+","+new_seen);
				dialog.dismiss();
			}
		});
    	
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;  	
    	return dialog;
	}
	
	public static CustomDialog createNotiReplyDialog(Context context,final int type, String to_username, String avatar_uri,final NotiListener replyListener) {
		final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
    	dialog.setContentView(view);
    	TextView msg_tosb = (TextView) view.findViewById(R.id.msg_tosb);
    	ImageView m_avatar = (ImageView) view.findViewById(R.id.m_avatar);
    	msg_tosb.setText(to_username);
    	Picasso.with(context).load(avatar_uri).into(m_avatar);
    	
    	final EditText txt = (EditText) view.findViewById(R.id.messge_con);
    	if(type == 1){
    		txt.setHint("reply message");
    	}else{
    		txt.setHint("reply comment");
    	}
    	Button message_cancel = (Button) view.findViewById(R.id.message_cancel);
    	Button message_send = (Button) view.findViewById(R.id.message_send);
    	message_cancel.setOnTouchListener(Util.TouchDark);
    	message_send.setOnTouchListener(Util.TouchDark);
    	message_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				replyListener.showDialog(type,txt.getText().toString());
				dialog.dismiss();
			}
		});
    	message_send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				replyListener.showDialog(type,txt.getText().toString());
				dialog.dismiss();
			}
		});
    	
    	float dialogWidth = Util.SCREENWIDTH - context.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	
		return dialog;
	}
	
	public static CustomDialog createFavDeleteDialog(Context mContext, final DialogListener deleteListener) {
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete, null);
    	dialog.setContentView(view);
    	
    	Button btn_del = (Button) view.findViewById(R.id.btn_delete);
    	btn_del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				deleteListener.showDialog("");
				dialog.dismiss();
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;  	
    	return dialog;
	}
	
	public static CustomDialog createFriendDeleteDialog(Context mContext,final DialogListener deleteListener) {
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete, null);
    	dialog.setContentView(view);
    	
    	Button btn_del = (Button) view.findViewById(R.id.btn_delete);
    	btn_del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				deleteListener.showDialog("");
				dialog.dismiss();
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;  	
    	return dialog;
	}

	public static CustomDialog createPromRedeemDialog(Context mContext, String trans_des) {
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_redeem, null);
    	dialog.setContentView(view);
    	TextView tranDes = (TextView) view.findViewById(R.id.trans_des);
    	tranDes.setText(trans_des);
    	
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	return dialog;
	}
	
	public static CustomDialog createPhoneDialog(final Context mContext, final DialogListener phoneListener){
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_phone_input, null);
    	dialog.setContentView(view);
    	
    	final EditText phone_txt = (EditText) view.findViewById(R.id.phone_txt);
    	final String[] string_quhao={"+852","+86"};

    	final Spinner quhao = (Spinner) view.findViewById(R.id.phone_quhao);   
    	ArrayAdapter<String> quhaoAdapter=new ArrayAdapter<String>(mContext,R.layout.profile_spinner_item,string_quhao);  
		quhaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        quhao.setAdapter(quhaoAdapter);  
        
    	Button btnOk = (Button) view.findViewById(R.id.phone_btn);
    	btnOk.setOnTouchListener(Util.TouchDark);
    	btnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if((int) quhao.getSelectedItemId() == 0 && phone_txt.getText().toString().length() != 8){
					Toast.makeText(mContext, "telphone number mismatch", Toast.LENGTH_SHORT).show();
					return;
				}
				if((int) quhao.getSelectedItemId() == 1 && phone_txt.getText().toString().length() != 11){
					Toast.makeText(mContext, "telphone number mismatch", Toast.LENGTH_SHORT).show();
					return;
				}
				phoneListener.showDialog(string_quhao[(int) quhao.getSelectedItemId()]+phone_txt.getText().toString());
				dialog.dismiss();
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
		return dialog;
	}
	
	public static CustomDialog createPhoneVerifyDialog(final Context mContext, final DialogListener verifyListener){
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_phone_check, null);
    	dialog.setContentView(view);
    	
    	final EditText verify_txt = (EditText) view.findViewById(R.id.verify_txt);

    	Button btnOk = (Button) view.findViewById(R.id.verify_btn);
    	btnOk.setOnTouchListener(Util.TouchDark);
    	btnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(verify_txt.getText().toString().equals("")){
					Toast.makeText(mContext, "verification code can not be empty", Toast.LENGTH_SHORT).show();
					return;
				}
				verifyListener.showDialog(verify_txt.getText().toString());
				dialog.dismiss();
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
		return dialog;
	}
	
	public static CustomDialog createMissionRedeemDialog(Context mContext, MissionResult mission) {
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_mission_redeem, null);
    	dialog.setContentView(view);
    	TextView mission_des = (TextView) view.findViewById(R.id.mission_des);
    	TextView mission_timestamp = (TextView) view.findViewById(R.id.mission_timestamp);
    	TextView veri_id = (TextView) view.findViewById(R.id.veri_id);
    	TextView veri_uid = (TextView) view.findViewById(R.id.veri_uid);
    	TextView veri_code = (TextView) view.findViewById(R.id.veri_code);
    	ImageView mission_qr_code = (ImageView) view.findViewById(R.id.mission_qr_code);
    	ImageView mission_img = (ImageView) view.findViewById(R.id.mission_img);
    	
    	mission_des.setText(mission.getDescription());
    	mission_timestamp.setText("TimeStamp:"+mission.getTimestamp());
    	veri_id.setText("ID:"+mission.getRedeem_id());
    	veri_uid.setText("UID:"+mission.getUid());
    	veri_code.setText("Verification code:"+mission.getVeri_code());
    	
    	if(!mission.getMission_img().equals("")){
    		Picasso.with(mContext).load(mission.getMission_img()).into(mission_img);
    	}
    	
    	if(!mission.getVeri_qr_img().equals("")){
    		Picasso.with(mContext).load(mission.getVeri_qr_img()).into(mission_qr_code);
    	}
    	
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	return dialog;
	}

	public static CustomDialog createClassUpDialog(Context mContext,int new_class, String store_name, final DialogListener upListener) {
		Integer[] class_icon_array = {
				R.drawable.ic_class_bronze,
				R.drawable.ic_class_bronze,
				R.drawable.ic_class_silver,
				R.drawable.ic_class_gold,
				R.drawable.ic_class_diamond,
				R.drawable.ic_class_vip,
				R.drawable.ic_class_vvip,
			};
		String[] class_name_array = {
				  "Bronze","Bronze","Sliver","Gold","Diamond","Vip","Vvip"
				};
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_class_up, null);
    	dialog.setContentView(view);
    	
    	TextView class_con = (TextView) view.findViewById(R.id.class_con);
    	Button dialog_ok = (Button) view.findViewById(R.id.dialog_ok);
    	dialog_ok.setOnTouchListener(Util.TouchDark);
    	ImageView class_avatar = (ImageView) view.findViewById(R.id.class_avatar);
    	
    	class_avatar.setImageResource(class_icon_array[new_class]);
    	class_con.setText("Congratulations.You are now a "+class_name_array[new_class]+" Member of "+store_name);
    	
    	dialog_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				upListener.showDialog("");
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	return dialog;
	}

	public static CustomDialog createLevelUpDialog(Context mContext, int new_level, final DialogListener upListener) {
		final CustomDialog dialog = new CustomDialog(mContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_level_up, null);
    	dialog.setContentView(view);
    	
    	TextView level_con = (TextView) view.findViewById(R.id.level_con);
    	level_con.setText("Congratulations!You are now level "+new_level+".");
    	
    	Button level_ok = (Button) view.findViewById(R.id.level_ok);
    	level_ok.setOnTouchListener(Util.TouchDark);
    	level_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				upListener.showDialog("");
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - mContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	return dialog;
	}
	
	public static CustomDialog createTutorialDialog(Context context, final DialogListener stiqerListener) {
		final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
    	View view = LayoutInflater.from(context).inflate(R.layout.dialog_tutorial, null);
    	dialog.setContentView(view);
    	TextView stiqer_text = (TextView) view.findViewById(R.id.stiqer_text);
    	TextView egg_text = (TextView) view.findViewById(R.id.egg_text);
    	stiqer_text.setText("This is a stiqer. \nAfter each scan at a store, you get one.\nCollect them at each store to upgrade \nyour class status--start from Bronze, \nget to VVIP! ");
    	egg_text.setText("This is an egg. \nYou still get them by purchasing \nat a store, but the amount can vary--\nfactors include your purchase history, \nclass status, and even luck! \nCollect eggs to level up. \nLevels are universal: you have different\nclass status per store, but you \nonly have one level per account.");
    	Button btn_done = (Button) view.findViewById(R.id.btn_done);
    	btn_done.setOnTouchListener(Util.TouchDark);
    	btn_done.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				stiqerListener.showDialog("");
			}
		});
    	float dialogWidth = Util.SCREENWIDTH - context.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	return dialog;
	}

	public CustomDialog createShowLikeDiaolog(String[] mList) {
		final CustomDialog dialog = new CustomDialog(dContext, R.style.CustomDialog);
    	View view = LayoutInflater.from(dContext).inflate(R.layout.dialog_like_show, null);
    	dialog.setContentView(view);
    	String str = " ";
    	for(int i =0; i<mList.length; i++){
    		if(i>1){
    			str = str + mList[i]+", ";
    	    }
    	}
    	TextView like_show = (TextView) view.findViewById(R.id.like_show);
    	like_show.setText(str.substring(0,str.length()-2)+"  ");
    	
    	like_show.setAutoLinkMask(0);
		Pattern mentionsPattern = Pattern.compile("(\\s)(\\w+?)(,|\\s{2})");
		String mentionsScheme = String.format("%s/?%s=", Defs.MENTIONS_SCHEMA, Defs.PARAM_UID);
		Linkify.addLinks(like_show, mentionsPattern, mentionsScheme, new MatchFilter() {
		@Override
		public boolean acceptMatch(CharSequence s, int start, int end) {
		      return s.charAt(end-1) !='.';
		    }
		  }, new TransformFilter() {
		  @Override
		  public String transformUrl(Matcher match, String url)
		  {
		      return match.group(2); 
		   }
	    });
		
		Spannable s = (Spannable)like_show.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        like_show.setText(s);
        
    	float dialogWidth = Util.SCREENWIDTH - dContext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
    	return dialog;
	}
	
	public class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @SuppressLint("ResourceAsColor")
		public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            int myColor = dContext.getResources().getColor(R.color.subhead);
            ds.setColor(myColor);
        }
    }

	
	

}

