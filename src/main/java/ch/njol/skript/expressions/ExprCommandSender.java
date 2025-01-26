package ch.njol.skript.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.EventValueExpression;
import org.bukkit.command.CommandSender;

@Name("Command Sender")
@Description({
	"The player or the console who sent a command. Mostly useful in <a href='commands'>commands</a> and <a href='events.html#command'>command events</a>.",
	"If the command sender is a command block, its location can be retrieved by using %block's location%"
})
@Examples({
	"make the command sender execute \"/say hi!\"",
	"",
	"on command:",
		"\tlog \"%executor% used command /%command% %arguments%\" to \"commands.log\""
})
@Since("2.0")
@Events("command")
public class ExprCommandSender extends EventValueExpression<CommandSender> {

	static {
		register(ExprCommandSender.class, CommandSender.class, "[command['s]] (sender|executor)");
	}

	public ExprCommandSender() {
		super(CommandSender.class);
	}

}
