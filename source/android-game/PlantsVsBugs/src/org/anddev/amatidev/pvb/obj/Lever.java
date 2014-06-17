package org.anddev.amatidev.pvb.obj;

public class Lever {
	private static Lever[] levers = new Lever[25];
	private int lever;
	private int needExperence;
	/**
	 * 复活时间=等级*4
	 */
	private int aliveTime;
	
	
	
	public static Lever[] getLevers() {
		return levers;
	}

	public int getLever() {
		return lever;
	}

	public int getNeedExperence() {
		return needExperence;
	}

	public int getAliveTime() {
		return aliveTime;
	}

	public static int[] getExperences() {
		return experences;
	}

	private static int experences[] ={200,500,900,1400,2000,2700,3500,4400,5400,6500,7700,9000,10400,11900,13500,15200,17000,18900,20900,23000,25200,27500,29900,32400,35400};
	static{
		for(int a=0;a<25;a++){
			levers[a] = new Lever(a+1,experences[a],(a+1)*4);
		}
	}
	private Lever(int lever,int needExperence,int aliveTime){
		this.lever = lever;
		this.needExperence = needExperence;
		this.aliveTime = aliveTime;
	}
	
	public static Lever getLever(int lever){
		if(lever>25)
			return levers[24];
		else
			return levers[lever-1];
	}
	

}