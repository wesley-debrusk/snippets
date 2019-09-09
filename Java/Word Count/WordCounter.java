import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordCounter {
	public static void main(String[] args) throws FileNotFoundException {

		final String filename = "src/sample.txt";
		System.out.println("Select an option: ");
		System.out.println("1. Text File (" + filename + ")");
		System.out.println("2. User Input");
		Scanner kbd = new Scanner(System.in);
		int option = kbd.nextInt();
		if (option == 1) {
			File file = new File(filename);
			int count = 0;
			Scanner input = new Scanner(file);
			while (input.hasNext()) {
				input.next();
				count++;
			}
			System.out.println("Wordcount is: " + count);
			input.close();
		} else if (option == 2) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter text: ");
			String s = sc.nextLine();
			System.out.println("Wordcount is = " + s.split(" ").length);
			sc.close();
		}
		kbd.close();
	}
}
