import java.util.Random;

public class Main {
	public static void main(String[] args) {

		// Declaring variables
		int m = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		int prob = Integer.parseInt(args[2]);

		// Creates a grid with the correct dimensions and cushion
		boolean[][] grid = new boolean[m + 2][n + 2];

		// Populates the grid according the the probability
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				Random rand = new Random();
				int r = rand.nextInt(100) + 1;
				if (r <= prob) {
					grid[i][j] = true;
				} else {
					grid[i][j] = false;
				}
			}
		}

		// Prints the grid with values for the game
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (grid[i][j]) {
					System.out.print("* ");
				} else {
					System.out.print(". ");
				}
			}
			System.out.println();
		}

		// Creates a space between problem and solution
		System.out.println(" ");

		// Creates a new grid for the solution
		int[][] game = new int[m + 2][n + 2];
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++)
				// Indexes cells adjacent to mine
				for (int k = i - 1; k <= i + 1; k++)
					for (int l = j - 1; l <= j + 1; l++)
						if (grid[k][l])
							game[i][j]++;

		// Prints the solution
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (grid[i][j])
					System.out.print("* ");
				else
					System.out.print(game[i][j] + " ");
			}
			System.out.println();
		}
	}
}
