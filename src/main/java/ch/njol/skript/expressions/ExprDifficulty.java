package ch.njol.skript.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Difficulty")
@Description("The difficulty of a world.")
@Examples("set the difficulty of \"world\" to hard")
@Since("2.3")
public class ExprDifficulty extends SimplePropertyExpression<World, Difficulty> {

	static {
		register(ExprDifficulty.class, Difficulty.class, "difficult(y|ies)", "worlds");
	}
	
	@Override
	@Nullable
	public Difficulty convert(World world) {
		return world.getDifficulty();
	}
	
	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET)
			return CollectionUtils.array(Difficulty.class);
		return null;
	}
	
	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		if (delta == null)
			return;
		
		Difficulty difficulty = (Difficulty) delta[0];
		for (World world : getExpr().getArray(e)) {
			world.setDifficulty(difficulty);
			if (difficulty != Difficulty.PEACEFUL)
				world.setSpawnFlags(true, world.getAllowAnimals()); // Force enable spawn monsters as changing difficulty won't change this by itself
		}
	}
	
	@Override
	protected String getPropertyName() {
		return "difficulty";
	}
	
	@Override
	public Class<Difficulty> getReturnType() {
		return Difficulty.class;
	}

}