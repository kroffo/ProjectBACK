

import java.util.HashMap;

public abstract class Character {
	private Satchel satchel = new Satchel();
	private String name;
	private int health;
	private int strength;
	private int attack;
	private int defense;
	private int agility;
	private int luck;
	private Room room;
	private Armor shirt; //Equipped armor for a given character
	private Armor pants;
	private Armor boots;
	private Armor gloves;
	private Armor shield;
	private Weapon weapon;
	
	public Character(String characterName, int startHealth, int startStrength, int startDefense, int startAgility, int startLuck, Room startRoom) {
		name = characterName;
		health = startHealth;
		attack = strength = startStrength;
		defense = startDefense;
		agility = startAgility;
		luck = startLuck;
		changeRoom(startRoom);
	}
	
	public void setHealth(int healthValue) {
		health = healthValue;
	}
	
	public void setAttack(int attackValue) {
		attack = attackValue;
	}
	
	public void setStrength(int strengthValue) {
		strength = strengthValue;
	}
	
	public void setDefense(int defenseValue) {
		defense = defenseValue;
	}
	
	public void setAgility(int agilityValue) {
		agility = agilityValue;
		if (agility > 100) {
			agility = 100;
		}
	}
	
	public void setLuck(int luckValue) {
		luck = luckValue;
		if (luck > 100) {
			luck = 100;
		}
	}
	
	public void initializeShirt(Armor initialShirt) {
		addArmor(initialShirt);
		equipArmor(initialShirt.getName());
	}
	
	public void initializePants(Armor initialPants) {
		addArmor(initialPants);
		equipArmor(initialPants.getName());
	}
	
	public void initializeBoots(Armor initialBoots) {
		addArmor(initialBoots);
		equipArmor(initialBoots.getName());
	}
	
	public void initializeGloves(Armor initialGloves) {
		addArmor(initialGloves);
		equipArmor(initialGloves.getName());
	}
	
	public void initializeShield(Armor initialShield) {
		addArmor(initialShield);
		equipArmor(initialShield.getName());
	}
	
	public void initializeWeapon(Weapon initialWeapon) {
		addWeapon(initialWeapon);
		equipWeapon(initialWeapon.getName());
	}
	
	public String status() {
		return name + ":\n  Health:\t" + health + "\n  Strength:\t" + strength;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public Weapon getWeapon(String weaponName) { //For get methods, check that weapon is existent in satchel before calling
		return satchel.getWeapon(weaponName);
	}
	
	public Armor getArmor(String armorName) {
		return satchel.getArmor(armorName);
	}
	
	public Key getKey(String keyName) {
		return satchel.getKey(keyName);
	}
	
	public void equipWeapon(String weaponName) {
		Weapon weap = satchel.getWeapon(weaponName);
		unequipWeapon(weapon);
		weap.equip();
		weapon = weap;
		attack += weap.getAttack();
	}
	
	public void equipArmor(String armorName) {
		Armor arm = satchel.getArmor(armorName);
		if (arm != null) {
			String type = arm.getType();
			if (type.equals("Shirt")) {
				unequipArmor(shirt);
				shirt = arm;
			} else if (type.equals("Pants")) {
				unequipArmor(pants);
				pants = arm;
			} else if (type.equals("Boots")) {
				unequipArmor(boots);
				boots = arm;
			} else if (type.equals("Gloves")) {
				unequipArmor(gloves);
				gloves = arm;
			} else if (type.equals("Shield")) {
				unequipArmor(shield);
				 shield= arm;
			}
			arm.equip();
			defense += arm.getDefense();
		}
	}
	
	public void unequipArmor(Armor arm) {
		if (arm != null) {
			arm.unequip();
			defense -= arm.getDefense();
		}
	}
	
	public void unequipWeapon(Weapon weap) {
		if (weap != null) {
			weap.unequip();
			attack -= weap.getAttack();
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getStrength() {
		return strength;
	}
		
	public int getDefense() {
		return defense;
	}	
	
	public int getAgility() {
		return agility;
	}
	
	public int getLuck() {
		return luck;
	}
		
	public void addWeapon(Weapon weap) {
		satchel.addWeapon(weap);
	}
	
	public void addArmor(Armor arm) {
		satchel.addArmor(arm);
	}
	
	public void addKey(Key key) {
		satchel.addKey(key);
	}
	
	public void removeWeapon(String weaponName) {
		satchel.removeWeapon(weaponName);
	}
	
	public void removeArmor(String armorName) {
		satchel.removeArmor(armorName);
	}
	
	public void removeKey(String keyName) {
		satchel.removeKey(keyName);
	}
	
	public String satchelContents() {
		return "\n" + getName() + "'s satchel:\n" + satchel.contents();
	}

	public String getName() {
		return name;
	}
	
	public void changeRoom(Room newRoom) {
		if (room != null) {
			room.removeCharacter(name);
		}
		room = newRoom;
		newRoom.addCharacter(this);
	}
	
	public void takeWeaponFromChest(Chest chest, String weaponName) {
		if (chest.containsWeapon(weaponName)) { //See if chest has the item
			satchel.addWeapon(chest.removeWeapon(weaponName));
		}
	}
	
	public void takeArmorFromChest(Chest chest, String armorName) {
		if (chest.containsArmor(armorName)) { //See if chest has the item
			satchel.addArmor(chest.removeArmor(armorName));
		}
	}
	
	public void takeKeyFromChest(Chest chest, String keyName) {
		if (chest.containsKey(keyName)) { //See if chest has the item
			satchel.addKey(chest.removeKey(keyName));
		}
	}
	
	public Weapon takeWeaponFromSatchel(String weaponName) {
		satchel.getWeapon(weaponName).unequip();
		return satchel.removeWeapon(weaponName);
	}
	
	public Armor takeArmorFromSatchel(String armorName) {
		satchel.getArmor(armorName).unequip();
		return satchel.removeArmor(armorName);
	}
	
	public Key takeKeyFromSatchel(String keyName) {
		return satchel.removeKey(keyName);
	}
	
	public void takeDamage(int damage) {
		health -= damage;
		if (health < 0) {
			health = 0;
		}
	}
	
	public String getEquippedWeaponName() {
		if (weapon != null) {
			return weapon.getName();
		} else {
			return "hands";
		}
	}
	
	public Weapon getEquippedWeapon() {
		return weapon;
	}
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public abstract String toString();
}