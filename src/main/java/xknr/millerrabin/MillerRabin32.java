package xknr.millerrabin;

/**
 * Version using ints.
 * can only operate when n < 25_326_000
 * because of internal 
 * Three witnesses (2, 7, 61) are enough in this range.
 * 1_373_653 <= n < 4_759_123_141
 * 
 */
public class MillerRabin32
{
  private static final int LIMIT1 = 2047;
  private static final long LIMIT2 = 1_373_653L;
  
  private static final int PRIME1 = 7, PRIME2 = 61;

  public static final long ARG_LIMIT = Integer.MAX_VALUE;
  
	public boolean isPrime(int n) {		
    if (n <= 1) 
      return false;
				
    // n-1 = 2^k * m
    final int k = Integer.numberOfTrailingZeros(n - 1);
    final int m = (n - 1) >>> k;

    // 2 is common in all cases. 
    if (n % 2 == 0) 
      return n == 2;
    if (!tryWitness(2, m, n, k)) 
      return false;

    if (n < LIMIT2) { 
      if (n >= LIMIT1) {
        // use 2, 3
        if (n % 3 == 0) 
          return n == 3;        
        if (!tryWitness(3, m, n, k)) 
          return false;
      }
      // n < LIMIT1
      // use only 2, which is already tested, so do nothing.
    } else { // n >= LIMIT2,
      // Use 2, 7, 61
      // 2 is already tested.
      
      if (n % PRIME1 == 0) 
        return n == PRIME1;
      
      if (n % PRIME2 == 0) 
        return n == PRIME2;
      
      if (!tryWitness(PRIME1, m, n, k)) 
        return false;
      
      if (!tryWitness(PRIME2, m, n, k)) 
        return false;
    }  

    return true;
	}

	public static boolean tryWitness(int a, int m, int n, int k) {
		// b0 = a^m % n
		int b = modularExp(a, m, n);		

		int n1 = n - 1;
		if (b == 1 || b == n1) 
		  return true;
		
    --k; // Already made one check above.

		for(; k > 0; --k) {
			b = (int)((long)b * b % n);
			if (b == 1) {
			  return false; 
			} else if (b == n1) {
			  return true;
			}
		}
		
		return false;
	}

	public static int modularExp(int b, int e, int m) {
		int r = 1;
		while(e > 0) {
			if ((e & 1) != 0) {
				long rb = (long)r * b;
				r = (int)(rb % m);
			}
			e >>>= 1;			
			long b2 = ((long)b) * b;
			b = (int)(b2 % m);
		}
		return r;
	}
}
