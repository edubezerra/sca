package br.cefetrj.sca.infra;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Generator {

	public static String generate(String input) {
		MessageDigest digest;

		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes(), 0, input.length());
			BigInteger i = new BigInteger(1, digest.digest());
			return String.format("%1$032x", i);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
