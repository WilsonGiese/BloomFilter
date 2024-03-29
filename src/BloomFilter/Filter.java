package BloomFilter;

public interface Filter<T> {
	
	/**
	 * Add an item to the Filter. 
	 */
	public void add(T item); 
	
	/**
	 * Check if an item is in the Filter
	 */
	public boolean contains(T item); 
	
}
