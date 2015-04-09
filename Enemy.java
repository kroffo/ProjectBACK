



public class Enemy extends Character {
	private String race;
	private boolean searched;
	
	public Enemy(String raceType, String humanName, int startHealth, int startAttack, int startDefense, int startAgility, int startLuck, Room startRoom) {
		super(humanName, startHealth, startAttack, startDefense, startAgility, startLuck, startRoom);
		race = raceType;
	}
	
	public String getRace() {
		return race;
	}
	
	public String toString() {
		 String rv = race + ":\t" + getName();
		 if (isDead()) {
			 rv = rv + "\t[DEAD]";
			 if (searched) {
				 rv = rv + "  (Searched)";
			 }
		 }
		 return rv;
	}
	
	public void searched() {
		searched = true;
	}
}