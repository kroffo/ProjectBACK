

import java.util.HashMap;

public class Chest extends Satchel {
	private String name; 
	private boolean locked;
	private Key correctKey;
	private boolean searched = false;
	
	public Chest(String chestName, boolean lockState, Key key, Room room) {
		name = chestName;
		locked = lockState;
		correctKey = key;
		setRoom(room);
	}
	
	public String toString() {
		if (locked) {
			return name + "\t(Locked)";
		}
		if (searched) {
			return name + "\t(Searched)";
		}
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRoom(Room room) {
		if (room != null) {
			room.addChest(this);
		}
	}
	
	public String searchChest() {
		if (locked) {
			return "The " + name + " is Locked.";
		}
		searched = true;
		String rv = name + ":\n" + contents();
		return rv;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public String getCorrectKeyName() {
		return correctKey.getName();
	}
 	
	public void unlock() {
			locked = false;
	}
	
	public boolean containsWeapon(String weaponName) {
		if (!isLocked() && weapons.get(weaponName) != null) {
			return true;
		}
		return false;
	}
	
	public boolean containsArmor(String armorName) {
		if (!isLocked() && armors.get(armorName) != null) {
			return true;
		}
		return false;
	}
	
	public boolean containsKey(String keyName) {
		if (!isLocked() && keys.get(keyName) != null) {
			return true;
		}
		return false;
	}
	
}