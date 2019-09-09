
public class CreditCard {
	public static void main(String[] args) {

		final long card = Long.parseLong(args[0]);
		check(card);
		long newcard = solver(card);
		check(newcard);
	}

	public static boolean isEven(long x) {
		if ((x % 2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static long fn(long x) {
		long timestwo = x * 2;
		String digits = Long.toString(timestwo);
		long sum = 0;
		for (char c : digits.toCharArray())
			sum = sum + c - '0';
		return sum;
	}

	public static long[] longToArray(long x) {
		String digits = Long.toString(x);
		long[] longarray = new long[digits.length()];
		for (long i = 0; i < digits.length(); i++) {
			longarray[(int) i] = digits.charAt((int) i) - '0';
		}
		return longarray;
	}

	public static void printArray(long[] input) {
		for (long i : input) {
			System.out.print(i);
		}
	}

	public static long checkSum(long x) {
		long checksum = 0;
		long[] digits = longToArray(x);
		for (long i = 0; i < digits.length; i++) {
			if (isEven(i)) {
				checksum += digits[(int) i];
			} else {
				checksum += fn(digits[(int) i]);
			}
		}
		return checksum;
	}

	public static boolean isValid(long x) {
		long checksum = checkSum(x);
		if (checksum % 10 == 0) {
			return true;
		} else {
			return false;

		}
	}

	public static long solver(long x) {
		long test = x * 10;
		if (isValid(test)) {
			return test;
		} else {
			for (long i = 1; i <= 9; i++) {
				if (isValid(test + i)) {
					test = test + i;
				}
			}
		}
		return test;
	}

	public static void check(long x) {
		if (isValid(x)) {
			System.out.println("The number " + x + " is valid.");
		} else {
			System.out.println("The number " + x + " is not valid.");
		}
	}
}
