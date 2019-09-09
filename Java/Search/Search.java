package pa1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

// Wesley DeBrusk

public class Main {

	public static void main(String[] args) throws IOException {

		Path path = Paths.get("insert path here", "listNumbers-10.txt");
		Charset charset = Charset.forName("UTF-8");
		List<String> lines = Files.readAllLines(path, charset);
		String[] temparr = lines.toArray(new String[lines.size()]);
		int[] arr = new int[temparr.length];
		for (int i = 0; i < temparr.length; i++) {
			arr[i] = Integer.parseInt(temparr[i]);
		}

		int n = 50;

		System.out.println("Array: " + Arrays.toString(arr));
		System.out.println("Sum: " + n);
		System.out.println();
		bruteForce(n, arr);
		binarySearch(n, arr);
		indexSearch(n, arr);

	}

	static void bruteForce(int n, int[] array) {
		long startTime = System.nanoTime();
		String answer = "There were no matches.";
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (i == j) {
					continue;
				} else if (array[i] + array[j] == n) {
					answer = Integer.toString(array[i]) + " + " + Integer.toString(array[j]) + " = " + n;
				}
			}
		}
		System.out.println(answer);
		long stopTime = System.nanoTime();
		System.out.println(stopTime - startTime);
	}

	static void binarySearch(int n, int[] array) {
		long startTime = System.nanoTime();
		String answer = "There were no matches.";
		Arrays.sort(array);

		for (int i = 0; i < array.length; i++) {
			int needed = n - array[i];
			int index = Arrays.binarySearch(array, needed);
			if (index < 0) {
				continue;
			} else if (i == index) {
				continue;
			} else {
				answer = Integer.toString(array[index]) + " + " + Integer.toString(array[i]) + " = " + n;
			}
		}
		System.out.println(answer);
		long stopTime = System.nanoTime();
		System.out.println(stopTime - startTime);
	}

	static void indexSearch(int n, int[] array) {
		long startTime = System.nanoTime();

		String answer = "There were no matches.";
		Arrays.sort(array);

		int i = 0;
		int j = array.length - 1;

		while (i < j) {
			int sum = array[i] + array[j];
			if (sum < n) {
				i = i + 1;
			} else if (sum > n) {
				j = j - 1;
			} else if (sum == n) {
				answer = Integer.toString(array[i]) + " + " + Integer.toString(array[j]) + " = " + Integer.toString(n);
				break;
			}
		}
		System.out.println(answer);

		long stopTime = System.nanoTime();
		System.out.println(stopTime - startTime);
	}
}
