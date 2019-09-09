import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner kbd = new Scanner(System.in);
		int times = 100000;
		int matches = 0;
		boolean contin = true;

		while (contin) {
			System.out.println("What is your option?");
			System.out.println("1. Run");
			System.out.println("2. Exit");
			int option = kbd.nextInt();
			boolean out = false;

			if (option == 2) {
				System.out.println("Program terminated.");
				contin = false;
			} else if (option == 1) {
				matches = 0;
				System.out.println("How many people?");
				double people = kbd.nextInt();
				double days[] = new double[(int) people];

				for (int i = 0; i < times; i++) {
					for (int j = 0; j < people; j++) {
						days[j] = (int) (Math.random() * 365) + 1;
					}
					for (int j = 0; j < people; j++) {
						for (int k = j + 1; k < people; k++) {
							if (days[j] == days[k]) {
								matches++;
								out = true;
								break;
							}
						}
						if (out) {
							out = false;
							break;
						}
					}
				}
				double prob = (double) matches / 100000;
				System.out.println("probability is; " + (prob * 100) + "%");
			}
		}
		kbd.close();
	}
}
