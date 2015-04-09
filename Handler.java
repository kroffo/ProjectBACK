import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Integer;

public class Handler extends DefaultHandler {
	private HashMap<String, Room> rooms = new HashMap<String, Room>();
	private HashMap<String, Key> keys = new HashMap<String, Key>();
	private Room currentRoom = null;
	private Room startRoom = null;
	private Enemy currentEnemy = null;
	private Chest currentChest = null;
	
	public Room getStartRoom() {
		return startRoom;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (qName.equals("Room")) {
			String roomName = attributes.getValue("name");
			Room room = new Room(roomName, attributes.getValue("description"));
			rooms.put(roomName, room);
			if (roomName.equals("Start Room")) {
				startRoom = room;
			}
			currentRoom = room;
		} else if (qName.equals("Enemy")) {
			currentEnemy = new Enemy(attributes.getValue("race"), attributes.getValue("name"), Integer.parseInt(attributes.getValue("health")),
				Integer.parseInt(attributes.getValue("attack")), Integer.parseInt(attributes.getValue("defense")),
				Integer.parseInt(attributes.getValue("agility")), Integer.parseInt(attributes.getValue("luck")), currentRoom);
			currentChest = null;
		} else if (qName.equals("Chest")) {
			boolean lockState = false;
			if (attributes.getValue("lockState").equals("true")) {
				lockState = true;
			}
			currentChest = new Chest(attributes.getValue("name"), lockState, keys.get(attributes.getValue("key")), currentRoom);
			currentEnemy = null;
		} else if (qName.equals("Key")) {
			String name = attributes.getValue("name");
			Key key = new Key(name);
			keys.put(name, key);
			if (currentEnemy != null) {
				currentEnemy.addKey(key);
			} else if (currentChest != null) {
				currentChest.addKey(key);
			}
		} else if (qName.equals("Weapon")) {
			Weapon weapon = new Weapon(attributes.getValue("name"), Integer.parseInt(attributes.getValue("attack")), Integer.parseInt(attributes.getValue("block")));
			if (currentEnemy != null) {
				currentEnemy.addWeapon(weapon);
				currentEnemy.equipWeapon(weapon.getName());
			} else if (currentChest != null) {
				currentChest.addWeapon(weapon);
			}
		} else if (qName.equals("Armor")) {
			Armor armor = new Armor(attributes.getValue("name"), Integer.parseInt(attributes.getValue("defense")), attributes.getValue("type"));
			if (currentEnemy != null) {
				currentEnemy.addArmor(armor);
				currentEnemy.equipArmor(armor.getName());
			} else if (currentChest != null) {
				currentChest.addArmor(armor);
			}
		} else if (qName.equals("Door")) {
			boolean lockState = false;
			if (attributes.getValue("lockState").equals("true")) {
				lockState = true;
			}
			new Door(attributes.getValue("name"), rooms.get(attributes.getValue("room1")), rooms.get(attributes.getValue("room2")), lockState, keys.get(attributes.getValue("key")));
		}
	}
}

/* Input File Format:
	
	<Room>s must have names and descriptions
		<Enemy>s must have a race, name, health, attack, defense, agility, luck
		<Chest>s must have a name, lockState, key
		<Key>s must have a name
		<Weapon>s must have a name, attack, block
		<Armor>s must have a name, defense, type

	<Door>s must have a name, room1, room2, key, lockState    Doors should all be after all of the rooms in the XML file.

	The first tag must be a room called "Start Room".
	Items will be stored in the last container created in the input file. Containers are Enemies and chests.
	!!!!!!! IMPORTANT !!!!!!!  A Key MUST be created BEFORE the Chest or Door it unlocks  !!!!!!! IMPORTANT !!!!!!!
	To aid in doing this, create enemies before chests in a given room. Definately put enemies holding keys before the corresponding chests, or the game won't work.

	There must be a room called the "Heaven Room", which the player enters to win the game.
*/