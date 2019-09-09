import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner kbd = new Scanner(System.in);
		int min; //number the program will start at
		int max; //number the program will end at
		boolean control = true; //loop control variable

		while (control) { //loop that displays the user interface

			System.out.println("What is your option?");
			System.out.println("1. Run Happy Number Checker");
			System.out.println("2. Exit");
			int option = kbd.nextInt(); //the user chooses their option

			if (option == 2) {
				System.out.println("Program Terminated");
				control = false; //the loop is exited, program ends
			} else if (option == 1) {

				System.out.println("---Happy Number Checker---");//user enters in information
				System.out.println("Enter the minmum integer: ");
				min = kbd.nextInt();
				System.out.println("Enter the maximum integer: ");
				max = kbd.nextInt(); 

				for (int i = min; i <= max; i++) { //displays all of the happy numbers between the two specified values
					HappyNumber h = new HappyNumber(Integer.toString(i));
					if (h.isHappy()) {
						System.out.println(i);
					}
				}
			}
		}
		kbd.close();
	}
}
