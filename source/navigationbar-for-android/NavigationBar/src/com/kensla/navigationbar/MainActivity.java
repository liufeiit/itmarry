package com.kensla.navigationbar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		/*
		 * LinearLayout ll = new LinearLayout(this);
		 * 
		 * NavigationBar nb = new NavigationBar(this);
		 * nb.setLeftBarButton("Back"); nb.setRightBarButton("Menu");
		 * nb.setBarTitle("The Bar Title"); NavigationBar.NavigationBarListener
		 * nbl = new NavigationBar.NavigationBarListener() {
		 * 
		 * @Override public void OnNavigationButtonClick(int which) { // do
		 * stuff here } }; nb.setNavigationBarListener(nbl); ll.addView(nb);
		 * setContentView(ll);
		 */

		NavigationBar nb = (NavigationBar) findViewById(R.id.navigationBar);
		nb.setLeftBarButton("Íê³É");
		nb.setRightBarButton("Menu");
		nb.setBarTitle("The Bar Title");
		NavigationBar.NavigationBarListener nbl = new NavigationBar.NavigationBarListener() {

			@Override
			public void OnNavigationButtonClick(int which) {
				// do stuff here
				switch (which) {
				case NavigationBar.NAVIGATION_BUTTON_LEFT:
					Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_LONG).show();
					break;
				case NavigationBar.NAVIGATION_BUTTON_RIGHT:
					Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
		};
		nb.setNavigationBarListener(nbl);
	}

}
