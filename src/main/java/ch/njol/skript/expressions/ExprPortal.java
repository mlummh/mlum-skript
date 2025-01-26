package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.event.world.PortalCreateEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

@Name("Portal")
@Description("The blocks associated with a portal in the portal creation event.")
@Examples({"on portal creation:",
		"	loop portal blocks:",
		"		broadcast \"%loop-block% is part of a portal!\""})
@Since("2.4")
@Events("portal_create")
public class ExprPortal extends SimpleExpression<Block> {

	// 1.14+ returns List<BlockState>, 1.13.2 and below returns ArrayList<Block> 
	private static final boolean USING_BLOCKSTATE = Skript.isRunningMinecraft(1, 14);
	
	static {
		Skript.registerExpression(ExprPortal.class, Block.class, ExpressionType.SIMPLE, 
				"[the] portal['s] blocks",
				"[the] blocks of [the] portal");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
		if (getParser().isCurrentEvent(PortalCreateEvent.class))
			return true;
		Skript.error("The 'portal' expression may only be used in a portal creation event.");
		return false;
	}

	@Nullable
	@Override
	protected Block[] get(Event e) {
		if (!(e instanceof PortalCreateEvent))
			return null;

		List<?> blocks = ((PortalCreateEvent) e).getBlocks();
		if (USING_BLOCKSTATE)
			return blocks.stream()
					.map(block -> ((BlockState) block).getBlock())
					.toArray(Block[]::new);
		return blocks.stream()
				.map(Block.class::cast)
				.toArray(Block[]::new);
	}

	@Nullable
	@Override
	public Iterator<Block> iterator(Event e) {
		if (!(e instanceof PortalCreateEvent))
			return null;

		List<?> blocks = ((PortalCreateEvent) e).getBlocks();
		if (USING_BLOCKSTATE) 
			return blocks.stream()
					.map(block -> ((BlockState) block).getBlock())
					.iterator();
		return (Iterator<Block>) blocks.iterator();
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public Class<Block> getReturnType() {
		return Block.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "the portal blocks";
	}

}
