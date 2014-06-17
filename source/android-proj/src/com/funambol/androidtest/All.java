package com.funambol.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;


public class All extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all);
        ListView list = (ListView) findViewById(R.id.ListView01);
        ListAdapter adapter = null;
		list.setAdapter(adapter);
    }

}