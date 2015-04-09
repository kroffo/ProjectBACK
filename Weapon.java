



public class Weapon extends Item {
	private int attack;
	private int block;
	public Weapon(String weaponName, int startAttack, int startBlock) {
		super(weaponName);
		attack = startAttack;
		block = startBlock;
	}
	
	public String toString() {
		String rv = getName() + "\t~ ATK:" + attack + "  Block: " + block;
		if (this.isEquipped()) {
			rv = rv + "  (EQUIPPED)";
		}
		return rv;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getBlock() {
		return block;
	}
}