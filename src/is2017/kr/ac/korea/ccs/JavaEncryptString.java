package is2017.kr.ac.korea.ccs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaEncryptString
{
	public static void main(String[] args)
	{
		String className = args[0];
		try
		{
			// LOAD start
			String src = String.join("\n", Files.readAllLines(Paths.get(new File(className + ".java").getAbsolutePath())));
			// LOAD end

			// ENCRYPT start
			Matcher matcher = Pattern.compile("\\\"[^\\\"]*\\\"").matcher(src);
			StringBuilder sb = new StringBuilder();
			while (matcher.find())
			{
				matcher.appendReplacement(sb, "new String(Base64.getDecoder().decode(\"" + Base64.getEncoder().encodeToString(matcher.group().substring(1, matcher.group().length() - 1).getBytes()) + "\"))");
			}
			matcher.appendTail(sb);
			src = sb.toString();
			// ENCRYPT end

			// IMPORT start
			matcher = Pattern.compile("[ \\t\\n]*package[ \\t\\n]+[^;]*;").matcher(src);
			sb = new StringBuilder();
			if (matcher.find())
			{
				matcher.appendReplacement(sb, "$0\nimport java.util.Base64;");
			}
			else
			{
				sb.append("import java.util.Base64;\n");
			}
			matcher.appendTail(sb);
			src = sb.toString();
			// IMPORT end

			// WRITE start
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("encrypted/" + className + ".java")));
			for (String line : src.split("\n", 0))
			{
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			// WRITE end

			// BATCH start
			bw = new BufferedWriter(new FileWriter(new File("encrypted/" + className + ".bat")));
			bw.write("javac " + className + ".java");
			bw.newLine();
			bw.write("java " + className);
			bw.newLine();
			bw.write("pause");
			bw.close();
			// BATCH end
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}