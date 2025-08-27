package iri.elearningapi.utils.elearningFunction;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;



public class Cryptage {

	public static String getMd5(String input) {
		String streengeSelPassword="e@l#e$a%r^n&i*n(g";
		if (input!=null) {
			input=input+streengeSelPassword;
		}
		try {

			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new ElearningException(
					new ErrorAPI("Impossible de crypter la donne recu...!"));
		}
	}
}
