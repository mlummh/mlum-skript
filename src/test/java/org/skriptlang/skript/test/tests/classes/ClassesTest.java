package org.skriptlang.skript.test.tests.classes;

import ch.njol.skript.entity.*;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.*;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryType;
import org.junit.Test;

public class ClassesTest {

	@Test
	public void serializationTest() {
		Object[] random = {
				// Java
				(byte) 127, (short) 2000, -1600000, 1L << 40, -1.5f, 13.37,
				"String",
				
				// Skript
				SkriptColor.BLACK, StructureType.RED_MUSHROOM, WeatherType.THUNDER,
				new Date(System.currentTimeMillis()), new Timespan(1337), new Time(12000), new Timeperiod(1000, 23000),
				new Experience(15), new Direction(0, Math.PI, 10), new Direction(new double[] {0, 1, 0}),
				new EntityType(new SimpleEntityData(HumanEntity.class), 300),
				new CreeperData(),
				new SimpleEntityData(Snowball.class),
				new ThrownPotionData(),
				new WolfData(),
				new XpOrbData(50),
				
				// Bukkit - simple classes only
				GameMode.ADVENTURE, InventoryType.CHEST, DamageCause.FALL,
				
				// there is also at least one variable for each class on my test server which are tested whenever the server shuts down.
		};
		for (Object o : random)
			Classes.serialize(o); // includes a deserialisation test
	}

}
