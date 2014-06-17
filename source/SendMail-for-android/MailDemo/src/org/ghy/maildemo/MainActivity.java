package org.ghy.maildemo;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText from_emailEditText;
	EditText from_emailpswEditText;
	EditText to_emailEditText;
	EditText send_content;
	Button btn_send;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FindView();
		SetClick();
        
    }

    public void FindView()
	{
		btn_send = (Button) findViewById(R.id.btn_send);
		from_emailEditText = (EditText)findViewById(R.id.editText1);
		from_emailpswEditText = (EditText)findViewById(R.id.editText2);
		to_emailEditText = (EditText)findViewById(R.id.editText3);
		send_content = (EditText)findViewById(R.id.editText4);
		
		from_emailEditText.setText("747022951@qq.com");
		to_emailEditText.setText("446108264@qq.com");
		send_content.setText("测试");
	}

	public void SetClick()
	{
		// 发送
		btn_send.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				 String from_emailaddr = from_emailEditText.getText().toString(); // 源邮箱地址
				 String from_emailpsw = from_emailpswEditText.getText().toString(); // 源邮箱密码
				 String to_emailaddr = to_emailEditText.getText().toString(); // 目标邮箱地址
				 String to_emailcontent = send_content.getText().toString(); // 目标邮箱内容 
				 if(from_emailaddr.equals("")||from_emailpsw.equals("")||to_emailaddr.equals("")||to_emailcontent.equals(""))
				 {
					 Toast.makeText(MainActivity.this, "不能有空", Toast.LENGTH_SHORT).show();
					 return;
				 }
				 EmailSender sender = new EmailSender();
				sender.SendEmail(from_emailaddr, from_emailpsw, to_emailaddr, "EmailSender", to_emailcontent,MainActivity.this);
				btn_send.setClickable(false);
				Toast.makeText(MainActivity.this, "正在发送", Toast.LENGTH_SHORT).show();
			}
		});
	
	}
    
	public void sendmessage(String logcat)
	{
		Message msg = new Message();  
        msg.what = 1;  
        Bundle bundle = new Bundle();    
        bundle.putString("logcat",logcat);  //往Bundle中存放数据       接收的消息
        msg.setData(bundle);//mes利用Bundle传递数据   
        handler.sendMessage(msg);//用activity中的handler发送消息   
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			    case 1:   //获得发送具体详情
			    	btn_send.setClickable(true);
			    	 new AlertDialog.Builder(MainActivity.this)
			    	.setTitle("标题")
			    	.setMessage(msg.getData().getString("logcat"))
			    	.setPositiveButton("确定", null)
			    	.show();
				break;

			}
		}
	};

}
