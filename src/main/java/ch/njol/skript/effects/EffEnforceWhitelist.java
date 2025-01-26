package ch.njol.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Utils;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@Name("Enforce Whitelist")
@Description({
	"Enforces or un-enforce a server's whitelist.",
	"All non-whitelisted players will be kicked upon enforcing the whitelist."
})
@Examples({
	"enforce the whitelist",
	"unenforce the whitelist"
})
@Since("2.9.0")
@RequiredPlugins("MC 1.17+")
public class EffEnforceWhitelist extends Effect {

	private static String NOT_WHITELISTED_MESSAGE = "You are not whitelisted on this server!";

	static {
		if (Skript.methodExists(Bukkit.class, "setWhitelistEnforced", boolean.class)) {
			try {
				YamlConfiguration spigotYml = YamlConfiguration.loadConfiguration(new File("spigot.yml"));
				NOT_WHITELISTED_MESSAGE = spigotYml.getString("messages.whitelist", NOT_WHITELISTED_MESSAGE);
			} catch (Exception ignored) {}
			Skript.registerEffect(EffEnforceWhitelist.class, "[:un]enforce [the] [server] white[ ]list");
		}
	}

	private boolean enforce;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		enforce = !parseResult.hasTag("un");
		return true;
	}

	@Override
	protected void execute(Event event) {
		Bukkit.setWhitelistEnforced(enforce);
		reloadWhitelist();
	}

	// A workaround for Bukkit's not kicking non-whitelisted players upon enforcement
	public static void reloadWhitelist() {
		Bukkit.reloadWhitelist();
		if (!Bukkit.hasWhitelist() || !Bukkit.isWhitelistEnforced())
			return;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!player.isWhitelisted() && !player.isOp())
				player.kickPlayer(Utils.replaceChatStyles(NOT_WHITELISTED_MESSAGE));
		}
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return (!enforce ? "un" : "") + "enforce the whitelist";
	}

}
