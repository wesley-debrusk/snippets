//Wesley DeBrusk
//CSE2100 Project 3
//Recursion- Collatz Conjecture

public class Main {
	public static void main(String[] args) { // main method
		for (int i = 1; i <= 1000; i++) { // a loop to display values from n=1
											// to n=10000
			System.out.println(g2(i)); // what is printed out
		}
	}

	static int g1(int n) { // returns g1(n)
		if (n == 1) {
			return 1; // base case, if 1 return 1
		}
		int times = 1 + g1(next1(n)); // number of times the recursive method is
										// called
		return times;
	}

	static int[] cache = new int[10000];// a place to store values from g1Fast()

	static int g1Fast(int n) {// a faster way of calculating g1()
		if (n == 1)
			return 1;// base case, if 1 return 1
		if (n < 10000 && cache[n] != 0)
			return cache[n]; // if the cache isn't empty for this location the
								// value is the answer
		int times = 1 + g1Fast(next1(n));// the recursive call
		if (n < 10000)
			cache[n] = times;// adds the value to the cache
		return times;// returns the amount of recursions
	}

	static int g2(int n) { // returns g2(n), Collatz sequence
		if (n == 1) {
			return 1; // base case, if 1 return 1
		}
		int times = 1 + g2(next2(n)); // number of times the recursive method is
										// called
		return times;
	}

	public static int next1(int x) { // returns the next number in the sequence
										// for g1()
		if (isEven(x) == false && x > 1) {
			return x - 1;
		} else {
			return x / 2;
		}
	}

	public static int next2(int x) { // returns the next number in the sequence
										// for g2()
		if (isEven(x) == false && x > 1) {
			return 3 * x + 1;
		} else {
			return x / 2;
		}
	}

	static boolean isEven(int x) { // a method to tell if an integer is even
		if ((x % 2) == 0) {
			return true; // returns true if even
		} else
			return false; // returns false if odd
	}
}
