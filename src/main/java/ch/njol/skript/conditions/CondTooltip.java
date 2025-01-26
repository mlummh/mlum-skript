package ch.njol.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

@Name("Has Item Tooltips")
@Description({
	"Whether the entire or additional tooltip of an item is shown or hidden.",
	"The 'entire tooltip' is what shows to the player when they hover an item (i.e. name, lore, etc.).",
	"The 'additional tooltip' hides certain information from certain items (potions, maps, books, fireworks, and banners)."
})
@Examples({
	"send true if entire tooltip of player's tool is shown",
	"if additional tooltip of {_item} is hidden:"
})
@RequiredPlugins("Spigot 1.20.5+")
@Since("2.9.0")
public class CondTooltip extends Condition {

	static {
		if (Skript.methodExists(ItemMeta.class, "isHideTooltip")) {// this method was added in the same version as the additional tooltip item flag
			Skript.registerCondition(CondTooltip.class,
				"[the] [entire|:additional] tool[ ]tip[s] of %itemtypes% (is|are) (:shown|hidden)",
				"[the] [entire|:additional] tool[ ]tip[s] of %itemtypes% (isn't|is not|aren't|are not) (:shown|hidden)",
				"%itemtypes%'[s] [entire|:additional] tool[ ]tip[s] (is|are) (:shown|hidden)",
				"%itemtypes%'[s] [entire|:additional] tool[ ]tip[s] (isn't|is not|aren't|are not) (:shown|hidden)"
			);
		}
	}

	@SuppressWarnings("NotNullFieldNotInitialized")
	private Expression<ItemType> items;
	private boolean entire;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		items = (Expression<ItemType>) exprs[0];
		entire = !parseResult.hasTag("additional");
		setNegated(parseResult.hasTag("shown") ^ (matchedPattern == 1 || matchedPattern == 3));
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (entire)
			return items.check(event, item -> item.getItemMeta().isHideTooltip(), isNegated());
		return items.check(event, item -> item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP), isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "the " + (entire ? "entire" : "additional") + " tooltip of " + items.toString(event, debug) + " is " + (isNegated() ? "hidden" : "shown");
	}

}
