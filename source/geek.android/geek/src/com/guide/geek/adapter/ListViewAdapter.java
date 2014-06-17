package com.guide.geek.adapter;

import java.io.IOException;
import java.io.InputStream;

import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.galhttprequest.GalHttpRequest;
import com.galhttprequest.GalHttpRequest.GalHttpLoadImageCallBack;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.MeasureSpec;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.guide.geek.R;
import com.guide.geek.app.AppContext;
import com.guide.geek.utils.AsyncImageLoader;
public class ListViewAdapter extends BaseAdapter{
    private Context context;                        //运行上下文
    private List<Map<String, Object>> listItems;    //信息集合
    private LayoutInflater listContainer;            //视图容器
    private boolean[] hasChecked;                    //记录商品选中状态
    private String TAG = "ListViewAdapter";
//    private Handler mhandler = new Handler();
    private  AsyncImageLoader mImageAsynLoader;

    public final class ListItemView{                //自定义控件集合  
//            public ImageView image;  
//            public TextView title; 
//            public TextView text;
//            public TextView info;
//            public CheckBox check;
//            public Button detail;
            public ImageView avatar;
            public TextView username;          
			public TextView signature;
			public TextView follow;
			public int content_type = 0;
		   

//			public TextView contentType;
     }  
    ListItemView  listItemView = null;
  
    
    public ListViewAdapter(Context context, List<Map<String, Object>> listItems, Handler mHandler) {
        this.context = context;            
        listContainer = LayoutInflater.from(context);    //创建视图容器并设置上下文
        
        this.listItems = listItems;
        mImageAsynLoader = new AsyncImageLoader(mHandler);
        System.out.println(listItems.toString());
        hasChecked = new boolean[getCount()];
     
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return listItems.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * 记录勾选了哪个物品
     * @param checkedID 选中的物品序号
     */
    private void checkedChange(int checkedID) {
        hasChecked[checkedID] = !hasChecked[checkedID];
    }
    
    /**
     * 判断物品是否选择
     * @param checkedID 物品序号
     * @return 返回是否选中状态
     */
    public boolean hasChecked(int checkedID) {
        return hasChecked[checkedID];
    }
    
    /**
     * 显示物品详情
     * @param clickID
     */
    private void showDetailInfo(int clickID) {
        new AlertDialog.Builder(context)
        .setTitle("物品详情：" + listItems.get(clickID).get("name"))
        .setMessage(listItems.get(clickID).get("content").toString())              
        .setPositiveButton("确定", null)
        .show();
    }
    
       
    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
      //  Log.e("method", "getView");
        final int selectID = position;
        //自定义视图
        
        
        if (convertView == null) {
            listItemView = new ListItemView(); 
            //获取list_item布局文件的视图
            try{
            	//获取控件对象
            	convertView = listContainer.inflate(R.layout.user_list_items , null);
            	//设置控件集到convertView
            	listItemView.avatar = (ImageView)convertView.findViewById(R.id.user_list_item_avatar);
	            listItemView.username = (TextView)convertView.findViewById(R.id.user_list_item_username);
	            listItemView.signature = (TextView)convertView.findViewById(R.id.user_list_item_signature);
	            listItemView.follow = (TextView)convertView.findViewById(R.id.user_list_item_follow);
	            convertView.setTag(listItemView);
            }catch(Exception e){
            	System.out.println(e+"ss");
            };
            
            
           
        }else {
            listItemView = (ListItemView) convertView.getTag();
        }
       // Log.e("image", (String) listItems.get(position).get("title"));    //测试
//        Log.e("image", (String) listItems.get(position).get("info"));
        
        //设置文字和图片
        /*listItemView.image.setBackgroundResource((Integer) listItems.get(
                position).get("image"));*/
 //       String imageUrl=(String) listItems.get(position).get("image");
//		Bitmap bitmap = getHttpBitmap(imageUrl);//从网络获取图片
//		imageView.setImageBitmap(bitmap); 
       
//        Bitmap bitmap = getHttpBitmap(imageUrl);//从网络获取图片
//        BitmapDrawable icon = null;
//        try{   
//              String headurl=(String) listItems.get(position).get("image");
//               URL url = new URL(headurl);
//              HttpURLConnection hc = (HttpURLConnection)url.openConnection();
//                icon = new BitmapDrawable(hc.getInputStream());
//              hc.disconnect();
//            }catch(Exception e){
//            
//            }
      //  listItemView.image.setImageDrawable(icon);
       // listItemView.image.setImageBitmap(bitmap);
        Bitmap mDefautBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_avatar);
        listItemView.avatar.setTag("http://img0.bdstatic.com/img/image/big-banner.png");
        
        mImageAsynLoader.loadBitmap((ImageView) listItemView.avatar, mDefautBitmap);

        listItemView.username.setText((String) listItems.get(position).get("username"));
        //Log.i("TAG", "result:set name success!");
//        try{
//        	listItemView.content_type=(Integer) listItems.get(position).get("content_type");//Integer.parseInt(string);
//        }catch(Exception e){
//        	
//        	e.printStackTrace();
//        }
        
        listItemView.signature.setText((String) listItems.get(position).get("create_time"));
        String content = (String) listItems.get(position).get("content");
        if(listItemView.content_type == 2){
        	 Log.i("fuck", "这里有一张图片");
        	
	        Drawable draw = AppContext.getContext().getResources().getDrawable(R.drawable.ic_avatar);	        
	        listItemView.follow.setCompoundDrawablesWithIntrinsicBounds(null, draw, null,null);
	     // 异步请求Bitmap  
	        final int positionItem = position;
	    	GalHttpRequest request = GalHttpRequest.requestWithURL(this.context, content);  
	        request.startAsynRequestBitmap(new GalHttpLoadImageCallBack()  
	        {  
	            @Override  
	            public void imageLoaded(Bitmap bitmap)  
	            {  
        			Drawable draw = convertBitmap2Drawable(bitmap);
        			
        			listItemView.follow.setCompoundDrawablesWithIntrinsicBounds(null, draw, null,null);
	            }

				@Override
				public void loadFailed() {
					// TODO Auto-generated method stub
					
				}  
	        });
        }else{
        	listItemView.follow.setText(content);
        }
   
        //listItemView.info.setText((String) listItems.get(position).get("info"));
       // listItemView.detail.setText("商品详情");
        //注册按钮点击时间爱你
//        listItemView.topicContent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //显示物品详情
//                showDetailInfo(selectID);
//            }
//        });
        // 注册多选框状态事件处理
//        listItemView.check
//                .setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView,
//                            boolean isChecked) {
//                        //记录物品选中状态
//                        checkedChange(selectID);
//                    }
//        });
        
        
     
        return convertView;
    }
    public  Bitmap getHttpBitmap(String url)  
    {  
        Bitmap bitmap = null;  
        try  
        {  
            URL pictureUrl = new URL(url);  
            InputStream in = pictureUrl.openStream();  
            bitmap = BitmapFactory.decodeStream(in);  
            in.close();  
                      
        } catch (MalformedURLException e)  
        {  
            e.printStackTrace();  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
          
        return bitmap;  
    } 

    public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
    	BitmapDrawable bd = new BitmapDrawable(bitmap);
    	// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
    	return bd;
    }
 
}