/**
 * This class overrides the String class.
 * 
 * @author Wes DeBrusk
 */

public class MyString {

	private String string;// String member variable
	private int x = 39;// int used in calculating the hashcode

	/**
	 * Constructor for the MyString class.
	 */
	public MyString(String s) {
		string = s;
	}

	/**
	 * Returns the String member variable for an instance of MyString.
	 */
	public String getString() {
		return string;
	}

	/**
	 * Returns the int representation of a MyString's hash code.
	 */
	@Override
	public int hashCode() {
		int code = 0;
		for (int i = 0; i < string.length(); i++) {
			code = x * code + string.charAt(i);
		}
		return code;
	}

	/**
	 * Checks to see if two instances of MyString are equal based on their hash
	 * codes. Returns true if equal, false if not.
	 */
	@Override
	public boolean equals(Object ms) {
		if (this.hashCode() == ms.hashCode()) {
			return true;
		} else {
			return false;
		}
	}
}
