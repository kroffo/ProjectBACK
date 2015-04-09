
import java.util.Random;
import java.util.Scanner;

public class Player extends Character {
	private static Player player;
	private int wins = 0;
	private int maxHealth;
	
	private Player(String playerName, int startHealth, int startAttack, int startDefense, int startAgility, int startLuck, Room startRoom) {
		super(playerName, startHealth, startAttack, startDefense, startAgility, startLuck, startRoom);
		maxHealth = startHealth;
	}
	
	public static void createPlayer(String playerName, int startHealth, int startAttack, int startDefense, int startAgility, int startLuck, Room startRoom) {
		if (Player.getPlayer() == null) {
			player = new Player(playerName, startHealth, startAttack, startDefense, startAgility, startLuck, startRoom);
		}
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	public String toString() {
		return "Player: " + getName();
	}
	
	public String status() {
		return "\n" + getName() + ":\nLevel: " + (wins/5) + "\n  Health:\t" + getHealth() + "\n  Attack:\t" + getAttack() + "\n  Defense:\t" + getDefense() + "\n  Strength:\t" + getStrength() + "\n  Agility:\t" + getAgility() + "\n  Luck:\t\t" + getLuck() + "\n  Wins:\t\t" + wins+ "\n";
	}
	
	public void play(Player pc) {
		printHelpMessage();
		boolean playerWon = false;
		Scanner sc = new Scanner(System.in);
		System.out.println("\nYou, " + pc.getName() + ", are now playing.\n");
		System.out.println("Enter a command:");
		String command = sc.next();
		sc.nextLine();
		while (true) {  	//Commands:	"Room", "Bag"/"Satchel", "Status", "Open", "Unlock", "Fight",  "Quit",
							//			"Help", "Loot", "Equip", "Fight"/"Battle", "Enemy" 
			if (command.equalsIgnoreCase("Room") || command.equalsIgnoreCase("Look")) {		
				System.out.println(pc.getRoom().toString());
			} else if (command.equalsIgnoreCase("Bag") || command.equalsIgnoreCase("Satchel")) {
				System.out.println(pc.satchelContents());
			} else if (command.equalsIgnoreCase("Status") || command.equalsIgnoreCase("Stats")) {
				System.out.println(status());
			} else if (command.equalsIgnoreCase("Help")) {
				printHelpMessage();
			} else if (command.equalsIgnoreCase("Exit") || command.equalsIgnoreCase("Quit")) {
				System.out.println("\nYou have chosen to exit the game. You will lose all progress made during this session.");
				System.out.println("Are you sure you want to quit? (Anything other than \"Yes\" will be considered a \"No\")");
				if (sc.next().equalsIgnoreCase("Yes")) {
					break;
				}
			} else if (command.equalsIgnoreCase("Enemy")) {
				System.out.println("\nWhich enemy would you like to learn about? (Case-Sensitive)");
				String charName = sc.nextLine();
				Enemy charCheck = getRoom().getEnemy(charName);
				if (charCheck != null) {
					if (charCheck.getHealth() > 0) {
						System.out.println("\n" + charCheck.status());
						String weaponName = charCheck.getEquippedWeaponName();
						if (!weaponName.equals("hands")) {
							System.out.println(charName + " is wielding a " + weaponName);
						}
					} else {
						System.out.println("\n" + charName + " is dead.");
					}
				} else {
					System.out.println("\nNo such enemy exists in the room.");
				}
			} else if (command.equalsIgnoreCase("Fight") || command.equalsIgnoreCase("Battle") || command.equalsIgnoreCase("Attack")) {
				System.out.println("\nWhich character would you like to battle? (Case-Sensitive)");
				String charName = sc.nextLine();
				Enemy charBattle = getRoom().getEnemy(charName);
				if (charBattle != null) {
					if (charBattle.getHealth() > 0) {
						if(battle(sc, pc, charBattle)) {
							break;
						}
					} else {
						System.out.println("\n" + charName + " is already dead.");
					}
				} else {
					System.out.println("\nNo such enemy exists in the room.");
				}
			} else if (command.equalsIgnoreCase("Equip")) {
				/*System.out.println("\nWould you like to equip a \"Weapon\" or \"Armor\"?");
				String secondaryCommand = sc.next();
				sc.nextLine();
				if (secondaryCommand.equalsIgnoreCase("Weapon")) {
					System.out.println("\nEnter the name of the Weapon you would like to equip: (Case-Sensitive)");
					String weaponName = sc.nextLine();
					if (getWeapon(weaponName) != null) {
						equipWeapon(weaponName);
						System.out.println("\nYou wield the " + weaponName + " and your ATK has changed accordingly.");
					} else {
						System.out.println("\nYou do not have a weapon called " + weaponName + ".");
					}
				} else if (secondaryCommand.equalsIgnoreCase("Armor")) {
					System.out.println("\nEnter the name of the Armor you would like to equip: (Case-Sensitive)");
					String armorName = sc.nextLine();
					if (getArmor(armorName) != null) {
						equipArmor(armorName);
						System.out.println("\nYou don the " + armorName + " and your DEF has changed accordingly.");
					} else {
						System.out.println("\nYou do not have an armor called " + armorName + ".");
					}
				} else {
					System.out.println("\nInvalid Command");
				}*/
				System.out.println("\n" + pc.satchelContents());
				System.out.println("\nEnter the name of the Weapon or Armor you would like to equip:");
				String itemName = sc.nextLine();
				if (getWeapon(itemName) != null) {
					Weapon weapon = getWeapon(itemName);
					if (!getWeapon(itemName).isEquipped()) {
						equipWeapon(itemName);
						System.out.println("\nYou wield the " + itemName + " and your ATK has changed accordingly.");
					} else {
						System.out.println("\nThe " + itemName + " is already equipped.");
					}
				} else if (getArmor(itemName) != null) {
					if (!getArmor(itemName).isEquipped()) {
						equipArmor(itemName);
						System.out.println("\nYou don the " + itemName + " and your DEF has changed accordingly.");
					} else {
						System.out.println("\nThe " + itemName + " is already equipped.");
					}
				} else {
					System.out.println("\nYou do not have a weapon or armor called " + itemName + ".");
				}
			} else if (command.equalsIgnoreCase("Peek")) {
				System.out.println("\nEnter the name of the Door. (Case-Sensitive)");
				String doorName = sc.next() + " Door";
				sc.nextLine();
				Door doorCheck = getRoom().getDoor(doorName);
				if (doorCheck != null) {
					if (!doorCheck.isLocked()) {
						System.out.println("\nYou crack the door open and peek inside. You see:");
						System.out.println(doorCheck.getNeighbor(getRoom()).toString());
					} else {
						String keyName = doorCheck.getCorrectKeyName();
						if (pc.getKey(keyName) != null) {
							doorCheck.unlock();
							pc.removeKey(keyName);
							System.out.println("\nYou use the " + keyName + " to unlock the " + doorCheck.getName() + ", breaking the key in the process.");
							System.out.println("\nYou crack the door open and peek inside. You see:");
							System.out.println(doorCheck.getNeighbor(getRoom()).toString());
						} else {
							System.out.println("\nThe " + doorCheck.getName() + " is locked.");
						}
					}
				} else {
						System.out.println("\nNo such door exists in the room.");
				}
			} else if (command.equalsIgnoreCase("Unlock")) {  // 
				/*System.out.println("\nDo you want to unlock a \"Chest\" or a \"Door\"?");
				String secondaryCommand = sc.next();
				sc.nextLine();
				if (secondaryCommand.equalsIgnoreCase("Chest")) {
					System.out.println("\nEnter the name of the chest. (Case-Sensitive)");
					String chestName = sc.next() + " Chest";
					sc.nextLine();
					Chest chestCheck = getRoom().getChest(chestName);
					if (chestCheck != null) {
						if (chestCheck.isLocked()) {
							String keyName = chestCheck.getCorrectKeyName();
							if (pc.getKey(keyName) != null) { //If the player has the key that opens the chest
								chestCheck.unlock();
								pc.removeKey(keyName);
								System.out.println("\nYou use the " + keyName + " to unlock the " + chestCheck.getName() + ", breaking the key in the process.");
							} else {
								System.out.println("\nYou try all of your keys, but none of them open the " + chestCheck.getName() + ".");
							}
						} else {
							System.out.println("\nThe " + chestCheck.getName() + " is already unlocked.");
						}
					} else {
						System.out.println("\nNo such chest exists in the room.");
					}
				} else if (secondaryCommand.equalsIgnoreCase("Door")) {
					System.out.println("\nEnter the name of the Door. (Case-Sensitive)");
					String doorName = sc.next() + " Door";
					sc.nextLine();
					Door doorCheck = getRoom().getDoor(doorName);
					if (doorCheck != null) {
						if (doorCheck.isLocked()) {
							String keyName = doorCheck.getCorrectKeyName();
							if (pc.getKey(keyName) != null) {
								doorCheck.unlock();
								pc.removeKey(keyName);
								System.out.println("\nYou use the " + keyName + " to unlock the " + doorCheck.getName() + ", breaking the key in the process.");
							} else {
								System.out.println("\nYou try all of your keys, but none of them open the " + doorCheck.getName() + ".");
							}
						} else {
							System.out.println("\nThe " + doorCheck.getName() + " is already unlocked.");
						}
					} else {
						System.out.println("\nNo such door exists in the room.");
					}
				}*/
				System.out.println("\nEnter the name of the Door or Chest you would like to unlock: (Case-Sensitive)");
				String unlockName = sc.nextLine();
				Chest chestCheck = getRoom().getChest(unlockName);
				Door doorCheck = getRoom().getDoor(unlockName);
				if (chestCheck != null) {
					if (chestCheck.isLocked()) {
						String keyName = chestCheck.getCorrectKeyName();
						if (pc.getKey(keyName) != null) {
							chestCheck.unlock();
							pc.removeKey(keyName);
							System.out.println("\nYou use the " + keyName + " to unlock the " + chestCheck.getName() + ", breaking the key in the process.");
						} else {
							System.out.println("\nYou try all of your keys, but none of them open the " + chestCheck.getName() + ".");
						}
					} else {
						System.out.println("\nThe " + chestCheck.getName() + " is already unlocked.");
					}
				} else if (doorCheck != null) {
					if (doorCheck.isLocked()) {
						String keyName = doorCheck.getCorrectKeyName();
						if (pc.getKey(keyName) != null) {
							doorCheck.unlock();
							pc.removeKey(keyName);
							System.out.println("\nYou use the " + keyName + " to unlock the " + doorCheck.getName() + ", breaking the key in the process.");
						} else {
							System.out.println("\nYou try all of your keys, but none of them open the " + doorCheck.getName() + ".");
						}
					} else {
						System.out.println("\nThe " + doorCheck.getName() + " is already unlocked.");
					}
				} else {
					System.out.println("\nNo Door or Chest called \"" + unlockName + "\" exists in the room.");
				}
			} else if (command.equalsIgnoreCase("Enter") || command.equalsIgnoreCase("Open")) {
				System.out.println("\nEnter the name of the Door. (Case-Sensitive)");
				String doorName = sc.next() + " Door";
				sc.nextLine();
				Door doorCheck = getRoom().getDoor(doorName);
				if (doorCheck != null) {
					if (!doorCheck.isLocked()) {
						pc.changeRoom(getRoom().getDoor(doorName).getNeighbor(getRoom()));
						System.out.println("\n" + pc.getName() + " enters through the " + doorName + " into the " + getRoom().getName());
					} else {
						String keyName = doorCheck.getCorrectKeyName();
						if (pc.getKey(keyName) != null) {
							doorCheck.unlock();
							pc.removeKey(keyName);
							System.out.println("\nYou use the " + keyName + " to unlock the " + doorCheck.getName() + ", breaking the key in the process.");
							pc.changeRoom(getRoom().getDoor(doorName).getNeighbor(getRoom()));
							System.out.println("\n" + pc.getName() + " enters through the " + doorName + " into the " + getRoom().getName());
						} else {
							System.out.println("\nThe " + doorCheck.getName() + " is locked.");
						}
					}
				} else {
					System.out.println("\nNo such door exists in the room.");
				}
			} else if (command.equalsIgnoreCase("Loot") || command.equalsIgnoreCase("Search")) {
				/*System.out.println("\nDo you want to loot a \"Chest\" or \"Body\".");
				String secondaryCommand = sc.next();
				sc.nextLine();
				if (secondaryCommand.equalsIgnoreCase("Chest")) {
					System.out.println("\nEnter the name of the chest. (Case-Sensitive)");
					String chestName = sc.next() + " Chest";
					sc.nextLine();
					Chest chestCheck = getRoom().getChest(chestName);
					if (chestCheck != null) {
						if (!chestCheck.isLocked()) {
							System.out.println("\nYou open the chest to find the following:\n");
							System.out.println(chestCheck.searchChest());
							System.out.println("\nTo take an item, enter the type, \"Weapon\", \"Armor\" or \"Key\".");
							System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
							String lootCommand = sc.next();
							sc.nextLine();
							while (!lootCommand.equalsIgnoreCase("Exit")) { // Loop to take multiple items from chest without relooting every time.
								if (lootCommand.equalsIgnoreCase("Weapon")) {
									System.out.println("\nEnter the name of the weapon: (Case-Sensitive)");
									String weaponName = sc.nextLine();
									if (chestCheck.containsWeapon(weaponName)) {
										takeWeaponFromChest(chestCheck, weaponName);
										System.out.println("\nYou take the " + weaponName + " from the " + chestName + "\n");
									} else {
										System.out.println("\nThe " + chestName + " does not contain a weapon called " + weaponName + "\n");
									}
								} else if (lootCommand.equalsIgnoreCase("Armor")) {
									System.out.println("\nEnter the name of the Armor: (Case-Sensitive)");
									String armorName = sc.nextLine();
									if (chestCheck.containsArmor(armorName)) {
										takeArmorFromChest(chestCheck, armorName);
										System.out.println("\nYou take the " + armorName + " from the " + chestName + "\n");
									} else {
										System.out.println("\nThe " + chestName + " does not contain an armor called " + armorName + "\n");
									}
								} else if (lootCommand.equalsIgnoreCase("Key")) {
									System.out.println("\nEnter the name of the Key: (Case-Sensitive)");
									String keyName = sc.next() + " Key";
									sc.nextLine();
									if (chestCheck.containsKey(keyName)) {
										takeKeyFromChest(chestCheck, keyName);
										System.out.println("\nYou take the " + keyName + " from the " + chestName + "\n");
									} else {
										System.out.println("\nThe " + chestName + " does not contain a key called " + keyName + "\n");
									}
								}
								System.out.println(chestCheck.searchChest());
								System.out.println("\nTo take an item, enter the type, \"Weapon\", \"Armor\" or \"Key\".");
								System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
								lootCommand = sc.next();
								sc.nextLine();
							}
						} else {
							String chestKeyName = chestCheck.getCorrectKeyName();
							if (pc.getKey(chestKeyName) != null) {
								chestCheck.unlock();
								pc.removeKey(chestKeyName);
								System.out.println("\nYou use the " + chestKeyName + " to unlock the " + chestCheck.getName() + ", breaking the key in the process.");
								
								System.out.println("\nYou open the chest to find the following:\n");
								System.out.println(chestCheck.searchChest());
								System.out.println("\nTo take an item, enter the type, \"Weapon\", \"Armor\" or \"Key\".");
								System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
								String lootCommand = sc.next();
								sc.nextLine();
								while (!lootCommand.equalsIgnoreCase("Exit")) { // Loop to take multiple items from chest without relooting every time.
									if (lootCommand.equalsIgnoreCase("Weapon")) {
										System.out.println("\nEnter the name of the weapon: (Case-Sensitive)");
										String weaponName = sc.nextLine();
										if (chestCheck.containsWeapon(weaponName)) {
											takeWeaponFromChest(chestCheck, weaponName);
											System.out.println("\nYou take the " + weaponName + " from the " + chestName + "\n");
										} else {
											System.out.println("\nThe " + chestName + " does not contain a weapon called " + weaponName + "\n");
										}
									} else if (lootCommand.equalsIgnoreCase("Armor")) {
										System.out.println("\nEnter the name of the Armor: (Case-Sensitive)");
										String armorName = sc.nextLine();
										if (chestCheck.containsArmor(armorName)) {
											takeArmorFromChest(chestCheck, armorName);
											System.out.println("\nYou take the " + armorName + " from the " + chestName + "\n");
										} else {
											System.out.println("\nThe " + chestName + " does not contain an armor called " + armorName + "\n");
										}
									} else if (lootCommand.equalsIgnoreCase("Key")) {
										System.out.println("\nEnter the name of the Key: (Case-Sensitive)");
										String keyName = sc.next() + " Key";
										sc.nextLine();
										if (chestCheck.containsKey(keyName)) {
											takeKeyFromChest(chestCheck, keyName);
											System.out.println("\nYou take the " + keyName + " from the " + chestName + "\n");
										} else {
											System.out.println("\nThe " + chestName + " does not contain a key called " + keyName + "\n");
										}
									}
									System.out.println(chestCheck.searchChest());
									System.out.println("\nTo take an item, enter the type, \"Weapon\", \"Armor\" or \"Key\".");
									System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
									lootCommand = sc.next();
									sc.nextLine();
								}
							} else {
								System.out.println("\nYou try to pry open the " + chestCheck.getName() + " but it is locked shut.");
							}
						}
					} else {
						System.out.println("\nNo such chest exists in the room.");
					}
				} else if (secondaryCommand.equalsIgnoreCase("Body")) {
					System.out.println("\nEnter the name of the character. (Case-Sensitive)");
					String charName = sc.nextLine();
					Enemy charCheck = getRoom().getEnemy(charName);
					if (charCheck != null) {
						if (charCheck.isDead()) {
							System.out.println("\nYou search " + charName + "'s satchel to find the following:\n");
							charCheck.searched();
							System.out.println(charCheck.satchelContents());
							System.out.println("\nTo take an item, enter the type, \"Weapon\", \"Armor\" or \"Key\".");
							System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
							String lootCommand = sc.next();
							sc.nextLine();
							while (!lootCommand.equalsIgnoreCase("Exit")) { // Loop to take multiple items from chest without relooting every time.
								if (lootCommand.equalsIgnoreCase("Weapon")) {
									System.out.println("\nEnter the name of the weapon: (Case-Sensitive)");
									String weaponName = sc.nextLine();
									if (charCheck.getWeapon(weaponName) != null) {
										pc.addWeapon(charCheck.takeWeaponFromSatchel(weaponName));
										System.out.println("\nYou take the " + weaponName + " from " + charName + "'s satchel.\n");
									} else {
										System.out.println("\n" + charName + "'s satchel does not contain a weapon called " + weaponName + "\n");
									}
								} else if (lootCommand.equalsIgnoreCase("Armor")) {
									System.out.println("\nEnter the name of the Armor: (Case-Sensitive)");
									String armorName = sc.nextLine();
									if (charCheck.getArmor(armorName) != null) {
										pc.addArmor(charCheck.takeArmorFromSatchel(armorName));
										System.out.println("\nYou take the " + armorName + " from " + charName + "'s satchel.\n");
									} else {
										System.out.println("\n" + charName + "'s satchel does not contain an armor called " + armorName + "\n");
									}
								} else if (lootCommand.equalsIgnoreCase("Key")) {
									System.out.println("\nEnter the name of the Key: (Case-Sensitive)");
									String keyName = sc.next() + " Key";
									sc.nextLine();
									if (charCheck.getKey(keyName) != null) {
										pc.addKey(charCheck.takeKeyFromSatchel(keyName));
										System.out.println("\nYou take the " + keyName + " from " + charName + "'s satchel.\n");
									} else {
										System.out.println("\n" + charName + "'s satchel does not contain a key called " + keyName + "\n");
									}
								}
								System.out.println(charCheck.satchelContents());
								System.out.println("\nTo take an item, enter the type, \"Weapon\", \"Armor\" or \"Key\".");
								System.out.println("Enter \"Exit\" to exit the satchel and resume the game.");
								lootCommand = sc.next();
								sc.nextLine();
							}
						} else {
							System.out.println("\nYou cannot loot a living character.");
						}
					} else {
						System.out.println("\nNo such character exists in the room.");
					}
				} else {
					System.out.println("\nInvalid Entry");
				}*/
				System.out.println("\nEnter the name of the Body or Chest you would like to loot:");
				String lootName = sc.nextLine();
				Chest chestCheck = getRoom().getChest(lootName);
				Enemy charCheck = getRoom().getEnemy(lootName);
				if (chestCheck != null) {
					if (!chestCheck.isLocked()) {
						pc.lootLoop(chestCheck, sc);
					} else {
						String chestKeyName = chestCheck.getCorrectKeyName();
						if (pc.getKey(chestKeyName) != null) {
							chestCheck.unlock();
							pc.removeKey(chestKeyName);
							System.out.println("\nYou use the " + chestKeyName + " to unlock the " + chestCheck.getName() + ", breaking the key in the process.");
							pc.lootLoop(chestCheck, sc);
						} else {
							System.out.println("\nYou try to pry open the " + chestCheck.getName() + " but it is locked shut.");
						}
					}
				} else if (charCheck != null) {
					if (charCheck.isDead()) {
						pc.lootLoop(charCheck, sc);
					} else {
						System.out.println("\nYou cannot loot a living character.");
					}
				} else {
					System.out.println("No Body or Chest called \"" + lootName + "\" exists in the room.");
				}
			} else {
				System.out.println("\nInvalid Command");
			}
			if (Player.getPlayer().getRoom().getName().equals("Heaven Room")) {
				playerWon = true;
				break;
			}
			System.out.println("\nEnter a command:");
			command = sc.next();
			sc.nextLine();
		}
		if (playerWon) {
			System.out.println("\nCongragulations! You have found the Heaven Room!");
			System.out.println("You have won the game!");
		} else {
			System.out.println("\nSadly, you appear to have failed to win.");
			System.out.println("Better luck next time!");
		}
		System.out.println("\nPlease give me some feedback! Be critical, and tell me what you think could be changed to improve the game.");
		System.out.println("Also, if you can think of a concept for winning that might be better, let me know.");
		System.out.println("Lastly, I wrote the program to run the game to be general, so if you want to write your own world of");
		System.out.println("\trooms, chests, enemies and whatnot, feel free to ask me for an example, formatting details, and guidelines");
		System.out.println("\tand you can write your own world without changing any of the programming. The commands will remain the same,");
		System.out.println("\tbut the world will change.");
		System.out.println("\nAgain, thanks for playing, and have a great day!");
	}
	
	public void printHelpMessage() {
		System.out.println("\nYou are playing ProjectBACK, a text-based game by Kenny Roffo.");
		System.out.println("\nTo win, you must find and enter the Heaven Room");
		System.out.println("\nCommands are not case-sensitive unless stated otherwise.");
		System.out.println("Here is a list of commands you can use to play the game:");
		System.out.println("\tHelp  \t-- Displays this help message again.");
		System.out.println("\tQuit  \t-- Ends the game (Progress will not be saved). [Alt. Command - Exit]");
		System.out.println("\tRoom  \t-- Displays Characters, Chests, and Doors in the current room. [Alt. Command - Look]");
		System.out.println("\tBag   \t-- Displays the Weapons, Armors, and Keys in your personal Satchel. [Alt. Command - Satchel]");
		System.out.println("\tEnter \t-- Move your character through a door to a neighboring room. [Alt. Command - Open]");
		System.out.println("\tStatus\t-- Displays the current stats of your character. [Alt. Command - Stats]");
		System.out.println("\tPeek  \t-- Peek through a door to see what's in a neighboring room.");
		System.out.println("\tUnlock\t-- Unlock a chest or door if you have the right key.");
		System.out.println("\tSearch\t-- Search and take Items from a chest or fallen character. [Alt. Command - Loot]");
		System.out.println("\tEquip \t-- Prompts for input to equip an item from your personal satchel.");
		System.out.println("\tEnemy \t-- Check the Health and Strength of an enemy without battling.");
		System.out.println("\tFight \t-- Prompts for a character which you will then fight to the death. [Alt. Commands - Battle, Attack]");

		System.out.println("\nWeapons raise ATK, Armors raise DEF. Keys unlock at most one door or chest.");
	}
	
	public void lootLoop(Chest chestCheck, Scanner sc) {
		System.out.println("\nYou open the chest to find the following:\n");
		System.out.println(chestCheck.searchChest());
		System.out.println("\nTo take an item, enter its name.");
		System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
		String itemName = sc.nextLine();
		
		while (!itemName.equalsIgnoreCase("Exit")) { 
			String chestName = chestCheck.getName();
			if (chestCheck.containsWeapon(itemName)) {
				takeWeaponFromChest(chestCheck, itemName);
				System.out.println("\nYou take the " + itemName + " from the " + chestName + "\n");
			} else if (chestCheck.containsArmor(itemName)) {
				takeArmorFromChest(chestCheck, itemName);
				System.out.println("\nYou take the " + itemName + " from the " + chestName + "\n");
			} else if (chestCheck.containsKey(itemName)) {
				takeKeyFromChest(chestCheck, itemName);
				System.out.println("\nYou take the " + itemName + " from the " + chestName + "\n");
			} else {
				System.out.println("\nThe " + chestName + " does not contain an item called " + itemName + "\n");
			}
			System.out.println(chestCheck.searchChest());
			System.out.println("\nTo take an item, enter its name.");
			System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
			itemName = sc.nextLine();
		}
	}
	
	public void lootLoop(Enemy charCheck, Scanner sc) {
		String charName = charCheck.getName();
		System.out.println("\nYou search " + charName + "'s satchel to find the following:\n");
		charCheck.searched();
		System.out.println(charCheck.satchelContents());
		System.out.println("\nTo take an item, enter its name.");
		System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
		String itemName = sc.nextLine();
		while (!itemName.equalsIgnoreCase("Exit")) { 
			if (charCheck.getWeapon(itemName) != null) {
				addWeapon(charCheck.takeWeaponFromSatchel(itemName));
				System.out.println("\nYou take the " + itemName + " from " + charName + "'s satchel.\n");
			} else if (charCheck.getArmor(itemName) != null) {
				addArmor(charCheck.takeArmorFromSatchel(itemName));
				System.out.println("\nYou take the " + itemName + " from " + charName + "'s satchel.\n");
			} else if (charCheck.getKey(itemName) != null) { 
				addKey(charCheck.takeKeyFromSatchel(itemName));
				System.out.println("\nYou take the " + itemName + " from " + charName + "'s satchel.\n");
			} else {
				System.out.println("\n" + charName + "'s satchel does not contain a key called " + itemName + "\n");
			}
			System.out.println(charCheck.satchelContents());
			System.out.println("\nTo take an item, enter its name.");
			System.out.println("Enter \"Exit\" to exit the chest and resume the game.");
			itemName = sc.nextLine();
		}
	}

	public void unlockDoor(Door doorCheck) {
		if (doorCheck.isLocked()) {
			String keyName = doorCheck.getCorrectKeyName();
			if (Player.getPlayer().getKey(keyName) != null) {
				doorCheck.unlock();
				Player.getPlayer().removeKey(keyName);
				System.out.println("\nYou use the " + keyName + " to unlock the " + doorCheck.getName() + ", breaking the key in the process.");
			} else {
				System.out.println("\nYou try all of your keys, but none of them open the " + doorCheck.getName() + ".");
			}
		}
	}
	
	public boolean battle(Scanner sc, Player pc, Enemy char2) {
		Random r = new Random();
		boolean playerDied = false;
		boolean playerEscaped = false;
		boolean enemyStumbled = false;
		int startHealth = pc.getHealth();
		int foeHealth = char2.getHealth();
		System.out.println("\nYou have begun a duel with " + char2.getName() + " the " + char2.getRace() + ".");
		System.out.println("To see the rules for battle, enter the command \"Rules\".");
		System.out.println("Let the Battle begin!\n");
		System.out.println("Enter a command:");
		String command = sc.next();
		sc.nextLine();
		while (pc.getHealth() > 0 && char2.getHealth() > 0) {
			int damage = r.nextInt(char2.getAttack()) + 1; //Raw damage is a random number from 1 to the total ATK of a character at that moment.
			damage = damage - pc.getDefense();
			if (damage <= 0) {
				damage = 1;
			}
			if (r.nextInt(100) + 1 < char2.getLuck()) {
				damage *= 2;
			}
			if (command.equalsIgnoreCase("Quit") || command.equalsIgnoreCase("Exit")) {
				System.out.println("\nYou cannot quit the game during a battle. To kill the game press ctrl+\'c\'");
				System.out.println("    (Using this method will work during any point while running a program)");
			} else if (command.equalsIgnoreCase("Rules")) {
				printBattleRules();
			} else if (command.equalsIgnoreCase("Room") || command.equalsIgnoreCase("Look")) {
				System.out.println(pc.getRoom().toString());
			} else if (command.equalsIgnoreCase("Flee")) {  // Finish writing battle method!!!!!!!!!!
				System.out.println("\nEnter the name of the Door:");
				String doorName = sc.next() + " Door";
				sc.nextLine();
				Door doorCheck = getRoom().getDoor(doorName);
				if (doorCheck != null) {
					if (doorCheck.isLocked()) {
						System.out.println("\n" + pc.getName() + " struggles to open the " + doorName + ", but it is locked tight.");
						if (!enemyStumbled) {
							pc.takeDamage(damage);
							System.out.println(char2.getName() + " attacks with the " + char2.getEquippedWeaponName() + " dealing " + damage + " damage to " + pc.getName() + ".");
						} else {
							enemyStumbled = false;
							System.out.println(char2.getName() + " gets up from the ground, and is now ready to fight.");
						}
					} else {
						System.out.println("\n" + pc.getName() + " makes a break for the " + doorName + ".");
						if (!enemyStumbled) {
							pc.takeDamage(damage);
							System.out.println(char2.getName() + " attacks with the " + char2.getEquippedWeaponName() + " dealing " + damage + " damage to " + pc.getName() + ".");
						} else {
							enemyStumbled = false;
							System.out.println(char2.getName() + " gets up from the ground, and is now ready to fight.");
						}
						if (pc.getHealth() <= 0) {
							pc.setHealth(1);
						}
						System.out.println(pc.getName() + " escapes through the " + doorName + " clutching the wound.");
						pc.changeRoom(getRoom().getDoor(doorName).getNeighbor(getRoom()));
						System.out.println(pc.getName() + " hears " + char2.getName() + "'s triumphant laughter through the door.");
						System.out.println(pc.getName() + " is safe...   For now...");
						playerEscaped = true;
						break;
					}
				} else {
					System.out.println("\nNo such door exists in the room.");
				}
			} else if (command.equalsIgnoreCase("Dodge")) {
				if (!enemyStumbled) {
					System.out.println("\n" + char2.getName() + " attacks with the " + char2.getEquippedWeaponName() + ".");
					if (r.nextInt(100) + 1 < pc.getAgility()) {
						System.out.println("\n" + pc.getName() + " dodges the attack!");
						System.out.println(char2.getName() + " stumbles and falls to the ground!");
						enemyStumbled = true;
						System.out.println("\n" + char2.getName() + " is on the ground, and will take a turn to get up before attacking again.");
					} else {
						System.out.println("\n" + pc.getName() + " attempts to dodge the attack, but fails!");
						System.out.println(char2.getName() + " hits, dealing " + damage + " damage to " + pc.getName() + ".");
						pc.takeDamage(damage);
						System.out.println(pc.getName() + " now has a health of " + pc.getHealth() + " while " + char2.getName() + " has a health of " + char2.getHealth());
					}
				} else {
					System.out.println("\nThe enemy is on the ground. There is no attack to dodge.");
				}
			} else if (command.equalsIgnoreCase("Bag") || command.equalsIgnoreCase("Satchel")) {
				System.out.println(pc.satchelContents());
			} else if (command.equalsIgnoreCase("Equip")) {
				System.out.println("\nEnter the name of the Weapon you would like to equip: (Case-Sensitive)");
				String weaponName = sc.nextLine();
				if (getWeapon(weaponName) != null) {
					equipWeapon(weaponName);
					System.out.println("\nYou wield the " + weaponName + " and your ATK has changed accordingly.");
					if (!enemyStumbled) {
						pc.takeDamage(damage);
						System.out.println(char2.getName() + " attacks with the " + char2.getEquippedWeaponName() + " dealing " + damage + " damage to " + pc.getName() + ".");
					} else {
						enemyStumbled = false;
						System.out.println(char2.getName() + " gets up from the ground, and is now ready to fight.");
					}
				} else {
					System.out.println("\nYou do not have a weapon called " + weaponName + ".");
				}
			} else if (command.equalsIgnoreCase("Status") || command.equalsIgnoreCase("Stats")) {
				System.out.println("\n" + char2.status());
				String weaponName = char2.getEquippedWeaponName();
				if (!weaponName.equals("hands")) {
					System.out.println(char2.getName() + " is wielding a " + weaponName);
				}
				System.out.println("\n" + pc.status());
			} else if (command.equalsIgnoreCase("Attack") || command.equalsIgnoreCase("Fight")) {
				System.out.println("\n" + pc.getName() + " attacks with the " + pc.getEquippedWeaponName());
				int inflicting = r.nextInt(getAttack()) + 1;
				inflicting = inflicting - char2.getDefense();
				if (inflicting <= 0) {
					inflicting = 1;
				}
				if (r.nextInt(100) + 1 < pc.getLuck()) {
					inflicting *= 2;
					System.out.println("Cricitcal Hit! Damage is doubled!");
				}
				char2.takeDamage(inflicting);
				System.out.println("\n" + pc.getName() + " deals " + inflicting + " damage to " + char2.getName() + ".");
				if (!enemyStumbled) {
					pc.takeDamage(damage);
					System.out.println("Simultaneously, " + char2.getName() + " attacks with the " + char2.getEquippedWeaponName() + " dealing " + damage + " damage to " + pc.getName() + ".");
				}
				System.out.println(pc.getName() + " now has a health of " + pc.getHealth() + " while " + char2.getName() + " has a health of " + char2.getHealth());
				if (enemyStumbled) {
					enemyStumbled = false;
					System.out.println("\n" + char2.getName() + " gets up from the ground, and is now ready to fight.");
				}
			} else if (command.equalsIgnoreCase("Block")) {
				if (!enemyStumbled) {
					if (pc.getEquippedWeapon() != null) {
						System.out.println("\n" + char2.getName() + " attacks with the " + char2.getEquippedWeaponName() + ".");
						System.out.println(pc.getName() + " blocks the attack with the "+ pc.getEquippedWeaponName() + ".");
						if (r.nextInt(100) + 1 < (pc.getStrength()/char2.getStrength())*100) { // Test the ratio of player strength to enemy strength
							damage -= 2 * pc.getEquippedWeapon().getBlock();
							if (damage <= 0) {
								damage = 1;
							}
							pc.takeDamage(damage);
							System.out.println("\n" + pc.getName() + " takes " + damage + " damage.");
							System.out.println(char2.getName() + " falls due to the resistance of " + pc.getName() + "'s block.");
							System.out.println(char2.getName() + " will take a turn to get back up before attacking again.");
							enemyStumbled = true;
						} else {
							damage -= pc.getWeapon(pc.getEquippedWeaponName()).getBlock();
							if (damage <= 0) {
								damage = 1;
							}
							pc.takeDamage(damage);
							System.out.println("\n" + pc.getName() + " takes " + damage + " damage.");
							System.out.println(char2.getName() + " is knocked back, but is ready to attack again.");
						}
					} else {
						System.out.println(pc.getName() + " has no weapon equipped to block with.");
					}
				} else {
					System.out.println("The enemy is on the ground. There is no attack to block.");
				}
			} else {
				System.out.println("Invalid Command");
			}
			if (pc.getHealth() <= 0) {
				playerDied = true;
				break;
			}
			if (char2.getHealth() <= 0) {
				break;
			}
			System.out.println("\nEnter a command:");
			command = sc.next();
			sc.nextLine();
		}
		if (playerDied) {
			System.out.println("\n" + pc.getName() + " has lost all health, and has fallen to the cold, stone floor.");
			return true;
		} else if (char2.getHealth() == 0) {
			System.out.println("\n" + char2.getName() + " falls to the ground, dead.");
			System.out.println(pc.getName() + " drinks " + char2.getName() + "'s blood, restoring health.");
			maxHealth = maxHealth + foeHealth/10;
			pc.setHealth(maxHealth);
			wins += 1;
			if (wins % 5 == 0) {
				System.out.println("You have won " + wins + " battles! Your stats all increase!");
				setAttack(getAttack() + 5);
				setDefense(getDefense() + 5);
				setStrength(getStrength() + 5);
				setAgility(getAgility() + 3);
				setLuck(getLuck() + 2);	
			}
			return false;
		} else if (playerEscaped) {
			return false;
		}
		return false;
	}
	
	public void printBattleRules() {
		System.out.println("\nThe Battle commands are as follows:");
		System.out.println("\tAttack\t-- Attack with your current weapon. Player and CPU attacks are simultaneous.");
		System.out.println("\tFlee  \t-- Escape the battle by entering a door. Player will take a hit by the enemy before fleeing.");
		System.out.println("\t\tShould the player enter the name of a nonexistent door, the player will not be attacked.");
		System.out.println("\tDodge \t-- A successful dodge might cause the opponent to stumble. Success based on agility.");
		System.out.println("\tEquip \t-- Allows the player to switch weapons, but at the expense of a turn of attack.");
		System.out.println("\tBlock \t-- Player prepares for an incoming attack, decreasing damage taken, possibly causing opponent to stumble.");
		System.out.println("\tRoom  \t-- Prints the contents of the current room. (Does not use a turn)");
		System.out.println("\tStatus\t-- Check the status of the enemy and yourself. (Does not use a turn)");
		System.out.println("\tBag   \t-- Displays the Weapons, Armors, and Keys in your personal Satchel. (Does not use a turn)");
		System.out.println("\nEvery turn consists of one move from the player and one move from the CPU.");
		System.out.println("ATK is determined by base ATK of a player, added witht the ATK of the equipped weapon.");
		System.out.println("Damage taken is determined randomly from 1 to ATK, then divided by the receiver's DEF.");
		System.out.println("Battle continues until one character dies, or the player decides to flee.");
		System.out.println("Should the player die, the game will end in a loss.");
		System.out.println("But should the other character die, the player will live on to progress through the game.");
		System.out.println("Each attack may be critical, doubling damage. This is based on a character's luck.");
		System.out.println("In the event that the player wins, player health goes up by 1/10 of the enemy's starting health (rounded down).");
	}
}