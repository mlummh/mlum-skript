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
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ExprClickedRawSlot extends SimpleExpression<Integer> {

	static {
		Skript.registerExpression(ExprClickedCursor.class, ItemStack.class, ExpressionType.SIMPLE,
			"[the] [clicked] raw slot");
	}

	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}

	public boolean isSingle() {
		return true;
	}

	public boolean init(Expression<?>[] args, int arg1, Kleenean arg2, SkriptParser.ParseResult arg3) {
		if (!ParserInstance.get().isCurrentEvent(InventoryClickEvent.class)) {
			Skript.error("You can not use clicked raw slot expression in any event but inventory click!");
			return false;
		}
		return true;
	}

	public String toString(@Nullable Event arg0, boolean arg1) {
		return "clicked cursor";
	}

	@Nullable
	protected Integer[] get(Event e) {
		int slot = ((InventoryClickEvent)e).getRawSlot();
		return new Integer[]{slot};
	}
}