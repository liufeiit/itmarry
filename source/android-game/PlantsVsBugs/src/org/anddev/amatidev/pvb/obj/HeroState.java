package org.anddev.amatidev.pvb.obj;


public class HeroState{
	
	protected HeroAttribute maximalValue;
	protected HeroAttribute minimumValue; 
	protected HeroAttribute currentValue;
	
	protected boolean isAlive;
	protected int reliveSecond;
	protected Lever lever;
	protected int currentExperence;
	protected int attackDistance;
	protected HeroType heroType;
	protected Blood blood;
	protected int powerIncreasePerLever;
	protected int agileIncreasePerLever;
	protected int smartIncreasePerLever;
	
	private Equipment[] equipments = new Equipment[6];
	
	public HeroState(){
		isAlive = true;
		lever = Lever.getLever(1);
		reliveSecond = 0;
		currentExperence = 0;
		attackDistance = 3;
		maximalValue = new HeroAttribute(10,10,10,200);
		maximalValue.setDamage(50);
		maximalValue.setDefense(2);
		maximalValue.setSpeed(300);
		maximalValue.setMagicDefense(25);
		maximalValue.setEvasion(0.3f);
		maximalValue.setMagic(100);
		maximalValue.setShotDelay(2.0f);
		minimumValue = new HeroAttribute(10,10,10,200);
		minimumValue.setDamage(50);
		minimumValue.setDefense(2);
		minimumValue.setSpeed(300);
		minimumValue.setMagicDefense(25);
		minimumValue.setEvasion(0.3f);
		minimumValue.setMagic(100);
		minimumValue.setShotDelay(2.0f);
		currentValue = new HeroAttribute(10,10,10,200);
		currentValue.setDamage(50);
		currentValue.setDefense(2);
		currentValue.setSpeed(300);
		currentValue.setMagicDefense(25);
		currentValue.setEvasion(0.3f);
		currentValue.setMagic(100);
		currentValue.setShotDelay(2.0f);
		heroType=HeroType.AGILE;
		caculateBy3D();
	}
	
	/**
	 * 普通攻击伤害
	 * 伤害减少 = ((护甲)*0.06)/(1+0.06*(护甲)) 
	 * @param demage
	 */
	public int physicDemage(int demage){
		int realDemage = 0;
		float DamageReduction = (float) ((maximalValue.getDefense()*0.06)/(1+maximalValue.getDefense()*0.06));
		if(Math.random()>maximalValue.getEvasion()){
			realDemage = (int) (demage*(1-DamageReduction));
			currentValue.setBlood(currentValue.getBlood()-realDemage);
			if(currentValue.getBlood()<=0){
				this.isAlive=false;
			}
			blood.bloodChange(currentValue.getBlood());
		}
		return realDemage;
	}
	
	/**
	 * 吸收魔法伤害
	 * @param demage
	 */
	public void magicDemage(int demage){
		currentValue.setBlood(currentValue.getBlood()-demage);
		blood.bloodChange(currentValue.getBlood());
	}
	
	/**
	 * 获取经验
	 * @param experence
	 */
	public void getExperence(int experence){
		if((currentExperence+experence)>=lever.getNeedExperence()){
			currentExperence=currentExperence+experence-lever.getNeedExperence();
			leverUp();
		}
	}
	
	/**
	 * 等级增加
	 */
	private void leverUp(){
		lever = Lever.getLever(lever.getLever()+1);
		changeAttribute(maximalValue.getPower()+powerIncreasePerLever,
				maximalValue.getAgile()+agileIncreasePerLever,
				maximalValue.getSmart()+smartIncreasePerLever);
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	public boolean isDead() {
		return !isAlive;
	}
	public int getReliveSecond() {
		return reliveSecond;
	}
	public Lever getLever() {
		return lever;
	}
	public int getAttackDistance() {
		return attackDistance;
	}

	public void setBlood(Blood blood) {
		this.blood = blood;
	}
	
	/**
	 * 获取当前总血量
	 * @return
	 */
	public int getMaxBlood(){
		return maximalValue.getBlood();
	}
	
	/**
	 * 获取当前剩余血量
	 * @return
	 */
	public int getCurrentBlood(){
		return currentValue.getBlood();
	}
	
	/**
	 * 获取当前攻击力
	 * @return
	 */
	public int getDamage(){
		return currentValue.getDamage();
	}
	
	/**
	 * 获取当前护甲
	 * @return
	 */
	public int getDefense(){
		return currentValue.getDefense();
	}
	
	public int getAgile() {
		return currentValue.getAgile();
	}
	
	public int getPower() {
		return currentValue.getPower();
	}
	
	public int getSmart() {
		return currentValue.getSmart();
	}
	
	public int getSpeed() {
		return currentValue.getSpeed()/5;
	}
	
	public float getShotDelay() {
		return currentValue.getShotDelay();
	}
	
	
	protected void changeAttribute(int power,int agile,int smart){
		maximalValue.setAgile(agile+maximalValue.getAgile());
		maximalValue.setSmart(smart+maximalValue.getSmart());
		maximalValue.setPower(power+maximalValue.getPower());
		caculateBy3D();
	}
	
	private void caculateBy3D(){
		switch(heroType){
		case POWER:
			maximalValue.setDamage(maximalValue.getPower()+minimumValue.getDamage());break;
		case AGILE:
			maximalValue.setDamage(maximalValue.getAgile()+minimumValue.getDamage());break;
		case SMART:
			maximalValue.setDamage(maximalValue.getSmart()+minimumValue.getDamage());break;
		default:;
		maximalValue.setBlood(minimumValue.getBlood()+maximalValue.getPower()+19);
		maximalValue.setDefense(minimumValue.getDefense()+maximalValue.getAgile()/7);
		maximalValue.setMagic(minimumValue.getMagic()+maximalValue.getSmart()*13);
	}
	}

	
}