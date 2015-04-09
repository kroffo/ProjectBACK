

import java.util.HashMap;
import java.util.Collection;

public class Room {
	private String name;
	private String description;
	private HashMap<String, Door> doors = new HashMap<String, Door>();
	private HashMap<String, Character> characters = new HashMap<String, Character>();
	private HashMap<String, Chest> chests = new HashMap<String, Chest>();
	
	public Room(String roomName, String descript) {
		name = roomName;
		description = descript;
	}
	
	public String toString() {
		String rv = "\nRoom: " + name + "\nDescription: " + description;
		rv = rv + "\n\tDoors:\n";
		for (Door door : doors.values()) {
			rv = rv + "\t  " + door.toString() + "\n";
		}
		rv = rv + "\tChests:\n";
		for (Chest chest : chests.values()) {
			rv = rv + "\t  " + chest.toString() + "\n";
		}
		rv = rv + "\tCharacters:\n";
		for (Character character : characters.values()) {
			rv = rv + "\t  " + character.toString() + "\n";
		}
		return rv;
	}
	
	public void addCharacter(Character c) {
		characters.put(c.getName(), c);
	}

	public void addChest(Chest c) {
		chests.put(c.getName(), c);
	}
	
	public void addDoor(Door door) {
		doors.put(door.getName(), door);
	}
	
	public void removeCharacter(String characterName) {
		characters.remove(characterName);
	}
	
	public Chest getChest(String name) {
		return chests.get(name);
	}
	
	public Enemy getEnemy(String name) {
		Character c = characters.get(name);
		if (c instanceof Enemy) {
			return (Enemy)c;
		}
		return null;
	}
	
	public Door getDoor(String name) {
		return doors.get(name);
	}
	
	public String getName() {
		return name;
	}
}