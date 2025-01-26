package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

@Name("Item with CustomModelData")
@Description("Get an item with a CustomModelData tag. (Value is an integer between 0 and 99999999)")
@Examples({"give player a diamond sword with custom model data 2",
	"set slot 1 of inventory of player to wooden hoe with custom model data 357"})
@RequiredPlugins("1.14+")
@Since("2.5")
public class ExprItemWithCustomModelData extends PropertyExpression<ItemType, ItemType> {
	
	static {
		if (Skript.methodExists(ItemMeta.class, "hasCustomModelData")) {
			Skript.registerExpression(ExprItemWithCustomModelData.class, ItemType.class, ExpressionType.PROPERTY,
				"%itemtype% with [custom] model data %number%");
		}
	}
	
	@SuppressWarnings("null")
	private Expression<Number> data;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
		setExpr((Expression<ItemType>) exprs[0]);
		data = (Expression<Number>) exprs[1];
		return true;
	}
	
	@Override
	protected ItemType[] get(Event e, ItemType[] source) {
		Number data = this.data.getSingle(e);
		if (data == null)
			return source;
		return get(source.clone(), item -> {
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(data.intValue());
			item.setItemMeta(meta);
			return item;
		});
	}
	
	@Override
	public Class<? extends ItemType> getReturnType() {
		return ItemType.class;
	}
	
	@Override
	public String toString(@Nullable Event e, boolean d) {
		return getExpr().toString(e, d) + " with custom model data " + data.toString(e, d);
	}
	
}
