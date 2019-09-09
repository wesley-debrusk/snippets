import java.util.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MemCache {

	private static final int CS_VALID = 1;
	private static final int CS_DIRTY = 2;
	private static final int INT_MASK = 0b1111111111111111111111111111111;
	public boolean verbose = false;
	public int replacementPolicy = 1; // 0 for random, 1 for LRU
	private int n, b, a, s; // s is the number of bits for index
	private int numBytes, blockSize, numBlocks, associativity, numSets, indexMask;
	private int[][] flags, setFlags;
	private long[][] tags;
	private int numReferences, numMisses;

	/*
	 * Constructor 2^n : total number of bytes in cache, 2^b : block size, 2^a : set
	 * associativity
	 */
	public MemCache(int n, int b, int a) {
		this.n = n;
		this.b = b;
		this.a = a;

		// set other variables you may need

		this.numBytes = (int) Math.pow(2, this.n);
		this.blockSize = (int) Math.pow(2, this.b);
		this.associativity = (int) Math.pow(2, this.a);

		this.numBlocks = this.numBytes / this.blockSize;
		this.numSets = this.numBlocks / this.associativity;
		this.s = (int) (Math.log(this.numSets) / Math.log(2));

		this.indexMask = (1 << this.s) - 1;

		this.replacementPolicy = 1; // LRU
		this.reset();
	}

	private void reset() {
		// we do not care about data
		// create list (or arrays) for tags, flags, set_flags

		this.tags = new long[this.numSets][this.associativity];
		this.flags = new int[this.numSets][this.associativity];
		this.setFlags = new int[this.numSets][this.associativity];

		this.numReferences = 0;
		this.numMisses = 0;
	}

	// find a block in a set where the new block can be placed
	public int findWay(int index) {

		int value = 0;

		if (this.replacementPolicy == 1) {
			if (this.associativity == 1)
				return 0;
			int way = 0;
			// check if there is a way available
			// if yes, return the way number
			// if not, find a block by replacement policy

			for (int i = 0; i < this.associativity; i++) {
				if (this.flags[index][i] == 0) {
					this.flags[index][i] = 1;
					return i;
				}
			}

			for (int i = 0; i < this.associativity; i++) {
				if (this.setFlags[index][i] == 0) {
					updateSetFlags(index, i);
					return i;
				}
			}

			value = way;

		} else if (this.replacementPolicy == 0) {
			if (this.associativity == 1)
				return 0;

			Random rn = new Random();
			int rand = rn.nextInt(this.associativity);

			this.flags[index][rand] = 1;
			updateSetFlags(index, rand);

			value = rand;

		}
		return value;
	}

	// Book-keeping. Just accessed block identified by index and way
	private void updateSetFlags(int index, int way) {
		if (this.associativity <= 1) {
			return;
		}

		// if using LRU, maintain the access history for replacement
		if (this.setFlags[index][way] == 0) {
			for (int i = 0; i < this.associativity; i++) {
				if (this.setFlags[index][i] >= 1) {
					this.setFlags[index][i]--;
				}
			}
			this.setFlags[index][way] = this.associativity - 1;
		} else if (this.setFlags[index][way] > 0) {
			for (int i = 0; i < this.associativity; i++) {
				if (this.setFlags[index][i] > this.setFlags[index][way]) {
					this.setFlags[index][i]--;
				}
			}
			this.setFlags[index][way] = this.associativity - 1;
		}
	}

	// do not care about write for now
	// return 1 for hit and 0 for miss
	public int access(String addr, int write) {
		this.numReferences++;
		int hit = 0;
		int way = 0;
		int index = 0;
		// cache access. ignore write for now.
		// steps:
		// obtain index and tag
		// check if it is a hit
		// if it is a miss, replace a block in the set

		addr = "0x" + addr;
		long address = Long.decode(addr);

		index = (int) (address >> this.b) & this.indexMask;
		long tag = (address >> (this.s + this.b));

		for (int i = 0; i < this.associativity; i++) {
			if (tags[index][i] == tag) {
				hit = 1;
				way = i;
			}
		}

		if (hit == 0) {
			this.numMisses++;
			way = findWay(index);
			this.tags[index][way] = tag;
		}

		this.updateSetFlags(index, way);
		return hit;
	}

	// report the statistics
	public void report() {
		System.out.println("Number of references    = " + this.numReferences);
		System.out.println("Number of misses        = " + this.numMisses);
		System.out.println("Number of hits          = " + (this.numReferences - this.numMisses));
		if (this.numReferences > 0) {
			double rate = this.numMisses * 100.0 / this.numReferences;
			System.out.printf("Miss rate               = %.2f%%", rate);
		}
	}

}