package xknr.millerrabin;

import java.math.BigInteger;

import org.weakref.int128.Int128;
import org.weakref.int128.Int128Holder;
import org.weakref.int128.Int128Math;

import static xknr.util.BigInt.*;

public class MillerRabin64
{
  /*
   * 
   * https://oeis.org/A014233
   * Smallest odd number for which Miller-Rabin primality test on bases <= n-th prime does not reveal compositeness
   * 
   * https://oeis.org/A006945
   * Smallest odd composite number that requires n Miller-Rabin primality tests.
   * 
   * alternative sets:
   * The minimal witness set is {2, 7, 61}. n < 2^32
   * 2, 325, 9375, 28178, 450775, 9780504, 1795265022 for n 2^64 
   * 
   * 
   * 
   * 9, 
   * 2047 1 
   * 1373653 2 
   * 25326001 3 
   * 3215031751 4 
   * 2152302898747 5 
   * 3474749660383 6
   * 341550071728321 7  
   * 341550071728321, 8
   * 3825123056546413051, 9 
   * 3825123056546413051, 10
   * 3825123056546413051, 11
   * 318665857834031151167461, 12 
   * 3317044064679887385961981, 13
   * 
   */

  public static final long ARG_LIMIT = Long.MAX_VALUE;
 
  private static final int LIMIT1 = 2047;
  private static final int LIMIT2 = 1_373_653;
  private static final long LIMIT_SPECIAL1 = 4_759_123_141L;
  private static final long LIMIT5 = 2_152_302_898_747L;
  private static final long LIMIT6 = 3_474_749_660_383L;
  
  private static final int WIT_FIRST_PRIMES[] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};
  private static final int WIT_SPECIAL1[] = {2, 7, 61};
  private static final int WIT_SPECIAL2[] = {2, 325, 9375, 28178, 450775, 9780504, 1795265022};

  public boolean isPrime(long n) {
		if (n <= 1) 
		  return false;
		
		int[] witnesses = null;
		int numWitnesses;

    if (n < LIMIT_SPECIAL1) { 
      if (n < LIMIT2) {
        witnesses = WIT_FIRST_PRIMES;
        numWitnesses = n < LIMIT1 ? 1 : 2;
      } else {
        witnesses = WIT_SPECIAL1;
        numWitnesses = witnesses.length;
      }
    } else { 
      if (n < LIMIT6) {
        witnesses = WIT_FIRST_PRIMES;
        numWitnesses = n < LIMIT5 ? 5 : 6;
      } else {
        witnesses = WIT_SPECIAL2;
        numWitnesses = witnesses.length;
      }
    }

    for(int i = 0; i < numWitnesses; i++)
      if (n % witnesses[i] == 0) 
        return n == witnesses[i];
		  
		// n-1 = 2^k * m
		int k = Long.numberOfTrailingZeros(n - 1);
		long m = (n - 1) >>> k;
		
    for(int i = 0; i < numWitnesses; i++)
      if (!tryWitness(witnesses[i], m, n, k))
        return false;
    
    return true;
	}
  
  public boolean tryWitness(long a, long m, long n, int k) {
    // b0 = a^m % n
    long b = modularExp1(a, m, n);    

    long n1 = n - 1L;
    if (b == 1L || b == n1) 
      return true;
    --k; // Already made one check above.
    
    
    for(; k > 0; --k) {
      b = mulMod(b, b, n);
      if (b == 1L) {
        return false; 
      } else if (b == n1) {
        return true;
      }
    }
    
    assert b != n1; // proves that another step is unnecessary 
    
    return false;
  }  

  public long modularExp1(long b, long e, long m) {
    long r = 1L;
    while(e > 0L) {
      if ((e & 1L) != 0L) {
        r = mulMod(r, b, m);
      }
      e >>>= 1L;
      b = mulMod(b, b, m);
    }
    return r;
  }  
  
  private Int128Holder quotient = new Int128Holder();
  private Int128Holder remainder = new Int128Holder();
  
  public long mulMod(long r, long b, long m) {
    long rh = r >>> 63, rl = r;
    long bh = b >>> 63, bl = b;

    long rbh = Int128Math.multiplyHigh(rh, rl, bh, bl);
    long rbl = Int128Math.multiplyLow(rh, rl, bh, bl);
    
    long mh = m >>> 63, ml = m;
    
    Int128Math.divide(rbh, rbl, mh, ml, quotient, remainder);

    return remainder.low();
  }

  public static long mulMod128(long r, long b, long m) {
   Int128 rb = Int128.multiply(Int128.valueOf(r), Int128.valueOf(b));
   Int128 result = Int128.remainder(rb, Int128.valueOf(m));
   return result.low();
  }


  public static long mulModOriginal(long r, long b, long m) {
    r %= m;
    b %= m;
    
    if (Long.MAX_VALUE / r >= b)
      return (r * b) % m;
    
    BigInteger R = B(r);
    BigInteger B = B(b);
    BigInteger M = B(m);
    
    return R.multiply(B).remainder(M).longValueExact();
  }
	
}
