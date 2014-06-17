package com.yjying.MyPCMPlay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyPCMPlayActivity extends Activity {
    /** Called when the activity is first created. */
	byte []data = null;
	boolean fileonsdcard=false;

	
	Button btnPlay, btnStopButton;
	TextView textView;
	//String  filePath = "/sdcard/testmusic.pcm";
	String  filePath = "/sdcard/pcm_8k_m_16.DAT";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView = (TextView)findViewById(R.id.textview01);
        
        // 将PCM数据从固定路径的文件中读取（路径写死了，自己根据需要修改吧）
        data = getPCMData();
        
        if (data == null){
        	Toast.makeText(this, "can't find the file : " + filePath,
        			200).show();
        }
        
        init();
    }
    
public byte[] getPCMData(){
	byte[] data_pack = null;
	
   if(fileonsdcard)
   {
    	File file = new File(filePath);
    	if (file == null){
    		return null;
    	}
    	
    	FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		
    	if (inStream != null){
    		long size = file.length();
    		
    		data_pack = new byte[(int) size];
    		try {
				inStream.read(data_pack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
  
    	}
   }
   else
   {
	   try { 
           //InputStreamReader inputReader = new InputStreamReader( getResources().openRawResource(R.raw.pcmtest));
           //BufferedReader bufReader = new BufferedReader(inputReader);
   		
   		InputStream in =getResources().openRawResource(R.raw.pcmtest);
   		int pcmfilelength=in.available();
   		data_pack=new byte[pcmfilelength];
   		//data_pack[0]='P';
   		//data_pack[1]='C';
   		//data_pack[2]='M';

   		in.read(data_pack,0,pcmfilelength);

       } catch (Exception e) { 
           e.printStackTrace(); 
       }       
   }
    	
    	return data_pack;
    }
    
    public void play(){
    	if (data == null){
    		Toast.makeText(this, "No File...", 200).show();
    		return ;
    	}
    	
    	
    	textView.setText("Playing...");
    	// 获得构建对象的最小缓冲区大小
		int minBufSize = AudioTrack.getMinBufferSize(8000, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO, 
				AudioFormat.ENCODING_PCM_16BIT);
		
		//        STREAM_ALARM：警告声
		//        STREAM_MUSCI：音乐声，例如music等
		//        STREAM_RING：铃声
		//        STREAM_SYSTEM：系统声音
		//        STREAM_VOCIE_CALL：电话声音
		AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				8000, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				minBufSize,
				AudioTrack.MODE_STREAM);
		
		//mAudioTrack.play();	
		
		int index = 0;
		int offset = 0;
		while(true){
			try {
				Thread.sleep(0);//便于中断
				
				offset = index * 320;
				if (offset >= data.length){
                    break;
                 }
				
				// 这里是真正播放音频数据的地方
				mAudioTrack.play();	
				mAudioTrack.write(data, offset, 320);
					
			} catch (Exception e) {
				// TODO: handle exception
				break;
			}
			index++;
		}
		
		if (mAudioTrack != null){
			mAudioTrack.stop();				       	
			mAudioTrack.release();
		}
    }
    
    public void stop(){
    	textView.setText("Stop...");
    }
    
    
    public void init(){
    	btnPlay = (Button)findViewById(R.id.buttonPlay);
    	btnStopButton = (Button)findViewById(R.id.buttonStop);
    	
    	btnPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				play();
			}
		});
    	
    	
    	btnStopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop();
			}
		});
    }
 
    
    public void onDestroy(){
    	stop();
    	
    	super.onDestroy();
    }
  
}