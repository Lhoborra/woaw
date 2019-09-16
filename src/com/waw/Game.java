package com.waw;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListResourceBundle;

import com.waw.accessories.weapon.magic.MagicWand;
import com.waw.accessories.weapon.melee.Axe;
import com.waw.accessories.weapon.melee.Sword;
import com.waw.accessories.weapon.range.Bow;
import com.waw.activities.civilian.Builder;
import com.waw.activities.civilian.Engineer;
import com.waw.activities.military.Archer;
import com.waw.activities.military.Military;
import com.waw.activities.military.Swordman;
import com.waw.activities.military.Wizard;
import com.waw.activities.military.exceptions.NoCurrentWeaponException;
import com.waw.persons.Elf;
import com.waw.persons.Human;
import com.waw.persons.Orc;
import com.waw.persons.Person;
import com.waw.persons.exceptions.AccessoryException;
import com.waw.persons.exceptions.ActivityException;
import com.waw.persons.exceptions.InvalidAccessoryException;
import com.waw.persons.exceptions.InvalidActivityException;

public class Game {

	public static void main(String[] args) {

		//		scenario1();
//		scenario2();
//		scenario3();
		scenario4();
	}

	public static void scenario1() {

		// Civilian 

		try {

			Person gor = new Orc("Gor", 120, new Swordman(), null);
			//			gor.setActivity(new Builder());

			System.out.println(gor);

			//			gor.setActivity(new Engineer());
			//			System.out.println(gor);

			gor.addAccessory(new MagicWand());
			gor.setCurrent_accessory(gor.getAccessories().get(0));

		} catch (ActivityException | AccessoryException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Fin du sc�nario");
		}
	}

	public static void scenario2() {

		try {

			Person illidan = new Elf("Illidan", 130, new Archer(), null);
			illidan.addAccessory(new Bow());

			Person thrall = new Orc("Thrall", 100, new Swordman(), null);
			thrall.addAccessory(new Axe());

			Person medivh = new Human("Medivh", 80, new Wizard(), null);
			medivh.addAccessory(new MagicWand());

			List<Person> fighters = new ArrayList<Person>(Arrays.asList(
					illidan, thrall, medivh
					));
			Collections.shuffle(fighters);

			do {
				Collections.shuffle(fighters);
				fight(fighters);
			} while (areSeveralFightersAlive(fighters));

		} catch (ActivityException | NoCurrentWeaponException| AccessoryException  e) {

			e.printStackTrace();
			System.err.println();
			System.err.println("Scenario cannot go further...");

		} finally {
			System.out.println("Fin du scénario");
		}
	}

	@SuppressWarnings("rawtypes")
	public static void fight(List<Person> fighters) throws NoCurrentWeaponException {

		for (Iterator iterator = fighters.iterator(); iterator.hasNext();) {

			Person current = (Person) iterator.next();

			if (!iterator.hasNext()) break;

			Person enemy = (Person) iterator.next();
			Military activity = (Military)current.getActivity();

			try {
				activity.attack(current, enemy);
			} catch (NoCurrentWeaponException e) {
				throw new NoCurrentWeaponException(current);
			}

			if (!enemy.isAlive()) {
				System.out.println(enemy.getName() + " fought well ! But died...");
				iterator.remove();
				System.out.println("Remaining fighters :");
				fighters.forEach((p) -> System.out.println(p.getName() + " "));
				System.out.println();
			}
		}
	}

	public static boolean areSeveralFightersAlive(List<Person> fighters) {
		return fighters.size() > 1;
	}

	public static void scenario3 () {
		List<Person> dreamteam = new ArrayList<Person>();
		Person Lucky;
		Person Lhoborra;
		Person Apocali;

		File f = new File("heroes.txt");
		try (
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
					){
			Lucky = new Elf("Senpai", 130, new Archer(), null);
			Lucky.addAccessory(new Bow());

			Lhoborra = new Orc("Lhobo", 100, new Swordman(), null);
			Lhoborra.addAccessory(new Axe());

			Apocali = new Human("Apo", 80, new Wizard(), null);
			Apocali.addAccessory(new MagicWand());

			dreamteam.add(Lucky);
			dreamteam.add(Lhoborra);
			dreamteam.add(Apocali);

			for (Person mongolo : dreamteam) {
				oos.writeObject(mongolo);
			}
				
		} catch (InvalidAccessoryException | ActivityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void scenario4 () {
		File f = new File("heroes.txt");
		List<Person> mongolo = new ArrayList<Person>();
		
		try (
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		){
			while (true) {
				mongolo.add((Person)ois.readObject());
			}
			
		} catch (EOFException e) {
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Person person : mongolo) {
			System.out.println(person);
		}
	}
}