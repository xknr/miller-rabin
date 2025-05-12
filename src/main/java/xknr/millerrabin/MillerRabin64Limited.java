package xknr.millerrabin;

public class MillerRabin64Limited
{
  private static final int LIMIT1 = 2047;
  private static final long LIMIT2 = 1_373_653L;
  private static final int PRIME1 = 7, PRIME2 = 61;

  // Limit comes from long * long overflow, 
  // first occurrence of which was found with exhaustive search.
  // It happened at the line: b = (b * b) % n;
  // It produces an a b value of 3,037,001,041.
  // This n value itself is slightly higher than sqrt(Long.MAX_VALUE)
  // sqrt(4^32 * 2^32) ~= 3,037,000,499
  // Coincidentally, 3_215_031_751L, the first single false 
  // positive for witness set {2,3,5,7} is close to this number.
  public static final long ARG_LIMIT = 3_037_001_057L;
  
	public boolean isPrime(long n)
	{
	  if (n >= ARG_LIMIT) 
	    throw new IllegalArgumentException(
        String.format("Would cause overflow. %d >= %d", n, ARG_LIMIT));
	  
	  if (n <= 1) 
	    return false;

    // n-1 = 2^k * m
    int k = Long.numberOfTrailingZeros(n - 1);
    long m = (n - 1) >>> k;

    if (n % 2 == 0) 
      return n == 2;
    
    if (!tryWitness(2, m, n, k)) 
      return false;

    if (n < LIMIT2) { 
      if (n >= LIMIT1) {
        if (n % 3 == 0) 
          return n == 3;
        
        if (!tryWitness(3, m, n, k)) 
          return false;
      }
    } else {
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

  public static boolean tryWitness(int a, long m, long n, int k) 
  {
		// b0 = a^m % n
		long b = modExpLong(a, m, n);		

		long n1 = n - 1L;
		if (b == 1L || b == n1) {
			return true;
		}

    --k; // Already made one check above.

		for(; k > 0; --k) {

      // Check overflow:
		  //if (b > Long.MAX_VALUE / b)
		  //  throw new RuntimeException(String.format("n = %d b = %d", n, b));
		  
		  //assert b <= Long.MAX_VALUE / b; 
		  
			b = (b * b) % n;
			if (b == 1L) {
				return false; 
			} else if (b == n1) {
				return true;
			}
		}
		
		return false;
	}
	
	public static long modExpLong(long b, long e, long m) {
		long r = 1L;
		while(e > 0L) {
			if ((e & 1L) != 0L) {
	      //assert r <= Long.MAX_VALUE / b;  // Detect overflow
	      r = (r * b) % m;
			}
			e >>>= 1L;
			b = (b * b) % m;
		}
		return r;
	}

}
