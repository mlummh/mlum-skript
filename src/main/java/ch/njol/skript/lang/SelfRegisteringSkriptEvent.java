package ch.njol.skript.lang;

import ch.njol.skript.config.Config;
import org.bukkit.event.Event;

import java.util.Objects;

/**
 * @deprecated Regular {@link org.skriptlang.skript.lang.structure.Structure} methods should be used.
 * See individual methods for their equivalents.
 */
@Deprecated(since = "2.7.0", forRemoval = true)
public abstract class SelfRegisteringSkriptEvent extends SkriptEvent {

	/**
	 * This method is called after the whole trigger is loaded for events that fire themselves.
	 *
	 * @param t the trigger to register to this event
	 * @deprecated This method's functionality can be replaced by overriding {@link #postLoad()}.
	 * Normally, that method would register the parsed trigger with {@link ch.njol.skript.SkriptEventHandler}.
	 * A reference to the {@link Trigger} is available through {@link #trigger}.
	 */
	@Deprecated(since = "2.10.0", forRemoval = true)
	public abstract void register(Trigger t);

	/**
	 * This method is called to unregister this event registered through {@link #register(Trigger)}.
	 *
	 * @param t the same trigger which was registered for this event
	 * @deprecated This method's functionality can be replaced by overriding {@link #unload()}.
	 * Normally, that method would unregister the parsed trigger with {@link ch.njol.skript.SkriptEventHandler}.
	 * A reference to the {@link Trigger} is available through {@link #trigger}.
	 */
	@Deprecated(since = "2.10.0", forRemoval = true)
	public abstract void unregister(Trigger t);

	/**
	 * This method is called to unregister all events registered through {@link #register(Trigger)}.
	 * This is called on all registered events, thus it can also only unregister the
	 * event it is called on.
	 * @deprecated This method should no longer be used.
	 * Each trigger should be unregistered through {@link #unregister(Trigger)}.
	 */
	@Deprecated(since = "2.10.0", forRemoval = true)
	public abstract void unregisterAll();

	@Override
	public boolean load() {
		boolean load = super.load();
		if (load)
			afterParse(Objects.requireNonNull(getParser().getCurrentScript()).getConfig());
		return load;
	}

	@Override
	public boolean postLoad() {
		register(trigger);
		return true;
	}

	@Override
	public void unload() {
		unregister(trigger);
	}

	@Override
	public final boolean check(Event e) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is called when this event is parsed. Overriding this is
	 * optional, and usually not needed.
	 * @param config Script that is being parsed
	 * @deprecated Use {@link #postLoad()} instead.
	 */
	@Deprecated(since = "2.7.0", forRemoval = true)
	public void afterParse(Config config) {

	}

	@Override
	public boolean isEventPrioritySupported() {
		return false;
	}

}
