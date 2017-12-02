package is2017.kr.ac.korea.ccs;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class JESEncrypter
{
	public static String encrypt(String data, String key)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), "AES"));
			return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
		}
		catch (NoSuchPaddingException|NoSuchAlgorithmException|InvalidKeyException|BadPaddingException|IllegalBlockSizeException e)
		{
		}
		return null;
	}
}