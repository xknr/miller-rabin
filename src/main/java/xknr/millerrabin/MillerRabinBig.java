package xknr.millerrabin;

import java.math.BigInteger;

import static xknr.euler.util.BigInt.*;

public class MillerRabinBig
{	
  // Below this, {2} is enough.
  private static final BigInteger LIMIT1 = B(2047);
  
  // Below this, {2, 3} are enough.
  private static final BigInteger LIMIT2 = B(1373653);
  
  // Special set 1: {2, 7, 61}. This bound is higher than 2^32.
  private static final BigInteger LIMIT_SPECIAL1 = B(4759123141L);
  
  // {2, 3, 5, 7, 11}
  private static final BigInteger LIMIT5 = B(2152302898747L);

  // {2, 3, 5, 7, 11, 13}
  private static final BigInteger LIMIT6 = B(3474749660383L);
  
  // Special set 2: {2, 325, 9375, 28178, 450775, 9780504, 1795265022}
  private static final BigInteger B_2_64 = B(2).pow(64);   
  
  // {2,3,5,7,11,13,17,19,23,29,31,37} First 12 primes.
  private static final BigInteger LIMIT12 = new BigInteger("318665857834031151167461");
  
  // {2,3,5,7,11,13,17,19,23,29,31,37,41} First 13 primes.
  public static final BigInteger LIMIT13 = new BigInteger("3317044064679887385961981");

	public static final BigInteger ARG_LIMIT = LIMIT13.subtract(B1);
  
  public static final BigInteger LONG_MAX = B(Long.MAX_VALUE);
	
	private static final BigInteger WIT_FIRST_PRIMES[] = {
	  B(2), B(3), B(5), B(7), B(11), B(13), B(17), B(19), B(23), B(29), B(31), B(37), B(41)
	};
	
  private static final BigInteger WIT_SPECIAL1[] = {
    B(2), B(7), B(61)
  };
  
  private static final BigInteger WIT_SPECIAL2[] = {
    B(2), B(325), B(9375), B(28178), B(450775), B(9780504), B(1795265022)
  };

	public boolean isPrime(BigInteger n) {
		if (n.compareTo(B1) <= 0) {
			return false;
		}
		
		if (!less(n, LIMIT13)) 
      throw new IllegalArgumentException(
        String.format("No verified witness set for n >= %s", LIMIT13));
    
    BigInteger[] witnesses = null;
    int numWitnesses;
    
    if (less(n, B_2_64)) {
      if (less(n, LIMIT_SPECIAL1)) { 
        if (less(n, LIMIT2)) {
          witnesses = WIT_FIRST_PRIMES;
          numWitnesses = less(n, LIMIT1) ? 1 : 2;
        } else {
          witnesses = WIT_SPECIAL1;
          numWitnesses = witnesses.length;
        }
      } else { 
        if (less(n, LIMIT6)) {
          witnesses = WIT_FIRST_PRIMES;
          numWitnesses = less(n, LIMIT5) ? 5 : 6;
        } else {
          witnesses = WIT_SPECIAL2;
          numWitnesses = witnesses.length;
        }
      }
    } else {
      if (less(n, LIMIT12)) {
        witnesses = WIT_FIRST_PRIMES;
        numWitnesses = 12;
      } else {
        witnesses = WIT_FIRST_PRIMES;
        numWitnesses = 13;
      }
    }
       
    for(int i = 0; i < numWitnesses; i++)
      if (n.mod(witnesses[i]).signum() == 0) 
        return n.equals(witnesses[i]);
      
    // n-1 = 2^k * m
    BigInteger m = n.subtract(B1);
    int k = numberOfTrailingZeros(m);
    m = m.shiftRight(k);
    
    for(int i = 0; i < numWitnesses; i++)
      if (!tryWitness(witnesses[i], m, n, k))
        return false;
    
    return true;		
	}

	public static int numberOfTrailingZeros(BigInteger n) {
    if (n.signum() == 0) 
      return 0; // Handle zero case
    int count = 0;
    while (n.and(B1).equals(B0)) {
        count++;
        n = n.shiftRight(1);
    }
    return count;
  }

	public static boolean tryWitness(BigInteger a, BigInteger m, BigInteger n, int k) {
		// b = a ^ m % n
		BigInteger b = modularExp(a, m, n);		
		if (b.equals(B1)) 
		  return true;
		
		BigInteger n1 = n.subtract(B1);
		if (b.equals(n1)) 
		  return true;
    --k; // Already made one check above.

		for(; k > 0; --k) {
			b = b.multiply(b).remainder(n);
			if (b.equals(B1)) {
				return false; 
			} else if (b.equals(n1)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static BigInteger modularExp(BigInteger b, BigInteger e, BigInteger m) {
	  return b.modPow(e, m);
	}
  
}
