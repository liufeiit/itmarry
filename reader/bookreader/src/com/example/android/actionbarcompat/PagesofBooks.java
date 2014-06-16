package com.example.android.actionbarcompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class PagesofBooks extends Activity {

	int pageNow=0;
	File book=null;
	int currentPosition=0;
	private View mContentView;
    private View mLoadingView;
    private int mShortAnimationDuration;
	StringBuffer sbuffer=new StringBuffer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookpages);
		mContentView = (TextView) findViewById(R.id.text1);
		mLoadingView = findViewById(R.id.loading_spinner);
		
		// Initially hide the content view.
        mContentView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
		Intent intent = this.getIntent();
		String path = (String)intent.getExtras().get("txtPath");
		if(path.indexOf(Environment.getExternalStorageDirectory().getAbsolutePath(), 0)>-1){
			String rp=path.substring(Environment.getExternalStorageDirectory().getAbsolutePath().length());
			String[] pathstr=rp.split("/");
			File f=Environment.getExternalStorageDirectory();
			File[] fl=null;
			for(int i=1;i<pathstr.length;i++){
				final String s = pathstr[i];
				if(fl!=null&&fl.length>0){
					f=fl[0];
				}
				fl=f.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String filename) {
						// TODO Auto-generated method stub
						if(filename.endsWith(s)){
							return true;
						}
						return false;
					}
				});
			}
				book=fl[0];
				InputStreamReader isr=null;
				char[] buffer=new char[1024*8];
				
				int allchar=0;
				try {
					byte[] head = new byte[3];
					String code = "";  
		            code = "gb2312";
			        if (head[0] == -1 && head[1] == -2 )  
			            code = "UTF-16";
			        if (head[0] == -2 && head[1] == -1 )  
			            code = "Unicode";
			        if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
		            code = "UTF-8";
					isr = new InputStreamReader(new FileInputStream(book),code);
					int charnum=0;
					do{
						charnum=isr.read(buffer);
						sbuffer.append(buffer);
						allchar+=charnum;
					}while(charnum!=-1);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						isr.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
				currentPosition=sharedPref.getInt(book.getName()+"_bookmark", 0);
				positionpre=sharedPref.getFloat(book.getName()+"_positionpre", 0);
				allPageInOneView();
		}
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.position:
                Toast.makeText(this, positionpre+"%", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
	float positionpre=0;
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
			TextView tw = (TextView) findViewById(R.id.text1);
			Scroller mScroller = null;
			mScroller = new Scroller(tw.getContext());
			mScroller.forceFinished(false);
			int linenum=tw.getHeight()/tw.getLineHeight();
			if((screenHeight-event.getY())<tw.getHeight()/2.0){
				if(currentPosition<tw.getLineCount()*tw.getLineHeight()){
					
					mScroller.startScroll(0, currentPosition, 0, (linenum-1)*tw.getLineHeight());
				     // Invalidate to request a redraw
					currentPosition+=(linenum-1)*tw.getLineHeight();
				}
				
			}else{
				if((currentPosition-(linenum-1)*tw.getLineHeight())>0){
					
					currentPosition-=(linenum-1)*tw.getLineHeight();
					mScroller.startScroll(0, currentPosition, 0, -(linenum-1)*tw.getLineHeight());
				     // Invalidate to request a redraw
					
				}
			}
			positionpre=(float) (currentPosition*10000/tw.getLineHeight()/tw.getLineCount()/100.0);
			
			tw.setScroller(mScroller);
			tw.invalidate();
		}
		
		return true;
		
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(book.getName()+"_bookmark", currentPosition);
		//positionpre=currentPosition/tw.getLineHeight()/tw.getLineCount()*100;
		editor.putFloat(book.getName()+"_positionpre", positionpre);
		editor.commit();
		
		super.onBackPressed();
	}
	public void prePage(){
		if(pageNow>1){
			pageNow--;
			TextView tw = (TextView) findViewById(R.id.text1);
			CharSequence str = sbuffer.subSequence((pageNow-1)*256, pageNow*256);
			tw.setText(str);
		}
	}
	public void allPageInOneView(){
		TextView tw = (TextView) findViewById(R.id.text1);
		//CharSequence str = sbuffer.subSequence(0, 10240);
		tw.setText(sbuffer);
		
		Scroller mScroller = null;
		mScroller = new Scroller(tw.getContext());
		mScroller.forceFinished(false);
		mScroller.startScroll(0, 0, 0, currentPosition,0);
	     // Invalidate to request a redraw
		
		tw.setScroller(mScroller);
		
		tw.invalidate();
		crossfade();
	}
	public void nextPage(){
		if(pageNow<1000){
			pageNow++;
			TextView tw = (TextView) findViewById(R.id.text1);
			CharSequence str = sbuffer.subSequence((pageNow-1)*256, pageNow*256);
			tw.setText(str);
			Log.v("test", "test");
		}
	}
	public void onStart(){
		super.onStart();
		Log.v("test", "111");
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_position, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }
	
	private void crossfade() {
		mContentView.setVisibility(View.VISIBLE);
		mLoadingView.setVisibility(View.GONE);

//	    // Set the content view to 0% opacity but visible, so that it is visible
//	    // (but fully transparent) during the animation.
//	    mContentView.setAlpha(1f);;
//	    mContentView.setVisibility(View.VISIBLE);
//
//	    // Animate the content view to 100% opacity, and clear any animation
//	    // listener set on the view.
//	    mContentView.animate()
//	            .alpha(1f)
//	            .setDuration(mShortAnimationDuration)
//	            .setListener(null);
//
//	    // Animate the loading view to 0% opacity. After the animation ends,
//	    // set its visibility to GONE as an optimization step (it won't
//	    // participate in layout passes, etc.)
//	    mLoadingView.animate()
//	            .alpha(0f)
//	            .setDuration(mShortAnimationDuration)
//	            .setListener(new AnimatorListenerAdapter() {
//	                @Override
//	                public void onAnimationEnd(Animator animation) {
//	                    mLoadingView.setVisibility(View.GONE);
//	                }
//	            });
	}
	
}
