import java.util.Scanner;

//Wesley DeBrusk

public class Calculator {

	public static void main(String[] args){

		Scanner kbd = new Scanner(System.in);
		boolean contin = true;
		double accumulator = 0.0;
		double input;
		while(contin){
			int opt;
			System.out.println("Accumulator = " + accumulator);
			System.out.println("Please choose one of the following options: ");
			System.out.println("1) Addition");
			System.out.println("2) Subtraction");
			System.out.println("3) Multiplication");
			System.out.println("4) Division");
			System.out.println("5) Square root");
			System.out.println("6) Clear");
			System.out.println("0) Exit");
			System.out.print("What is your option? ");
			opt = kbd.nextInt();
			if(opt == 0){
				contin = false;
			}
			else if(opt == 1){
				//do addition
				System.out.print("Enter a number: ");
				input = kbd.nextDouble();
				accumulator = accumulator + input;
			}
			else if(opt == 2){
				//do subtraction
				System.out.print("Enter a number: ");
				input = kbd.nextDouble();
				accumulator = accumulator - input;
			}
			else if(opt == 3){
				//do multiplication
				System.out.print("Enter a number: ");
				input = kbd.nextDouble();
				accumulator = accumulator * input;
			}
			else if(opt == 4){
				//do division
				System.out.print("Enter a number: ");
				input = kbd.nextDouble();
				accumulator = accumulator / input;
			}
			else if(opt == 5){
				//do square root
				if(accumulator > 0){
					accumulator = Math.sqrt(accumulator);
				}
				else{
					System.out.println("Not taking square root of a negative number");
				}
			}
			else if(opt == 6){
				//do clear
				accumulator = 0.0;
			}
		}
	}
}
