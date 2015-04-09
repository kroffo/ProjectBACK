



public abstract class Item {
	private String name;
	private boolean equipped;
	
	public Item(String itemName) {
		name = itemName;
	}
	
	public void equip() {
		equipped = true;
	}
	
	public void unequip() {
		equipped = false;
	}
	
	public boolean isEquipped() {
		return equipped;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract String toString();
}