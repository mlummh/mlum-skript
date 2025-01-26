package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * @author Peter Güttinger
 */
@Name("UUID")
@Description({"The UUID of a player, entity or world.",
		"In the future there will be an option to use a player's UUID instead of the name in variable names (i.e. when %player% is used), but for now this can be used.",
		"<em>Please note that this expression does not work for offline players if you are under 1.8!</em>"}) 
// TODO [UUID] update documentation after release. Add note about requiring Bukkit 1.7.(9/10)?
@Examples({"# prevents people from joining the server if they use the name of a player",
		"# who has played on this server at least once since this script has been added",
		"on login:",
		"	if {uuid::%name of player%} exists:",
		"		{uuid::%name of player%} is not uuid of player",
		"		kick player due to \"Someone with your name has played on this server before\"",
		"	else:",
		"		set {uuid::%name of player%} to uuid of player"})
@Since("2.1.2, 2.2 (offline players' UUIDs), 2.2-dev24 (other entities' UUIDs)")
public class ExprUUID extends SimplePropertyExpression<Object, String> {

	static {
		register(ExprUUID.class, String.class, "UUID", "offlineplayers/worlds/entities");
	}

	@Override
	@Nullable
	public String convert(final Object o) {
		if (o instanceof OfflinePlayer) {
			try {
				return ((OfflinePlayer) o).getUniqueId().toString();
			} catch (UnsupportedOperationException e) {
				// Some plugins (ProtocolLib) try to emulate offline players, but fail miserably
				// They will throw this exception... and somehow server may freeze when this happens
				Skript.warning("A script tried to get uuid of an offline player, which was faked by another plugin (probably ProtocolLib).");
				e.printStackTrace();
				return null;
			}
		} else if (o instanceof Entity) {
			return ((Entity)o).getUniqueId().toString();
		} else if (o instanceof World) {
			return ((World) o).getUID().toString();
		}
		return null;
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "UUID";
	}
	
}
