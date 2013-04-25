package com.arcadiaprep.app.util;

import java.security.MessageDigest;
import android.util.Base64;

public class EncryptionUtil
{
    static String CHAR_ENCODING = "UTF-8";

    public static String dencrypt(String input, boolean isEncrypt, String key)
        throws Exception
    {
        if (input == null || input.length() == 0 || key == null || key.length() == 0)
            return null;

        long microTime = System.currentTimeMillis() * 1000;
        String dynKey = isEncrypt ? sha1(String.valueOf(microTime)) : input.substring(0, 40);
        String fixedKey = sha1(key);

        String dynKeyPart1 = dynKey.substring(0, 20);
        String dynKeyPart2 = dynKey.substring(20);
        String fixedKeyPart1 = fixedKey.substring(0, 20);
        String fixedKeyPart2 = fixedKey.substring(20);

        key = sha1(dynKeyPart1 + fixedKeyPart1 + dynKeyPart2 + fixedKeyPart2);

        byte[] bytes = isEncrypt ? (fixedKeyPart1 + input + dynKeyPart2).getBytes(CHAR_ENCODING) : base64_decode(input.substring(40));

        int len = bytes.length;
        byte[] result = new byte[len];

        byte[] keyBytes = key.getBytes(CHAR_ENCODING);
        for (int n = 0; n < len; n++)
            result[n] = (byte)(bytes[n] ^ keyBytes[n % 40]);

        return isEncrypt ? dynKey + base64_encode(result).replace("=", "") : new String(result, 20, len - 40, CHAR_ENCODING);
    }

    public static String sha1(String input)
    {
        byte[] result;
        try
        {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            result = mDigest.digest(input.getBytes(CHAR_ENCODING));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++)
        {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static byte[] base64_decode(String input)
    {
    	try
        {
            byte[] decodedBytes = Base64.decode(input,Base64.DEFAULT);
            return decodedBytes;
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String base64_encode(byte[] input)
    {
    	try {
        	return Base64.encodeToString(input, Base64.DEFAULT);
    	}
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}

