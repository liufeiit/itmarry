package org.anddev.amatidev.pvb.obj;

public class HeroAttribute {
	//攻击力
	private int damage;
	//闪避
	private float evasion;
	//护甲
	private int defense;
	//血量
	private int blood;
	//魔法值
	private int magic;
	
	//敏捷
	private int agile;
	//力量
	private int power;
	//智力
	private int smart;

	
	//速度
	private int speed;
	//魔抗
	private int magicDefense;
	//攻击间隔
	private float shotDelay=1.0f;
	
	/**
	 * 
	 * @param power 初始力量
	 * @param agile 初始敏捷
	 * @param smart 初始智慧
	 * @param blood 初始血量
	 */
	public HeroAttribute(int power, int agile, int smart,int blood) {
		super();
		this.agile = agile;
		this.power = power;
		this.smart = smart;
		this.blood = blood;
	}
	
	
	
	
	public int getAgile() {
		return agile;
	}
	
	public void setAgile(int agile) {
		this.agile = agile;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public int getSmart() {
		return smart;
	}
	
	public void setSmart(int smart) {
		this.smart = smart;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public float getEvasion() {
		return evasion;
	}

	public void setEvasion(float evasion) {
		this.evasion = evasion;
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getMagicDefense() {
		return magicDefense;
	}

	public void setMagicDefense(int magicDefense) {
		this.magicDefense = magicDefense;
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public float getShotDelay() {
		return shotDelay;
	}

	public void setShotDelay(float shotDelay) {
		this.shotDelay = shotDelay;
	}
	
	
	
	
	
	
	
}