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
package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ExprClickedCursor extends SimpleExpression<ItemStack> {

	static {
		Skript.registerExpression(ExprClickedCursor.class, ItemStack.class, ExpressionType.SIMPLE,
			"[the] [click[ed]] cursor",
			"player's cursor");
	}

	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}
	public boolean isSingle() {
		return true;
	}
	public boolean init(Expression<?>[] args, int arg1, Kleenean arg2, SkriptParser.ParseResult arg3) {
		if (!ParserInstance.get().isCurrentEvent(InventoryClickEvent.class, PlayerArmorChangeEvent.class)) {
			Skript.error("You can not use clicked cursor expression in any event but inventory click!");
			return false;
		}
		return true;
	}
	public String toString(@Nullable Event arg0, boolean arg1) {
		return "clicked cursor";
	}
	@Nullable
	protected ItemStack[] get(Event e) {
		ItemStack item = null;
		if (e instanceof InventoryClickEvent) {
			item = ((InventoryClickEvent) e).getCursor();
		} else if (e instanceof PlayerArmorChangeEvent) {
			item = ((PlayerArmorChangeEvent) e).getNewItem();
		}
		return new ItemStack[]{item};
	}

	public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
		Player player = (Player) ((InventoryClickEvent) event).getWhoClicked();
		if (mode == Changer.ChangeMode.SET){
			player.setItemOnCursor((ItemStack)delta[0]);
		} else if (mode == Changer.ChangeMode.DELETE){
			player.setItemOnCursor(new ItemStack(Material.AIR));
		} else if (mode == Changer.ChangeMode.ADD){
			player.getItemOnCursor().setAmount(player.getItemOnCursor().getAmount() + ((Number)delta[0]).intValue());
		} else if (mode == Changer.ChangeMode.REMOVE){
			player.getItemOnCursor().setAmount(player.getItemOnCursor().getAmount() - ((Number)delta[0]).intValue());
		}
	}

	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) {
			return (Class[]) CollectionUtils.array(new Class[] { ItemStack.class });
		}
		return super.acceptChange(mode);
	}
}