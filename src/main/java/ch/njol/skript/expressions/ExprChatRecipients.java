package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Name("Chat Recipients")
@Description("Recipients of chat events where this is called.")
@Examples("chat recipients")
@Since("2.2-Fixes-v7, 2.2-dev35 (clearing recipients)")
public class ExprChatRecipients extends SimpleExpression<Player> {

	static {
		Skript.registerExpression(ExprChatRecipients.class, Player.class, ExpressionType.SIMPLE, "[chat][( |-)]recipients");
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<Player> getReturnType() {
		return Player.class;
	}

	@Override
	public Class<?>[] acceptChange(final ChangeMode mode) {
		return CollectionUtils.array(Player[].class);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!(getParser().isCurrentEvent(AsyncPlayerChatEvent.class))) {
			Skript.error("Cannot use chat recipients expression outside of a chat event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "chat recipients";
	}

	@Override
	@Nullable
	protected Player[] get(Event event) {
		if (!(event instanceof AsyncPlayerChatEvent))
			return null;

		AsyncPlayerChatEvent ae = (AsyncPlayerChatEvent) event;
		Set<Player> playerSet = ae.getRecipients();
		return playerSet.toArray(new Player[playerSet.size()]);
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		if (!(event instanceof AsyncPlayerChatEvent))
			return;

		final Player[] recipients = (Player[]) delta;
		switch (mode) {
			case REMOVE:
				assert recipients != null;
				for (Player player : recipients)
					((AsyncPlayerChatEvent) event).getRecipients().remove(player);
				break;
			case ADD:
				assert recipients != null;
				for (Player player : recipients)
					((AsyncPlayerChatEvent) event).getRecipients().add(player);
				break;
			case SET:
				change(event, delta, ChangeMode.DELETE);
				change(event, delta, ChangeMode.ADD);
				break;
			case REMOVE_ALL:
			case RESET:
			case DELETE:
				((AsyncPlayerChatEvent) event).getRecipients().clear();
				break;
		}
	}
}
