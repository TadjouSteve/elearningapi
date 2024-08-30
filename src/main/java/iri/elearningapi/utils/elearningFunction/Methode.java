package iri.elearningapi.utils.elearningFunction;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;

public class Methode {

	public static String upperCaseFirst(String val) {
		if (val != null && !val.isEmpty()) {
			char[] arr = val.toCharArray();
			arr[0] = Character.toUpperCase(arr[0]);
			return new String(arr).trim();
		} else {
			return val;
		}
	}

	public static boolean isValidEmail(String email) {
		if (email == null || email.isBlank()) {
			return false;
		}
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static String generateRandomString() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = new Random().nextInt(3) + 7; // Generates a length between 7 and 9
		Random random = new Random();

		return random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}

	public static Boolean isCorrectString(String string, int minLength,int maxLength) {
		return (string != null && !string.isBlank() && (minLength < 1 || string.length() >= minLength) && (maxLength<1 ||string.length()<maxLength));
	}

	public static Boolean isCorrectString(String string) {
		return isCorrectString(string, 0,0);
	}
	
	public static void returnErrorAPI(String meassage,int step) {
		throw new ElearningException(new ErrorAPI("cette question n'existe pas...!",step));
	}
	
	public static void returnErrorAPI(String meassage) {
		returnErrorAPI(meassage, 0);
	}
	
	public static void printOut(String meassage) {
		System.out.println(meassage);
	}

}
