package org.ghy.maildemo;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


/**
 * ʹ��ʾ��
 * @author Administrator
 * EmailSender sender = new EmailSender();s
   sender.SendEmail("wuduohangcheng@163.com", "******", "446108264@qq.com", "EmailSender", "Java Mail ��");
 */

public class EmailSender {
    private Properties properties;
    private Session session;
    private Message message;
    private MimeMultipart multipart;
    public String logcat = "";   //���Ա�־
    
    //��������ַ�Ͷ˿ڶ��ձ�
    private class SmtpServerInfo
    {
        String host;      //����
        String prot;      //�˿� 
        SmtpServerInfo(String host,String port)
        {
            this.host = host;
            this.prot = port;
        }
    }   
    private HashMap<String,SmtpServerInfo> HashMap_AutoProperties = new HashMap<String,SmtpServerInfo>();
    
    private String from_emailaddr;    //Դ�����ַ
    private String from_emailpsw;     //Դ��������
    private String to_emailaddr;      //Ŀ�������ַ
    private String to_emailtitle;     //Ŀ���������
    private String to_emailcontent;   //Ŀ����������
    
    public EmailSender() {
        super();
        this.properties = new Properties();
        //��ʼ�����ձ� - Ԥ���⼸���������ͣ���Ҫ�������������
        HashMap_AutoProperties.put("qq.com", new SmtpServerInfo("smtp.qq.com","25"));               //qq
        HashMap_AutoProperties.put("126.com", new SmtpServerInfo("smtp.126.com","25"));             //126
        HashMap_AutoProperties.put("163.com", new SmtpServerInfo("smtp.163.com","25"));             //163
        HashMap_AutoProperties.put("yahoo.com.cn", new SmtpServerInfo("smtp.mail.yahoo.com","25")); //�Ż�
        HashMap_AutoProperties.put("sina.com", new SmtpServerInfo("smtp.sina.com","25"));           //����
        HashMap_AutoProperties.put("vip.sina.com", new SmtpServerInfo("smtp.vip.sina.com","25"));   //����VIP
        HashMap_AutoProperties.put("sohu.com", new SmtpServerInfo("smtp.sohu.com","25"));           //�Ѻ�
        HashMap_AutoProperties.put("gmail.com", new SmtpServerInfo("smtp.gmail.com","25"));         //Gmail
    }
    
    /**
     * �����ʼ�
     * @param from_emailaddr    Դ�����ַ
     * @param from_emailpsw     Դ��������
     * @param to_emailaddr      Ŀ�������ַ
     * @param to_emailtitle     Ŀ���������
     * @param to_emailcontent   Ŀ����������
     */
    public void SendEmail(String _from_emailaddr,String _from_emailpsw,String _to_emailaddr,String _to_emailtitle,String _to_emailcontent,MainActivity ma) {
       
        this.from_emailaddr = _from_emailaddr;
        this.from_emailpsw = _from_emailpsw;
        this.to_emailaddr = _to_emailaddr;
        this.to_emailtitle = _to_emailtitle;
        this.to_emailcontent = _to_emailcontent;
        
        Log.v("����from_emailaddr", from_emailaddr);
        Log.v("����from_emailpsw", from_emailpsw);
        Log.v("����to_emailaddr", to_emailaddr);
        Log.v("����to_emailtitle", to_emailtitle);
        Log.v("����to_emailcontent", to_emailcontent);
        
        Thread thread=new Thread(new Send_thread(this,ma));
        thread.start();

    }
    
    //��ȡ������״
    public String getlogcat()
    {
        return logcat;
    }
    
    //�����߳�
    private class Send_thread  implements Runnable {
        private EmailSender emailSender;   //����
        private MainActivity ma;
        Send_thread(EmailSender _emailSender,MainActivity _ma)
        {
           this.emailSender = _emailSender; 
           this.ma = _ma;
        }
        @Override
        public void run(){  
        	
          logcat = "�ȴ�����";
          String hostName  = emailSender.IsEmail(emailSender.from_emailaddr);
          if(null != hostName && (null != IsEmail(emailSender.to_emailaddr)))   //Դ�����ַ��ȷ
          {
              
            try {
                //ƥ���������ַ�Ͷ˿�
                if(!emailSender.autoProperties(hostName)) return;
                //���÷�����Ϣ
                emailSender.SetFrom();
                //�����ռ���Ϣ
                emailSender.SetTo();
                //����ʱ��
                emailSender.message.setSentDate(new Date()); 
                //���͵����ݣ��ı��͸���
                emailSender.message.setContent(emailSender.multipart); 
                //�����޸�
                emailSender.message.saveChanges();
                //�����ʼ����Ͷ��󣬲�ָ����ʹ��SMTPЭ�鷢���ʼ�  
                Transport transport=emailSender.session.getTransport("smtp"); 
                //��ȡ����
                SmtpServerInfo ssi =  emailSender.HashMap_AutoProperties.get(hostName); 
                logcat = "Դ�����������";
                //��¼����  
                transport.connect(ssi.host,emailSender.from_emailaddr, emailSender.from_emailpsw);
                //�����ʼ�
                transport.sendMessage(emailSender.message, emailSender.message.getAllRecipients()); 
                logcat = "���ͳɹ�";
                Log.v("����", logcat);
                
                ma.sendmessage(logcat);
                
                //�ر�����
                transport.close();
                return ;
            } catch (AddressException e) {
            	ma.sendmessage("����ʧ��");
            e.printStackTrace();
           } catch (MessagingException e) {
        	   ma.sendmessage("����ʧ��");
            e.printStackTrace();
          }
        }
          ma.sendmessage("����ʧ��");
     }
  }
      
    //�ж��ʼ���ʽ�Ϸ���
    private String IsEmail(String _Email) {
//      if (!_Email.matches("w+([-_.]w+)*@w+([-.]w+)*.w+([-.]w+)*")) { //�жϸ�ʽ
//          return null;
//      }
        String hostName = _Email.split("@")[1]; //�����163.com
        Log.v("�����ʼ���ʽ�Ϸ���", _Email);
        return hostName;   //��ʽ��ȷ��������
    }
    
    //ƥ���������ַ�Ͷ˿�
    private boolean autoProperties(String _hostName){
        SmtpServerInfo current_SmtpServerInfo = HashMap_AutoProperties.get(_hostName);
        if(null != current_SmtpServerInfo)
        {
            setProperties(current_SmtpServerInfo.host , current_SmtpServerInfo.prot);
            return true;
        }
        logcat = "�޷���ѯ����Ӧ�����Ͷ˿�";
        return false;
    }   
    
    //���÷�������ַ�Ͷ˿� - ��������
    private void setProperties(String _host,String _post) {
        //��ַ
        this.properties.put("mail.smtp.host",_host);
        //�˿ں�
        this.properties.put("mail.smtp.post",_post);
        //�Ƿ���֤
        this.properties.put("mail.smtp.auth",true);
        this.session=Session.getInstance(properties);
        this.message = new MimeMessage(session);
        this.multipart = new MimeMultipart("mixed");        
    }
    
    //���÷�����Ϣ
    private void SetFrom() throws AddressException, MessagingException{
        this.message.setFrom(new InternetAddress(this.from_emailaddr));
        this.message.setSubject(this.to_emailtitle);
        //���ı��Ļ���setText()���У������и�������ʾ������������
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(this.to_emailcontent,"text/html;charset=gbk");
        this.multipart.addBodyPart(textBody);
    }
    
    //�����ռ���Ϣ
    private void SetTo() throws MessagingException{
        String[] receiver = new String[]{this.to_emailaddr};
        Address[] address = new InternetAddress[receiver.length];
        for(int i=0;i<receiver.length;i++){
            address[i] = new InternetAddress(receiver[i]);
        }
        this.message.setRecipients(Message.RecipientType.TO, address);
    }
    
//�������hadler - ����һ�ݵ�����
//    public void sendmessage(String logcat)
//	{
//		Message msg = new Message();  
//        msg.what = 1;  
//        Bundle bundle = new Bundle();    
//        bundle.putString("logcat",logcat);  //��Bundle�д������       ���յ���Ϣ
//        msg.setData(bundle);//mes����Bundle��������   
//        handler.sendMessage(msg);//��activity�е�handler������Ϣ   
//	}
//	
//	private Handler handler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			super.handleMessage(msg);
//			switch (msg.what)
//			{
//			    case 1:   //��÷��;�������
//			    	 new AlertDialog.Builder(MainActivity.this)
//			    	.setTitle("����")
//			    	.setMessage(msg.getData().getString("logcat"))
//			    	.setPositiveButton("ȷ��", null)
//			    	.show();
//				break;
//
//			}
//		}
//	};

}
