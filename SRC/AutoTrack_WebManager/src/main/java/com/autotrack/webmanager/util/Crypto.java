package com.autotrack.webmanager.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
	private static byte[] genHash(String content, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(content.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	private static String hashToString(byte[] bytesMessage) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytesMessage.length; i++) {
			int highPart = ((bytesMessage[i] >> 4) & 0xf) << 4;
			int lowPart = bytesMessage[i] & 0xf;
			if (highPart == 0)
				s.append('0');
			s.append(Integer.toHexString(highPart | lowPart));
		}
		return s.toString();
	}

	public static String textToSHA1(String text) {
		return hashToString(genHash(text, "SHA-1"));
	}

	public static String textToMD5(String text) {
		return hashToString(genHash(text, "MD5"));
	}

	public static String textToSHA256(String text) {
		return hashToString(genHash(text, "SHA-256"));
	}

}
