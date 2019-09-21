import java.util.Random;
import java.util.Scanner;

public class Passwordify {
	public static void main(String[] args) {
		System.out.println("Welcome to the passwordify generator!");
		Scanner sc = new Scanner(System.in);

		while (true) {
			generate(sc);
			System.out.print("To continue type 'yes' or type anything else to exit: ");
			String option = sc.nextLine();
			if (option.equals("yes")) {
				continue;
			} else {
				System.out.println("Thanks for using the generator, exiting now.");
				break;
			}
		}
	}

	public static void generate(Scanner sc) {
		System.out.print("Enter a password phrase: ");
		String input = sc.nextLine();
		String returnString = "";

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == ' ') {
				returnString += '_';
			} else if (c == 'a' || c == 'A') {
				returnString += '@';
			} else if (c == 'o' || c == 'O') {
				returnString += '0';
			} else if (c == 's' || c == 'S') {
				returnString += '$';
			} else {
				returnString += c;
			}
		}
		Random r = new Random();
		int suffix = r.nextInt((999 - 111) + 1) + 111;
		returnString += Integer.toString(suffix);
		System.out.println("Generated password: " + returnString);
	}
}
