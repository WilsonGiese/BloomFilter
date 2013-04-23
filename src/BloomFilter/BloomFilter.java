package BloomFilter;

/**
 * A Bloom filter, conceived by Burton Howard Bloom in 1970,
 *  is a space-efficient probabilistic data structure that 
 *  is used to test whether an element is a member of a set. 
 *  False positive retrieval results are possible, but false 
 *  negatives are not; i.e. a query returns either "inside set 
 *  (may be wrong)" or "definitely not in set". Elements can 
 *  be added to the set, but not removed (though this can be 
 *  addressed with a counting filter). The more elements that 
 *  are added to the set, the larger the probability of false 
 *  positives.[1]
 *  
 *  [1] http://en.wikipedia.org/wiki/Bloom_filter
 * 
 * @author Wilson Giese <giese.wilson@gmail.com> 
 *
 * @param <T>
 */
public class BloomFilter<T> implements Filter<T> {
	
	/* Filter specs */ 
	public static int INDEX_SIZE = 8; /* Byte */ 
	
	/* Instance Variables */
	public final int k; 
	public final int size;
	private byte[] filter; 
	
	
	/**
	 * @param size Number of buckets in the bloom filter. 
	 * @param k Number of hashes per item
	 */
	public BloomFilter(int size, int k) { 
		this.k = k; 
		
		/* Size must be >= INDEX_SIZE */ 
		if(size < INDEX_SIZE) { 
			size = INDEX_SIZE; 
		}
		
		/* Odd numbers yield better performance */ 
		if((size / INDEX_SIZE) % 2 == 0) { 
			this.size = (size / INDEX_SIZE) + 1; 
		} else { 
			this.size = size / INDEX_SIZE; 
		}
		
		filter = new byte[this.size]; 
	}
	
	/**
	 * Add an item to the bloom filter. 
	 * 
	 * @param item
	 */
	public void add(T item) {
		int[] hashes = khash(item); 
		
		for(int h : hashes) { 
			int[] indices = getIndex(h); 
			byte mask = (byte)(1 << indices[1]); 
			filter[indices[0]] |= mask; 
		}
	}

	/**
	 * Check if an item is in the bloom filter. 
	 * 
	 * @param item
	 * @return
	 */
	public boolean contains(T item) {
		int[] hashes = khash(item); 
		
		for(int h : hashes) { 
			int[] indices = getIndex(h); 			
			/* Math.abs is used because of Java's wonderful lack of unsigned bytes */ 
			if(Math.abs(filter[indices[0]] >> indices[1]) % 2 != 1) { 
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Generate k hashes for item. 
	 * There will be k linear combinations
	 * of MurmurHash. 
	 * 
	 * @param item
	 * @return
	 */
	private int[] khash(T item) { 
		int[] hashes = new int[k];
		
		int seed = 0; 
		for(int i = 0; i < k; i++) { 
			hashes[i] = MurmurHash.MurmurHash2(item.toString().getBytes(), seed); 
			seed = hashes[i]; 
		}
		
		return hashes; 
		
	}
	
	/* Get major and minor indices */ 
	private int[] getIndex(int hash) { 
		int[] indices = new int[2];
		indices[0] = hash % size; 
		indices[1] = hash % INDEX_SIZE; 
		
		return indices; 
	}
	
	/**
	 * Estimate the Probability of false positives. 
	 * Info: http://en.wikipedia.org/wiki/Bloom_filter#Probability_of_false_positives
	 * 
	 * @param n Number of elements
	 * @param m Number of buckets(size)
	 * @param k Number of hash functions
	 * @return
	 */
	public static double estimateFalsePositiveProbability(int n, int m, int k) { 
		double dn = (double)n; 
		double dm = (double)m;
		double dk = (double)k; 

		return Math.pow((1 - Math.pow(Math.E, (-dk * dn)/dm)), dk); 
	}
}
