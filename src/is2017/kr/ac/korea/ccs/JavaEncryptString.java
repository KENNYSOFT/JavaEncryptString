package is2017.kr.ac.korea.ccs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaEncryptString
{
	final static int STATE_NORMAL = 0;
	final static int STATE_QUOTE = 1;
	final static int STATE_BACKSLASH = 2;
	final static int[][] STATE_TRANSITION_TABLE = new int[3][65536];
	final static char[] BACKSLASH_CHARACTER = new char[65536];
	
	static
	{
		for (int i = 0; i <= 2; ++i)
		{
			Arrays.fill(STATE_TRANSITION_TABLE[i], i);
		}
		STATE_TRANSITION_TABLE[STATE_NORMAL]['\"'] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_QUOTE]['\\'] = STATE_BACKSLASH;
		STATE_TRANSITION_TABLE[STATE_QUOTE]['\"'] = STATE_NORMAL;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['t'] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['b'] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['n'] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['r'] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['f'] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['\''] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['\"'] = STATE_QUOTE;
		STATE_TRANSITION_TABLE[STATE_BACKSLASH]['\\'] = STATE_QUOTE;
		BACKSLASH_CHARACTER['t'] = '\t';
		BACKSLASH_CHARACTER['b'] = '\b';
		BACKSLASH_CHARACTER['n'] = '\n';
		BACKSLASH_CHARACTER['r'] = '\r';
		BACKSLASH_CHARACTER['f'] = '\f';
		BACKSLASH_CHARACTER['\''] = '\'';
		BACKSLASH_CHARACTER['\"'] = '\"';
		BACKSLASH_CHARACTER['\\'] = '\\';
	}
	
	public static void main(String[] args)
	{

		String encryptionType = args[0];
		String className = args[1];

		System.out.println(String.format("Type -> %s, Class -> %s", encryptionType, className));

		try
		{
			// LOAD start
			String src = String.join("\n", Files.readAllLines(Paths.get(new File(className + ".java").getAbsolutePath())));
			// LOAD end
			
			// ESCAPE start
			Matcher matcher = Pattern.compile("\\\\u+([0-9A-Fa-f]{4})").matcher(src);
			StringBuilder sb = new StringBuilder();
			while (matcher.find())
			{
				matcher.appendReplacement(sb, Character.toString((char)Integer.parseInt(matcher.group(1), 16)));
			}
			matcher.appendTail(sb);
			src = sb.toString();
			// ESCAPE end

			// ENCRYPT start
			byte[] keyarr = new byte[16];
			Random random = new Random();
			for (int i = 0; i < 16; ++i)
			{
				keyarr[i] = (byte)random.nextInt(256);
			}
			String key = Base64.getEncoder().encodeToString(keyarr);
			sb = new StringBuilder();
			StringBuilder sb2 = null;
			char[] srcarr = src.toCharArray();
			int state = STATE_NORMAL;
			int prev_state;
			for (int i = 0; i < srcarr.length; ++i)
			{
				prev_state = state;
				state = STATE_TRANSITION_TABLE[state][srcarr[i]];
				if (state == STATE_NORMAL)
				{
					if (prev_state == STATE_QUOTE)
					{
						if (encryptionType.equals("-b"))
						{
							sb.append("new String(Base64.getDecoder().decode(\"" + Base64.getEncoder().encodeToString(sb2.toString().getBytes()) + "\"))");
						}
						else if (encryptionType.equals("-a"))
						{
							sb.append("JESDecrypter.decrypt(\"" + JESEncrypter.encrypt(sb2.toString(), key) + "\", \"" + key + "\")");
						}
						else
						{
							System.out.println("Nope");
						}
					}
					else
					{
						sb.append(srcarr[i]);
					}
				}
				else if (state == STATE_QUOTE)
				{
					if (prev_state == STATE_NORMAL)
					{
						sb2 = new StringBuilder();
					}
					else if (prev_state == STATE_BACKSLASH)
					{
						sb2.append(BACKSLASH_CHARACTER[srcarr[i]]);
					}
					else
					{
						sb2.append(srcarr[i]);
					}
				}
			}
			src = sb.toString();
			// ENCRYPT end

			// IMPORT start
			matcher = Pattern.compile("[ \\t\\n]*package[ \\t\\n]+[^;]*;").matcher(src);
			sb = new StringBuilder();
			if (matcher.find())
			{
				matcher.appendReplacement(sb, "$0\nimport java.util.Base64;\nimport is2017.kr.ac.korea.ccs.JESDecrypter;");
			}
			else
			{
				sb.append("import java.util.Base64;\nimport is2017.kr.ac.korea.ccs.JESDecrypter;\n");
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
			bw.write("javac -d . JESDecrypter.java");
			bw.newLine();
			bw.write("javac -cp . " + className + ".java");
			bw.newLine();
			bw.write("java -cp . " + className);
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