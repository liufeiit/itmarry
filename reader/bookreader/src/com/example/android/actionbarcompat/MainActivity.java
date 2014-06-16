/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionbarcompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    
	
	private boolean mAlternateTitle = false;
    Map<String, ?> historylistmap = null;
    
    
    
    private static final int menuInflate[] = {
    	R.menu.main,R.menu.history_select_menu
    };
    private int menuIndex=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listview=null;
        listview=new ListView(this);
        setContentView(listview);
        
        
        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.main,R.id.historylist,getHistoryList());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				clickHistory((String)historylistmap.get(getHistoryList().get(position)));
			}
        });
        //listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    /* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if(menuIndex==1){
			menuIndex=0;
			onResume();
			return;
		}
		super.onBackPressed();
	}
    public List<String> getHistoryList(){
    	List<String> history=null;
    	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        historylistmap = sharedPref.getAll();
        history=new ArrayList<String>();
        history.addAll(historylistmap.keySet());
        return history;
    }

    public void clickHistory(String position){
    	Intent intent = new Intent(this, PagesofBooks.class);
		intent.putExtra("txtPath", position);
		startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(menuInflate[menuIndex], menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_search:
                //Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
            	goNextActivity();
                break;
            case R.id.menu_share:
            	dellist();
                break;
            case R.id.menu_del:
                itemDel();
                break;
            case R.id.menu_selectall:
                
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		
		super.onResume();
		menuIndex=0;
		ListView listview=null;
        listview=new ListView(this);
        
        
        
        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.main,R.id.historylist,getHistoryList());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				clickHistory((String)historylistmap.get(getHistoryList().get(position)));
			}
        });
        setContentView(listview);
		
	}
    /**
     * 显示删除列表
     */
    private void dellist() {
    	ListView lview=null;
    	lview=new ListView(this);
    	lview.setId(R.id.dellist);
    	lview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, getHistoryList()));
    	lview.setItemsCanFocus(false);
        lview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        setContentView(lview);
        menuIndex=1;
        
	}

    /**
     * 
     * 条目删除
     */
    public void itemDel(){
    	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    	SharedPreferences sharedPrefbookpages = getSharedPreferences("PagesofBooks", Context.MODE_PRIVATE);
    	ListView lview = (ListView)findViewById(R.id.dellist);
    	long[] itemids=lview.getCheckItemIds();
    	SharedPreferences.Editor editor = sharedPref.edit();
    	SharedPreferences.Editor bookpageseditor = sharedPrefbookpages.edit();
    	for(long delid:itemids){
    		editor.remove(getHistoryList().get((int)delid));
    		bookpageseditor.remove(getHistoryList().get((int)delid)+"_bookmark");
    		bookpageseditor.remove(getHistoryList().get((int)delid)+"_positionpre");
    	}
    	editor.commit();
    	bookpageseditor.commit();
    	dellist();
    	

    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
    	menu.clear();
    	MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(menuInflate[menuIndex], menu);
		return super.onPrepareOptionsMenu(menu);
    	
    }

	public void goNextActivity(){
    	Intent intent = new Intent(this,FileList.class);
    	startActivity(intent);
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory. 
        File file = Environment.getExternalStorageDirectory();
        return file;
    }

}
