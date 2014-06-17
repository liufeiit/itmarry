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
		send_content.setText("����");
	}

	public void SetClick()
	{
		// ����
		btn_send.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				 String from_emailaddr = from_emailEditText.getText().toString(); // Դ�����ַ
				 String from_emailpsw = from_emailpswEditText.getText().toString(); // Դ��������
				 String to_emailaddr = to_emailEditText.getText().toString(); // Ŀ�������ַ
				 String to_emailcontent = send_content.getText().toString(); // Ŀ���������� 
				 if(from_emailaddr.equals("")||from_emailpsw.equals("")||to_emailaddr.equals("")||to_emailcontent.equals(""))
				 {
					 Toast.makeText(MainActivity.this, "�����п�", Toast.LENGTH_SHORT).show();
					 return;
				 }
				 EmailSender sender = new EmailSender();
				sender.SendEmail(from_emailaddr, from_emailpsw, to_emailaddr, "EmailSender", to_emailcontent,MainActivity.this);
				btn_send.setClickable(false);
				Toast.makeText(MainActivity.this, "���ڷ���", Toast.LENGTH_SHORT).show();
			}
		});
	
	}
    
	public void sendmessage(String logcat)
	{
		Message msg = new Message();  
        msg.what = 1;  
        Bundle bundle = new Bundle();    
        bundle.putString("logcat",logcat);  //��Bundle�д������       ���յ���Ϣ
        msg.setData(bundle);//mes����Bundle��������   
        handler.sendMessage(msg);//��activity�е�handler������Ϣ   
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			    case 1:   //��÷��;�������
			    	btn_send.setClickable(true);
			    	 new AlertDialog.Builder(MainActivity.this)
			    	.setTitle("����")
			    	.setMessage(msg.getData().getString("logcat"))
			    	.setPositiveButton("ȷ��", null)
			    	.show();
				break;

			}
		}
	};

}
