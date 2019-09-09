import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DNA {

	public static void main(String[] args) throws IOException {
		String file = args[0];
		DNAreader(file);
	}

	// Returns true if the DNA is valid, allows for lower case letters
	static boolean isValidDNA(String s) {
		boolean isvalid = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!(c == 'A' || c == 'C' || c == 'T' || c == 'G' || c == 'a' || c == 'c' || c == 't' || c == 'g')) {
				isvalid = false;
			}
		}
		return isvalid;
	}

	// Returns the Watson-Crick complement of the DNA, allows for lower case
	static String complementWC(String s) {
		String DNA = "";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == 'A' || c == 'a') {
				DNA = DNA + "T";
			}
			if (c == 'T' || c == 't') {
				DNA = DNA + "A";
			}
			if (c == 'C' || c == 'c') {
				DNA = DNA + "G";
			}
			if (c == 'G' || c == 'g') {
				DNA = DNA + "C";
			}
		}
		return DNA;
	}

	// Returns true if the reverse of the WC complement is equal to the input string
	static boolean palindromeWC(String s) {
		String dna = s.toUpperCase();
		String complement = complementWC(s);
		String reversed = new StringBuilder(complement).reverse().toString().toUpperCase();
		if (dna.equals(reversed)) {
			return true;
		} else {
			return false;
		}
	}

	// Reads DNA from a text file in src
	// Returns DNA, if it is valid, and if it is valid the compliment and palindrome
	static void DNAreader(String s) throws IOException {
		String filename = s;
		String filepath = "src/" + filename + ".txt";
		File file = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println("DNA is: " + line.toUpperCase());
			System.out.println("Is Valid DNA: " + isValidDNA(line));
			if (isValidDNA(line)) {
				System.out.println("Complement: " + complementWC(line));
				System.out.println("Is a WC palindrome: " + palindromeWC(line));
			}
			System.out.println(" ");
		}
		br.close();
	}
}
