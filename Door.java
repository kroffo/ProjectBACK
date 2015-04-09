



public class Door {
	private Room r1;
	private Room r2;
	private boolean locked;
	private String name;
	private Key correctKey;
	
	public Door(String doorName, Room room1, Room room2, boolean lockState, Key key) {
		r1 = room1;
		r2 = room2;
		name = doorName;
		locked = lockState;
		correctKey = key;
		r1.addDoor(this);
		r2.addDoor(this);
	}
	
	public String toString() {
		if (locked) {
			return name + "\t(Locked)";
		}
		return name;
	}
	
	public String getCorrectKeyName() {
		return correctKey.getName();
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void unlock() {
			locked = false;
	}
	
	public Room getNeighbor(Room current) {
		if (r1.equals(current)) {
			return r2;
		} 
		return r1;
	}
	
	public String getName() {
		return name;
	}
}