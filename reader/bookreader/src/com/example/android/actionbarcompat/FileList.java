package com.example.android.actionbarcompat;

import java.io.File;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FileList extends ListActivity {

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(position==0){
			if(!filelist[0].equals("顶级目录")){
				curentFile=curentFile.getParentFile();
			}
			
		}else{
			File f = curentFile.listFiles()[position-1];
			if(f.isDirectory()){
				curentFile=f;
			}else{
				if(f.getName().endsWith(".txt")||f.getName().endsWith(".TXT")){
					Intent intent = new Intent(this, PagesofBooks.class);
					intent.putExtra("txtPath", curentFile.getAbsolutePath()+"/"+f.getName());
					SharedPreferences sharedPref = getSharedPreferences("MainActivity",Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(f.getName(), curentFile.getAbsolutePath()+"/"+f.getName());
					editor.commit();
					startActivity(intent);
					/*
					Intent intent = new Intent(this,ScreenSlideActivity.class);
					startActivity(intent);
					*/
				}
				return;
			}
		}
		filelist=new String[curentFile.list().length+1];
		if(curentFile.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())){
			filelist[0]="顶级目录";
		}else{
			filelist[0]=curentFile.getAbsolutePath()+"    返回上一级目录";
		}
		
		int i=1;
		for (String f:curentFile.list()){
			filelist[i]=f;
			i++;
		}
		//filelist = curentFile.list();
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.file_list_view,R.id.textView1,filelist);
		this.setListAdapter(adapter);
	}
	String[] filelist=null;
	File curentFile=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		curentFile = Environment.getExternalStorageDirectory();
		filelist=new String[curentFile.list().length+1];
		filelist[0]="顶级目录";
		int i=1;
		for (String f:curentFile.list()){
			filelist[i]=f;
			i++;
		}
		//filelist = curentFile.list();
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.file_list_view,R.id.textView1,filelist);
		this.setListAdapter(adapter);
	}

}
