package is2017.kr.ac.korea.ccs;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class JESDecrypter
{
	public static String decrypt(String data, String key)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), "AES"));
			return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
		}
		catch (NoSuchPaddingException|NoSuchAlgorithmException|InvalidKeyException|BadPaddingException|IllegalBlockSizeException e)
		{
		}
		return null;
	}
}