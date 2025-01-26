package ch.njol.skript.test.utils;

import java.util.Map;
import java.util.Set;

/**
 * Contains test results: successes, failures and doc failure. Will be serialized with Gson
 * for transfering it between processes of gradle/environment and the actual spigot server.
 */
public class TestResults {

	/**
	 * Succeeded tests.
	 */
	private final Set<String> succeeded;

	/**
	 * Failed tests.
	 */
	private final Map<String, String> failed;

	/**
	 * If the docs failed to generate when running gradle doc gen commands.
	 */
	private final boolean docsFailed;

	public TestResults(Set<String> succeeded, Map<String, String> failed, boolean docs_failed) {
		this.docsFailed = docs_failed;
		this.succeeded = succeeded;
		this.failed = failed;
	}

	public Set<String> getSucceeded() {
		return succeeded;
	}

	public Map<String, String> getFailed() {
		return failed;
	}

	public boolean docsFailed() {
		return docsFailed;
	}

	public String createReport() {
		StringBuilder sb = new StringBuilder("Succeeded:\n");
		if (succeeded.isEmpty())
			sb.append("<reset> - none\n");
		for (String test : succeeded)
			sb.append("<reset> - <light green>").append(test).append('\n');
		sb.append("<reset>Failed:\n");
		if (failed.isEmpty())
			sb.append("<reset> - none");
		for (Map.Entry<String, String> entry : failed.entrySet())
			sb.append("<reset> - <light red>").append(entry.getKey()).append("<reset>: <gray>").append(entry.getValue()).append('\n');
		return sb.toString();
	}

}
