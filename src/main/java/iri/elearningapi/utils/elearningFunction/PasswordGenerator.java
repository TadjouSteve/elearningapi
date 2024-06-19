package iri.elearningapi.utils.elearningFunction;

import java.util.Random;

public class PasswordGenerator {
	private static final String	CHAR_LOWER	= "abcdefghijklmnpqrstuvwxyz";	// chaine initial
																			// "abcdefghijklmnopqrstuvwxyz", j'ai
																			// retirer le 'o' la voyelle
	private static final String	CHAR_UPPER	= CHAR_LOWER.toUpperCase();
	private static final String	NUMBER		= "123456789";					// chaine initial "0123456789", j'ai retire
																			// le Zero '0' le chiffre

	private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER;

	private static Random random = new Random();

	public static String generatePassword(int length) {
		if (length < 1)
			throw new IllegalArgumentException();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int rndCharAt = random.nextInt(PASSWORD_ALLOW_BASE.length());
			char rndChar = PASSWORD_ALLOW_BASE.charAt(rndCharAt);

			sb.append(rndChar);
		}

		return sb.toString();
	}
}
