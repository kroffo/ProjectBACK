/* 	Coding of this game began in late April of 2014 as a text based DnD type game to allow me to practice Programming skills.
	The game uses an input XML file. Requirements for the file will be written here:
	All chests must have a key regardless of its lockstate. Create a master key to reference for unlocked chests and just don't put it in the game.
	Characters require attributes of: Name, Health, Strength, Toughness, Luck, and a Start Room.
	Characters may not have a Health, Strength or Defense of 0. Strength must be 100 or less for the player character.
*/

import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws ParserConfigurationException, SAXException {
		SAXParserFactory spfac = SAXParserFactory.newInstance();
	  	SAXParser sp = spfac.newSAXParser();
		Handler hand = new Handler();
		try {
			sp.parse("BACK.xml", hand);
		} catch (IOException e) {
			System.out.println("\nThe Program cannot run because the input file is not in the same directory as the Program.");
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.println("\nEnter a name for your character:");
		String name = sc.next();
		
		
		System.out.println();
		Player.createPlayer(name, 10, 4, 2, 20, 20, hand.getStartRoom());
		Player.getPlayer().addArmor(new Armor("Cloth Shirt", 0, "Shirt"));
		Player.getPlayer().equipArmor("Cloth Shirt");
		Player.getPlayer().addArmor(new Armor("Cloth Pants", 0, "Pants"));
		Player.getPlayer().equipArmor("Cloth Pants");
		Player.getPlayer().addArmor(new Armor("Cloth Shoes", 0, "Shoes"));
		Player.getPlayer().equipArmor("Cloth Shoes");
		
		Player.getPlayer().play(Player.getPlayer());
		
		System.out.println("Thanks for playing!");
	}
}