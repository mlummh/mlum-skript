package ch.njol.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

@Name("Item Tooltips")
@Description({
	"Show or hide the tooltip of an item.",
	"If changing the 'entire' tooltip of an item, nothing will show up when a player hovers over it.",
	"If changing the 'additional' tooltip, only specific parts (which change per item) will be hidden."
})
@Examples({
	"hide the entire tooltip of player's tool",
	"hide {_item}'s additional tool tip"
})
@RequiredPlugins("Spigot 1.20.5+")
@Since("2.9.0")
public class EffTooltip extends Effect {

	static {
		if (Skript.methodExists(ItemMeta.class, "setHideTooltip", boolean.class)) { // this method was added in the same version as the additional tooltip item flag
			Skript.registerEffect(EffTooltip.class,
				"(show|reveal|:hide) %itemtypes%'[s] [entire|:additional] tool[ ]tip",
				"(show|reveal|:hide) [the] [entire|:additional] tool[ ]tip of %itemtypes%"
			);
		}
	}

	@SuppressWarnings("NotNullFieldNotInitialized")
	private Expression<ItemType> items;
	private boolean hide, entire;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		items = (Expression<ItemType>) exprs[0];
		hide = parseResult.hasTag("hide");
		entire = !parseResult.hasTag("additional");
		return true;
	}

	@Override
	protected void execute(Event event) {
		for (ItemType item : items.getArray(event)) {
			ItemMeta meta = item.getItemMeta();
			if (entire) {
				meta.setHideTooltip(hide);
			} else {
				if (hide) {
					meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
				} else {
					meta.removeItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
				}
			}
			item.setItemMeta(meta);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return (hide ? "hide" : "show") + " the " + (entire ? "entire" : "additional") + " tooltip of " + items.toString(event, debug);
	}

}
