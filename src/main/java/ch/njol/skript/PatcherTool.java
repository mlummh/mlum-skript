package ch.njol.skript;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Allows binary-patching old versions of Skript from this jar. This should
 * probably be only used for fixing security issues etc.
 * 
 * <p>Command-line usage:
 * <pre>
 * java -cp Skript.jar ch.njol.Skript.PatcherTool OLD_JAR [OPTIONS]
 * Options:
 * --security: apply all security related patches
 * --memory-leak: patch a memory leak found in some old versions
 * (this might break some addons and is not compatible with all versions)
 * </pre>
 */
public class PatcherTool {
	
	private static enum Option {
		SECURITY,
		MEMORY_LEAK
	}
	
	public static void main(String... args) throws IOException, URISyntaxException {
		// Figure out paths for current jar and patched jar
		Path currentFile = Paths.get(PatcherTool.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		Path oldFile = Paths.get(args[0]);
		System.out.println("Patch source: " + currentFile);
		System.out.println("Old jar: " + oldFile);
		Path patchedFile = Paths.get("patched-" + oldFile.getFileName());
		Files.copy(oldFile, patchedFile, StandardCopyOption.REPLACE_EXISTING);
		
		// Parse all options.
		Set<Option> options = new HashSet<>();
		for (int i = 1; i < args.length; i++) {
			String option = args[i].substring(2).replace('-', '_').toUpperCase(Locale.ENGLISH);
			assert option != null;
			options.add(Option.valueOf(option));
		}
		
		// Open jars and patch target
		try (FileSystem source = FileSystems.newFileSystem(currentFile, (ClassLoader) null);
				FileSystem target = FileSystems.newFileSystem(patchedFile, (ClassLoader) null)) {
			assert source != null;
			assert target != null;
			new PatcherTool(source, target).patch(options);
		}
		System.out.println("Successfully patched to " + patchedFile);
	}
	
	private final FileSystem source;
	private final FileSystem target;
	
	private PatcherTool(FileSystem source, FileSystem target) {
		this.source = source;
		this.target = target;
	}

	private void patch(Set<Option> options) throws IOException {
		if (options.isEmpty()) {
			System.out.println("Patched nothing, as requested. For minimal changes, use --security.");
		}
		
		if (options.contains(Option.SECURITY)) {
			// JSON injection, see issue #2198
			copy("effects.EffMessage", true);
			copy("expressions.ExprArgument", true);
			copy("expressions.ExprColored", true);
			copy("lang.VariableString", true);
			copy("util.chat.BungeeConverter", true);
			copy("util.chat.ChatCode", true);
			copy("util.chat.ChatMessages", true);
			copy("util.chat.SkriptChatCode", true);
		}
		if (options.contains(Option.MEMORY_LEAK)) {
			copy("SkriptEventHandler", true);
			copy("effects.Delay", true);
			copy("lang.Trigger", true);
			copy("util.AsyncEffect", true);
		}
	}
	
	private void copy(String className, boolean overwrite) throws IOException {
		String fileName = "/ch/njol/skript/" + className.replace('.', '/') + ".class";
		Path targetFile = target.getPath(fileName);
		if (!overwrite && Files.exists(targetFile)) {
			System.out.println("Not patching " + className + ", it already exists.");
			return; // Not safe to patch
		}
		Path sourceFile = source.getPath(fileName);
		Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
	}
}
