package BloomFilter;

/**
 * Murmur hash function. 
 * This code is simply a C++ -> Java translation
 * from MurmurHash2.cpp from: 
 * https://sites.google.com/site/murmurhash/
 * <p>
 * All credit for the MurmurHash algorithm goes 
 * to its creator Austin Appleby. 
 * <p>
 * MurmurHash was chosen for its speed and
 * performance. 
 */
public class MurmurHash {
	public static final int M = 0x5bd1e995; 
	public static final int R = 24; 
	
	public static int MurmurHash2(byte[] key, int seed) { 
		int len = key.length; 
		int h = seed ^ len; 
		
		for(; len >= 4; len -= 4) { 
			int k = constructIntFromBytes(key, len-4);
			
			k *= M; 
			k ^= k >> R; 
			k *= M;
			
			h *= M;
			h ^= k;	
		}
		
		switch(len) { 
		case 3: h ^= key[2] << 16; 
		case 2: h ^= key[1] << 8; 
		case 1: h ^= key[0]; 
				h *= M; 
		}
		
		h ^= h >> 13; 
		h *= M; 
		h ^= h >> 15; 
		
		return h; 
	}
	
	/**
	 * 4 byte array to Integer 
	 * 
	 * @param data  There must be 4 bytes from start. 
	 * @param start Starting index position. 
	 * @return
	 */
	public static int constructIntFromBytes(byte[] data, int start) { 
		int value = 0; 
		
		value = (value | data[start]) << 8;
		value = (value | data[start + 1]) << 8;
		value = (value | data[start + 2]) << 8;
		value = (value | data[start + 3]);
		
		return value; 
	}
	
	/**
	 * Integer to 4 byte array. 
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] constructBytesFromInt(int data) { 
		byte[] bytes = new byte[4]; 
		
		bytes[3] = (byte)(data >> 24); 
		bytes[2] = (byte)(data >> 16); 
		bytes[1] = (byte)(data >> 8); 
		bytes[0] = (byte)(data); 
		
		return bytes; 
	}
}
