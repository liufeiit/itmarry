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
 * 使用示例
 * @author Administrator
 * EmailSender sender = new EmailSender();s
   sender.SendEmail("wuduohangcheng@163.com", "******", "446108264@qq.com", "EmailSender", "Java Mail ！");
 */

public class EmailSender {
    private Properties properties;
    private Session session;
    private Message message;
    private MimeMultipart multipart;
    public String logcat = "";   //调试标志
    
    //服务器地址和端口对照表
    private class SmtpServerInfo
    {
        String host;      //域名
        String prot;      //端口 
        SmtpServerInfo(String host,String port)
        {
            this.host = host;
            this.prot = port;
        }
    }   
    private HashMap<String,SmtpServerInfo> HashMap_AutoProperties = new HashMap<String,SmtpServerInfo>();
    
    private String from_emailaddr;    //源邮箱地址
    private String from_emailpsw;     //源邮箱密码
    private String to_emailaddr;      //目标邮箱地址
    private String to_emailtitle;     //目标邮箱标题
    private String to_emailcontent;   //目标邮箱内容
    
    public EmailSender() {
        super();
        this.properties = new Properties();
        //初始化对照表 - 预置这几种邮箱类型，需要可以在这里添加
        HashMap_AutoProperties.put("qq.com", new SmtpServerInfo("smtp.qq.com","25"));               //qq
        HashMap_AutoProperties.put("126.com", new SmtpServerInfo("smtp.126.com","25"));             //126
        HashMap_AutoProperties.put("163.com", new SmtpServerInfo("smtp.163.com","25"));             //163
        HashMap_AutoProperties.put("yahoo.com.cn", new SmtpServerInfo("smtp.mail.yahoo.com","25")); //雅虎
        HashMap_AutoProperties.put("sina.com", new SmtpServerInfo("smtp.sina.com","25"));           //新浪
        HashMap_AutoProperties.put("vip.sina.com", new SmtpServerInfo("smtp.vip.sina.com","25"));   //新浪VIP
        HashMap_AutoProperties.put("sohu.com", new SmtpServerInfo("smtp.sohu.com","25"));           //搜狐
        HashMap_AutoProperties.put("gmail.com", new SmtpServerInfo("smtp.gmail.com","25"));         //Gmail
    }
    
    /**
     * 发送邮件
     * @param from_emailaddr    源邮箱地址
     * @param from_emailpsw     源邮箱密码
     * @param to_emailaddr      目标邮箱地址
     * @param to_emailtitle     目标邮箱标题
     * @param to_emailcontent   目标邮箱内容
     */
    public void SendEmail(String _from_emailaddr,String _from_emailpsw,String _to_emailaddr,String _to_emailtitle,String _to_emailcontent,MainActivity ma) {
       
        this.from_emailaddr = _from_emailaddr;
        this.from_emailpsw = _from_emailpsw;
        this.to_emailaddr = _to_emailaddr;
        this.to_emailtitle = _to_emailtitle;
        this.to_emailcontent = _to_emailcontent;
        
        Log.v("测试from_emailaddr", from_emailaddr);
        Log.v("测试from_emailpsw", from_emailpsw);
        Log.v("测试to_emailaddr", to_emailaddr);
        Log.v("测试to_emailtitle", to_emailtitle);
        Log.v("测试to_emailcontent", to_emailcontent);
        
        Thread thread=new Thread(new Send_thread(this,ma));
        thread.start();

    }
    
    //获取发送现状
    public String getlogcat()
    {
        return logcat;
    }
    
    //发送线程
    private class Send_thread  implements Runnable {
        private EmailSender emailSender;   //对象
        private MainActivity ma;
        Send_thread(EmailSender _emailSender,MainActivity _ma)
        {
           this.emailSender = _emailSender; 
           this.ma = _ma;
        }
        @Override
        public void run(){  
        	
          logcat = "等待发送";
          String hostName  = emailSender.IsEmail(emailSender.from_emailaddr);
          if(null != hostName && (null != IsEmail(emailSender.to_emailaddr)))   //源邮箱地址正确
          {
              
            try {
                //匹配服务器地址和端口
                if(!emailSender.autoProperties(hostName)) return;
                //设置发件信息
                emailSender.SetFrom();
                //设置收件信息
                emailSender.SetTo();
                //发送时间
                emailSender.message.setSentDate(new Date()); 
                //发送的内容，文本和附件
                emailSender.message.setContent(emailSender.multipart); 
                //保存修改
                emailSender.message.saveChanges();
                //创建邮件发送对象，并指定其使用SMTP协议发送邮件  
                Transport transport=emailSender.session.getTransport("smtp"); 
                //获取域名
                SmtpServerInfo ssi =  emailSender.HashMap_AutoProperties.get(hostName); 
                logcat = "源邮箱密码错误";
                //登录邮箱  
                transport.connect(ssi.host,emailSender.from_emailaddr, emailSender.from_emailpsw);
                //发送邮件
                transport.sendMessage(emailSender.message, emailSender.message.getAllRecipients()); 
                logcat = "发送成功";
                Log.v("测试", logcat);
                
                ma.sendmessage(logcat);
                
                //关闭连接
                transport.close();
                return ;
            } catch (AddressException e) {
            	ma.sendmessage("发送失败");
            e.printStackTrace();
           } catch (MessagingException e) {
        	   ma.sendmessage("发送失败");
            e.printStackTrace();
          }
        }
          ma.sendmessage("发送失败");
     }
  }
      
    //判断邮件格式合法性
    private String IsEmail(String _Email) {
//      if (!_Email.matches("w+([-_.]w+)*@w+([-.]w+)*.w+([-.]w+)*")) { //判断格式
//          return null;
//      }
        String hostName = _Email.split("@")[1]; //获得如163.com
        Log.v("测试邮件格式合法性", _Email);
        return hostName;   //格式正确返回域名
    }
    
    //匹配服务器地址和端口
    private boolean autoProperties(String _hostName){
        SmtpServerInfo current_SmtpServerInfo = HashMap_AutoProperties.get(_hostName);
        if(null != current_SmtpServerInfo)
        {
            setProperties(current_SmtpServerInfo.host , current_SmtpServerInfo.prot);
            return true;
        }
        logcat = "无法查询到相应域名和端口";
        return false;
    }   
    
    //设置服务器地址和端口 - 邮箱类型
    private void setProperties(String _host,String _post) {
        //地址
        this.properties.put("mail.smtp.host",_host);
        //端口号
        this.properties.put("mail.smtp.post",_post);
        //是否验证
        this.properties.put("mail.smtp.auth",true);
        this.session=Session.getInstance(properties);
        this.message = new MimeMessage(session);
        this.multipart = new MimeMultipart("mixed");        
    }
    
    //设置发件信息
    private void SetFrom() throws AddressException, MessagingException{
        this.message.setFrom(new InternetAddress(this.from_emailaddr));
        this.message.setSubject(this.to_emailtitle);
        //纯文本的话用setText()就行，不过有附件就显示不出来内容了
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(this.to_emailcontent,"text/html;charset=gbk");
        this.multipart.addBodyPart(textBody);
    }
    
    //设置收件信息
    private void SetTo() throws MessagingException{
        String[] receiver = new String[]{this.to_emailaddr};
        Address[] address = new InternetAddress[receiver.length];
        for(int i=0;i<receiver.length;i++){
            address[i] = new InternetAddress(receiver[i]);
        }
        this.message.setRecipients(Message.RecipientType.TO, address);
    }
    
//主界面的hadler - 保存一份到这里
//    public void sendmessage(String logcat)
//	{
//		Message msg = new Message();  
//        msg.what = 1;  
//        Bundle bundle = new Bundle();    
//        bundle.putString("logcat",logcat);  //往Bundle中存放数据       接收的消息
//        msg.setData(bundle);//mes利用Bundle传递数据   
//        handler.sendMessage(msg);//用activity中的handler发送消息   
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
//			    case 1:   //获得发送具体详情
//			    	 new AlertDialog.Builder(MainActivity.this)
//			    	.setTitle("标题")
//			    	.setMessage(msg.getData().getString("logcat"))
//			    	.setPositiveButton("确定", null)
//			    	.show();
//				break;
//
//			}
//		}
//	};

}
