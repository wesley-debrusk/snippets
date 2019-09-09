import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class SimCache {

	private static final String usage = new StringBuilder().append("Usage: sim-cache config [-rX] [tracefile(s)]\n\n")
			.append("config is a string like n,b,a\n").append("\tn : log_2(size of data in bytes)\n")
			.append("\tb : log_2(size of blocks in bytes)\n")
			.append("\ta : log_2(associativity). 0 for direct mapped.\n\n")
			.append("For example, 10,6,1 means 1 KiB data, 64-byte blocks, and 2-way\n")
			.append("set associativiy. The cache has 16 blocks. Each set has two blocks.\n")
			.append("Therefore, there are 8 sets.\n\n")
			.append("Use '-rX' option to specify the cache replacement policy,\n")
			.append("where X is an integer representing the policy.\n").append("\t0 : use random replacement policy\n")
			.append("\t1 : use LRU\n\n").append("tracefile is a text file containing memory traces.\n")
			.append("You can run more than one tracefile at a time.\n").toString();

	public static void main(String[] args) {
		if (args.length < 2) {
			systemError(usage);
		}
		boolean verbose = false;
		String config = "";
		int replacementPolicy = 1;
		ArrayList<String> files = new ArrayList<String>();
		for (String arg : args) {
			if (arg.equals("-v")) {
				verbose = true;
			} else if (arg.indexOf("-r") == 0) {
				try {
					replacementPolicy = Integer.parseInt(arg.substring(2));
				} catch (NumberFormatException e) {
					systemError("Invalid replacement policy (must be an integer).");
				}
			} else if (arg.indexOf(',') != -1) {
				config = arg;
			} else {
				files.add(arg);
			}
		}
		if (config.length() == 0)
			systemError("Specify a cache configuration.");
		if (verbose) {
			System.out.println("config=" + config + " replacement policy=" + replacementPolicy);
		}
		int[] configParsed = parseConfig(config);
		MemCache cache = new MemCache(configParsed[0], configParsed[1], configParsed[2]);
		cache.replacementPolicy = replacementPolicy;
		cache.verbose = verbose;
		for (String filepath : files) {
			readFile(cache, filepath);
		}
		cache.report();
	}

	private static void readFile(MemCache cache, String filepath) {
		String line;
		BufferedReader br;
		/*
		 * This regex pattern matches when the line begins with an address that can
		 * (optionally) be prefixed with '0x', followed by one or more alphanumeric
		 * characters, followed by whitespace and then either a 0, L, l (load) or 1, S,
		 * s (store)
		 */
		String pattern = "^((0x)*([0-9A-Fa-f]+))\\s+(0|1|L|l|S|s)";
		Pattern r = Pattern.compile(pattern);

		try {
			br = new BufferedReader(new FileReader(filepath));
			while ((line = br.readLine()) != null) {
				Matcher m = r.matcher(line);
				if (m.find()) {
					// in the pattern, each set of parentheses represents a "group"
					String addr = m.group(1);
					if (addr.indexOf("0x") == 0)
						addr = addr.substring(2);
					// w = 1 if the current line is a STORE instruction
					int w = "1Ss".indexOf(m.group(4).charAt(0)) != -1 ? 1 : 0;
					cache.access(addr, w);
				}
			}
		} catch (IOException e) {
			systemError(e.getMessage());
		}
	}

	private static void systemError(String s) {
		System.err.println("Error:\n" + s);
		System.exit(1);
	}

	private static int[] parseConfig(String s) {
		if (s.length() == 0)
			return new int[] { 0, 0, 0 };
		String[] fields = s.split(",");
		if (fields.length != 3)
			systemError("Invalid configuration. Need three numbers, like 10,6,0.");
		try {
			int n = Integer.parseInt(fields[0]);
			int b = Integer.parseInt(fields[1]);
			int a = Integer.parseInt(fields[2]);
			if (n < 6 || n > 24 || b < 1 || b > 12 || b > n || a < 0 || a + b > n) {
				systemError("Arguments do not fall within the correct range.");
			}
			return new int[] { n, b, a };
		} catch (NumberFormatException e) {
			systemError("Invalid arguments (must be numbers).");
			return null;
		}
	}

}