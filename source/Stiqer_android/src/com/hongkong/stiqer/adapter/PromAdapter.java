package com.hongkong.stiqer.adapter;


import java.util.List;

import com.google.zxing.CaptureActivity;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Prom;
import com.hongkong.stiqer.entity.Redeem;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.MainActivity;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.squareup.picasso.Picasso;
import com.aphidmobile.utils.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PromAdapter extends BaseAdapter{
	private final static int    PROMOTION_SAVE_SUCCESS = 1000;
	private final static int    REQUEST_CODE_PROMOTION = 106;
	List<Prom>              mData;
	private LayoutInflater  mInflater;
	private Context         context;
	ViewHolder              holder;
	CustomDialog            PromDialog;
	DialogListener          dialogListener;
	Activity                act;
	
	
	public PromAdapter(Context context, List<Prom> mProm, Activity act){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = mProm;
		this.act = act;
		dialogListener = new DialogListener(){
			public void showDialog(Object o) {
				String p[] = ((String) o).split(",");
				if(p[0].equals("save")){
					doPromSave(p[1]);
				}else{
					doPromRedeem(p[1]);
				}
			}
		};
	}
	
	private void doPromSave(final String pid){
		new Thread(){
			public void run(){
			   int code = APIManager.sharedInstance().addFav(pid,2);
			   Message msg = mHandler.obtainMessage();
			   msg.what = code;
			   mHandler.sendMessage(msg);
			}
		}.start();
	}
	
    private void doPromRedeem(final String pid){
    	Intent intent = new Intent(context, CaptureActivity.class);
    	intent.putExtra("pid" , pid);
    	act.startActivityForResult(intent,REQUEST_CODE_PROMOTION);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
	    if (rowView == null) {
	        rowView = mInflater.inflate(R.layout.fragment_prom, null);
	        ViewHolder holder = new ViewHolder();
	        holder.prom_image = (ImageView) rowView.findViewById(R.id.prom_image);
	        holder.prom_click_more = (LinearLayout) rowView.findViewById(R.id.prom_click_more);
	        holder.swipe_btn = (TextView) rowView.findViewById(R.id.swipe_btn);
	        rowView.setTag(holder);
	    }
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    if(mData.get(position).getError_code() == 100){// if no image
	    	
	    	Picasso.with(context).load(mData.get(position).getDefault_img()).into(holder.prom_image);
	    	holder.prom_click_more.setVisibility(View.GONE);
	    	holder.swipe_btn.setVisibility(View.GONE);
	    	
	    }else{
	    	
	    	Picasso.with(context).load(mData.get(position).getBanner_uri()).into(holder.prom_image);
			UI
	        .<LinearLayout>findViewById(rowView, R.id.prom_click_more)
	        .setOnClickListener(new View.OnClickListener() {
	          @Override
	          public void onClick(View v) {
	        	  PromDialog = CustomDialog.createPromDialog(context,mData.get(position).getPromo_id(),mData.get(position).getPromo_des(),mData.get(position).getImg_uri(),dialogListener,0);
	        	  PromDialog.show();
	          }
	        });
			
	    }
		
		return rowView;
	}
	
	@Override  
    public boolean areAllItemsEnabled() {  
        return false;  
    }  
      
    @Override
    public boolean isEnabled(int position) {  
        return false;  
    }  
    
	static class ViewHolder{ 
		ImageView  prom_image;
		LinearLayout prom_click_more;
		TextView swipe_btn;
    } 
	
	 private Handler mHandler = new Handler()
     {
		 
         public void handleMessage(Message msg)
         {
        	 super.handleMessage(msg);
             switch (msg.what)
             {
              case PROMOTION_SAVE_SUCCESS:
            	 //清空缓存 
            	 Toast.makeText(context, "Save successful", Toast.LENGTH_SHORT).show();
            	 break;
             }
         }
     };
}