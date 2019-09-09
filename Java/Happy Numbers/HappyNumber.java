
public class HappyNumber {

	private String _number; // number we are checking for happiness
	private SinglyLinkedList<Integer> list = new SinglyLinkedList<Integer>(); // list
																				// we
																				// will
																				// use
																				// to
																				// store
																				// intermediate
																				// results

	public HappyNumber(String s) { // simple constructor
		_number = s;
	}

	public boolean isHappy() { // checks whether a number is happy or not
		if (_number.equals("1")) {
			return true; // if the number is one it is happy
		} else {
			int next; // variable we will use to check the list
			next = separate(_number); // separates the number
			// System.out.println(next); // used to display intermediate results
			list.addLast(next); // adds the number to the end of the list
			while (next != 1) { // we reach our result of 1
				next = separate(next); // separates the number again
				// System.out.println(next); //used to display intermediate
				// results
				if (list.isInList(next)) {
					return false; // if it is in the list the number is unhappy
				} else {
					list.addLast(next); // add the number to the list
				}
			}
			return true; // returns true if the number is happy
		}
	}

	public int separate(int n) { // returns the sum of the squares of an int
		int sum = 0;
		while (n > 0) {
			sum += (n % 10) * (n % 10);
			n = n / 10;
		}
		return sum;
	}

	public int separate(String s) { // returns the sum of the squares of the
									// digits of a string
		int total = 0;
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i))) {
				total += Math.pow(Character.getNumericValue(s.charAt(i)), 2);
			}
		}
		return total;
	}
}
