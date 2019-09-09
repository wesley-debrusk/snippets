import static java.lang.Math.toIntExact;

public class BranchPredictor {

	public int mode;
	public BranchPredictor predictor0;
	public BranchPredictor predictor1;
	public int m, n, nbI, nbA, nbits, gHist, maskGHist, maskAddr, counterMax, maskPrediction, maskSelectorIndex;
	public int[] bht;
	public int[] selectors;
	public int numBranches, numTaken, numMispredictions, numSelected0;
	public boolean verbose;

	// constructor for tournament predictor
	public BranchPredictor() {
		this.mode = -1;
	}

	public void configureTournament(int nbits, int m0, int n0, int k0, int m1, int n1, int k1) {
		this.mode = 2; // two because we are using two predictors
		this.nbits = nbits;
		// maskSelectorIndex = 0b11111... where the number of 1's is equal to nbits
		this.maskSelectorIndex = (1 << nbits) - 1;
		this.predictor0 = new BranchPredictor();
		this.predictor0.configure(m0, n0, k0);
		this.predictor0.verbose = this.verbose;
		this.predictor1 = new BranchPredictor();
		this.predictor1.configure(m1, n1, k1);
		this.predictor1.verbose = this.verbose;
		this.reset();
	}

	// constructor for bimodal predictor
	public void configure(int m, int n, int k) {
		this.mode = 1; // one because we are using one predictor
		this.m = m;
		this.n = n;
		// TODO: add your code here before calling reset
		// For example, you can generate masks here.
		// maskGHist = (1 << m) - 1

		this.maskGHist = (1 << m) - 1;
		this.maskAddr = (1 << k) - 1;
		this.maskPrediction = (1 << n) - 1;

		this.nbI = k + m;
		this.nbA = k;

		int length = (int) Math.pow(2, m + k);
		this.bht = new int[length];
		for (int i = 0; i < length; i++) {
			this.bht[i] = 0;
		}

		this.reset();
	}

	public void reset() {
		if (this.mode == 1) {
			this.gHist = 0;
			// creates an array of size 2^nbI filled with zeroes (this is by default in
			// Java) for counters
			// you need to calculate the number of counters. Use << operation
			// TODO
		} else if (this.mode == 2) {
			this.predictor0.reset();
			this.predictor1.reset();
			this.numSelected0 = 0;
			// create an array of bytes for selectors
			// TODO

			this.selectors = new int[(int) Math.pow(2, nbits)];

		}
		this.numBranches = 0;
		this.numTaken = 0;
		this.numMispredictions = 0;
	}

	// return 1 for taken prediction
	// return 0 for not-taken prediction
	public int predict(long addr, int outcome) {
		this.numBranches++;
		this.numTaken += outcome;
		// remember to set the prediction (the return value) for all cases
		// it is set to 0 for now, always predicting not-taken.
		int prediction = 0;
		if (this.mode == 1) {
			// Predict, update, return
			// TODO
			// update global history

			addr = addr & maskAddr;
			int newaddr = (int) ((addr << this.m) + (this.maskGHist & this.gHist));

			int pred = this.bht[newaddr];
			prediction = (pred >> (n - 1));

			if (outcome == 0) {
				if (this.bht[newaddr] == 0) {
					// Do nothing
				} else {
					this.bht[newaddr] = this.bht[newaddr] - 1;
				}
			} else if (outcome == 1) {
				if (pred < Math.pow(2, n) - 1) {
					this.bht[newaddr] = this.bht[newaddr] + 1;
				} else {
					// Do nothing
				}
			}

			if (prediction != outcome) {
				this.numMispredictions++;
			}

			this.gHist = (this.gHist << 1) + outcome;
			return prediction;
		} else {
			// Predict, update, return
			int prediction0 = this.predictor0.predict(addr, outcome);
			int prediction1 = this.predictor1.predict(addr, outcome);
			// now you need to check which prediction to use
			// and update the selector
			// keep track of how many times predictor0 is used
			// TODO

			addr = addr & this.maskSelectorIndex;

			int selector = this.selectors[(int) addr];
			if (selector < 2) {
				prediction = prediction0;
				this.numSelected0++;
			} else {
				prediction = prediction1;
			}

			if (prediction0 != prediction1) {

				if (outcome == prediction0) {
					if (this.selectors[(int) addr] == 0) {
						// Do nothing
					} else {
						this.selectors[(int) addr]--;
					}
				} else if (outcome == prediction1) {
					if (this.selectors[(int) addr] == 3) {
						// Do nothing
					} else {
						this.selectors[(int) addr]++;
					}
				}

			}

		}

		if (prediction != outcome) {
			this.numMispredictions++;
		}

		return prediction;
	}

	// report the statistics
	public void report() {
		if (this.mode == 1) {
			System.out.println("Total number of branches   = " + this.numBranches);
			System.out.println("Number of taken branches   = " + this.numTaken);
			System.out.println("Number of untaken branches = " + (this.numBranches - this.numTaken));
			System.out.println("Number of mispredictions   = " + this.numMispredictions);
			if (this.numBranches > 0) {
				double percentage = ((double) this.numMispredictions / (double) this.numBranches) * 100;
				System.out.printf("Misprediction rate         = %.2f%%\n", percentage);
			}
		} else if (this.mode == 2) {
			System.out.println("Predictor 0:");
			this.predictor0.report();
			System.out.println("\nPredictor 1:");
			this.predictor1.report();
			System.out.println("\n");
			if (this.numBranches > 0) {
				double mispredictionRate = ((double) this.numMispredictions / (double) this.numBranches) * 100;
				double percentUsing0 = ((double) this.numSelected0 / (double) this.numBranches) * 100;
				System.out.printf("Misprediction rate         = %.2f%%\n", mispredictionRate);
				System.out.printf("Percentage of using 0      = %.2f%%\n", percentUsing0);
			}
		}
	}
}
