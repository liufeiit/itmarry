package org.anddev.amatidev.pvb.obj;

public class DotaPoint {
	Graph theGraph;
	String map ="111111111111111"+
				"110000000000011"+
				"101000000000101"+
				"100100000001001"+
				"100010000010001"+
				"100001000100001"+
				"100000101000001"+
				"100000010000001"+
				"100000101000001"+
				"100001000100001"+
				"100010000010001"+
				"100100000001001"+
				"101000000000101"+
				"110000000000011"+
				"111111111111111";
	public void generatePoints(){
		char[] m = map.toCharArray();
		theGraph = new Graph();
		int b=0;
		for(int a=0;a<225;a++){
			if(a%15==14) b++;
			if(m[a]=='1'){
				
			}
		}
	}
}
