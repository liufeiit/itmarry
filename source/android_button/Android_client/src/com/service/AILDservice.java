package com.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.service.ICat.Stub;

public class AILDservice extends Service {
	private CatBinder catBinder;
	Timer timer =new Timer();
	String [] Colors = new String[]{"ºìÉ«","°×É«","ºÚÉ«"};
	double [] Weights = new double[]{2.3,3.1,1.58};
	private String color ;
	private double weight;
	public class CatBinder extends Stub{

		@Override
		public String getColor() throws RemoteException {
			// TODO Auto-generated method stub
			return color;
		}

		@Override
		public double getWeight() throws RemoteException {
			// TODO Auto-generated method stub
			return weight;
		}
		
	}
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return catBinder;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		catBinder = new CatBinder();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int rand = (int)(Math.random() *3);
				color = Colors[rand];
				weight = Weights[rand];
				
			}
		}, 0,500);
	}
	
@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	timer.cancel();
}
}
