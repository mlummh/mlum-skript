package org.skriptlang.skript.test.tests.utils;

import ch.njol.skript.util.Utils;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Test methods from the Utils class.
 */
public class UtilsTest {

	/**
	 * Testing method {@link Utils#getSuperType(Class...)}
	 */
	@Test
	public void testSuperClass() {
		Class<?>[][] classes = {
				{Object.class, Object.class},
				{String.class, String.class},
				{String.class, Object.class, Object.class},
				{Object.class, String.class, Object.class},
				{String.class, String.class, String.class},
				{Object.class, String.class, Object.class, String.class, Object.class},
				{Double.class, Integer.class, Number.class},
				{UnknownHostException.class, FileNotFoundException.class, IOException.class},
				{SortedMap.class, TreeMap.class, SortedMap.class},
				{LinkedList.class, ArrayList.class, AbstractList.class},
				{List.class, Set.class, Collection.class},
				{ArrayList.class, Set.class, Collection.class},
		};
		for (Class<?>[] cs : classes) {
			assertEquals(cs[cs.length - 1], Utils.getSuperType(Arrays.copyOf(cs, cs.length - 1)));
		}
	}

}
