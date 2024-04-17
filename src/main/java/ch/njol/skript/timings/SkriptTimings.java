/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.timings;

import ch.njol.skript.Skript;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsManager;
import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Static utils for Skript timings.
 */
@SuppressWarnings({"removal"})
public class SkriptTimings {
	
	private static volatile boolean enabled;
	@SuppressWarnings("null")
	private static Skript skript; // Initialized on Skript load, before any timings would be used anyway
	
	@Nullable
	public static Object start(String name) {
		if (!enabled()) // Timings disabled :(
			return null;
		Timing timing = SkriptTimings.of(skript, name);
		timing.startTimingIfSync(); // No warning spam in async code
		assert timing != null;
		return timing;
	}
	
	public static void stop(@Nullable Object timing) {
		if (timing == null) // Timings disabled...
			return;
		((Timing) timing).stopTimingIfSync();
	}
	
	public static boolean enabled() {
		// First check if we can run timings (enabled in settings + running Paper)
		// After that (we know that class exists), check if server has timings running
		return enabled && Timings.isTimingsEnabled();
	}
	
	public static void setEnabled(boolean flag) {
		enabled = flag;
	}
	
	public static void setSkript(Skript plugin) {
		skript = plugin;
	}


	@NotNull
	public static Timing of(@NotNull Plugin plugin, @NotNull String name) {
		try {
			Method method = Timings.class.getDeclaredMethod("ofSafe", String.class, String.class, Timing.class);
			method.setAccessible(true);
			//pluginHandler = Timings.ofSafe(plugin.getName(), "Combined Total", TimingsManager.PLUGIN_GROUP_HANDLER);
			Timing pluginHandler = (Timing) method.invoke(null, plugin.getName(), "Combined Total", TimingsManager.PLUGIN_GROUP_HANDLER);
			return SkriptTimings.of(plugin, name, pluginHandler);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@NotNull
	public static Timing of(@NotNull Plugin plugin, @NotNull String name, @Nullable Timing groupHandler) {
		Preconditions.checkNotNull(plugin, "Plugin can not be null");
		try {
			Method method = TimingsManager.class.getDeclaredMethod("getHandler", String.class, String.class, Timing.class);
			method.setAccessible(true);
			return (Timing) method.invoke(null, plugin.getName(), name, groupHandler);
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		//return TimingsManager.getHandler(plugin.getName(), name, groupHandler);
	}
	
}
