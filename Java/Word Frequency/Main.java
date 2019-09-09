
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) throws FileNotFoundException {

		// Creates a new ChainHashMap to map the words to
		ChainHashMap<MyString, Integer> words = new ChainHashMap<MyString, Integer>();

		// Imports the file of words, and creates a scanner to scan it.
		File file = new File("src/shakespeare.txt");
		Scanner doc = new Scanner(file);

		// maps all of the words in the file
		while (doc.hasNext()) {// while there are more words in the file

			// converts word to lowercase, gets previous word count
			MyString word = new MyString(doc.next().toLowerCase());
			Integer count = words.get(word);

			if (count == null)
				count = 0; // if not in map, previous count is zero
			words.put(word, 1 + count); // (re)assign new count for this word
		}

		// sorts the words in the map based on frequency
		int count = 0;
		while (count < 1000) {// will print the top 1000 words
			Entry<MyString, Integer> current = null;

			// finds the most used word in the list
			for (Entry<MyString, Integer> ent : words.entrySet()) {
				if (current == null) {
					current = ent;
				} else if (ent.getValue() > current.getValue()) {
					current = ent;
				}
			}

			// prints the most used word in the list and removes it
			System.out.println("'" + current.getKey().getString() + "'" + "--> " + current.getValue());
			words.remove(current.getKey());
			count++;// increments the count
		}
		doc.close();
	}
}
