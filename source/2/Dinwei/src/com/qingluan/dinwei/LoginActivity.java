package com.qingluan.dinwei;



import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText account;
	private EditText password;
	private Button login_user;
	private Animation anim_down_appearce;
	private Animation anim_up_gone;
	private int STATUE = R.string.InitState;
	private int LOGIN_STATE = R.string.login_state_pre;
	private int Times = 3;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		

		account = (EditText)findViewById(R.id.Account);
		password = (EditText)findViewById(R.id.textPassword);
		login_user = (Button)findViewById(R.id.login);
		anim_down_appearce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.search_anim);
		anim_up_gone = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.search_anim_2);
		anim_down_appearce.setFillAfter(true);
		anim_up_gone.setFillAfter(true);
		account.setAlpha(0);
		password.setAlpha(0);
	}

	@SuppressLint("NewApi")
	public void login_user(View view){
		if (LOGIN_STATE == R.string.login_state_pre){
			account.startAnimation(anim_down_appearce);
			password.startAnimation(anim_down_appearce);
			account.setAlpha(200);
			password.setAlpha(200);
			
			LOGIN_STATE = R.string.login_state_input;
			STATUE = R.string.LoginState;
		}else if (LOGIN_STATE== R.string.login_state_input){
			String user = account.getText().toString();
			String pass = password.getText().toString();
			
			/*set password
			 * 
			 * */
			
			if (Judege(user,pass)){
				final Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				password.clearComposingText();

				String Account = account.getText().toString();
				String Pass = password.getText().toString();
				String [] email_info = {Account ,Pass};
				if (Account.equals("")){
					Account = "874140517@qq.com";
				}
				intent.putExtra("EMAIL", email_info);
				anim_up_gone.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						LoginActivity.this.startActivityForResult(intent, 1);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				anim_up_gone.setFillAfter(true);
				password.setText(R.string.password);
				account.setText(null);
				account.setFocusable(false);
				account.setFocusableInTouchMode(true);
				password.setFocusable(false);
				password.setFocusableInTouchMode(true);
				account.startAnimation(anim_up_gone);
				password.startAnimation(anim_up_gone);
				
				LOGIN_STATE = R.string.login_state_pre;
				STATUE = R.string.InitState;
			}else {
				Times -= 1;
				Toast.makeText(this.getApplicationContext(),"Wrong pass ,Remaind time : "+String.valueOf(Times) +"to try again" , Toast.LENGTH_SHORT).show();
				if (Times ==0){
					this.finish();
				}
			}
		}
	}
	public boolean onKeyDown(int Keycode,KeyEvent event){
		if (Keycode == KeyEvent.KEYCODE_BACK){
			if (STATUE == R.string.LoginState){
				account.startAnimation(anim_up_gone);
				password.startAnimation(anim_up_gone);
				account.setFocusable(false);
				account.setFocusableInTouchMode(true);
				password.setFocusable(false);
				password.setFocusableInTouchMode(true);
				
				LOGIN_STATE = R.string.login_state_pre;
				STATUE = R.string.InitState;
			}else if (STATUE == R.string.InitState){
				this.finish();
			}
		}
		return false;
	}

	private boolean Judege(String user, String pass) {
		// TODO Auto-generated method stub
		if (!pass.equals("")){
			return true;
		}
		return false;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		LoginActivity.this.finish();
		Log.d("time",String.valueOf(resultCode));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
