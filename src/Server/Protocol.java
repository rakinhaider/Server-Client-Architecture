package Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Protocol {

	private static String[] regx = { "(.*)\\.java" };
	private static String[] regxNeg={"(.*)\\.exe"};
	private static Pattern pattern;
	private static Matcher matcher;

	public static boolean matchAll(String filename) {
		for (String s : regx) {

			pattern = Pattern.compile(s);
			matcher=pattern.matcher(filename);
			if(!matcher.find())
			{
				return false;
			}
		}
		for (String s : regxNeg) {

			pattern = Pattern.compile(s);
			matcher=pattern.matcher(filename);
			if(matcher.find())
			{
				return false;
			}
		}
		return true;
	}

}
