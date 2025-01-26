package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Name("View Distance of Client")
@Description("The view distance of the client. Can not be changed. " +
	"This differs from the server side view distance of player as this will retrieve the view distance the player has set on their client.")
@Examples({"set {_clientView} to the client view distance of player", "set view distance of player to client view distance of player"})
@RequiredPlugins("1.13.2+")
@Since("2.5")
public class ExprClientViewDistance extends SimplePropertyExpression<Player, Long> {
	
	static {
		if (Skript.methodExists(Player.class, "getClientViewDistance")) {
			register(ExprClientViewDistance.class, Long.class, "client view distance[s]", "players");
		}
	}
	
	@Nullable
	@Override
	public Long convert(Player player) {
		return (long) player.getClientViewDistance();
	}

	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}

	@Override
	protected String getPropertyName() {
		return "client view distance";
	}

}
